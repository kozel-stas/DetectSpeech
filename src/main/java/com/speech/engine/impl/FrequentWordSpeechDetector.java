package com.speech.engine.impl;

import com.speech.engine.Language;
import com.speech.engine.TrainingTextProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FrequentWordSpeechDetector extends AbstractSpeechDetector implements InitializingBean {

    private final TrainingTextProvider trainingTextProvider;

    public FrequentWordSpeechDetector(TrainingTextProvider trainingTextProvider) {
        this.trainingTextProvider = trainingTextProvider;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        for (Language language : Language.values()) {
            Map<String, Double> predictions = new HashMap<>();
            Map<String, Integer> result = getWordCount(trainingTextProvider.getTrainingText(language));
            int median = median(result.values());
            reduceByThreshold(result, median);
            int sumCount = result.values().stream().mapToInt(i -> i).sum();
            for (Map.Entry<String, Integer> entry : result.entrySet()) {
                predictions.put(entry.getKey(), (double) entry.getValue() / sumCount);
            }
            languageStatMap.put(language, new LanguageStats(predictions));
        }
    }

    private static int median(Collection<Integer> collection) {
        List<Integer> sortedValue = new ArrayList<>(collection);
        sortedValue.sort(Comparator.comparingInt(k -> k));
        if (sortedValue.size() % 2 == 0) {
            return (sortedValue.get(sortedValue.size() / 2) + sortedValue.get((sortedValue.size() / 2 - 1))) / 2;
        } else {
            return sortedValue.get(sortedValue.size() / 2);
        }
    }

}
