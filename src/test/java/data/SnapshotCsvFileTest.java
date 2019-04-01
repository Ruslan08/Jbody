package data;

import jbody.data.SnapshotCsvFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.Double.parseDouble;

public class SnapshotCsvFileTest {

    private URI file;

    @Before
    public void init() throws URISyntaxException {
        this.file = ClassLoader.getSystemResource("testOutPut.csv").toURI();
    }

    @Test
    public void writeDataTest() throws IOException {

        double[][] expectedData = {
                {1.2390996043280014, 0.9496296686258394, 0.4508758419116252, 4.610462531531555E-8, 2.1314583151345376E-7, -2.648710970351495E-7, 1},
                {-0.18783804137816418, 0.2832099512289661, 0.1970794551787467, -2.454145375143699E-7, -2.975835624479583E-7, -5.306079785129336E-7, 1}
        };
        SnapshotCsvFile snapshot = new SnapshotCsvFile(file);
        snapshot.writeData(expectedData);

        double[][] data = Files.lines(Path.of(file))
                .map(s -> s.split(","))
                .map(arr -> new double[]{
                        parseDouble(arr[0]),
                        parseDouble(arr[1]),
                        parseDouble(arr[2]),
                        parseDouble(arr[3]),
                        parseDouble(arr[4]),
                        parseDouble(arr[5]),
                        parseDouble(arr[6])})
                .toArray(double[][]::new);

        Assert.assertArrayEquals(expectedData[0], data[0], 0);
        Assert.assertArrayEquals(expectedData[1], data[1], 0);
    }

}
