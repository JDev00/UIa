package api.swing;

import java.awt.Color;
import java.io.File;

import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.JFrame;

/**
 * FileChooserSwing is used to select or save a file with OS file explorer.
 */

public class FileChooserSwing {
    private final JFrame jFrame;
    private final JFileChooser jFileChooser;

    public FileChooserSwing() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        jFrame = new JFrame();
        jFrame.setAlwaysOnTop(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFileChooser = new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        jFileChooser.setFileHidingEnabled(false);
        jFileChooser.setDragEnabled(true);
    }

    /**
     * Set the first directory that will be show
     *
     * @param file a non-null file
     */

    public void setCurrentDirectory(File file) {
        if (file != null) jFileChooser.setCurrentDirectory(file);
    }

    /**
     * Clear all file extensions
     */

    public void clearExtensions() {
        jFileChooser.resetChoosableFileFilters();
    }

    /**
     * Add a new file extension
     *
     * @param extension a non-null file extension
     */

    public void addExtension(String extension) {
        if (extension != null) jFileChooser.addChoosableFileFilter(new FileNameExtensionFilter(extension, extension));
    }

    /**
     * @return a new file or null if the operation failed
     */

    public File saveFile() {
        jFileChooser.setDialogTitle("Save file");

        if (jFileChooser.showSaveDialog(jFrame) == JFileChooser.APPROVE_OPTION) {
            String desc = jFileChooser.getFileFilter().getDescription();
            String std = jFileChooser.getAcceptAllFileFilter().getDescription();

            if (desc.equals(std)) {
                return jFileChooser.getSelectedFile();
            } else {
                return new File(jFileChooser.getSelectedFile().getAbsolutePath() + desc);
            }
        }

        return null;
    }

    /**
     * @return the selected file or null if the operation failed
     */

    public File getFile() {
        jFileChooser.setDialogTitle("Select file");
        jFileChooser.setMultiSelectionEnabled(false);
        return jFileChooser.showOpenDialog(jFrame) == JFileChooser.APPROVE_OPTION ? jFileChooser.getSelectedFile() : null;
    }

    /**
     * @return the selected files or null if the operation failed
     */

    public File[] getFiles() {
        jFileChooser.setDialogTitle("Select files");
        jFileChooser.setMultiSelectionEnabled(true);
        return jFileChooser.showOpenDialog(jFrame) == JFileChooser.APPROVE_OPTION ? jFileChooser.getSelectedFiles() : null;
    }

    /**
     * @return the picked color or null of the operation failed
     */

    public Color getColor() {
        return JColorChooser.showDialog(jFrame, "Color selector", Color.WHITE);
    }

    /**
     * @return the JFileChooser instance
     */

    public JFileChooser getJFileChooser() {
        return jFileChooser;
    }
}
