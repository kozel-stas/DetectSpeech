package com.speech.engine.impl;

import com.speech.engine.Language;
import com.speech.engine.TrainingTextProvider;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.IOUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;

import java.io.IOException;

@Repository
public class TrainingTextProviderImpl implements TrainingTextProvider {

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    private final Parser parser = new HtmlParser();

    @Override
    public String getTrainingText(Language language) {
        try {
            BodyContentHandler bodyContentHandler = new BodyContentHandler(-1);
            parser.parse(resourceLoader.getResource(language.name().toLowerCase() + ".html").getInputStream(), bodyContentHandler, new Metadata(), new ParseContext());
            return bodyContentHandler.toString();
        } catch (IOException | TikaException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

}
