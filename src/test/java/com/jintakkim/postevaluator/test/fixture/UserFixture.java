package com.jintakkim.postevaluator.test.fixture;

import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.test.TestRefectionUtils;

import java.util.Map;

public class UserFixture {
    /**
     * DefinitionFixture의 UserDefinition_1을 고려한 fixture
     */
    public static final class DEFINITION_1 {
        private DEFINITION_1() {}

        public static final User USER_1 = new User(null, Map.of(
                "likeTopic", "gaming",
                "dislikeTopic", "coding"
        ));
        public static final User USER_2 = new User(null, Map.of(
                "likeTopic", "coding",
                "dislikeTopic", "working"
        ));
        public static final User USER_3 = new User(null, Map.of(
                "likeTopic", "running",
                "dislikeTopic", "walking"
        ));
        public static final User USER_4 = new User(null, Map.of(
                "likeTopic", "music",
                "dislikeTopic", "studying"
        ));
    }

    public static User setId(User user, Long id) {
        return new User(id, user.features());
    }
}
