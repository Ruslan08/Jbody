package jbody;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.lang.Double.parseDouble;

public class CsvFile {

    private URI path;

    public CsvFile(URI path) {
        this.path = path;
    }

    public CsvFile(String path) throws URISyntaxException {
        this(new URI(path));
    }

    public double[][] data() {
        try {
            return Files.lines(Paths.get(path))
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
