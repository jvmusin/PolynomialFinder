package controller;

import core.GraphChart;
import core.Polynomial;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

public class PolynomialFinderController implements Initializable {
    @FXML public GraphChart chart;
    @FXML public TextField polynomialInputField;
    @FXML public TextField leftBorderInput;
    @FXML public TextField rightBorderInput;
    @FXML public TextField limiterInput;
    @FXML public ListView<String> log;

    public void initialize(URL location, ResourceBundle resources) {

    }

    private void calculatePolynomial(boolean useIterations) {
        Polynomial polynomial = getPolynomial();
        double left = getLeftBorder();
        double right = getRightBorder();
        if (left > right) {
            writeLog("Left border is greater than right one", true);
            return;
        }
        chart.updateChart(polynomial, left, right);
        log.getItems().clear();

        Consumer<String> rootFinderWorkListener = message -> writeLog(message, false);
        double root;
        if (useIterations) {
            int iterationsCount = getIterationsCount();
            root = polynomial.findRoot(left, right, iterationsCount, rootFinderWorkListener);
        } else {
            double epsilon = getEpsilon();
            root = polynomial.findRoot(left, right, epsilon, rootFinderWorkListener);
        }
        if (!Double.isNaN(root))
            writeLog("The root is " + root, false);
    }

    @FXML public void calculateUsingIterationsHandle() {
        calculatePolynomial(true);
    }

    @FXML public void calculateUsingEpsilonHandle() {
        calculatePolynomial(false);
    }

    private Polynomial getPolynomial() {
        try {
            return Polynomial.fromCoefficients(polynomialInputField.getText().trim());
        } catch (RuntimeException re) {
            writeLog(re.getMessage(), true);
            throw re;
        }
    }

    private double getLeftBorder() {
        return parseDouble(leftBorderInput.getText(), "left border");
    }

    private double getRightBorder() {
        return parseDouble(rightBorderInput.getText(), "right border");
    }

    private double getEpsilon() {
        return parseDouble(limiterInput.getText(), "epsilon");
    }

    private int getIterationsCount() {
        return parseInt(limiterInput.getText(), "iterations count");
    }

    private double parseDouble(String toParse, String name) {
        return parse(toParse.trim(), name, Double::parseDouble);
    }

    private int parseInt(String toParse, String name) {
        return parse(toParse.trim(), name, Integer::parseInt);
    }

    private <T> T parse(String toParse, String name, Function<String, T> parser) {
        try {
            return parser.apply(toParse);
        } catch (Exception e) {
            writeLog("Unable to parse " + name, true);
            throw new RuntimeException(e.getMessage());
        }
    }

    private void writeLog(String message, boolean clear) {
        if (clear)
            log.getItems().clear();
        String time = LocalTime.now().format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        log.getItems().add(time + ": " + message);
    }
}
