package com.jpomykala.sentiment.utils;

import com.google.common.collect.Lists;
import com.jpomykala.sentiment.model.Review;
import com.jpomykala.sentiment.model.SentimentType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DataReader {

  private static final String NEGATIVE_REVIEWS = "/Users/jpomykala/Downloads/aclImdb/train/neg";
  private static final String POSITIVE_REVIEWS = "/Users/jpomykala/Downloads/aclImdb/train/pos";
  private static final int MIN_REVIEW_LENGTH = 50;
  private final DirectoryFileReader directoryFileReader;


  public DataReader(DirectoryFileReader directoryFileReader) {
    this.directoryFileReader = directoryFileReader;
  }

  public List<Review> loadTestData(int limit) {
    List<Review> negativeReviews = loadNegativeReviews();
    List<Review> positiveReviews = loadPositiveReviews();

    List<Review> dataSet = Lists.newArrayList();
    dataSet.addAll(negativeReviews);
    dataSet.addAll(positiveReviews);
    Collections.shuffle(dataSet);
    return dataSet.stream().limit(limit).collect(Collectors.toList());
  }

  public List<Review> loadNegativeReviews() {
    return loadReviewsAndSetupType(NEGATIVE_REVIEWS, SentimentType.NEGATIVE);
  }

  public List<Review> loadPositiveReviews() {
    return loadReviewsAndSetupType(POSITIVE_REVIEWS, SentimentType.POSITIVE);
  }

  private List<Review> loadReviewsAndSetupType(String path, SentimentType type) {
    try {
      return directoryFileReader
              .readTextFilesInDirectory(Paths.get(path))
              .stream()
              .filter(Objects::nonNull)
              .filter(s -> s.length() > MIN_REVIEW_LENGTH)
              .map(s -> new Review(s, type))
              .collect(Collectors.toList());
    } catch (IOException e) {
      log.error("Could not load reviews from path {}", path, e);
    }
    return Collections.emptyList();
  }

}
