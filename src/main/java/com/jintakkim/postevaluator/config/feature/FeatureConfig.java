package com.jintakkim.postevaluator.config.feature;

import com.jintakkim.postevaluator.client.RecommendTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FeatureConfig {
    public final ViewCountConfig viewCountConfig;
    public final LikeCountConfig likeCountConfig;
    public final DislikeCountConfig dislikeCountConfig;
    public final CommentCountConfig commentCountConfig;
    public final ContentConfig contentConfig;
    public final CreatedAtConfig createdAtConfig;

    private FeatureConfig(
            ViewCountConfig viewCountConfig,
            LikeCountConfig likeCountConfig,
            DislikeCountConfig dislikeCountConfig,
            CommentCountConfig commentCountConfig,
            ContentConfig contentConfig,
            CreatedAtConfig createdAtConfig
    ) {
        this.viewCountConfig = viewCountConfig;
        this.likeCountConfig = likeCountConfig;
        this.dislikeCountConfig = dislikeCountConfig;
        this.commentCountConfig = commentCountConfig;
        this.contentConfig = contentConfig;
        this.createdAtConfig = createdAtConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ViewCountConfig viewCountConfig;
        private LikeCountConfig likeCountConfig;
        private DislikeCountConfig dislikeCountConfig;
        private CommentCountConfig commentCountConfig;
        private ContentConfig contentConfig;
        private CreatedAtConfig createdAtConfig;

        public Builder viewCountConfig(ViewCountConfig viewCountConfig) {
            this.viewCountConfig = viewCountConfig;
            return this;
        }

        public Builder likeCountConfig(LikeCountConfig likeCountConfig) {
            this.likeCountConfig = likeCountConfig;
            return this;
        }

        public Builder dislikeCountConfig(DislikeCountConfig dislikeCountConfig) {
            this.dislikeCountConfig = dislikeCountConfig;
            return this;
        }

        public Builder commentCountConfig(CommentCountConfig commentCountConfig) {
            this.commentCountConfig = commentCountConfig;
            return this;
        }

        public Builder contentConfig(ContentConfig contentConfig) {
            this.contentConfig = contentConfig;
            return this;
        }

        public Builder createdAtConfig(CreatedAtConfig createdAtConfig) {
            this.createdAtConfig = createdAtConfig;
            return this;
        }

        public FeatureConfig build() {
            if(viewCountConfig == null) {
                log.warn("조회수 관련 설정을 하지 않았습니다. 실제 앱과 유사한 테스트를 위해서 조회수 설정을 하는 것을 권장합니다.");
                this.viewCountConfig = ViewCountConfig.builder().build();
            }
            if(likeCountConfig == null) {
                log.warn("좋아요수 관련 설정을 하지 않았습니다. 실제 앱과 유사한 테스트를 위해서 좋아요수 설정을 하는 것을 권장합니다.");
                this.likeCountConfig = LikeCountConfig.builder().build();
            }
            if(dislikeCountConfig == null) {
                log.warn("싫어요수 관련 설정을 하지 않았습니다. 실제 앱과 유사한 테스트를 위해서 싫어요수 설정을 하는 것을 권장합니다.");
                this.dislikeCountConfig = DislikeCountConfig.builder().build();
            }
            if(commentCountConfig == null) {
                log.warn("댓글수 관련 설정을 하지 않았습니다. 실제 앱과 유사한 테스트를 위해서 댓글수 설정을 하는 것을 권장합니다.");
                this.commentCountConfig = CommentCountConfig.builder().build();
            }
            if(contentConfig == null) {
                log.warn("컨텐츠 관련 설정을 하지 않았습니다. 실제 앱과 유사한 테스트를 위해서 컨텐츠 설정을 하는 것을 권장합니다.");
                this.contentConfig = ContentConfig.builder().build();
            }
            if(createdAtConfig == null) {
                log.warn("생성일 관련 설정을 하지 않았습니다. 실제 앱과 유사한 테스트를 위해서 생성일 설정을 하는 것을 권장합니다.");
                this.createdAtConfig = CreatedAtConfig.builder().build();
            }
            return new FeatureConfig(
                    viewCountConfig, likeCountConfig, dislikeCountConfig, commentCountConfig, contentConfig, createdAtConfig
            );
        }
    }
}
