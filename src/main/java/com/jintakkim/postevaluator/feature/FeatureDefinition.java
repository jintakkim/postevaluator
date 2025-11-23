package com.jintakkim.postevaluator.feature;

import com.jintakkim.postevaluator.FeatureType;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public record FeatureDefinition(
        String name,
        FeatureType type,
        GenerationCriteria generationCriteria
) {
    public String getDefinitionHash() {
        String definitionString = String.join(
                name,
                type.name(),
                generationCriteria.toString()
        );
        return UUID.nameUUIDFromBytes(definitionString.getBytes(StandardCharsets.UTF_8)).toString();
    }
}
