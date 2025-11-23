package com.jintakkim.postevaluator;

public enum FeatureType {
    INTEGER {
        @Override
        public Object parse(Object value) {
            return TypeConvertUtils.toInteger(value);
        }
    },
    LONG {
        @Override
        public Object parse(Object value) {
            return TypeConvertUtils.toLong(value);
        }
    },
    STRING {
        @Override
        public Object parse(Object value) {
            return TypeConvertUtils.toString(value);
        }
    },
    DOUBLE {
        @Override
        public Object parse(Object value) {
            return TypeConvertUtils.toDouble(value);
        }
    },
    DATE {
        @Override
        public Object parse(Object value) {
            return TypeConvertUtils.toOffsetDateTime(value);
        }
    };

    public abstract Object parse(Object value);
}
