package com.jintakkim.postevaluator.client;

import com.jintakkim.postevaluator.config.*;
import com.jintakkim.postevaluator.core.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.EvaluateResult;
import com.jintakkim.postevaluator.search.GridSearcher;
import com.jintakkim.postevaluator.search.ParametricRecommendAlgorithm;
import com.jintakkim.postevaluator.search.SearchResult;
import com.jintakkim.postevaluator.search.param.CombinationProvider;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PostRecommendTest {
    private final ApplicationConfig appConfig;

    public PostRecommendTest(
            ApplicationConfig appConfig
    ) {
        this.appConfig = appConfig;
    }

    public EvaluateResult test(RecommendAlgorithm recommendAlgorithm) {
        return appConfig.evaluatorConfig.evaluator.evaluate(recommendAlgorithm);
    }

    public List<EvaluateResult> bulkTest(List<RecommendAlgorithm> recommendAlgorithms) {
        return appConfig.evaluatorConfig.bulkEvaluator.evaluate(recommendAlgorithms);
    }

    public SearchResult gridSearchTest(
            CombinationProvider combinationProvider,
            ParametricRecommendAlgorithm parametricRecommendAlgorithm
    ) {
        GridSearcher gridSearcher = new GridSearcher(appConfig.evaluatorConfig.evaluator, combinationProvider);
        return gridSearcher.search(parametricRecommendAlgorithm);
    }
}
