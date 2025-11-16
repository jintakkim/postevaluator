package com.jintakkim.postevaluator.search.space;

import java.util.ArrayList;
import java.util.List;

/**
 * 값이 매번 step만큼 증가하는 서치 스페이스
 */
public class StepIncreaseSearchSpace implements SearchSpace {
    private final double start;
    private final double step;

    public StepIncreaseSearchSpace(double start, double step) {
        this.start = start;
        this.step = step;
    }

    @Override
    public List<Double> generateParamValues(int searchTimes) {
        double curValue = start;
        List<Double> paramValues = new ArrayList<>();
        for (int i = 0; i < searchTimes; i++) {
            paramValues.add(curValue);
            curValue += step;
        }
        return paramValues;
    }
}
