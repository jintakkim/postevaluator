package com.jintakkim.postevaluator.client;

import com.jintakkim.postevaluator.evaluation.EvaluateResult;
import com.jintakkim.postevaluator.evaluation.SamplePrediction;
import com.jintakkim.postevaluator.search.SearchResult;
import com.jintakkim.postevaluator.search.param.Combination;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResultFormatter {

    public static String formatSearchResult(SearchResult searchResult) {
        StringBuilder sb = new StringBuilder();
        String line = "=".repeat(70) + "\n";

        sb.append(line);
        sb.append("[ 최적 조합 탐색 결과 ]\n");
        sb.append("-".repeat(70)).append("\n");

        sb.append(formatCombination(searchResult.combination()));
        sb.append(formatEvaluateResult(searchResult.evaluateResult()));

        sb.append(line);
        return sb.toString();
    }

    private static String formatCombination(Combination combination) {
        String params = combination.getParameters().values().stream()
                .map(parameter -> String.format("%s: %.3f", parameter.name(), parameter.value()))
                .collect(Collectors.joining(", "));
        return String.format("> 하이퍼파라미터 조합:\n  { %s }\n", params);
    }

    public static String formatEvaluateResult(EvaluateResult result) {
        StringBuilder sb = new StringBuilder();
        sb.append("> 평가 결과:\n");
        sb.append(String.format("  - 평가 지표: %s\n", result.algorithmMetric()));
        sb.append(String.format("  - 비용 (Cost): %.4f\n", result.cost()));

        if (result.topErrorOccurredSamples() == null || result.topErrorOccurredSamples().isEmpty()) {
            sb.append("  - 주요 오류 발생 샘플: 없음\n");
        } else {
            sb.append("  - 주요 오류 발생 샘플 (예측 오차가 큰 순):\n");
            int count = 1;
            for (SamplePrediction sample : result.topErrorOccurredSamples()) {
                double error = Math.abs(sample.labeledScore() - sample.predictScore());
                sb.append(String.format(
                        "    [%d] PostID: %-5d | UserID: %-5d | 실제: %.2f | 예측: %.2f | 오차: %.2f\n",
                        count++,
                        sample.postId(),
                        sample.userId(),
                        sample.labeledScore(),
                        sample.predictScore(),
                        error
                ));
                if (sample.labeledReason() != null && !sample.labeledReason().trim().isEmpty()) {
                    sb.append(String.format(
                            "        사유: %s\n",
                            sample.labeledReason()
                    ));
                }
            }
        }
        return sb.toString();
    }
}