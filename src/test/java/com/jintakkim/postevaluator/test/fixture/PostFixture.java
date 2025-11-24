package com.jintakkim.postevaluator.test.fixture;

import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.test.TestRefectionUtils;

import java.util.Map;

public final class PostFixture {
    private PostFixture() {}
    /**
     * DefinitionFixture의 PostDefinition_1을 고려한 fixture
     */
    public static final class DEFINITION_1 {
        private DEFINITION_1() {}

        public static final Post POST_1 = new Post(null, Map.of(
                "commentCount", 100,
                "content", "오늘 날씨가 좋네요"
        ));
        public static final Post POST_2 = new Post(null, Map.of(
                "commentCount", 10,
                "content", "할게 많다."
        ));
        public static final Post POST_3 = new Post(null, Map.of(
                "commentCount", 5,
                "content", "오늘이 벌써 마감일"
        ));
        public static final Post POST_4 = new Post(null, Map.of(
                "commentCount", 25,
                "content", "화창한 날씨 입니다."
        ));
    }

    public static Post setId(Post post, Long id) {
        return new Post(id, post.features());
    }
}
