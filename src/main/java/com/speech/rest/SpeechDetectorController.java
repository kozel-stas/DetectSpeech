package com.speech.rest;

import com.speech.engine.DetectionResult;
import com.speech.engine.SpeechDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.DefaultParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@RequestMapping("/speech")
public class SpeechDetectorController {

    private final Parser parser = new DefaultParser();
    private final SpeechDetector shortWordSpeechDetector;
    private final SpeechDetector frequentWordSpeechDetector;

    public SpeechDetectorController(SpeechDetector shortWordSpeechDetector, SpeechDetector frequentWordSpeechDetector) {
        this.shortWordSpeechDetector = shortWordSpeechDetector;
        this.frequentWordSpeechDetector = frequentWordSpeechDetector;
    }

    @RequestMapping(
            value = "/shortWord",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<DetectionResult> shortWord(@RequestParam("file") MultipartFile file) throws IOException, TikaException, SAXException {
        return ResponseEntity.ok(shortWordSpeechDetector.detect(asText(file)));
    }


    @RequestMapping(
            value = "/frequentWord",
            method = RequestMethod.POST,
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public ResponseEntity<DetectionResult> frequentWord(@RequestParam("file") MultipartFile file) throws IOException, TikaException, SAXException {
        return ResponseEntity.ok(frequentWordSpeechDetector.detect(asText(file)));
    }

    private String asText(MultipartFile multipartFile) throws IOException, TikaException, SAXException {
        BodyContentHandler contentHandler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        metadata.add(Metadata.CONTENT_TYPE, multipartFile.getContentType());
        parser.parse(multipartFile.getInputStream(), contentHandler, metadata, new ParseContext());
        return contentHandler.toString().trim();
    }

}
