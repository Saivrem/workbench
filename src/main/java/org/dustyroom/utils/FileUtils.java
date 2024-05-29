package org.dustyroom.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;

@Slf4j
@UtilityClass
public class FileUtils {

    /**
     * Created recursive deep copy of input into output with possible existing files replacement
     *
     * @param input           root input directory
     * @param output          root output directory
     * @param replaceExisting boolean flag to replace existing files in target directory
     */
    public static void deepCopyFolder(Path input, Path output, boolean replaceExisting) {
        StandardCopyOption standardCopyOption = replaceExisting ? StandardCopyOption.REPLACE_EXISTING : null;
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(input)) {
            if (!Files.exists(output)) {
                Files.createDirectories(output);
            }
            for (Path sourceFile : paths) {
                Path targetFile = output.resolve(sourceFile.getFileName());
                if (Files.isRegularFile(sourceFile)) {
                    try {
                        Files.copy(sourceFile, targetFile, standardCopyOption);
                        log.info("Successfully copied to {}", targetFile.toAbsolutePath());
                    } catch (IOException e) {
                        log.warn("{} {} -> {}", e.getClass().getSimpleName(), sourceFile.toAbsolutePath(), e.getMessage());
                    }
                } else {
                    deepCopyFolder(sourceFile, targetFile, replaceExisting);
                }
            }
        } catch (IOException e) {
            log.error("{}: {}", e.getClass().getSimpleName(), e.getMessage());
        }
    }

    public static void removeDirectories(Path origin) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(origin)) {
            if (!stream.iterator().hasNext()) {
                Files.delete(origin);
            }
        } catch (IOException ignored) {
        }
    }
}
