# Naive Bayes Classifier - BITS F464 Assignment 3

## Introduction

The Naive Bayes Classifier is a very power machine learning technique that provides exceptionally good accuracy and precision with excellent time complexity compared to other algorithms such as ID3.

In this project, a Naive Bayes Classifier is trained to classify movie reviews using the training dataset from Stanford's IMDB dataset. This is tested against the testing dataset to obtain the results for accuracy, precision, and recall. This project is written in Java and uses HashMaps for sstoring the key (the ID of the word according to vocab.txt) and the value being the number of times the word occurs in the dataset. The classifier has been trained in three different ways - the standard Naive Bayes Classifier, a binary variation of the Naive Bayes Classifier, and another instance of the Naive Bayes Classifier but with stop words removed from the dataset.

## Results

### Output

```
Standard Naive Bayes Classifier
-----------------
Positive precision: 0.8153774
Positive recall: 0.83312
Positive F1 score: 0.82415324
Negative precision: 0.82940793
Negative recall: 0.81136
Negative F1 score: 0.8202847
Accuracy: 0.82224

Binary Naive Bayes Classifier
-----------------
Positive precision: 0.8439769
Positive recall: 0.81832
Positive F1 score: 0.8309505
Negative precision: 0.8236801
Negative recall: 0.84872
Negative F1 score: 0.8360126
Accuracy: 0.83352

Naive Bayes Classifier With Stop Words Removed
-----------------
Positive precision: 0.81967086
Positive recall: 0.84472
Positive F1 score: 0.83200693
Negative precision: 0.83982503
Negative recall: 0.81416
Negative F1 score: 0.8267934
Accuracy: 0.82944
```

Execution time: ```0m12.840s```

In the output data, a positive value indicates if the movie was classified as a good movie (rating >= 7) and a negative value indicates if the movie was classified as a bad movie (rating <= 4). As per the requirements of the assignment, these values were calculated separately.

As it can be seen from the output, all three classifiers have very similar results, with the best output coming from the Binary Naive Bayes Classifier in terms of precision and accuracy.
