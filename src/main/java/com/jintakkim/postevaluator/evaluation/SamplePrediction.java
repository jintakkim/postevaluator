package com.jintakkim.postevaluator.evaluation;

import com.jintakkim.postevaluator.LabeledSample;

public record SamplePrediction(
        long postId,
        long userId,
        double labeledScore,
        String labeledReason,
        double predictScore
) {
    public SamplePrediction(LabeledSample labeledSample, double predictScore) {
        this(labeledSample.post().id(), labeledSample.user().id(), labeledSample.labelScore(), labeledSample.labelReason(), predictScore);
    }
}
