package com.jintakkim.postevaluator.labeling;

import com.jintakkim.postevaluator.Post;
import com.jintakkim.postevaluator.User;
import com.jintakkim.postevaluator.feature.LabelingCriteria;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LabelPrompt {
    private static final String CONTENT_TEMPLATE = """
            <지시문>
            JSON 배열 형태의 '게시글' 데이터 리스트이다
            각 게시글을 주어진 유저, 게시글 특징을 고려해서 고려하여 0.0 ~ 1.0 사이의 스코어를 매기고 이유를 작성하고 출력한다.
            <유저 평가 기준 설명>
            %s
            <유저 데이터>
            %s
            <게시글 평가 기준 설명>
            %s
            <게시글 특징 데이터>
            %s
            <출력>
            출력은 스키마를 따른다
            """;

    public static String generateContent(
            LabelingCriteria userLabelingCriteria,
            User user,
            LabelingCriteria postLabelingCriteria,
            List<Post> posts
    ) {
        return String.format(CONTENT_TEMPLATE,
                userLabelingCriteria.toString(),
                user.toString(),
                postLabelingCriteria.toString(),
                posts.toString()
        );
    }
}
