package com.speech.engine.impl;

import com.speech.engine.DetectionResult;
import com.speech.engine.Language;
import com.speech.engine.SpeechDetector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractSpeechDetector implements SpeechDetector {

    private static final String WORDS_SEPARATOR = "((\\s+)|([,.()\\[\\]:-;—/1234567890_&^%$#@!<>\"«»]))";
    private static final double DEFAULT_PREDICTION = 0.0001;

    protected final Map<Language, LanguageStats> languageStatMap = new HashMap<>();

    @Override
    public DetectionResult detect(String text) {
        Map<Language, Double> result = new HashMap<>();
        Set<String> words = getWordCount(text).keySet();
        for (Map.Entry<Language, LanguageStats> entry : languageStatMap.entrySet()) {
            double prediction = 1;
            for (String word : words) {
                prediction *= entry.getValue().predictions.getOrDefault(word, DEFAULT_PREDICTION);
            }
            result.put(entry.getKey(), prediction);
        }
        return new DetectionResult(result);
    }

    protected Map<String, Integer> getWordCount(String text) {
        Map<String, Integer> result = new HashMap<>();
        String[] words = text.split(WORDS_SEPARATOR);
        for (String word : words) {
            if (reduceStrategy(word) && !word.equals("")) {
                result.computeIfPresent(word, (k, v) -> ++v);
                result.putIfAbsent(word, 1);
            }
        }
        return result;
    }

    protected static void reduceByThreshold(Map<?, Integer> map, int threshold) {
        map.values().removeIf(val -> val < threshold);
    }

    protected boolean reduceStrategy(String word) {
        return true;
    }

    protected static class LanguageStats {
        private final Map<String, Double> predictions;

        protected LanguageStats(Map<String, Double> predictions) {
            this.predictions = predictions;
        }

    }

}
