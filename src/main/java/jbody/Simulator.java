package jbody;

import java.util.Arrays;
import java.util.stream.DoubleStream;

public class Simulator {

    private static double G = 1;
    private static double EPS = 1E-04;
    private static final double N = 1E-03;

    private static double[][] acc0;
    private static double[][] acc1;
    private static double[][] deriv0;
    private static double[][] deriv1;
    private static double[][] secondDeriv;
    private static double[][] thirdDeriv;
    private static double timeStep;
    private static double [][] predictions;

    public static void main(String[] args) {

        String srcFile = args[0];
        double time = Double.parseDouble(args[1]);

        var sourceData = new CsvFile(srcFile).data();

        acc0 = acceleration(sourceData);
        deriv0 = firstDeriv(sourceData);

        timeStep = timeStep(acc0, deriv0);

        double timeStepSum = timeStep;
        double timeCount = 0;
        int stepCount = 0;

        while (timeStepSum < time) {
            predictions = prediction(sourceData, timeStep, acc0, deriv0);

            timeStepSum += 0.001;
        }

    }

    private static double[][] acceleration(double[][] coords) {
        var accelerations = new double[coords.length][3];    // {a_x, a_y, a_z}

        for (int i = 0; i < coords.length; i++) {
            for (int j = 0; j < coords.length; j++) {
                if (j != i) {
                    double distance = distance(coords[j], coords[i]);
                    for (int n = 0; n < 3; n++) {  // x,y,z
                        double xij = coords[j][n] - coords[i][n];
                        double ai = G * coords[j][6] * xij / Math.pow(distance + Math.pow(EPS, 2), 1.5);
                        accelerations[i][n] += ai;
                    }
                }
            }
        }
        return accelerations;
    }

    private static double[][] firstDeriv(double[][] source) {
        var derivatives = new double[source.length][3];

        for (int i = 0; i < source.length; i++) {
            for (int j = 0; j < source.length; j++) {

                if (j != i) {
                    double x_ij = source[j][0] - source[i][0];
                    double y_ij = source[j][1] - source[i][1];
                    double z_ij = source[j][2] - source[i][2];

                    double vx_ij = source[j][3] - source[i][3];
                    double vy_ij = source[j][4] - source[i][4];
                    double vz_ij = source[j][5] - source[i][5];

                    double distance = distance(source[j], source[i]);

                    double pow1_5 = Math.pow(distance + Math.pow(EPS, 2), 1.5);
                    double pow2_5 = Math.pow(distance + Math.pow(EPS, 2), 2.5);
                    double v_r = x_ij * vx_ij + y_ij * vy_ij + z_ij * vz_ij;

                    double d_i_x = G * source[j][6] * (vx_ij / pow1_5 - 3 * v_r * (x_ij) / pow2_5);
                    double d_i_y = G * source[j][6] * (vy_ij / pow1_5 - 3 * v_r * (y_ij) / pow2_5);
                    double d_i_z = G * source[j][6] * (vz_ij / pow1_5 - 3 * v_r * (z_ij) / pow2_5);

                    derivatives[i][0] += d_i_x;
                    derivatives[i][1] += d_i_y;
                    derivatives[i][2] += d_i_z;
                }
            }
        }
        return derivatives;
    }

    private static double timeStep(double[][] acc, double[][] deriv) {
        var t = new double[acc.length];
        for (int i = 0; i < acc.length; i++) {
            var a = absVec(acc[i]);
            var d = absVec(deriv[i]);

            t[i] = N * (a / d);
        }
        return Arrays.stream(t).min().orElse(1E-8);
    }

    private static double[][] prediction(double[][] source, double timeStep, double[][] acc, double[][] deriv) {

        var predictions = new double[source.length][6];
        var tSquaredInHalf = Math.pow(timeStep, 2) / 2;

        for (int i = 0; i < source.length; i++) {
            predictions[i][0] = source[i][0] + timeStep * source[i][3] + (tSquaredInHalf) * acc[i][0] + Math.pow(timeStep, 3) / 6 * deriv[i][0];
            predictions[i][1] = source[i][1] + timeStep * source[i][4] + (tSquaredInHalf) * acc[i][1] + Math.pow(timeStep, 3) / 6 * deriv[i][1];
            predictions[i][2] = source[i][2] + timeStep * source[i][5] + (tSquaredInHalf) * acc[i][2] + Math.pow(timeStep, 3) / 6 * deriv[i][2];

            predictions[i][3] = source[i][3] + timeStep * acc[i][0] + (tSquaredInHalf) * deriv[i][0];
            predictions[i][4] = source[i][4] + timeStep * acc[i][1] + (tSquaredInHalf) * deriv[i][1];
            predictions[i][5] = source[i][5] + timeStep * acc[i][2] + (tSquaredInHalf) * deriv[i][2];
        }
        return predictions;
    }

    private static double absVec(double[] vec) {
        return Math.sqrt(DoubleStream.of(vec).map(d -> Math.pow(d, 2)).sum());
    }

    /**
     * @param first     coordinates for first point
     * @param second    coordinates for second point
     * @return          distance between 2 points squared
     */
    private static double distance(double[] first, double[] second) {
        var x = Math.pow(second[0] - first[0], 2);
        var y = Math.pow(second[1] - first[1], 2);
        var z = Math.pow(second[2] - first[2], 2);
        return x + y + z;
    }

}
