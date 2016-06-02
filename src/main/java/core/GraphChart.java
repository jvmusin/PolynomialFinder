package core;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class GraphChart extends LineChart<Number, Number> {
    public GraphChart() {
        super(
                new NumberAxis("X axis", -1, 1, 0.5),
                new NumberAxis("Y axis", -1, 1, 0.5));
        getXAxis().setAutoRanging(true);
        getYAxis().setAutoRanging(true);
    }

    public void updateChart(Polynomial polynomial, double from, double to) {
        setTitle(String.format("Function %s chart", polynomial));

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        double step = (to - from) / 50;
        for (double x = from; x < to + step; x += step)
            series.getData().add(new XYChart.Data<>(x, polynomial.evaluate(x)));

        getData().clear();
        getData().add(series);
    }
}