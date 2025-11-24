package com.jintakkim.postevaluator.config.properties;

import com.jintakkim.postevaluator.search.space.SearchSpace;

import java.util.Map;

public record SearchProperties(
        int searchTimes,
        Map<String, SearchSpace> spaces
) {
}
