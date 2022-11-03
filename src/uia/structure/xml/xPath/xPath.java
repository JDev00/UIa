package uia.structure.xml.xPath;

import uia.structure.xml.XMLTag;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * xPath is used to compose simple queries for an XML structure
 */

public class xPath {

    /**
     * Given a xPath expression, extracts the xRegexes
     *
     * @param expr a not null xPath expression
     */

    protected static List<xRegex> extractXRegexes(String expr) {
        List<xRegex> xRegexes = new ArrayList<>();

        if (expr != null) {
            int i = 0;
            int iLiteral = -1;
            int iLastChild = 0;
            char[] chars = expr.trim().toCharArray();

            while (i < chars.length) {

                if (chars[i] == '[') iLiteral = i;
                if (chars[i] == ']') iLiteral = -1;

                if (iLiteral == -1 && chars[i] == '/') {
                    String data = String.valueOf(chars, iLastChild, i - iLastChild).trim();

                    if (data.length() > 0) {
                        xRegexes.add(xRegex.create(data));
                        iLastChild = i + 1;
                    }
                }

                i++;
            }

            // if the last child wasn't added, add it now
            if (iLastChild < chars.length) {
                String data = String.valueOf(chars, iLastChild, chars.length - iLastChild).trim();
                if (data.length() > 0) xRegexes.add(xRegex.create(data));
            }
        }

        return xRegexes;
    }

    /**
     * Given an XML tree root, returns the Tags specified by the given expression
     *
     * @param root a not null {@link XMLTag}
     * @param expr a not null String
     */

    public static List<XMLTag> search(XMLTag root, String expr) {
        List<xRegex> regex = extractXRegexes(expr);

        List<XMLTag> out = new ArrayList<>();

        if (root != null) {
            Stack<XMLTag> stack = new Stack<>();
            stack.add(root);

            int size = regex.size();

            while (!stack.isEmpty()) {
                XMLTag tag = stack.pop();
                int height = tag.getHeight();

                if (height < size && regex.get(height).match(tag)) {

                    for (int i = tag.children() - 1; i >= 0; i--) {
                        stack.add(tag.getChild(i));
                    }

                    if (height == size - 1) out.add(tag);
                }
            }
        }

        return out;
    }
}
