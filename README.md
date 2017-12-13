# Premier League Prediction
Group 07 Premier League Prediction
1. Fanqi Zheng
2. Yihang Li


# Goal
As for our final project, we would like to analyze the data of teams in Premier League.
We analyze their performance based on the first 19 rounds in one season and then predict the results of the rest of matches.
Finally, we would give a prediction of final ranking(top 5).



# Methodology
  1.	Use Scala and SQL to clean and filter the data by teams
  2.	Read training data from SQLite database
  3.	Use RDD to save training data
  4.	Use DecisionTree and NaiveBayes to build a predict model and present the Premier League Standing


# Use Case
Input: 
1. When you start the program, you should run the Main.scala. The command line will guide you to input a season between 2008 and 2015 in format “**_**”, such as "08_09", "15_16".

Output:
The program will output such results:
1. Predict results of matches in last 19 rounds in this season
2. Accuracy rate of the predict results
3. A prediction of the final ranking (top 5 teams) in this season
4. 	The real top 5 teams in this season
