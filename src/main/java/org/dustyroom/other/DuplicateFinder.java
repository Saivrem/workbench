package org.dustyroom.other;

import lombok.RequiredArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;


@RequiredArgsConstructor
public class DuplicateFinder {
    private final String hashAlgorithm;

    public void findDuplicated(Path sourceDir, Consumer<List<HashedFileInfo>> action) throws Exception {
        DuplicateFileVisitor duplicateFileVisitor = new DuplicateFileVisitor(hashAlgorithm);
        Files.walkFileTree(sourceDir, duplicateFileVisitor);

        duplicateFileVisitor.getFileHashes().values()
                .stream()
                .filter(v -> v.size() > 1)
                .forEach(action);
    }
}
