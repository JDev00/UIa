package uia.structure.xml;

import uia.structure.xml.xPath.xPath;
import uia.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * XML file representation.
 * <br>
 * This implementation stores an XML file as a tree with an undetermined number of children,
 * as consequence, it is quite scalable but a search of a given Tag is done in linear time.
 */

public class XML {
    public static final String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private XMLTag root;

    public StringBuilder header;

    public XML() {
        root = null;

        header = new StringBuilder(1000);
    }

    /**
     * Load an XML file
     *
     * @param path the file's path
     * @return true if the operation succeed
     */

    public boolean load(String path) {
        clear();
        root = XMLParser.parse(path != null && path.endsWith(".xml") ? Utils.read(path) : null, header);
        return root != null;
    }

    /**
     * Write this XML on disk
     *
     * @param path the file's path
     * @return true if the operation succeed
     */

    public boolean write(String path) {
        if (root != null) {
            if (header.length() == 0) header.append(HEADER);
            return Utils.write(path, header + "\n" + print(root));
        }
        return false;
    }

    /**
     * Clear all tags and release memory
     *
     * @return this XML
     */

    public XML clear() {
        XMLTag.deepClear(root);
        root = null;
        return this;
    }

    /**
     * Set a new root.
     * When {@code makeCopy = false} the given Tag won't be copied, as consequence, it could be externally altered.
     *
     * @param root     a not null {@link XMLTag}
     * @param makeCopy true to make a copy of the given Tag
     * @return this XML
     */

    public XML setRoot(XMLTag root, boolean makeCopy) {
        if (root != null) this.root = makeCopy ? XMLTag.deepCopy(root) : root;
        return this;
    }

    /**
     * Add a new Tag.
     * The given Tag will be automatically copied.
     *
     * @param tag  a not null {@link XMLTag} to insert
     * @param expr a {@link xPath} expression that specifies where insert the Tag
     */

    public void add(XMLTag tag, String expr) {
        if (tag != null) {
            List<XMLTag> forest = xPath.search(root, expr);

            for (XMLTag i : forest) {
                i.addChild(tag, true);
            }
        }
    }

    /**
     * Remove the specified Tags
     *
     * @param expr a {@link xPath} expression that specifies the Tags to be deleted
     * @return the removed {@link XMLTag}s
     */

    public List<XMLTag> remove(String expr) {
        List<XMLTag> forest = xPath.search(root, expr);

        for (XMLTag i : forest) {

            if (i.getParent() == null) {
                root = null;
                break;
            } else {
                i.getParent().removeChild(i);
            }
        }

        return forest;
    }

    /**
     * Remove the specified node
     *
     * @param expression an xPath expression that specifies which nodes must be deleted
     * @param upperLimit the maximum nodes that will be removed
     */

    @Deprecated
    public List<XMLTag> remove(String expression, int upperLimit) {
        List<XMLTag> forest = xPath.search(root, expression);
        List<XMLTag> nodes;

        if (upperLimit > 0) {
            nodes = new ArrayList<>(upperLimit);

            int size = Math.min(forest.size(), upperLimit);

            for (int i = 0; i < size; i++) {
                nodes.add(forest.get(i));
            }
        } else {
            return new ArrayList<>();
        }

        for (XMLTag i : nodes) {

            if (i.getParent() == null) {
                root = null;
                break;
            } else {
                i.getParent().removeChild(i);
            }
        }

        return nodes;
    }

    /**
     * @return the tree root
     */

    public XMLTag getRoot() {
        return root;
    }

    /**
     * Return a String representing the formatted Tag
     *
     * @param tag a not null {@link XMLTag}
     * @return a new String
     */

    public static String format(XMLTag tag) {
        StringBuilder builder = new StringBuilder(20 * tag.attributes());

        for (int i = 0; i < tag.attributes(); i++) {
            XMLTag.Attribute attribute = tag.getAttribute(i);
            builder.append(" ").append(attribute.getName())
                    .append("=\"").append(attribute.getValue()).append("\"");
        }

        if (tag.getText() == null) {
            return "<" + tag.getName() + builder + (tag.isAutoClosable() ? "/>" : '>');
        } else {
            return "<" + tag.getName() + builder + '>' + tag.getText();
        }
    }

    /**
     * Tree preorder visit.
     * <br>
     * Time required Theta(|V|+|E|) => because of tree nature, |V| = |E| + 1 => Theta(2|V|-1) = Theta(|V|)
     *
     * @param root    a not null {@link XMLTag}
     * @param height  the root's height
     * @param builder a {@link StringBuilder} used to store visit result
     */

    public static void preorder(XMLTag root, int height, StringBuilder builder) {
        builder.append("\n");
        for (int i = 0; i < root.getHeight() - height; i++) builder.append("  ");
        builder.append(format(root));

        for (int i = 0; i < root.children(); i++) {
            preorder(root.getChild(i), height, builder);
        }

        if (!root.isLeaf()) {
            builder.append("\n");
            for (int i = 0; i < root.getHeight() - height; i++) builder.append("  ");
        }

        if (!root.isAutoClosable()) {
            builder.append("</").append(root.getName()).append('>');
        }
    }

    /**
     * Tree preorder visit
     *
     * @return a String filled with the XMLTag structure
     * @see #preorder(XMLTag, int, StringBuilder)
     */

    public static String print(XMLTag root) {
        StringBuilder builder = new StringBuilder(1000);
        if (root != null) preorder(root, root.getHeight(), builder);
        return builder.toString();
    }

    //private static final String REGEX_COMMA = Pattern.quote(",");

    /*
     * @return the tag attributes
     *

    public static List<XMLTag.Attribute> getAttributes(String expression) {
        List<XMLTag.Attribute> attributes = new ArrayList<>();

        String[] data = expression.split(REGEX_COMMA);

        for (String i : data) {
            String t = i.trim();

            if (t.charAt(0) == '@' && t.contains("=")) {
                int index = t.indexOf("=");
                char[] chars = t.toCharArray();

                String name = String.valueOf(chars, 1, index - 1).trim();
                String value = String.valueOf(chars, index + 2, chars.length - index - 3).trim();

                attributes.add(new XMLTag.Attribute(name, value));
            }
        }

        return attributes;
    }*/

    /*public static void main(String[] args) {
        //System.out.println(Utils.countLines(new File("src\\")));

        XML xml = new XML();

        String path = null;

        Scanner scanner = new Scanner(System.in);

        while (true) {
            String line = scanner.nextLine();

            switch (line) {
                case "load":
                    xml.load(path = scanner.nextLine());
                    System.out.printf("File loaded from: %s\n", path);
                    break;

                case "reload":
                    xml.load(path);
                    System.out.printf("File reloaded from: %s\n", path);
                    break;

                case "exit":
                    System.out.println("Bye");
                    xml.write("src\\Test.xml");
                    System.exit(0);
                    break;

                default:
                    List<XMLTag> forest = xPath.search(xml.getRoot(), line);
                    for (XMLTag i : forest) {
                        System.out.println(XML.print(i));
                    }
                    break;
            }
        }
    }*/
}
