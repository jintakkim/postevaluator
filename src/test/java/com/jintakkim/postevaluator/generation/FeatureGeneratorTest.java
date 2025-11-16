package com.jintakkim.postevaluator.generation;

import com.jintakkim.postevaluator.core.PostFeature;
import com.jintakkim.postevaluator.fixture.GeneratorConfigFixture;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 실제 api를 사용하는 통합 테스트
 * 환경 변수에 GEMINI_API_KEY가 설정되어 있어야 한다.
 */
public class FeatureGeneratorTest {
    private static final FeatureGenerator featureGenerator = GeneratorConfigFixture.DEFAULT_GENERATOR_CONFIG.featureGenerator;

    @Test
    @DisplayName("생성할려는 데이터 사이즈가 20개라면 20개 만큼의 응답이 와야한다")
    void shouldGenerateExactSameNumberOfFeaturesRequested() {
        List<PostFeature> features = featureGenerator.generate(20);
        Assertions.assertThat(features).hasSize(20);
    }
}
