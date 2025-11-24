package com.jintakkim.postevaluator.config.properties;

import com.jintakkim.postevaluator.PostDefinition;
import com.jintakkim.postevaluator.UserDefinition;

public record DefinitionProperties(UserDefinition userDefinition, PostDefinition postDefinition) {}
