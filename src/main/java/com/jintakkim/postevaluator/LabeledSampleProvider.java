package com.jintakkim.postevaluator;

import javax.annotation.concurrent.ThreadSafe;
import java.util.List;

@ThreadSafe
public interface LabeledSampleProvider {
    /**
     * @return 전부 반환되었다면 빈 리스트를 반환한다.
     */
    List<LabeledSample> get(int offset, int limit);

    /**
     * 데이터 셋이 한번에 메모리에 올리지 못할 정도로 많다면 OOM 발생가능
     */
    List<LabeledSample> getAll();
    int getTotalSize();
}
