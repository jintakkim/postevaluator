package com.jintakkim.postevaluator.search.param;

import com.jintakkim.postevaluator.search.space.SearchSpace;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CombinationProvider {
    private final List<Combination> combinations;

    public List<Combination> get() {
        return combinations;
    }

    public static class Builder {
        private final int searchTimes;
        private final Map<String, SearchSpace> spaces = new HashMap<>();

        public Builder(int searchTimes) {
            this.searchTimes = searchTimes;
        }

        public Builder addParameter(String paramName, SearchSpace searchSpace) {
            validateNotNull(paramName, searchSpace);
            spaces.put(paramName, searchSpace);
            return this;
        }

        public CombinationProvider build() {
            Map<String, List<Double>> values = generateValueMap();
            List<Combination> combinations = createCombinations(values);
            return new CombinationProvider(combinations);
        }

        private void validateNotNull(String paramName, SearchSpace searchSpace) {
            if(paramName == null || paramName.isBlank())
                throw new IllegalArgumentException("파라미터 네임은 null이거나 빈칸일 수 없습니다.");
            if(searchSpace == null)
                throw new IllegalArgumentException("서치 스페이스는 null일 수 없습니다");
        }

        private Map<String, List<Double>> generateValueMap() {
            Map<String, List<Double>> values = new HashMap<>();
            spaces.forEach((name, searchSpace) -> {
                List<Double> generatedValues = searchSpace.generateParamValues(searchTimes);
                values.put(name, generatedValues);
            });
            return values;
        }

        private List<Combination> createCombinations(Map<String, List<Double>> values) {
            List<Combination> combinations = new ArrayList<>();
            for (int i = 0; i < searchTimes; i++) {
                Combination currentCombination = new Combination();
                for (String paramName : values.keySet()) {
                    double value = values.get(paramName).get(i);
                    currentCombination.put(new HyperParameter(paramName, value));
                }
                combinations.add(currentCombination);
            }
            return combinations;
        }
    }
}
