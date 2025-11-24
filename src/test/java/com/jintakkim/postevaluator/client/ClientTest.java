package com.jintakkim.postevaluator.client;

import com.jintakkim.postevaluator.RecommendAlgorithm;
import com.jintakkim.postevaluator.SampleAccessor;
import com.jintakkim.postevaluator.evaluation.EvaluateResult;
import com.jintakkim.postevaluator.test.ApplicationIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClientTest extends ApplicationIntegrationTest {
    @Test
    @DisplayName("BuiltIn 피처 기반 추천 알고리즘 테스트")
    void simpleAlgorithmTest() {
        EvaluateResult evaluateResult = postRecommendTest.simpleTest(new RecommendAlgorithm() {
            @Override
            public double calculateScore(SampleAccessor sampleAccessor) {
                // User Features
                String userLikeTopics = sampleAccessor.user().getFeature("likeTopics", String.class);
                String userDislikeTopics = sampleAccessor.user().getFeature("dislikeTopics", String.class);

                // Post Features
                String postContentTopics = sampleAccessor.post().getFeature("contentTopics", String.class);
                Double authorReputation = sampleAccessor.post().getFeature("reputation", Double.class);
                Integer likeCount = sampleAccessor.post().getFeature("likeCount", Integer.class);
                Integer dislikeCount = sampleAccessor.post().getFeature("dislikeCount", Integer.class);
                Integer viewCount = sampleAccessor.post().getFeature("viewCount", Integer.class);

                double score = 0.5;

                if (containsTopic(postContentTopics, userLikeTopics)) {
                    score += 0.3; // 좋아하는 주제면 가산점
                }
                if (containsTopic(postContentTopics, userDislikeTopics)) {
                    score -= 0.4; // 싫어하는 주제면 큰 감점
                }

                // 조회수 대비 좋아요 비율이 중요하도록 구성
                double popularity = Math.log10(viewCount + 1) * 0.05
                        + Math.log10(likeCount + 1) * 0.15
                        - Math.log10(dislikeCount + 1) * 0.2;
                score += popularity;

                // 작성자 명성 가산 (0.0 ~ 1.0)
                score += (authorReputation * 0.1);

                // 정규화 (0.0 ~ 1.0 사이로 클램핑)
                return Math.max(0.0, Math.min(1.0, score));
            }

            private boolean containsTopic(String target, String keywords) {
                if (target == null || keywords == null) return false;
                String[] keywordArray = keywords.split(",\\s*");
                for (String keyword : keywordArray) {
                    if (target.contains(keyword)) return true;
                }
                return false;
            }
        });
        System.out.println(evaluateResult);
    }
}
