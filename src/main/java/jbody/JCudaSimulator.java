package jbody;

//import static

import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.JCudaDriver;
import jcuda.vec.VecDouble;

import java.util.Arrays;

public class JCudaSimulator {

    private static double EPS = 1E-04;
    private static double G = 1;
    private static final double N = 1E-03;

    private static double[][] acc0;
    private static double[][] acc1;
    private static double[][] deriv0;
    private static double[][] deriv1;
    private static double[][] secondDeriv;
    private static double[][] thirdDeriv;
    private static double timeStep;
    private static double[][] predictions;

    public static void main(String[] args) {

        String srcFile = args[0];
        double time = Double.parseDouble(args[1]);

        var sourceData = new CsvFile(srcFile).newData();

        init();

        acc0 = acceleration(sourceData);
//        deriv0 = firstDeriv(sourceData);

//        timeStep = timeStep(acc0, deriv0);

        double timeStepSum = timeStep;
        double timeCount = 0;
        int stepCount = 0;

    }

    private static void init() {
        JCudaDriver.setExceptionsEnabled(true);
        JCudaDriver.cuInit(0);

        CUdevice device = new CUdevice();
        JCudaDriver.cuDeviceGet(device, 0);
        CUcontext context = new CUcontext();
        JCudaDriver.cuCtxCreate(context, 0, device);

        VecDouble.init();
    }

    private static double[][] acceleration(double[][] coords) {

        System.out.println("BEFORE " + Arrays.toString(coords[0]));

        int size = coords[0].length * Sizeof.DOUBLE;
        CUdeviceptr deviceX = createDevice(size, coords[0]);
        CUdeviceptr deviceY = createDevice(size, coords[1]);
        CUdeviceptr deviceZ = createDevice(size, coords[2]);

        CUdeviceptr xij = createResultDevice(size);
//        CUdeviceptr yij = createResultDevice(size);
//        CUdeviceptr zij = createResultDevice(size);

        VecDouble.sub(size, xij, deviceX, deviceY);

        JCudaDriver.cuMemcpyDtoH(Pointer.to(coords[0]), xij, size);

        System.out.println("AFTER " + Arrays.toString(coords[0]));

//        var accelerations = new double[coords.length][3];    // {a_x, a_y, a_z}
//
//        for (int i = 0; i < coords.length; i++) {
//            for (int j = 0; j < coords.length; j++) {
//                if (j != i) {
//                    double distance = distance(coords[j], coords[i]);
//                    for (int n = 0; n < 3; n++) {  // x,y,z
//                        double xij = coords[j][n] - coords[i][n];
//                        double ai = G * coords[j][6] * xij / Math.pow(distance + Math.pow(EPS, 2), 1.5);
//                        accelerations[i][n] += ai;
//                    }
//                }
//            }
//        }
//        return accelerations;
        return null;
    }

    private static CUdeviceptr createDevice(long size, double[] data) {
        CUdeviceptr device = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(device, size);
        JCudaDriver.cuMemcpyHtoD(device, Pointer.to(data), size);
        return device;
    }

    private static CUdeviceptr createResultDevice(long size) {
        CUdeviceptr resultDevice = new CUdeviceptr();
        JCudaDriver.cuMemAlloc(resultDevice, size);
        return resultDevice;
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
