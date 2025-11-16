package com.jintakkim.postevaluator.test;

import java.io.InputStream;

public class FileLoadTestProperties {

    try (
    InputStream is = this.getClass().getClassLoader().getResourceAsStream("config/test-config.properties")) {
        if (is == null) {
            throw new RuntimeException("Cannot find 'config/test-config.properties' in classpath");
        }
        // ... InputStream을 사용하여 파일 읽기 로직 ...
    }

}
