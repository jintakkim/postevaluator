package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.core.Post;
import com.jintakkim.postevaluator.core.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.metric.AlgorithmMetric;
import com.jintakkim.postevaluator.evaluation.metric.MetricResult;

import java.util.List;

public class SimpleEvaluator implements Evaluator {
    private final AlgorithmMetric algorithmMetric;
    private final List<LabeledPost> labeledPosts;

    public SimpleEvaluator(AlgorithmMetric algorithmMetric, List<LabeledPost> labeledPosts) {
        this.algorithmMetric = algorithmMetric;
        this.labeledPosts = labeledPosts;
        validate();
    }

    @Override
    public EvaluateResult evaluate(RecommendAlgorithm recommendAlgorithm) {
        List<PostPrediction> postPredictions = predictScores(recommendAlgorithm);
        MetricResult metricResult = algorithmMetric.calculateCost(postPredictions);
        return new EvaluateResult(algorithmMetric, metricResult.cost(), metricResult.topErrorOccurredPostIds());
    }

    private List<PostPrediction> predictScores(RecommendAlgorithm recommendAlgorithm) {
        return labeledPosts.stream()
                .map(labeledPost -> new PostPrediction(
                        labeledPost.id(),
                        labeledPost.labeledScore(),
                        recommendAlgorithm.calculateScore(labeledPost)
                ))
                .toList();
    }

    private void validate() {
        if(labeledPosts == null || labeledPosts.isEmpty()) {
            throw new IllegalArgumentException("평가 데이터는 한개 이상있어야 합니다.");
        }
    }
}
