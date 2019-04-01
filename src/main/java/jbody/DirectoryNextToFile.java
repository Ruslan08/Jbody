package jbody;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;

/**
 * Represent the directory next to the source file
 */
public class DirectoryNextToFile {

    private final URI sourcePath;
    private final String directoryName;

    public DirectoryNextToFile(URI path, String dirName) {
        this.sourcePath = path;
        this.directoryName = dirName;
    }

    public DirectoryNextToFile(String path, String dirName) {
        try {
            this.sourcePath = new URI(path);
            this.directoryName = dirName;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public URI pathToDirectory() {
        return Path.of(sourcePath)
                .getParent()
                .resolve(directoryName)
                .toUri();
    }

}
