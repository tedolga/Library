package edu.exigen.server;

import java.io.Closeable;
import java.io.IOException;

/**
 * Util class for working with i/o
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class IOUtils {

    /**
     * Safely closes {@link java.io.Closeable} instance.
     *
     * @param closeable {@link java.io.Closeable} instance.
     */
    public static void closeSafely(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }
}
