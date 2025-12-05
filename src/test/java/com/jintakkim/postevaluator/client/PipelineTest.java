package com.jintakkim.postevaluator.client;

import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.config.ApplicationConfig;
import com.jintakkim.postevaluator.config.properties.*;
import com.jintakkim.postevaluator.evaluation.EvaluateResult;
import com.jintakkim.postevaluator.feature.*;
import com.jintakkim.postevaluator.persistance.SetupStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PipelineTest {
    static ApplicationConfig applicationConfig = new ApplicationConfig(
            new GeminiProperties(null), //env를 통해 가져옴
            new DefinitionProperties(
                    new UserDefinition(
                            Map.of("likedCryptoTopic", new FeatureDefinition("likedCryptoTopic", FeatureType.STRING,
                                    new GenerationCriteriaBuilder()
                                            .addCondition("좋아하는 코인 관련 토픽들","(,로 이어서 표기)")
                                            .build())
                            ),
                            LabelingCriteria.builder()
                                    .addCriterion("암호화폐 주제의 토픽을 선호")
                                    .addCriterion("게시글의 요소(리엑션 수, 조회수, 내용, 댓글 수)들을 전반적으로 고려")
                                    .build()
                    ),
                    new PostDefinition(
                            Map.of(
                                    "likeCount", DefinitionProperties.BuiltIn.POST_FEATURE.LIKE_COUNT,
                                    "dislikeCount", DefinitionProperties.BuiltIn.POST_FEATURE.DISLIKE_COUNT,
                                    "content", new FeatureDefinition("content", FeatureType.STRING,
                                            new GenerationCriteriaBuilder()
                                                    .addCondition("글 내용","코인 관련 커뮤니티의 글이다.")
                                                    .build()),
                                    "viewCount", DefinitionProperties.BuiltIn.POST_FEATURE.VIEW_COUNT,
                                    "commentCount", DefinitionProperties.BuiltIn.POST_FEATURE.COMMENT_COUNT
                            ),
                            LabelingCriteria.builder()
                                    .addCriterion("리엑션 수, 조회수, 내용, 댓글 수를 고려")
                                    .build()
                    )
            ),
            new DatasetProperties(10, 10, SetupStrategy.CLEAR),
            new AlgorithmMetricProperties(AlgorithmMetricProperties.BuiltIn.MEAN_SQUARED_ERROR),
            null
    );

    static PostRecommendTest postRecommendTest = new PostRecommendTest(applicationConfig);

    static PipelineAlgorithmRegistry algorithmRegistry = new PipelineAlgorithmRegistry();
    static {
        algorithmRegistry.registerSubAlgorithm(new PopularityScoreAlgorithm());
        algorithmRegistry.registerSubAlgorithm(new EngagementScoreAlgorithm());
        algorithmRegistry.registerSubAlgorithm(new ContentScoreAlgorithm());
        algorithmRegistry.registerSubAlgorithm(new QualityScoreAlgorithm());
        algorithmRegistry.registerFinalAlgorithm(new FinalScoreAlgorithm());
    }

    @Test
    @DisplayName("파이프라인 테스트")
    void pipelineTest() {
        EvaluateResult result = postRecommendTest.pipelinedTest(algorithmRegistry);
        System.out.println(ResultFormatter.formatEvaluateResult(result));
    }

    // ========== 서브 알고리즘들 ==========

    static class PopularityScoreAlgorithm implements PipelineRecommendAlgorithm {
        @Override
        public double calculateScore(SampleAccessor sample, SubAlgorithmResultAccessor context) {
            Integer viewCount = sample.post().getFeature("viewCount", Integer.class);
            return Math.min(Math.log10(viewCount + 1) / 5.0, 1.0);
        }

        @Override
        public String name() {
            return "popularity";
        }
    }

    static class EngagementScoreAlgorithm implements PipelineRecommendAlgorithm {
        private static final double ENGAGEMENT_BUFFER = 50.0;
        private static final double COMMENT_MULTIPLIER = 3.0;

        @Override
        public double calculateScore(SampleAccessor sample, SubAlgorithmResultAccessor context) {
            Integer likeCount = sample.post().getFeature("likeCount", Integer.class);
            Integer dislikeCount = sample.post().getFeature("dislikeCount", Integer.class);
            Integer viewCount = sample.post().getFeature("viewCount", Integer.class);
            Integer commentCount = sample.post().getFeature("commentCount", Integer.class);

            double totalInteractions = likeCount + dislikeCount + (commentCount * COMMENT_MULTIPLIER);
            return totalInteractions / (viewCount + ENGAGEMENT_BUFFER);
        }

        @Override
        public String name() {
            return "engagement";
        }
    }

    static class QualityScoreAlgorithm implements PipelineRecommendAlgorithm {
        @Override
        public double calculateScore(SampleAccessor sample, SubAlgorithmResultAccessor context) {
            Integer likeCount = sample.post().getFeature("likeCount", Integer.class);
            Integer dislikeCount = sample.post().getFeature("dislikeCount", Integer.class);
            return calculateWilsonScore(likeCount, dislikeCount + likeCount);
        }

        private double calculateWilsonScore(long positive, long total) {
            if (total == 0) return 0.0;

            double z = 1.96; // 95% 신뢰구간
            double phat = (double) positive / total; // 관측된 성공 비율

            double numerator = phat + (z * z) / (2 * total) - z * Math.sqrt((phat * (1 - phat) + (z * z) / (4 * total)) / total);
            double denominator = 1 + (z * z) / total;

            return numerator / denominator;
        }

        @Override
        public String name() {
            return "quality";
        }
    }

    static class ContentScoreAlgorithm implements PipelineRecommendAlgorithm {
        private static final double WEIGHT_LENGTH = 2.5;
        private static final double WEIGHT_TTR = 2.0;
        private static final double WEIGHT_STRUCTURE = 1.5;

        @Override
        public double calculateScore(SampleAccessor sample, SubAlgorithmResultAccessor context) {
            String content = sample.post().getFeature("content", String.class);
            if (content == null || content.isBlank()) {
                return 0.0;
            }

            double lengthScore = calculateLengthScore(content);
            double ttrScore = calculateTtrScore(content);
            double structureScore = calculateStructureScore(content);

            double finalScore = (lengthScore * WEIGHT_LENGTH) +
                    (ttrScore * WEIGHT_TTR) +
                    (structureScore * WEIGHT_STRUCTURE);

            return Math.max(0.0, finalScore); //최종 점수가 음수가 되지 않도록 보정

        }

        private double calculateLengthScore(String content) {
            int length = content.length();
            if (length < 300) return 1.0;
            if (length < 1000) return 3.0;
            if (length < 5000) return 5.0;
            return 2.0; // 너무 긴 글은 오히려 가독성이 떨어질 수 있다
        }

        //어휘 다양성 점수
        private double calculateTtrScore(String content) {
            String[] words = content.trim().split("\\s+");
            if (words.length < 50) return 0.0;

            Set<String> uniqueWords = new HashSet<>(Arrays.asList(words));
            double ttr = (double) uniqueWords.size() / words.length;

            return ttr * 10.0;
        }


        private double calculateStructureScore(String content) {
            double score = 0.0;

            //문단 수
            int lineBreaks = content.length() - content.replace("\n", "").length();
            if (lineBreaks > 10) score += 3.0;
            else if (lineBreaks > 3) score += 2.0;

            //평균 문장 길이
            String[] sentences = content.split("[.!?]+");
            if (sentences.length > 1) {
                double avgSentenceLength = (double) content.length() / sentences.length;
                if (avgSentenceLength > 120) {
                    score -= 2.0; // 평균 120자가 넘는 긴 문장이면 감점
                } else if (avgSentenceLength < 80) {
                    score += 1.0; // 가독성이 좋다고 판단하여 가점
                }
            }
            return score;
        }

        @Override
        public String name() {
            return "content";
        }
    }

    /**
     * 최종 점수 계산: 콘텐츠 매칭에 높은 가중치
     */
    static class FinalScoreAlgorithm implements PipelineRecommendAlgorithm {
        private static final double WEIGHT_POPULARITY = 0.15;
        private static final double WEIGHT_ENGAGEMENT = 0.20;
        private static final double WEIGHT_QUALITY = 0.15;
        private static final double WEIGHT_CONTENT = 0.50; // 콘텐츠 매칭 중요

        @Override
        public double calculateScore(SampleAccessor sample, SubAlgorithmResultAccessor context) {
            double popularityScore = context.getScore("popularity");
            double engagementScore = context.getScore("engagement");
            double qualityScore = context.getScore("quality");
            double contentScore = context.getScore("content");

            // 정규화
            double normPopularity = normalizeWithStats(popularityScore, context.getScoreStatistics("popularity"));
            double normEngagement = normalizeWithStats(engagementScore, context.getScoreStatistics("engagement"));
            double normQuality = normalizeWithStats(qualityScore, context.getScoreStatistics("quality"));
            double normContent = normalizeWithStats(contentScore, context.getScoreStatistics("content"));

            double finalScore = (normPopularity * WEIGHT_POPULARITY) +
                    (normEngagement * WEIGHT_ENGAGEMENT) +
                    (normQuality * WEIGHT_QUALITY) +
                    (normContent * WEIGHT_CONTENT);

            return Math.max(0.0, Math.min(finalScore, 1.0));
        }

        private double normalizeWithStats(double value, AlgorithmStatistics stats) {
            double range = stats.max() - stats.min();
            if (range < 0.0001) return 0.5;
            return (value - stats.min()) / range;
        }

        @Override
        public String name() {
            return "final";
        }
    }
}
