package com.jintakkim.postevaluator.gemini;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.genai.Client;
import com.jintakkim.postevaluator.*;
import com.jintakkim.postevaluator.labeling.Labeler;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Slf4j
public class BatchGeminiLabeler extends SequentialGeminiLabeler implements Labeler {
    private static final int BATCH_SIZE = 30;
    private final ExecutorService executorService;

    public BatchGeminiLabeler(UserDefinition userDefinition, PostDefinition postDefinition, Client client, ObjectMapper objectMapper, ExecutorService executorService) {
        super(userDefinition, postDefinition, client, objectMapper);
        this.executorService = executorService;
    }

    @Override
    public void label(List<UnlabeledSample> unlabeledSamples, BatchCallback<Label> callback) {
        int sampleSize = unlabeledSamples.size();
        if (sampleSize<= 0) throw new IllegalArgumentException("생성하려는 데이터셋 사이즈는 0보다 커야 합니다.");
        if (sampleSize <= BATCH_SIZE) {
            super.label(unlabeledSamples, callback);
            return;
        }

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < sampleSize; i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, sampleSize);
            List<UnlabeledSample> batch = unlabeledSamples.subList(i, endIndex);
            CompletableFuture<Void> future = CompletableFuture.runAsync(() ->
                    generateSingleBatch(batch, callback), executorService);
            futures.add(future);
        }
        //전부 마무리 될때 까지 대기
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        log.debug("모든 병렬 작업 완료.");

    }

    private void generateSingleBatch(List<UnlabeledSample> unlabeledSamples, BatchCallback<Label> callback) {
        log.debug("Gemini API 부분 요청 ({}개)", unlabeledSamples.size());
        try {
            super.label(unlabeledSamples, callback);
        } catch (Exception e) {
            log.error("Gemini API 호출 실패", e);
            throw new RuntimeException("데이터 생성 중 오류 발생", e);
        }
    }
}
