package com.jintakkim.postevaluator.gemini;

import com.google.genai.Client;
import com.google.genai.types.*;
import com.jintakkim.postevaluator.BatchCallback;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
public abstract class AbstractBatchGeminiGenerator<T> extends AbstractGeminiGenerator<T> {
    private static final int BATCH_SIZE = 30;
    private final ExecutorService executorService;

    public AbstractBatchGeminiGenerator(Client client, String entityName, GenerateContentConfig generateContentConfig, ExecutorService executorService) {
        super(client, entityName, generateContentConfig);
        this.executorService = executorService;
    }

    @Override
    public void generate(int totalSize, BatchCallback<T> callback) {
        if (totalSize <= 0) throw new IllegalArgumentException("생성하려는 데이터셋 사이즈는 0보다 커야 합니다.");
        if (totalSize <= BATCH_SIZE) {
            super.generate(totalSize, callback);
            return;
        }
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        int remaining = totalSize;

        while (remaining > 0) {
            int currentBatchSize = Math.min(remaining, BATCH_SIZE);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                    generateSingleBatch(currentBatchSize, callback), executorService);
            futures.add(future);
            remaining -= currentBatchSize;
        }
        //전부 마무리 될때 까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.debug("모든 병렬 작업 완료.");
    }

    private void generateSingleBatch(int size, BatchCallback<T> callback) {
        log.debug("Gemini API 부분 요청 ({}개)", size);
        try {
            super.generate(size, callback);
        } catch (Exception e) {
            log.error("Gemini API 호출 실패 (size={})", size, e);
            throw new RuntimeException("데이터 생성 중 오류 발생", e);
        }
    }
}
