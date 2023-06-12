## Projeto para matéria Big data
Este projeto realiza a análise de sentimentos usando o Spark. Ele treina um modelo Naive Bayes em um conjunto de dados de análise de sentimentos e faz previsões de sentimentos em um conjunto de dados de teste. Também gera um gráfico de pizza para visualizar a distribuição dos sentimentos.

#### Pré-requisitos
Apache Spark (versão 3.3.2)\
Scala (versão 2.12.15)\
Hadoop (versão 3.2.2)

#### Primeiros Passos

1. Clone o repositório
2. Baixe o conjunto de dados de análise de sentimentos e coloque-o no diretório data: Sentiment Analysis Dataset
3. Atualize o caminho do conjunto de dados no código: val dataset = spark.read.format("csv").option("header", true).option("inferSchema", false).schema(schema).load("data/Sentiment Analysis Dataset.csv")
4. Execute o projeto: sbt run

#### Estrutura do Projeto
* src/main/scala/sparkproject/SparkProject.scala: Arquivo Scala principal contendo o código do projeto Spark.
* data/Sentiment Analysis Dataset.csv: Arquivo do conjunto de dados contendo os dados de análise de sentimentos.

#### Funcionalidade
1. Carrega o conjunto de dados de análise de sentimentos.
2. Pré-processa os dados dividindo-os em palavras, removendo palavras irrelevantes e vetorizando-os.
3. Divide os dados em conjuntos de treinamento e teste.
4. Cria um modelo Naive Bayes e constrói um pipeline.
5. Treina o modelo com o conjunto de treinamento.
6. Realiza a análise de sentimentos no conjunto de teste.
7. Avalia a precisão do modelo.
8. Adiciona uma coluna "sentimento" com os valores mapeados.
9. Conta os sentimentos e calcula as porcentagens.
10. Salva o modelo treinado.
Carrega o modelo salvo.
11. Gera um gráfico de pizza para visualizar a distribuição dos sentimentos.
Resultados
12. A precisão do modelo de análise de sentimentos é exibida no console. Além disso, um gráfico de pizza é gerado para visualizar a distribuição dos sentimentos.
