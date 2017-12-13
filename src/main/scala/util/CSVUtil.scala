package util



import org.apache.spark
import org.apache.spark.SparkContext
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.rdd.RDD

import scala.util.Try

object CSVUtil {
  var index = 0
  def CSVToRDD(path: String, sparkContext: SparkContext): RDD[LabeledPoint] = {

    val csv = sparkContext.textFile(path)
//    val df = spark.read.format("csv").option("header", "true").load("csvfile.csv")
    //To find the headers
    val header = csv.first

    //To remove the header
    val data = csv.filter(_ (0) != header(0))

    //To create a RDD of (label, features) pairs
      val rddData = data.map { line =>
      val parts = line.split(',')
//      val partss = util.Arrays.copyOfRange(parts,1,parts.length).mkString(" ")
    val partss = parts.slice(1,parts.length).mkString(" ")
      LabeledPoint(parts(0).toDouble, Vectors.dense(partss.split(' ').map(x=>Try(x.toDouble).getOrElse(0.toDouble))))
    }.cache()

    rddData
  }
}
