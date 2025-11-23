package com.jintakkim.postevaluator.core.persistance.initialization;

public enum InitStatus {
    /**
     * 관련 스키마가 존재하지 않아 새롭게 스키마를 생성했을때
     */
    NOT_EXISTS,
    /**
     * 관련 스키마가 변경되어 다시 스키마를 생성했을때
     */
    CHANGED,
    /**
     * 관련 스키마가 변경되지 않아 아무 행동도 하지 않았을때
     */
    NOT_CHANGED
}
