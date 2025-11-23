package com.jintakkim.postevaluator.core.feature;

import com.jintakkim.postevaluator.core.FeatureType;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Getter
public record FeatureDefinition(
        String name,
        FeatureType type,
        GenerationCriteria generationCriteria,
        LabelingCriteria labelingCriteria
) {
    public String getDefinitionHash() {
        String definitionString = String.join(
                name,
                type.name(),
                generationCriteria.toString(),
                labelingCriteria.toString()
        );
        return UUID.nameUUIDFromBytes(definitionString.getBytes(StandardCharsets.UTF_8)).toString();
    }

    public String getFormattedLabelingCriteria() {
        return name + ":" + labelingCriteria.getValues();
    }
}
