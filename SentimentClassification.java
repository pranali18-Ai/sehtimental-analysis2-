package com.jpomykala.sentiment.model;

import lombok.Data;

@Data
public class SentimentClassification {
  private double veryPositive;
  private double positive;
  private double neutral;
  private double negative;
  private double veryNegative;
}
