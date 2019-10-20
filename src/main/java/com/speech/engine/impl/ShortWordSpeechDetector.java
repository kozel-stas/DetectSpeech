package com.speech.engine.impl;

import com.speech.engine.Language;
import com.speech.engine.TrainingTextProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ShortWordSpeechDetector extends AbstractSpeechDetector implements InitializingBean {

    private static final int MAX_WORD_LENGTH = 5;
    private static final int MIN_WORD_COUNT = 1;

    private final TrainingTextProvider trainingTextProvider;

    public ShortWordSpeechDetector(TrainingTextProvider trainingTextProvider) {
        this.trainingTextProvider = trainingTextProvider;
    }

    @Override
    public void afterPropertiesSet() {
        for (Language language : Language.values()) {
            Map<String, Double> predictions = new HashMap<>();
            Map<String, Integer> result = getWordCount(trainingTextProvider.getTrainingText(language));
            reduceByThreshold(result, MIN_WORD_COUNT);
            int sumCount = result.values().stream().mapToInt(i -> i).sum();
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                predictions.put(entry.getKey(), (double) entry.getValue() / sumCount);
            }
            languageStatMap.put(language, new LanguageStats(predictions));
        }
    }

    @Override
    protected boolean reduceStrategy(String word) {
        return word.length() <= MAX_WORD_LENGTH;
    }

}
