package com.jintakkim.postevaluator.feature;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Getter
public class LabelingCriteria {
    private final List<String> values;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<String> criteria = new ArrayList<>();

        public Builder addCriterion(String criterion) {
            criteria.add(criterion);
            return this;
        }

        public LabelingCriteria build() {
            return new LabelingCriteria(criteria);
        }
    }


}
