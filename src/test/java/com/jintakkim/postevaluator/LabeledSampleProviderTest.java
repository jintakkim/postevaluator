package com.jintakkim.postevaluator;

import com.jintakkim.postevaluator.persistance.SetupStrategy;
import com.jintakkim.postevaluator.test.SimpleFixtureAppliedTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class LabeledSampleProviderTest extends SimpleFixtureAppliedTest {

    @Test
    @DisplayName("SetupStrategy가 REUSE_PARTITION일때 실제 db에 데이터가 설정 데이더 셋보다 많더라도 설정 데이터 셋만큼의 데이터만 반환해야한다")
    void shouldReturnOnlyRemainingSamplesWhenRequestExceedsTotalSize() {
        LabeledSampleProvider labeledSampleProvider = createLabeledSampleProvider(2, 2, SetupStrategy.REUSE_PARTITION);
        List<LabeledSample> labeledSamples = labeledSampleProvider.get(2, 3);
        //3번째 데이터 부터 3,4,5번째 데이터 셋을 가져오기 위해 시도하지만 총 데이터 셋이 4개이므로 3,4번째 데이터 셋만 가져오기 떄문에 2개
        Assertions.assertThat(labeledSamples).hasSize(2);
    }

    @Test
    @DisplayName("SetupStrategy가 REUSE_STRICT일때 실제 db에 데이터가 설정 데이더 셋보다 많더라도 설정 데이터 셋만큼의 데이터만 반환해야한다")
    void shouldCapResultSizeToTargetDatasetSize() {
        LabeledSampleProvider labeledSampleProvider = createLabeledSampleProvider(4, 4, SetupStrategy.REUSE_STRICT);
        List<LabeledSample> labeledSamples = labeledSampleProvider.get(0, 17);
        Assertions.assertThat(labeledSamples).hasSize(16);
    }
}
