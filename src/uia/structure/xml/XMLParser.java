package uia.structure.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * XML Parser class
 */

public class XMLParser {

    /**
     * @return the Tag's name
     */

    protected static String getTagName(char[] input, int i, int j) {
        for (int k = i; k < j; k++) {
            if (input[k] == ' ') return String.valueOf(input, i, k - i);
        }

        if (isAutoClosableTag(input, j)) {
            return String.valueOf(input, i, j - i - 1);
        } else {
            return String.valueOf(input, i, j - i);
        }
    }

    /**
     * @return the Tag's text
     */

    protected static String getTagText(char[] input, int i) {
        int k = i;
        while (input[k] != '>') k--;
        return String.valueOf(input, k + 1, i - k - 1).replaceAll("\\s+", " ");
    }

    /**
     * @return new Tag's {@link XMLTag.Attribute}s
     */

    protected static List<XMLTag.Attribute> getAttributes(char[] input, int i, int j) {
        List<XMLTag.Attribute> list = new ArrayList<>();

        if (i < j) {
            int k = i;

            while (k < j) {

                if (input[k] == '=') {
                    int s = k;
                    int e = k + 2;

                    while (input[s] != ' ') s--;
                    while (input[e] != '"') e++;

                    list.add(new XMLTag.Attribute(
                            String.valueOf(input, s + 1, k - s - 1),
                            String.valueOf(input, k + 2, e - k - 2)));
                }

                k++;
            }
        }

        return list;
    }

    /**
     * Create a new XMLTag
     *
     * @param parent the Tag's parent
     * @param input  a not null array of chars
     * @param i      the first index after the open bracket
     * @param j      the last index before the close bracket
     * @return a new {@link XMLTag}
     */

    protected static XMLTag createTag(XMLTag parent, char[] input, int i, int j) {
        return new XMLTag(getTagName(input, i, j), parent).addAttribute(getAttributes(input, i, j));
    }

    /**
     * Check if the given array is a tag
     *
     * @param input a not null array of chars
     * @param i     the first index
     * @param j     the last index
     */

    protected static int[] getTag(char[] input, int i, int j) {
        if (j <= input.length && i < j) {
            int k = i;
            int f = -1;
            while (k < j) {
                if (input[k] == '<') f = k;
                if (input[k] == '>' && f != -1) return new int[]{f, k};
                k++;
            }
        }
        return null;
    }

    /**
     * Check if the given array is an end tag
     *
     * @param input a not null array of chars
     * @param i     the first index after the open bracket
     */

    protected static boolean isEndTag(char[] input, int i) {
        return input[i + 1] == '/';
    }

    /**
     * Check if the given array is an auto-closable tag
     *
     * @param input a not null array of chars
     * @param j     the last index before the close bracket
     */

    protected static boolean isAutoClosableTag(char[] input, int j) {
        return input[j - 1] == '/';
    }

    /**
     * Check if the given array is a HEADER tag
     *
     * @param input a not null array of chars
     * @param i     the first index after the open bracket
     * @param j     the last index before the close bracket
     */

    protected static boolean isHeaderTag(char[] input, int i, int j) {
        return input[i] == '?' && input[j] == '?';
    }

    /**
     * Parse an XML file
     *
     * @param data   the not null XML data
     * @param header a not null {@link StringBuilder} used to store XML header
     * @return a new {@link XMLTag} otherwise null
     */

    public static XMLTag parse(String data, StringBuilder header) {
        XMLTag root = null;

        if (data != null && header != null) {
            // clear chars buffer
            header.delete(0, header.length());

            XMLTag parent = null;

            char[] chars = data.toCharArray();
            int l = data.length();
            int[] ind = {0, 0};

            while ((ind = getTag(chars, ind[1], l)) != null) {

                if (root == null) {

                    if (isHeaderTag(chars, ind[0] + 1, ind[1] - 1)) {
                        header.append(chars, ind[0], ind[1] + 1);
                    } else {
                        root = createTag(null, chars, ind[0] + 1, ind[1]);
                        parent = root;
                    }
                } else {

                    if (isEndTag(chars, ind[0])) {
                        // if Tag is a leaf compute the text between the open and close brackets
                        if (parent.isLeaf()) parent.setText(getTagText(chars, ind[0]));

                        parent = parent.getParent();
                    } else {
                        XMLTag tag = createTag(parent, chars, ind[0] + 1, ind[1]);

                        parent.addChild(tag, false);
                        parent = tag;

                        if (isAutoClosableTag(chars, ind[1])) parent = parent.getParent();
                    }
                }
            }
        }

        return root;
    }
}
