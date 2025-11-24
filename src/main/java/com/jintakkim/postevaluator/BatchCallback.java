package com.jintakkim.postevaluator;

import java.util.List;

public interface BatchCallback<T> {
    void call(List<T> t);
}
