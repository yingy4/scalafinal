package predictors

import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import util.CSVUtil
class DecisionTreePredictorSpec extends FlatSpec with Matchers {

  behavior of "myFilter"


  it should "work" in {
    val config = new SparkConf().setMaster("local[1]").setAppName("TestDscision")
    val sc = new SparkContext(config)
    val test = CSVUtil.CSVToRDD("FinalData/unittest.csv",sc).collect()

   test should matchPattern {
      case Array(LabeledPoint(1.0,_))=>
    }
    sc.stop()
  }

}
