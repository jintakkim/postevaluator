package com.jintakkim.postevaluator.client;

import com.jintakkim.postevaluator.config.*;
import com.jintakkim.postevaluator.RecommendAlgorithm;
import com.jintakkim.postevaluator.evaluation.EvaluateResult;
import com.jintakkim.postevaluator.search.GridSearcher;
import com.jintakkim.postevaluator.search.ParametricRecommendAlgorithm;
import com.jintakkim.postevaluator.search.SearchResult;
import com.jintakkim.postevaluator.search.param.CombinationProvider;

public class PostRecommendTest {
    private final ApplicationConfig appConfig;

    public PostRecommendTest(
            ApplicationConfig appConfig
    ) {
        this.appConfig = appConfig;
    }

    public EvaluateResult simpleTest(RecommendAlgorithm recommendAlgorithm) {
        return appConfig.evaluatorConfig.evaluator.evaluate(recommendAlgorithm);
    }

    public SearchResult gridSearchTest(
            CombinationProvider combinationProvider,
            ParametricRecommendAlgorithm parametricRecommendAlgorithm
    ) {
        GridSearcher gridSearcher = new GridSearcher(appConfig.evaluatorConfig.evaluator, combinationProvider);
        return gridSearcher.search(parametricRecommendAlgorithm);
    }
}
