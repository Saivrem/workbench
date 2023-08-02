package org.dustyroom.operations;

import lombok.RequiredArgsConstructor;
import org.dustyroom.model.ParamsMap.Params;

import java.nio.file.Path;

import static org.dustyroom.utils.FileUtils.deepCopyFolder;

/**
 * This class performs backup and restore file operations with flag REPLACE_EXISTING
 * Boolean property restore indicates if files should be moved
 * from Origin to Destination (restore = false)
 * from Destination to Origin (restore = true)
 */
@RequiredArgsConstructor
public class BackupOperationExecutor implements Runnable {

    private final Params params;
    private final boolean restore;

    @Override
    public void run() {
        Path origin = Path.of(params.getBackupOrigin());
        Path destination = Path.of(params.getBackupDestination());
        if (restore) {
            deepCopyFolder(destination, origin, true);
        } else {
            deepCopyFolder(origin, destination, true);
        }
    }
}
