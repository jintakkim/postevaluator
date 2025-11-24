package com.jintakkim.postevaluator.gemini;

import java.util.List;

public interface BatchCallback<T> {
    void call(List<T> t);
}
