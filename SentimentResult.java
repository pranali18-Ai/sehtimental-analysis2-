package com.jpomykala.sentiment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SentimentResult {

  private int score;
  private SentimentType type;
  private SentimentClassification classification;


}
