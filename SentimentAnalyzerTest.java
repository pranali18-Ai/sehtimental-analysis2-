package com.jpomykala.sentiment;

import com.jpomykala.sentiment.model.Review;
import com.jpomykala.sentiment.model.SentimentResult;
import com.jpomykala.sentiment.model.SentimentType;
import com.jpomykala.sentiment.utils.DataReader;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
class SentimentAnalyzerTest {

  private SentimentAnalyzer sut = new SentimentAnalyzer();

  @Autowired
  private DataReader dataReader;

  @Test
  void findSentiment() {
    //given
    String negativeReview = "poorly acted, trying hard comedy";

    //when
    SentimentResult result = sut.findSentiment(negativeReview);

    //then
    Assertions.assertThat(result.getType()).isEqualTo(SentimentType.NEGATIVE);
  }

  @Test
  void testData() {
    List<Review> reviews = dataReader.loadTestData(200);
    int total = reviews.size();

    int numberOfMatches = 0;
    int numberOfProcessed = 0;
    for (Review review : reviews) {
      SentimentType foundType = map(sut.findSentiment(review.getText()).getType());
      SentimentType reviewType = review.getType();
      if (foundType == reviewType) {
        numberOfMatches++;
      }
      numberOfProcessed++;
      logResult(numberOfMatches, numberOfProcessed);
    }


    logResult(numberOfMatches, total);
  }

  private SentimentType map(SentimentType type) {
    switch (type) {
      case VERY_NEGATIVE:
      case NEGATIVE:
        return SentimentType.NEGATIVE;
      case POSITIVE:
      case VERY_POSITIVE:
        return SentimentType.POSITIVE;
      default:
        return SentimentType.NEUTRAL;
    }

  }

  private void logResult(int positiveMatch, int total) {
    log.info("{}/{} = {}% hits", positiveMatch, total, positiveMatch * 100 / total);
  }
}
