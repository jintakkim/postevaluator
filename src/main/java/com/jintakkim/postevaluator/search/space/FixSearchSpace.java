package com.jintakkim.postevaluator.search.space;

import java.util.Collections;
import java.util.List;

/**
 * 값이 고정된 서치 스페이스
 * 탐색마다 값을 고정시키고 싶을때 사용
 */
public class FixSearchSpace implements SearchSpace {
    private final double value;

    public FixSearchSpace(double value) {
        this.value = value;
    }

    @Override
    public List<Double> generateParamValues(int searchTimes) {
        return Collections.nCopies(searchTimes, value);
    }
}
