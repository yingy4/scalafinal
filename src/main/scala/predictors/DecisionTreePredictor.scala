package predictors

import org.apache.spark.SparkContext
import org.apache.spark.mllib.tree.DecisionTree
import org.apache.spark.mllib.tree.model.DecisionTreeModel

case class DecisionTreePredictor(dataSet: String, sc: SparkContext) extends GenericPredictor {
    def buildModel(saveModel: Boolean = false): Seq[(String, Double) ] = {
    val trainData = loadData(dataSet, sc)


    val splits = trainData.randomSplit(Array(0.5, 0.5), seed = 11L)
    val training = splits(0)
    val test = splits(1)

    val numClasses = 10
    val categoricalFeaturesInfo = Map[Int, Int]()
    val impurity = "gini"
    val maxDepth = 5
    val maxBins = 32

    val model = DecisionTree.trainClassifier(training, numClasses, categoricalFeaturesInfo, impurity, maxDepth, maxBins)
        val testAndLabel = training.map(p => (p.label,p.label,p.features ))
    val predictionAndLabel = test.map(p => (model.predict(p.features), p.label,p.features))

        var predict:Array[String] = Array()

        var map = scala.collection.mutable.Map[String, Double]()
        for(p <-testAndLabel.collect()) {

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
                //println("mapValue:" + map.getOrElse(parts(0),0.0))
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
        //    println(map.size)

        var valueArr = new Array[Double](20)
        var index:Int = 0
        var seqTest = map.toSeq.sortWith(_._2> _._2).take(5)

//        for  ((key,value) <- seqTest){
//            println("key:"+key +",value:"+value)
//        }

    val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()

        val roundedAccuracy = accuracy - (accuracy % 0.0001)

    if (saveModel) {
      model.save(sc, s"models/NaiveBayes/${roundedAccuracy}-${dataSet}")
    }
    println(s"Decision Tree predicts test data with ${roundedAccuracy} accuracy")
    seqTest


  }
}
