package uia.structure.algorithms;

import uia.structure.list.BInt;

public final class Search {

    /**
     * Linear search algorithm.
     * <br>
     * Return the occurrences of the given pattern
     *
     * @param text    a not null string
     * @param pattern a not null string to search inside the given text
     * @return a new {@link BInt} filled with the position (inside the given text) of every occurrence
     */

    public static BInt occurrences(String text, String pattern) {
        if (text == null || pattern == null
                || text.length() < pattern.length()) return new BInt(0);

        BInt bInt = new BInt(10);

        char[] tChar = text.toCharArray();
        char[] pChar = pattern.toCharArray();

        int pIndex = 0;

        for (int i = 0; i < tChar.length; i++) {

            if (tChar[i] == pChar[pIndex]) {
                pIndex++;
            } else {
                pIndex = 0;
            }

            if (pIndex == pChar.length) {
                bInt.add(i - pIndex + 1);
                pIndex = 0;
            }
        }

        return bInt;
    }
}
