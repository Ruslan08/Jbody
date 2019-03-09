package jbody;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Double.parseDouble;

public class CsvFile {

    private final URI path;

    public CsvFile(URI path) {
        this.path = path;
    }

    public CsvFile(String path) {
        try {
            this.path = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
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

    public void writeData(double[][] data) {
        try {
            List<String> lines = Arrays.stream(data)
                    .map(point -> Arrays.stream(point)
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(",")))
                    .collect(Collectors.toList());
            Files.write(Path.of(path), lines, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
