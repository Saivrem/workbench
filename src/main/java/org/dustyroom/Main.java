package org.dustyroom;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.dustyroom.utils.SerDeUtils.readParams;

@Slf4j
public class Main {
    public static void main(String[] args) {
        try (InputStream paramsInputStream = new FileInputStream(args[0])) {
            readParams(paramsInputStream).execute();
        } catch (IOException ioEx) {
            log.warn("Couldn't create fileInputStream: {}", ioEx.getMessage());
        } catch (ArrayIndexOutOfBoundsException aE) {
            log.warn("Unexpected number of arguments provided {}", aE.getMessage());
        }
    }
}