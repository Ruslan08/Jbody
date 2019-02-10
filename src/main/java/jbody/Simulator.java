package jbody;

public class Simulator {

    private static double G = 1;
    private static double EPS = .0001;
    private static final double N = .001;

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
        Double time = Double.parseDouble(args[1]);

        double[][] sourceData = new CsvFile(srcFile).data();

        acc0 = acceleration(sourceData);
//        deriv0 = firstDeriv(sourceData);

    }

    public static double[][] acceleration(double[][] coords) {
        double [][] accelerations = new double[coords.length][3];    // {a_x, a_y, a_z}

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

    /**
     * @param first     coordinates for first point
     * @param second    coordinates for second point
     * @return          distance between 2 points squared
     */
    static double distance(double[] first, double[] second) {
        double x = Math.pow(second[0] - first[0], 2);
        double y = Math.pow(second[1] - first[1], 2);
        double z = Math.pow(second[2] - first[2], 2);
        return x + y + z;
    }

}
