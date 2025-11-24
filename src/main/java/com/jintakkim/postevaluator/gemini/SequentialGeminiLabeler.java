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
    public void label(List<UnlabeledSample> unlabeledSamples, BatchCallback<Label> callback) {
        Map<User, List<Post>> samplesByUser = samplesByUser(unlabeledSamples);
        samplesByUser.forEach((user, posts) -> super.label(user, posts, callback));
    }

    private Map<User, List<Post>> samplesByUser(List<UnlabeledSample> unlabeledSamples) {
        return unlabeledSamples.stream().collect(Collectors.groupingBy(UnlabeledSample::user, Collectors.mapping(
                UnlabeledSample::post,
                Collectors.toList()
        )));
    }
}
