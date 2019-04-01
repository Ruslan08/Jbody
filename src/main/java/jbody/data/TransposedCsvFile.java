package jbody.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.lang.Double.parseDouble;

/**
 * CSV file with transposed structure of source data.
 * Each line of the file represents one attribute for all particulars:
 * <pre>
 *      x1, x2, ... , xn
 *      y1, y2, ... , yn
 *      z1, z2, ... , zn
 *      Vx1, Vx2, ... , Vxn
 *      Vy1, Vy2, ... , Vyn
 *      Vz1, Vz2, ... , Vzn
 *      m1, m2, ... , mn
 * </pre>
 */
public class TransposedCsvFile implements SourceFile {

    private final URI path;

    public TransposedCsvFile(URI path) {
        this.path = path;
    }

    public TransposedCsvFile(String path) {
        try {
            this.path = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double[][] data() {
        try {
            List<String> lines = Files.readAllLines(Path.of(path));
            double[][] data = new double[7][lines.size()];
            for (int i = 0; i < lines.size(); i++) {
                String[] split = lines.get(i).replace("\"", "").split(",");
                for (int j = 0; j < 7; j++) {
                    data[j][i] = parseDouble(split[j]);
                }
            }
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
