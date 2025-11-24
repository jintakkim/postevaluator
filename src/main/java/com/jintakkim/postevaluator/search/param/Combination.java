package com.jintakkim.postevaluator.search.param;

import com.jintakkim.postevaluator.search.CombinationAccessor;

import java.util.*;

public class Combination implements CombinationAccessor {
    private final Map<String, HyperParameter> parameters;

    public Combination(Map<String, HyperParameter> parameters) {
        this.parameters = parameters;
    }

    public Combination() {
        this(new HashMap<>());
    }

    public void put(HyperParameter parameter) {
        parameters.put(parameter.name(), parameter);
    }

    @Override
    public HyperParameter get(String paramName) {
        return findByNameElseThrow(paramName);
    }

    private HyperParameter findByNameElseThrow(String paramName) {
        return Optional.ofNullable(parameters.get(paramName))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파라미터 이름입니다"));
    }
}
