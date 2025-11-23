package com.jintakkim.postevaluator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TypeConvertUtils {
    public static Integer toInteger(Object value) {
        if (value instanceof Number n) return n.intValue();
        if (value instanceof String s) {
            try {
                return Integer.parseInt(s.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Integer 타입이 아닙니다: " + value, e);
            }
        }
        throw new IllegalArgumentException("Integer 타입이 아닙니다: " + value);
    }

    public static Long toLong(Object value) {
        if (value instanceof Number n) return n.longValue();
        if (value instanceof String s) {
            try {
                return Long.parseLong(s.trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Long 타입이 아닙니다: " + value, e);
            }
        }
        throw new IllegalArgumentException("Long 타입이 아닙니다: " + value);
    }

    public static String toString(Object value) {
        if (value instanceof String s) return s;
        return String.valueOf(value);
    }

    public static Double toDouble(Object value) {
        if (value instanceof Number n) {
            return n.doubleValue();
        }
        if (value instanceof String s) {
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("숫자 형식이 아닙니다 (Double 기대): " + value);
            }
        }
        throw new IllegalArgumentException("DOUBLE 타입으로 변환할 수 없는 값입니다: " + value);
    }

    public static OffsetDateTime toOffsetDateTime(Object value) {
        if (value instanceof OffsetDateTime odt) {
            return odt;
        }
        if (value instanceof String s) {
            try {
                return OffsetDateTime.parse(s.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("날짜 형식이 잘못되었습니다 (ISO-8601(offset 포함) 형식을 기대): " + value);
            }
        }
        throw new IllegalArgumentException("ISO-8601(offset 포함)으로 변환 할수 있는 값이 아닙니다: " + value);
    }
}
