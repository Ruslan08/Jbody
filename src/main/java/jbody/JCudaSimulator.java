package jbody;

//import static

import jbody.data.NaturalCsvFile;
import jbody.data.TransposedCsvFile;
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

        init();
        String srcFile = args[0];
        double time = Double.parseDouble(args[1]);

        var sourceData = new TransposedCsvFile(srcFile).data();


        acc0 = acceleration(sourceData);
//        deriv0 = firstDeriv(sourceData);

//        timeStep = timeStep(acc0, deriv0);

        double timeStepSum = timeStep;
        double timeCount = 0;
        int stepCount = 0;

    }

    private static void init() {
        System.load("D:\\user\\IdeaProjects\\Jbody\\src\\main\\resources\\JCudaDriver-0.9.2-windows-x86_64.dll");
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
