package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MetricResult;
import lombok.RequiredArgsConstructor;

import java.util.DoubleSummaryStatistics;
import java.util.List;

@RequiredArgsConstructor
public class PipelinedEvaluator {
    private final AlgorithmMetric algorithmMetric;
    private final List<LabeledSample> labeledSamples;

    public EvaluateResult evaluate(PipelineAlgorithmRegistry registry) {
        validateRegistry(registry);
        AlgorithmContext context = new AlgorithmContext();
        runSubAlgorithms(registry.getSubAlgorithms(), context);
        List<SamplePrediction> predictions = predictScores(registry.getFinalAlgorithm(), context);
        MetricResult metricResult = algorithmMetric.calculateCost(predictions);
        return new EvaluateResult(algorithmMetric, metricResult.cost(), metricResult.topErrorOccurred());
    }

    private void runSubAlgorithms(List<PipelineRecommendAlgorithm> subAlgorithms, AlgorithmContext context) {
        subAlgorithms.forEach(subAlgorithm -> runSubAlgorithm(subAlgorithm, context));
    }

    private void runSubAlgorithm(PipelineRecommendAlgorithm subAlgorithm, AlgorithmContext context) {
        List<SamplePrediction> samplePredictions = predictScores(subAlgorithm, context);
        context.add(subAlgorithm.name(), samplePredictions, calStatisticsFromPredictions(samplePredictions));
    }

    private AlgorithmStatistics calStatisticsFromPredictions(List<SamplePrediction> predictions) {
        DoubleSummaryStatistics stats = predictions.stream()
                .mapToDouble(SamplePrediction::predictScore)
                .summaryStatistics();
        double mean = stats.getAverage();
        double min = stats.getMin();
        double max = stats.getMax();
        double variance = predictions.stream()
                .mapToDouble(SamplePrediction::predictScore)
                .map(score -> Math.pow(score - mean, 2)) // 편차 제곱
                .average()
                .orElse(0.0);
        double std = Math.sqrt(variance);
        return new AlgorithmStatistics(mean, std, min, max);
    }

    private List<SamplePrediction> predictScores(PipelineRecommendAlgorithm algorithm, AlgorithmContext context) {
        return labeledSamples.stream()
                .map(sample -> {
                    context.setCurrentSample(sample.user().id(), sample.post().id());
                    return new SamplePrediction(sample, algorithm.calculateScore(sample, context));
                })
                .toList();
    }

    private void validateRegistry(PipelineAlgorithmRegistry registry) {
        if(registry.getSubAlgorithms().isEmpty())
            throw new IllegalArgumentException("서브 알고리즘은 최소 한개 이상 등록되어야 합니다, 서브 알고리즘이 없다면 simpleTest를 사용해야합니다.");
        if(registry.getFinalAlgorithm() == null)
            throw new IllegalArgumentException("최종 알고리즘이 비여있습니다.");
    }
}
