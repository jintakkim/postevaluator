package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.labeling.Labeler;

import java.util.List;

/**
 * 전체 유저에 대해 순자적으로 api를 콜한다.
 */
public class SequentialGeminiLabeler extends SingleUserGeminiLabeler implements Labeler {
    public SequentialGeminiLabeler(UserDefinition userDefinition, PostDefinition postDefinition, Client client, ObjectMapper objectMapper) {
        super(userDefinition, postDefinition, client, objectMapper);
    }

    @Override
    public List<Label> label(List<User> users, List<Post> posts) {
        return users.stream().map(user -> super.label(user, posts)).flatMap(List::stream).toList();
    }
}
