package uia.application.ui.component.text.edit.structure;

public class EdiTextAlgorithms {

    /**
     * Returns the next break line.
     * <br>
     * Time complexity: O(n)
     * <br>
     * Space complexity: O(1)
     *
     * @param chars      a not null array of chars
     * @param length     the number of elements to scan
     * @param startIndex the scanner start position
     * @return the next break line position or {@code length}
     */

    public static int getNextBreakLine(char[] chars, int length, int startIndex) {
        int position = startIndex;
        while (position < length && chars[position] != '\n') {
            position++;
        }
        return position;
    }
}
