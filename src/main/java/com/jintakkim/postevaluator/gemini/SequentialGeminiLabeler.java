package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.labeling.Labeler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 전체 유저에 대해 순자적으로 api를 콜한다.
 */
public class SequentialGeminiLabeler extends SingleUserGeminiLabeler implements Labeler {
    public SequentialGeminiLabeler(UserDefinition userDefinition, PostDefinition postDefinition, Client client, ObjectMapper objectMapper) {
        super(userDefinition, postDefinition, client, objectMapper);
    }

    @Override
    public List<Label> label(List<UnlabeledSample> unlabeledSamples) {
        Map<User, List<UnlabeledSample>> samplesByUser = unlabeledSamples.stream()
                .collect(Collectors.groupingBy(UnlabeledSample::user));

        return samplesByUser.entrySet().stream()
                .map(entry -> {
                    User user = entry.getKey();
                    List<Post> posts = entry.getValue().stream()
                            .map(UnlabeledSample::post)
                            .toList();
                    return super.label(user, posts);
                })
                .flatMap(List::stream)
                .toList();
    }
}
