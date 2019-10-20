package com.speech.engine;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DetectionResult {

    private final Map<Language, Double> languages;

    public DetectionResult(Map<Language, Double> languages) {
        this.languages = new HashMap<>(languages);
    }

    public Map<Language, Double> getLanguages() {
        return Collections.unmodifiableMap(languages);
    }

}
