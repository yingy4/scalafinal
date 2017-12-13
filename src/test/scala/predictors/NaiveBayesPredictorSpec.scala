package predictors
//import org.apache.spark.sql.SparkSession
import org.apache
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}
import util.CSVUtil

class NaiveBayesPredictorSpec extends FlatSpec with Matchers{

  behavior of "myFilter1"
  val config = new SparkConf().setMaster("local[1]").setAppName("TestNaiveBates")
  val sc = new SparkContext(config)
  val test = CSVUtil.CSVToRDD("FinalData/unittest.csv",sc).collect()
  "LabeledPoint" should "is created" in {
    test should matchPattern {
      case Array(LabeledPoint(1.0,_))=>
    }
  }

  sc.stop()
}
