package com.jintakkim.postevaluator.search.space;

import java.util.List;

public interface SearchSpace {
    List<Double> generateParamValues(int searchTimes);
}
