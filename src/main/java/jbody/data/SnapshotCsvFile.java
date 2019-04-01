package jbody.data;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SnapshotCsvFile implements SnapshotFile {

    private final URI path;

    public SnapshotCsvFile(URI path) {
        this.path = path;
    }

    public SnapshotCsvFile(String path) {
        try {
            this.path = new URI(path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
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
