package org.dustyroom.duplicate_processing;

import java.nio.file.Path;
import java.util.Objects;

public record HashedFileInfo(Path fileName, Path parent, Path absolutePath, String hashSum) {
    private final static String csvRowPattern = """
            "%s";"%s";"%s";"%s"
            """;

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj instanceof HashedFileInfo hfi) {
            return Objects.equals(hashSum(), hfi.hashSum());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashSum());
    }

    @Override
    public String toString() {
        return csvRowPattern.formatted(fileName, parent, absolutePath, hashSum);
    }
}
