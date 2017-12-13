package predictors
import java.lang.Double

import org.apache.spark.SparkContext
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.sql.Dataset

case class NaiveBayesPredictor(dataSet: String, sc: SparkContext) extends GenericPredictor {

  def buildModel(saveModel: Boolean = false):  Seq[(String, Double) ] = {

    val trainData = loadData(dataSet, sc)

    val splits = trainData.randomSplit(Array(0.5, 0.5), seed = 11L)

    val training = splits(0)

    val test = splits(1)


    val model = NaiveBayes.train(training, lambda = 1.0, modelType = "multinomial")

    val testAndLabel = training.map(p => (p.label,p.label,p.features ))
    val predictionAndLabel = test.map(p => (model.predict(p.features), p.label,p.features))

    var map = scala.collection.mutable.Map[String, Double]()

    for(p <-testAndLabel.collect()) {

//    println(p._1 + "||"+ p._2 + "||"+p._3)
      val p3 = p._3.toString.substring(1)
      val parts = p3.split(",")

      if (!map.contains(parts(1))) {
        map += (parts(1) -> 0.0)
      }
      if (!map.contains(parts(0))) {
        map += (parts(0) -> 0.0)
      }
      if (p._1 == 0.0) {
        var temp2: Double = map.getOrElse(parts(0), 0.0)
        temp2 += 3

        map(parts(0)) = temp2
      } else if (p._1 == 1.0) {
        var temp: Double = map.getOrElse(parts(1), 0.0)
        temp += 1
        map(parts(1)) = temp
        var temp2: Double = map.getOrElse(parts(0), 0.0)
        temp2 += 1
        map(parts(0)) = temp2
      } else {
        var temp: Double = map.getOrElse(parts(1), 0.0)
        temp += 3
        map(parts(1)) = temp

      }
    }
    for(p <-predictionAndLabel.collect()){

      val p3 = p._3.toString.substring(1)
      val parts = p3.split(",")

      if (!map.contains(parts(1))){
        map += (parts(1) -> 0.0)
      }
      if (!map.contains(parts(0))){
        map += (parts(0) -> 0.0)
      }
      if (p._1 == 0.0){
        var temp2:Double = map.getOrElse(parts(0), 0.0)
        temp2 += 3
        map(parts(0)) = temp2
      }else if(p._1 == 1.0){
        var temp:Double = map.getOrElse(parts(1), 0.0)
        temp += 1
        map(parts(1)) = temp
        var temp2:Double = map.getOrElse(parts(0), 0.0)
        temp2 += 1
        map(parts(0)) = temp2
      }else{
        var temp:Double = map.getOrElse(parts(1), 0.0)
        temp += 3
        map(parts(1)) = temp
      }
    }
    var index:Int = 0
    var seqTest = map.toSeq.sortWith(_._2> _._2).take(5)

    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
    val roundedAccuracy = accuracy - (accuracy % 0.0001)



    if (saveModel) {
      model.save(sc, s"models/NaiveBayes/${roundedAccuracy}-${dataSet}")
    }
    println(s"Naive Bayes predicts test data with ${roundedAccuracy} accuracy")

    seqTest
  }
}
