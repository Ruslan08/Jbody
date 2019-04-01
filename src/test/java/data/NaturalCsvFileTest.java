package data;

import jbody.data.NaturalCsvFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class NaturalCsvFileTest {

    private URI file;

    @Before
    public void init() throws URISyntaxException {
        this.file = ClassLoader.getSystemResource("test.csv").toURI();
    }

    @Test
    public void dataTest() {
        double[][] data = new NaturalCsvFile(file).data();

        Assert.assertArrayEquals(
                new double[]{1.2390996043280014,0.9496296686258394,0.4508758419116252,4.610462531531555E-8,2.1314583151345376E-7,-2.648710970351495E-7,1},
                data[0],
                0);
        Assert.assertArrayEquals(
                new double[]{-0.18783804137816418,0.2832099512289661,0.1970794551787467,-2.454145375143699E-7,-2.975835624479583E-7,-5.306079785129336E-7,1},
                data[1],
                0);
    }

}
