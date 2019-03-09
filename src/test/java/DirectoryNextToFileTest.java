import jbody.DirectoryNextToFile;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class DirectoryNextToFileTest {

    private URI file;

    @Before
    public void init() throws URISyntaxException {
        this.file = ClassLoader.getSystemResource("test.csv").toURI();
    }

    @Test
    public void pathToDirectoryTest() {
        URI directory = new DirectoryNextToFile(file, "result").pathToDirectory();

        Assert.assertEquals(Paths.get(directory), Paths.get(file).getParent().resolve("result"));
    }

}
