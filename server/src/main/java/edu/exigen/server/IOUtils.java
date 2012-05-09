package edu.exigen.server;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class IOUtils {

    public static void closeSafely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
}
