package com.jintakkim.postevaluator;

import com.google.common.base.CaseFormat;
import com.jintakkim.postevaluator.persistance.ColumnDefinition;
import com.jintakkim.postevaluator.persistance.SqlType;
import com.jintakkim.postevaluator.feature.FeatureDefinition;
import com.jintakkim.postevaluator.feature.LabelingCriteria;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public record UserDefinition(
        Map<String, FeatureDefinition> featureDefinitions,
        LabelingCriteria labelingCriteria
) implements EntitySchema {

    public Object parseFeature(String featureName, Object featureValue) {
        return featureDefinitions.get(featureName).type().parse(featureValue);
    }

    @Override
    public String getDefinitionHash() {
        String definitionString = getFeatureDefinitionHash();
        return UUID.nameUUIDFromBytes(definitionString.getBytes(StandardCharsets.UTF_8)).toString();
    }

    @Override
    public List<ColumnDefinition> getColumnDefinitions() {
        return featureDefinitions.values().stream()
                .map(feature -> new ColumnDefinition(feature.name(), SqlType.convertFeatureTypeToSqlType(feature.type()), false))
                .toList();
    }

    @Override
    public ColumnDefinition getColumnDefinitionElseThrow(String name) {
        FeatureDefinition featureDefinition = Optional.ofNullable(featureDefinitions.get(name)).orElseThrow();
        return new ColumnDefinition(featureDefinition.name(), SqlType.convertFeatureTypeToSqlType(featureDefinition.type()), false);
    }

    @Override
    public Map<String, String> getFieldToColumnMap() {
        return featureDefinitions().keySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        camelName -> camelName,
                        camelName -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelName)
                ));
    }

    @Override
    public Map<String, String> getColumnToFieldMap() {
        return featureDefinitions().keySet().stream()
                .collect(Collectors.toUnmodifiableMap(
                        camelName -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, camelName),
                        camelName -> camelName
                ));
    }

    private String getFeatureDefinitionHash() {
        return featureDefinitions.values().stream()
                .map(FeatureDefinition::getDefinitionHash)
                .sorted()
                .collect(Collectors.joining("|"));
    }
}
