package uia.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
     * @throws NullPointerException if {@code file == null}
     */

    public static String readAll(File file) {
        Objects.requireNonNull(file);

        StringBuilder builder = new StringBuilder(5000);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return builder.toString();
    }

    /**
     * Reads the content of the specified file and returns a list containing every single line.
     *
     * @param file a not null {@link File} to read
     * @return if the specified exists, its lines, otherwise an empty List
     * @throws NullPointerException if {@code file == null}
     */

    public static List<String> read(File file) {
        Objects.requireNonNull(file);

        List<String> lines = Collections.emptyList();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            lines = new ArrayList<>(750);
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (Exception error) {
            error.printStackTrace();
        }
        return lines;
    }

    /**
     * Writes the specified data to disk. Each data element is written on a new line.
     *
     * @param file a not null {@link File} where write the specified array
     * @param data the object to write on disk
     * @return true if the file has been written
     * @throws NullPointerException     if {@code file == null or data == null}
     * @throws IllegalArgumentException if at least one data element is null
     */

    public static boolean write(File file, Object... data) {
        // input checks
        Objects.requireNonNull(file);
        Objects.requireNonNull(data);
        for (Object element : data) {
            if (element == null) {
                throw new IllegalArgumentException("data can't contains null elements");
            }
        }

        // operations
        boolean result = false;
        try (PrintWriter writer = new PrintWriter(file)) {
            for (Object i : data) {
                writer.println(i.toString());
            }
            writer.flush();
            result = true;
        } catch (Exception error) {
            error.printStackTrace();
        }
        return result;
    }
}
