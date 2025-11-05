package com.jintakkim.postevaluator.evaluate.metric;

import java.util.List;

public interface AlgorithmMetric {
    /**
     * 알고리즘 평가시 사용된 메트릭 이름( 예: MSE)
     */
    String getName();

    /**
     * 비용(cost)을 계산하는 로직
     * @param score 스코어
     * @param scorePredict 알고리즘으로 부터 예측된 스코어, 스코어와 리스트 내에서의 인덱스가 일치해야한다.
     * @return 비용
     */
    double calculateCost(List<Double> score, List<Double> scorePredict);
}
