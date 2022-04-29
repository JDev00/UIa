package uia.utils;

import uia.core.Page;

import java.util.Stack;

/**
 * Utility object used to store pages
 */

public final class PageSaver {
    private static final PageSaver PAGESAVER = new PageSaver();

    private final Stack<Page> list;

    private PageSaver() {
        list = new Stack<>();
    }

    /**
     * Remove all pages
     */

    public static void clear() {
        PAGESAVER.list.clear();
    }

    /**
     * Push a new {@link Page} on top of others
     *
     * @param page a non-null Page
     */

    public static void push(Page page) {
        PAGESAVER.list.push(page);
    }

    /**
     * Add at the specified position a new {@link Page}
     *
     * @param i    the position where add the Page
     * @param page a non-null Page
     */

    public static void add(int i, Page page) {
        PAGESAVER.list.add(i, page);
    }

    /**
     * @return the amount of stored pages
     */

    public static int size() {
        return PAGESAVER.list.size();
    }

    /**
     * @return the last added Page
     */

    public static Page peek() {
        return PAGESAVER.list.peek();
    }

    /**
     * @param i the position of the page inside this saver
     * @return the specified Page
     */

    public static Page get(int i) {
        return PAGESAVER.list.get(i);
    }
}
