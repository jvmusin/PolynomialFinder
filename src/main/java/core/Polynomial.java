package core;

import java.util.StringJoiner;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Polynomial {
    private final double[] coefficients;

    public Polynomial(double... coefficients) {
        int length = coefficients.length;
        this.coefficients = new double[length];
        for (int i = 0; i < length; i++)
            this.coefficients[i] = coefficients[length - i - 1];
    }

    public static Polynomial fromCoefficients(String s) {
        try {
            double[] coefficients = Stream.of(s.split(" ")).mapToDouble(Double::parseDouble).toArray();
            return new Polynomial(coefficients);
        } catch (NumberFormatException nfe) {
            throw new RuntimeException("Unable to parse coefficients");
        }
    }

    public double evaluate(double x) {
        double result = 0;
        double multiplier = 1;
        for (int i = 0; i < coefficients.length; i++, multiplier *= x)
            result += coefficients[i] * multiplier;
        return result;
    }

    public double findRoot(double leftBorder, double rightBorder, double epsilon, Consumer<String> listener) {
        return new RootFinder(leftBorder, rightBorder, epsilon, listener).find();
    }

    public double findRoot(double leftBorder, double rightBorder, int iterationsCount, Consumer<String> listener) {
        return new RootFinder(leftBorder, rightBorder, iterationsCount, listener).find();
    }

    private class RootFinder {
        private double leftBorder;
        private double rightBorder;
        private int iterationsDone;
        private BooleanSupplier stopIterations;
        private final Consumer<String> listener;

        private RootFinder(double leftBorder, double rightBorder, Consumer<String> listener) {
            this.leftBorder = leftBorder;
            this.rightBorder = rightBorder;
            this.listener = listener;
        }

        public RootFinder(double leftBorder, double rightBorder, int iterationsCount, Consumer<String> listener) {
            this(leftBorder, rightBorder, listener);
            stopIterations = () -> iterationsDone == iterationsCount;
        }

        public RootFinder(double leftBorder, double rightBorder, double epsilon, Consumer<String> listener) {
            this(leftBorder, rightBorder, listener);
            stopIterations = () -> Math.abs(midValue) <= epsilon;
        }

        private double mid;
        private double midValue = Double.NaN;
        public double find() {
            double leftValue = evaluate(leftBorder);
            double rightValue = evaluate(rightBorder);
            if (Math.signum(leftValue) == Math.signum(rightValue)) {
                listener.accept("f(left) and f(right) should have different signs");
                return Double.NaN;
            }
            boolean shouldBeReversed = evaluate(leftBorder) > evaluate(rightBorder);
            for (iterationsDone = 0; !stopIterations.getAsBoolean(); iterationsDone++) {
                mid = (leftBorder + rightBorder) / 2;
                midValue = evaluate(mid);
                if (listener != null)
                    listener.accept(String.format("iteration = %02d   left = %.7f   right = %.7f   middle = %.7f   result = %.10f\n",
                            iterationsDone + 1, leftBorder, rightBorder, mid, midValue));
                if ((midValue > 0) ^ shouldBeReversed) rightBorder = mid;
                else leftBorder = mid;
            }
            return mid;
        }
    }

    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(" + ");
        for (int i = coefficients.length - 1; i >= 0; i--)
            result.add(String.format("%.1f*x^%d", coefficients[i], i));
        return "y = " + result.toString();
    }
}
