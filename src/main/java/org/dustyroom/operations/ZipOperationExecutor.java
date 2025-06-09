package org.dustyroom.operations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dustyroom.model.ParamsMap;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.dustyroom.utils.ZipUtils.unzipFromFolder;
import static org.dustyroom.utils.ZipUtils.zipFromFolder;

@Slf4j
@RequiredArgsConstructor
public class ZipOperationExecutor implements Runnable {

    private final ParamsMap.Params params;
    private final boolean unzip;

    @Override
    public void run() {
        Path origin = Path.of(params.getZipOrigin());
        Path destination = Path.of(params.getZipDestination());
        List<String> restrictedFiles =
                Optional.ofNullable(params.getRestrictedFiles())
                        .orElse(Collections.emptyList());

        if (unzip) {
            unzipFromFolder(origin, destination);
        } else {
            zipFromFolder(origin, destination, restrictedFiles);
        }
        log.info("{} done for {}", unzip ? "unzip" : "zip", origin);
    }
}
