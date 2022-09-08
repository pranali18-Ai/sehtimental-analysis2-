package com.jpomykala.sentiment;

import com.google.common.base.Enums;
import com.jpomykala.sentiment.model.SentimentClassification;
import com.jpomykala.sentiment.model.SentimentResult;
import com.jpomykala.sentiment.model.SentimentType;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import lombok.extern.slf4j.Slf4j;
import org.ejml.simple.SimpleMatrix;
import org.springframework.stereotype.Component;

import java.util.Properties;


@Slf4j
@Component
public class SentimentAnalyzer {

  private StanfordCoreNLP pipeline;

  public SentimentAnalyzer() {
    // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and sentiment
    Properties props = new Properties();
    props.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
    pipeline = new StanfordCoreNLP(props);
  }

  public SentimentResult findSentiment(String text) {
    try {
      return tryFindSentiment(text);
    } catch (Exception e) {
      log.error("Sentiment could not be found for {}", text, e);
      throw e;
    }
  }

  private SentimentResult tryFindSentiment(String text) {

    SentimentResult sentimentResult = new SentimentResult();
    SentimentClassification classification = new SentimentClassification();

    if (text != null && text.length() > 0) {
      Annotation annotation = pipeline.process(text);

      for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
        Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
        SimpleMatrix simplePredictionMatrix = RNNCoreAnnotations.getPredictions(tree);
        String sentimentString = sentence.get(SentimentCoreAnnotations.SentimentClass.class).toUpperCase().replace(" ", "_");
        SentimentType sentimentType = Enums.getIfPresent(SentimentType.class, sentimentString).orNull();

        classification.setVeryPositive((double) Math.round(simplePredictionMatrix.get(4) * 100d));
        classification.setPositive((double) Math.round(simplePredictionMatrix.get(3) * 100d));
        classification.setNeutral((double) Math.round(simplePredictionMatrix.get(2) * 100d));
        classification.setNegative((double) Math.round(simplePredictionMatrix.get(1) * 100d));
        classification.setVeryNegative((double) Math.round(simplePredictionMatrix.get(0) * 100d));

        sentimentResult.setScore(RNNCoreAnnotations.getPredictedClass(tree));
        sentimentResult.setType(sentimentType);
        sentimentResult.setClassification(classification);
      }
    }
    return sentimentResult;
  }
}
