import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}
import predictors.{DecisionTreePredictor, NaiveBayesPredictor}
import scala.io.Source

object Main {
  def main(args: Array[String]) {
    val nameMap = Map("10260.0" -> "MU","9825.0" -> "Arsenal","10252.0" -> "Aston Villa","8455.0" -> "Chelsea","8586.0" -> "Hotspur","10194.0" -> "Stoke City","9879.0" -> "Fulham",
      "8650.0" -> "Liverpool","8668.0" -> "Everton","8456.0" -> "Manchester City","8658.0" -> "Birmingham City","8654.0" -> "West Ham United","8472.0" -> "Sunderland",
      "8655.0" -> "Blackburn Rovers","8559.0" -> "Bolton Wanderers","8602.0" -> "Wolverhampton Wanderers","8667.0" -> "Hull City ","8462.0" -> "Portsmouth","8528.0" -> "Wigan Athletic"
      ,"8191.0" -> "Burnley", "10261.0" ->"Newcastle United","8659.0"->"West Bromwich Albion","8466.0"->"Southampton",
      "10003.0"->"Swansea City", "8197.0"->"Leicester City")

    val realMap = Map(
      "08_09"->
      "MU, Liverpool, Chelsea, Arsenal, Everton",
      "09_10" ->
      "Chelsea, MU, Arsenal, Hotspur, Manchester City",
      "10_11" ->
      "MU, Chelsea, Manchester City, Arsenal, Hotspur",
      "11_12"->
      "Manchester City, MU, Arsenal, Hotspur, Newcastle United",
      "12_13"->
      "MU, Manchester City, Chelsea, Arsenal, Hotspur",
      "13_14"->
      "Manchester City, Liverpool, Chelsea, Arsenal, Everton",
      "14_15"->
      "Chelsea, Manchester City, Arsenal, MU, Hotspur",
      "15_16"->
      "Leicester City, Arsenal, Hotspur, Manchester City, MU")
    Logger.getLogger("org").setLevel(Level.WARN)
    Logger.getLogger("akka").setLevel(Level.WARN)

    val config = new SparkConf().setMaster("local[2]").setAppName("Test App")
    val sc = new SparkContext(config)

    println("Type the season you  want to predict : ")
    //println("1 : Based on first navieBay     2: Based on Decisiontree")
//    println("1 : Based on first 15 round     2: Based on team statistics")
    val input = scala.io.StdIn.readLine()
    //println("Real Top 5 Ranking :MU, Liverpool, Chelsea, Arsenal, Everton")

    var pathWay = "FinalData/" + input + "_FinalData.csv"
    val naiveBayesPredictor = new NaiveBayesPredictor(pathWay, sc)
    val rateN = naiveBayesPredictor.buildModel()
    for  ((key,value) <- rateN){
      print(nameMap(key) + "  ")
    }
    println()
    val decisionTreePredictor = new DecisionTreePredictor(pathWay, sc)
    val rateD = decisionTreePredictor.buildModel()
    for  ((key,value) <- rateN){
      print(nameMap(key) + "  ")
    }
    println()
    println("Real ranking(top 5):")
    val real: String = realMap.getOrElse(input, "11")
    println(real)



//
  }
}