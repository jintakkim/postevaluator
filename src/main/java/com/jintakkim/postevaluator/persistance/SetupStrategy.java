package com.jintakkim.postevaluator.persistance;

public enum SetupStrategy {
    /**
     * 기존 데이터가 있더라도 다 삭제후 실행한다.
     */
    CLEAR,
    /**
     * 기존 데이터가 있다면 재사용한다.
     * 기존 데이터가 필요한 데이터보다 많을 경우 일부를 사용한다.
     */
    REUSE_PARTITION,
    /**
     * 기존 데이터가 있다면 재사용한다.
     * 기존 데이터가 필요한 데이터보다 많을 경우 예외가 발생한다.
     */
    REUSE_STRICT
}
