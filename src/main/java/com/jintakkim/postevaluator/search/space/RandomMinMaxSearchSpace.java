package com.jintakkim.postevaluator.search.space;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMinMaxSearchSpace implements SearchSpace {
    private static final Random random = new Random();
    private final double minValue;
    private final double maxValue;

    public RandomMinMaxSearchSpace(double minValue, double maxValue) {
        validate();
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    @Override
    public List<Double> generateParamValues(int searchTimes) {
        List<Double> paramValues = new ArrayList<>();
        for (int i = 0; i < searchTimes; i++)
            paramValues.add(random.nextDouble());
        return paramValues;
    }

    private void validate() {
        if (minValue > maxValue) throw new IllegalArgumentException("최대값이 최소값보다 커야합니다.");
    }

    private double generateRandomValue() {
        double range = maxValue - minValue;
        return minValue + range * random.nextDouble();
    }
}
