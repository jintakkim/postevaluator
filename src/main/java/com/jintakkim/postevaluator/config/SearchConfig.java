package com.jintakkim.postevaluator.config;

import com.jintakkim.postevaluator.config.properties.SearchProperties;
import com.jintakkim.postevaluator.search.GridSearcher;
import com.jintakkim.postevaluator.search.param.CombinationProvider;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SearchConfig {
    private final static String WARN_MESSAGE = "Search 관련 설정이 되어있지 않습니다, Grid Search를 하기 위해서는 searchProperties를 ApplicationConfig의 인자로 전달해야합니다.";

    private final GridSearcher gridSearcher;
    public volatile boolean isReady = false;

    public SearchConfig(SearchProperties searchProperties, EvaluatorConfig evaluatorConfig) {
        this.gridSearcher = buildGridSearcher(searchProperties, evaluatorConfig);
    }

    public GridSearcher getGridSearcher() {
        if(isReady) return gridSearcher;
        throw new IllegalStateException(WARN_MESSAGE);
    }

    private GridSearcher buildGridSearcher(SearchProperties searchProperties, EvaluatorConfig evaluatorConfig) {
        if(searchProperties == null) {
            log.warn(WARN_MESSAGE);
            return null;
        }
        isReady = true;
        return new GridSearcher(evaluatorConfig.evaluator, buildCombinationProvider(searchProperties));
    }

    private CombinationProvider buildCombinationProvider(SearchProperties searchProperties) {
        CombinationProvider.Builder builder = new CombinationProvider.Builder(searchProperties.searchTimes());
        searchProperties.spaces().forEach(builder::addParameter);
        return builder.build();
    }
}
