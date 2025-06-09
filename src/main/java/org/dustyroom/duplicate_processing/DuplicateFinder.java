package org.dustyroom.duplicate_processing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dustyroom.filevisitor.DuplicateFileVisitor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

/**
 * Finds and processes duplicate files based on their hash values.
 * <p>
 * Example usage:
 * <pre>
 * Path dirWithDuplicates = Path.of("some/dir");
 * DuplicateFinder finder = new DuplicateFinder("SHA-256");
 * finder.findDuplicated(dirWithDuplicates, new DeleteFileConsumer());
 * </pre>
 */

@Slf4j
@RequiredArgsConstructor
public class DuplicateFinder {
    private final String hashAlgorithm;

    public void findDuplicated(Path sourceDir, Consumer<List<HashedFileInfo>> consumer) throws Exception {
        DuplicateFileVisitor duplicateFileVisitor = new DuplicateFileVisitor(hashAlgorithm);
        Files.walkFileTree(sourceDir, duplicateFileVisitor);

        duplicateFileVisitor.getFileHashes().values()
                .stream()
                .filter(v -> v.size() > 1)
                .forEach(consumer);
    }
}