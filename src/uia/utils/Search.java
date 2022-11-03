package uia.utils;

import uia.structure.BInt;

public class Search {

    /**
     * Return the position of the given pattern's occurrences.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(n)
     *
     * @param text    a not null string
     * @param pattern a not null string to search inside the given text
     * @return a new {@link BInt} filled with the position of every occurrence
     */

    public static BInt occurrences(String text, String pattern) {
        if (text == null || pattern == null || text.length() < pattern.length()) return new BInt(0);

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
