package predictors

import org.apache.spark.SparkContext
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD
import util.CSVUtil

trait GenericPredictor {
  def loadData(dataSet: String, sc: SparkContext): RDD[LabeledPoint] = {
//    println(dataSet)
    CSVUtil.CSVToRDD(dataSet, sc)
  }

//  def buildModel(): ClassificationModel
}
