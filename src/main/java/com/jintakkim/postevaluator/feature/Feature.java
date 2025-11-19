package com.jintakkim.postevaluator.feature;

import com.jintakkim.postevaluator.core.FeatureType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Feature {
    private final String name;
    private final FeatureType type;
    private final GenerationCriteria generationCriteria;
    private final LabelingCriteria labelingCriteria;

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
