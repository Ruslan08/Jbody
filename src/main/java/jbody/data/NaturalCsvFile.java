package jbody.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Double.parseDouble;

/**
 * CSV file with natural structure of source data.
 * Each line of the file represents one particular with all attributes:
 * <pre>
 *      x1, y1, z1, Vx1, Vy1, Vz1, m1
 *      ...
 *      xn, yn, zn, Vxn, Vyn, Vzn, mn
 * </pre>
 */
public class NaturalCsvFile implements SourceFile {

    private final URI path;

    public NaturalCsvFile(URI path) {
        this.path = path;
    }

    public NaturalCsvFile(String path) {
        try {
            this.path = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double[][] data() {
        try {
            return Files.lines(Path.of(path))
                    .map(s -> s.replace("\"", "").split(","))
                    .map(arr -> new double[]{
                            parseDouble(arr[0]),
                            parseDouble(arr[1]),
                            parseDouble(arr[2]),
                            parseDouble(arr[3]),
                            parseDouble(arr[4]),
                            parseDouble(arr[5]),
                            parseDouble(arr[6])})
                    .toArray(double[][]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
