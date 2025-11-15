package com.jintakkim.postevaluator.core;

public enum SetupStrategy {
    /**
     * 기존 데이터가 있더라도 다 삭제후 실행한다.
     */
    CLEAR,
    /**
     * 기존 데이터가 있다면 재사용한다.
     * 기존 데이터가 필요한 데이터보다 많을 경우 랜덤하게 선택하여 사용한다.
     */
    REUSE_RANDOM,
    /**
     * 기존 데이터가 있다면 재사용한다.
     * 기존 데이터가 필요한 데이터보다 많을 경우 예외가 발생한다.
     */
    REUSE_STRICT
}
