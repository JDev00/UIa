package uia.structure.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * XML tag representation.
 * A typical XML tag is made as follows: {@code <name a1="x" a2="y">text</name>}
 */

public class XMLTag implements Iterable<XMLTag> {
    private XMLTag parent;

    private int height;

    private String name;
    private String text;

    private final List<Attribute> attributes;

    private final List<XMLTag> children;

    public XMLTag(String name, XMLTag parent) {
        this.name = name;

        attributes = new ArrayList<>(1);

        children = new ArrayList<>(1);

        setParent(parent);
    }

    /**
     * Set the Tag's name
     *
     * @param name a not null String
     * @return this Tag
     */

    public XMLTag setName(String name) {
        if (name != null) this.name = name;
        return this;
    }

    /**
     * Set the Tag's text
     *
     * @param text a String; it could be null
     * @return this Tag
     */

    public XMLTag setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * Set the Tag's height and adjust the children's one
     *
     * @param height a value {@code >= 0}
     * @return this Tag
     */

    public XMLTag setHeight(int height) {
        this.height = Math.max(0, height);

        Stack<XMLTag> stack = new Stack<>();
        for (XMLTag i : this) {
            stack.push(i);
        }

        while (!stack.isEmpty()) {
            XMLTag tag = stack.pop();
            tag.height = tag.getParent().getHeight() + 1;

            for (XMLTag i : tag) {
                stack.push(i);
            }
        }

        return this;
    }

    /**
     * Set the Tag's parent.
     * The Tag's height will be adjusted respect to the parent's height.
     *
     * @param parent a {@link XMLTag}
     * @return this Tag
     */

    public XMLTag setParent(XMLTag parent) {
        this.parent = parent;
        return setHeight(parent == null ? 0 : parent.height + 1);
    }

    /**
     * Add a new Tag's Attribute.
     * The given Attribute will be copied to prevent external manipulation.
     *
     * @param attribute a not null {@link Attribute}
     * @return this Tag
     */

    public XMLTag addAttribute(Attribute attribute) {
        if (attribute != null) attributes.add(new Attribute(attribute.name, attribute.value));
        return this;
    }

    /**
     * Add a new Tag's Attribute.
     * The given Attributes will be copied to prevent external manipulation.
     *
     * @param attributes a not null List of {@link Attribute}s
     * @return this Tag
     */

    public XMLTag addAttribute(List<Attribute> attributes) {
        if (attributes != null) {
            for (Attribute i : attributes) {
                addAttribute(i);
            }
        }
        return this;
    }

    /**
     * Add a new Tag; the given Tag will be a child of the current one.
     * <br>
     * When {@code makeCopy = false} the given Tag won't be copied, as consequence, it could be externally altered.
     * <br>
     * The given Tag's parent and height will be automatically set.
     *
     * @param tag a not null {@link XMLTag}
     * @return this Tag
     */

    public XMLTag addChild(XMLTag tag, boolean makeCopy) {
        if (tag != null) children.add((makeCopy ? deepCopy(tag) : tag).setParent(this));
        return this;
    }

    /**
     * Remove the specified child Tag
     *
     * @param i the position of the child to remove
     * @return the removed Tag otherwise null
     */

    public XMLTag removeChild(int i) {
        return i >= 0 && i < children() ? children.remove(i) : null;
    }

    /**
     * Remove the specified child Tag
     *
     * @param i the child to remove
     * @return true if the operation succeed
     */

    public boolean removeChild(XMLTag i) {
        return i != null && children.remove(i);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(20 * attributes.size());

        for (Attribute i : attributes) {
            builder.append(" ").append(i);
        }

        if (text == null) {
            return "XMLTag{<" + name + builder + (isAutoClosable() ? "/>" : '>') + ", children:" + children.size() + '}';
        } else {
            return "XMLTag{<" + name + builder + '>' + text + ", children:" + children.size() + '}';
        }
    }

    @Override
    public Iterator<XMLTag> iterator() {
        return children.iterator();
    }

    /**
     * @return the Tag's name
     */

    public String getName() {
        return name;
    }

    /**
     * @return the Tag's text
     */

    public String getText() {
        return text;
    }

    /**
     * @return the number of children
     */

    public int children() {
        return children.size();
    }

    /**
     * @return the number of Attributes
     */

    public int attributes() {
        return attributes.size();
    }

    /**
     * @return the Tag's height
     */

    public int getHeight() {
        return height;
    }

    /**
     * @return true if this Tag hasn't children
     */

    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * @return true if this Tag is <b>a leaf AND has no text</b>
     */

    public boolean isAutoClosable() {
        return isLeaf() && text == null;
    }

    /**
     * @param i the Attribute's position
     * @return the specified {@link Attribute}
     */

    public Attribute getAttribute(int i) {
        return attributes.get(i);
    }

    /**
     * @return the Tag's parent
     */

    public XMLTag getParent() {
        return parent;
    }

    /**
     * @param i the child position
     * @return the specified Tag otherwise null
     */

    public XMLTag getChild(int i) {
        return i >= 0 && i < children.size() ? children.get(i) : null;
    }

    /**
     * Make a deep copy of the given Tag.
     * In a deep copy, every child will be entirely copied recursively.
     * <br>
     * The resulting Tag will have {@code height = 0}.
     *
     * @param tag a not null {@link XMLTag} to copy
     * @return a new deep copied Tag
     */

    public static XMLTag deepCopy(XMLTag tag) {
        XMLTag out = new XMLTag(tag.getName(), null)
                .setText(tag.getText())
                .addAttribute(tag.attributes);

        for (XMLTag i : tag) {
            out.addChild(deepCopy(i), false);
        }

        return out;
    }

    /**
     * Deep clear of the given Tag.
     * A deep clear clears every child Tag recursively.
     *
     * @param tag a not null {@link XMLTag} to clear
     */

    public static void deepClear(XMLTag tag) {
        if (tag != null) {
            tag.parent = null;
            tag.name = null;
            tag.text = null;
            tag.attributes.clear();

            for (XMLTag i : tag) {
                deepClear(i);
            }

            tag.children.clear();
        }
    }

    /**
     * XML tag's attribute representation
     */

    public static class Attribute {
        private String name;
        private String value;

        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        /**
         * Set the attribute's name
         *
         * @param name a not null String
         */

        public Attribute setName(String name) {
            if (name != null) this.name = name;
            return this;
        }

        /**
         * Set the attribute's value
         *
         * @param value a not null String
         */

        public Attribute setValue(String value) {
            if (value != null) this.value = value;
            return this;
        }

        @Override
        public String toString() {
            return "Attribute{name=" + name + ", value=" + value + '}';
        }

        /**
         * @return the attribute's name
         */

        public String getName() {
            return name;
        }

        /**
         * @return the attribute's value
         */

        public String getValue() {
            return value;
        }
    }
}
