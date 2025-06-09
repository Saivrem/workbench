package org.dustyroom.filevisitor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@RequiredArgsConstructor
public class ZipFileVisitor extends SimpleFileVisitor<Path> {

    private final Path destination;
    private final List<String> restrictedFiles;

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        String fileName = file.getFileName().toString();
        if (isRestricted(fileName)) {
            try {
                log.info("Found restricted file {}", fileName);
                Files.delete(file);
            } catch (Exception e) {
                log.warn("Can't remove restricted file {}, {}", fileName, e.getMessage());
            }
        }
        return FileVisitResult.CONTINUE;
    }

    private boolean isRestricted(String fileName) {
        return restrictedFiles.stream()
                .anyMatch(f -> f.equalsIgnoreCase(fileName) || fileName.startsWith(f));
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        log.info("Visiting {}", dir);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            List<Path> fileList = new ArrayList<>();
            for (Path entry : stream) {
                if (Files.isRegularFile(entry)) {
                    String fileName = entry.getFileName().toString();
                    if (!isRestricted(fileName)) {
                        fileList.add(entry);
                    }
                }
            }
            if (!fileList.isEmpty()) {
                Path zipFile = destination.resolve(dir.getFileName().toString() + ".zip");
                try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFile))) {
                    for (Path file : fileList) {
                        ZipEntry zipEntry = new ZipEntry(dir.relativize(file).toString());
                        zos.putNextEntry(zipEntry);
                        Files.copy(file, zos);
                        zos.closeEntry();
                    }
                }
                for (Path file : fileList) {
                    Files.delete(file);
                }
                log.info("Zipped {} files from {}", fileList.size(), dir);
            }
        } catch (IOException e) {
            log.warn("Issue during zipping of {} - {}", dir, e.getMessage());
        }
        return FileVisitResult.CONTINUE;
    }
}

