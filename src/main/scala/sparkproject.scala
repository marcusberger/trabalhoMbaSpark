import org.apache.spark.sql.SparkSession

object sparkproject {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Sentiment Analysis")
      .master("local[*]")
      .getOrCreate()

    val df = spark.read.format("csv")
      .option("header", true)
      .load("C:/Users/marcu/trabalhoMba/data/Sentiment Analysis Dataset.csv")

    df.show(false)

  }
}
