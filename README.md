# Sentiment Analysis Experiment

I found the article about sentiment analysis.

http://www.hausmanmarketingletter.com/sad-state-sentiment-analysis/

and this statement:

> "Recent experiments suggest sentiment analysis data is LESS accurate than a coin toss (accuracy 50%). That’s really scary if your brand makes strategic decisions based on sentiment analysis."

made me curious. I decided to quickly run some tests on my own. 

## Test dataset
I used IMDB reviews from Stanford library.
https://ai.stanford.edu/~amaas/data/sentiment/


## Tech stack:
- Java 11
- Spring Framework (just for dependency injection)
- Stanford Core NLP


## Results:

Algorithm was right in ~50% cases, so the statement from the article was right 😉 Maybe I will try to improve this scores in the future.

![results](https://github.com/jpomykala/SentimentAnalysis-Experiment/blob/master/images/results.png?raw=true)



![memory](https://github.com/jpomykala/SentimentAnalysis-Experiment/blob/master/images/visual-vm.png?raw=true)


## How to load different data?
Check `DataReader.java` class. You can setup your own directories to positivie and negative reviews.

## Contribution 
If you think I made something wrong or you know how I can improve the results let me know or make a PR :) 
