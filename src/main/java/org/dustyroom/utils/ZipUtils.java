package org.dustyroom.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.dustyroom.filevisitor.DeleteFileVisitor;
import org.dustyroom.filevisitor.ZipFileVisitor;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
@UtilityClass
public class ZipUtils {

    public static void zipFromFolder(Path origin, Path destination, List<String> ignoredFiles) {
        try {
            if (!Files.exists(destination)) {
                Files.createDirectories(destination);
            }
            Files.walkFileTree(origin, new ZipFileVisitor(destination, ignoredFiles));
            Files.walkFileTree(origin, new DeleteFileVisitor());
        } catch (IOException e) {
            log.warn("Issue during zipping of {}", origin);
        }
    }

    public static void unzipFromFolder(Path origin, Path destination) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(origin, "*.zip")) {
            for (Path entry : stream) {
                log.info("Unzipping {}", entry.getFileName());
                Path unzipDir = destination.resolve(entry.getFileName().toString().replace(".zip", ""));
                Files.createDirectories(unzipDir);
                try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(entry))) {
                    ZipEntry zipEntry;
                    while ((zipEntry = zis.getNextEntry()) != null) {
                        Path newFile = unzipDir.resolve(zipEntry.getName());
                        if (zipEntry.isDirectory()) {
                            Files.createDirectories(newFile);
                        } else {
                            Files.createDirectories(newFile.getParent());
                            Files.copy(zis, newFile, StandardCopyOption.REPLACE_EXISTING);
                        }
                        zis.closeEntry();
                    }
                    FileUtils.removeDirectories(entry);
                }
            }
        } catch (IOException e) {
            log.warn("Couldn't unzip from {} - {}", origin, e.getMessage());
        }
    }
}
