import org.apache.spark.sql.DataFrame

import java.io.PrintWriter

class HTMLGenerator {
  def generatePieChart(percentages: DataFrame): Unit = {
    val chartContent =
    s"""
      <html>
        <head>
          <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        </head>
        <body>
          <canvas id="pieChart"></canvas>
          <script>
            const data = {
              labels: [${percentages.collect().map(row => s"'${row.getAs[String]("sentiment")}'").mkString(", ")}],
              datasets: [{
                data: [${percentages.collect().map(row => row.getAs[Long]("count")).mkString(", ")}],
                backgroundColor: ['#ff6384', '#36a2eb', '#ffce56'], // Cores das fatias do gráfico
                hoverBackgroundColor: ['#ff6384', '#36a2eb', '#ffce56'] // Cores ao passar o mouse sobre as fatias
              }]
            };

            const options = {
              responsive: false
            };

            const pieChart = new Chart(document.getElementById('pieChart'), {
              type: 'pie',
              data: data,
              options: options
            });
          </script>
        </body>
      </html>
    """

    val htmlFile = new PrintWriter("dashboard.html")
    htmlFile.write(chartContent)
    htmlFile.close()

    println("Dashboard gerado em: dashboard.html")
  }
}
