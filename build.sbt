ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "MarcusBergerProjetoSparkMBA"
  )

val sparkVersion = "3.3.2"

libraryDependencies += "org.apache.spark" %% "spark-mllib" % "3.3.2"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
)