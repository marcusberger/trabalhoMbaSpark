import org.apache.log4j.Logger
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.classification.NaiveBayes
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator
import org.apache.spark.ml.feature.{CountVectorizer, StopWordsRemover, Tokenizer}
import org.apache.spark.sql.functions.{col, udf}
import org.apache.spark.sql.types._
object sparkproject {

  import org.apache.log4j.Level
  Logger.getLogger("org").setLevel(Level.ERROR)


  def main(args: Array[String]): Unit = {
    val modelPath = "/Users/marcu/Documents/trabalhoMBA/englishPCFG.ser.gz"

    val spark = SparkSession
      .builder()
      .appName("Sentiment Analysis")
      .master("local[*]")
      .getOrCreate()

    val schema = StructType(Seq(
      StructField("ItemID", IntegerType),
      StructField("Sentiment", IntegerType),
      StructField("SentimentSource", StringType),
      StructField("SentimentText", StringType)
    ))

    val dataset = spark.read
      .format("csv")
      .option("header", true)
      .option("inferSchema", false)
      .schema(schema)
      .load("C:/Users/marcu/trabalhoMba/data/Sentiment Analysis Dataset.csv")

    dataset.show(false)

    // Pré-processamento dos dados
    val tokenizer = new Tokenizer().setInputCol("SentimentText").setOutputCol("words")
    val remover = new StopWordsRemover().setInputCol(tokenizer.getOutputCol).setOutputCol("filteredWords")
    val vectorizer = new CountVectorizer().setInputCol(remover.getOutputCol).setOutputCol("features")

    // Divisão dos dados em treinamento e teste
    val Array(trainData, testData) = dataset.randomSplit(Array(0.8, 0.2))

    // Criação do modelo Naive Bayes
    val nb = new NaiveBayes().setLabelCol("Sentiment").setFeaturesCol(vectorizer.getOutputCol)

    // Criação do pipeline
    val pipeline = new Pipeline().setStages(Array(tokenizer, remover, vectorizer, nb))

    // Treinamento do modelo
    val model = pipeline.fit(trainData)

    // Definir a função UDF para mapear as previsões para os valores desejados
    val sentimentMapping = udf((prediction: Double) => {
      prediction match {
        case 0.0 => "negativo"
        case 1.0 => "positivo"
      }
    })

    // Realização da predição
    val predictions = model.transform(testData)

    // Avaliação do modelo usando métricas de classificação
    val evaluator = new MulticlassClassificationEvaluator().setLabelCol("Sentiment").setPredictionCol("prediction")
    val accuracy = evaluator.evaluate(predictions)

    // Adicionar coluna "sentimento" com os valores mapeados
    val predictionsWithSentiment = predictions.withColumn("sentiment", sentimentMapping(col("prediction")))

    // Contagem dos sentimentos
    val countBySentiment = predictionsWithSentiment.groupBy("sentiment").count()

    // Contagem total de instâncias
    val totalCount = predictionsWithSentiment.count()

    // Cálculo das porcentagens
    val percentages = countBySentiment.withColumn("porcentagem", (col("count") / totalCount) * 100)

    println("Accuracy: " + accuracy)

    // Salvando o modelo treinado
    model.write.overwrite().save("C:/Users/marcu/trabalhoMba/data/modelo")

    // Carregando o modelo treinado
    val loadedModel = PipelineModel.load("C:/Users/marcu/trabalhoMba/data/modelo")

    // Realizando predições com o modelo carregado
    val newPredictions = loadedModel.transform(testData)

    // Visualizar resultados
    predictionsWithSentiment.select("SentimentText", "sentiment").show(false)

    // Visualizar porcentagens
    percentages.show()
  }
}