package org.dustyroom.duplicate_processing;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class DeleteFileConsumer implements Consumer<List<HashedFileInfo>> {
    private final static String PEEK_LOG_PATTERN = "Found duplicated file: {}";
    private final static String DELETE_LOG_PATTERN = "Status: {} File: {}";
    private final static String ERROR_LOG_PATTERN = "Error during deleting duplicated file: {}";

    @Override
    public void accept(List<HashedFileInfo> duplicatedFiles) {
        duplicatedFiles.stream().skip(1)
                .peek(duplicatedFile -> log.info(PEEK_LOG_PATTERN, duplicatedFile))
                .map(HashedFileInfo::absolutePath)
                .forEach(this::deleteIfExists);
    }

    private void deleteIfExists(Path absolutePath) {
        try {
            boolean result = Files.deleteIfExists(absolutePath);
            log.debug(DELETE_LOG_PATTERN, absolutePath, result);
        } catch (Exception e) {
            log.error(ERROR_LOG_PATTERN, e.getMessage());
        }
    }
}