package data;

import jbody.DirectoryNextToFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

public class DirectoryNextToFileTest {

    private URI file;

    @Before
    public void init() throws URISyntaxException {
        this.file = ClassLoader.getSystemResource("test.csv").toURI();
    }

    @Test
    public void pathToDirectoryTest() {
        URI directory = new DirectoryNextToFile(file, "result").pathToDirectory();

        Assert.assertEquals(Path.of(directory), Path.of(file).getParent().resolve("result"));
    }

}
