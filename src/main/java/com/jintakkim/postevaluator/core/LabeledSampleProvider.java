package com.jintakkim.postevaluator.core;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;

@ThreadSafe
public interface LabeledSampleProvider {
    /**
     * labeledSample이 전부 소진될때까지 next를 호출할 수 있다.
     * @param batchSize 만큼 sample을 반환한다. 만약 부족하다면 인자보다 적은 개수가 반환될 수 있다.
     * @return 전부 반환되었다면 빈 리스트를 반환한다.
     */
    List<LabeledSample> next(int batchSize);
    int getTotalSize();
    int getLeftSize();
}
