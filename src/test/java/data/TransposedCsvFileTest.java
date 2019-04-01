package data;

import jbody.data.TransposedCsvFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class TransposedCsvFileTest {

    private URI file;

    @Before
    public void init() throws URISyntaxException {
        this.file = ClassLoader.getSystemResource("test.csv").toURI();
    }

    @Test
    public void dataTest() {
        double[][] data = new TransposedCsvFile(file).data();

        Assert.assertArrayEquals(
                new double[]{
                        1.2390996043280014,
                        -0.18783804137816418,
                        1.0159814940276553,
                        0.48845486764395796,
                        -0.8723233074857637,
                        -0.6841308056431887,
                        -0.8634155170407354,
                        0.6579285980988989,
                        1.4996327416515256,
                        0.28515970177357053},
                data[0],
                0);
        Assert.assertArrayEquals(
                new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                data[6],
                0);
    }
}
