package com.jintakkim.postevaluator.search;

import com.jintakkim.postevaluator.search.param.HyperParameter;

public interface CombinationAccessor {
    HyperParameter get(String paramName);
}
