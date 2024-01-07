package uia.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class.
 * <br>
 * Collection of input/output functions.
 */

public final class IOUtility {

    private IOUtility() {
    }

    /**
     * Reads the content of the specified file
     *
     * @param file a not null {@link File} to read
     * @return if the specified exists, its content, otherwise an empty string
     */

    public static String readAll(File file) {
        StringBuilder builder = new StringBuilder(5000);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            if (builder.length() > 0) builder.deleteCharAt(builder.length() - 1);
        } catch (Exception ignored) {
        }

        return builder.toString();
    }

    /**
     * Reads the content of the specified file and returns a list containing every single line.
     *
     * @param file a not null {@link File} to read
     * @return if the specified exists, its lines, otherwise an empty List
     */

    public static List<String> read(File file) {
        List<String> lines = Collections.emptyList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines = new ArrayList<>(750);
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception ignored) {
        }
        return lines;
    }
}
