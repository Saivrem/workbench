package org.dustyroom.other;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.dustyroom.utils.HashUtils.calculateFileHash;

@Slf4j
@Getter
@RequiredArgsConstructor
public class DuplicateFileVisitor extends SimpleFileVisitor<Path> {

    private final String hashAlgorithm;
    private final Map<String, List<HashedFileInfo>> fileHashes = new HashMap<>();

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

        if (attrs.isRegularFile()) {
            log.info("Regular file {}", file);
            try {
                String fileHash = calculateFileHash(file.toFile(), hashAlgorithm);
                HashedFileInfo hashedFileInfo = new HashedFileInfo(file.getFileName(), file.getParent(), file.toAbsolutePath(), fileHash);
                fileHashes.computeIfAbsent(fileHash, k -> new ArrayList<>())
                        .add(hashedFileInfo);
            } catch (NoSuchAlgorithmException e) {
                log.error("Can't calculate {} hash for {}, error: {}", hashAlgorithm, file, e.getMessage());
            }
        }
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }
}
