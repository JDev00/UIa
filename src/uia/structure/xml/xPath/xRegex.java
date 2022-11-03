package uia.structure.xml.xPath;

import uia.structure.xml.XMLTag;
import uia.utils.Date;
import uia.utils.Utils;

import static uia.utils.Utils.isNumber;

/**
 * WARNING! Class not defined correctly!
 * <br>
 * xRegex is a piece of {@link xPath}'s query
 */

public class xRegex {
    private static final String[] BOOL_LOGIC = {" or ", " and "};
    private static final String[] FUNCTION = {"text()", "date()"};

    private String name;
    private String expression;

    /**
     * Evaluate a numerical expression
     */

    private static boolean evaluateNumber(XMLTag tag, String expr) {
        XMLTag p = tag.getParent();

        if (p != null) {
            int l = Integer.parseInt(expr) - 1;
            return l >= 0 && l < p.children() && p.getChild(l).equals(tag);
        }

        return false;
    }

    /**
     * Evaluate an universal predicate
     */

    private static boolean uEvalChild(XMLTag n, String r, String name, String value) {
        for (int i = 0; i < n.children(); i++) {
            XMLTag a = n.getChild(i);
            if (a.getName().equals(name) && !Evaluation.evaluator(r, a.getText(), value)) return false;
        }
        return true;
    }

    /**
     * Evaluate an existential predicate
     */

    private static boolean eEvalChild(XMLTag n, String r, String name, String value) {
        for (int i = 0; i < n.children(); i++) {
            XMLTag a = n.getChild(i);
            if (a.getName().equals(name) && Evaluation.evaluator(r, a.getText(), value)) return true;
        }
        return false;
    }

    /**
     * Evaluate a child expression
     */

    private static boolean evaluateChild(XMLTag n, String expression) {
        for (String i : Evaluation.R) {

            if (expression.contains(i)) {
                int index = expression.indexOf(i);
                int sum = i.length() - 1;
                char[] chars = expression.toCharArray();

                String name = String.valueOf(chars, 0, index).trim();
                String value = String.valueOf(chars, index + 2 + sum, chars.length - index - 3 - sum).trim();

                if (i.equals("!=")) {
                    return uEvalChild(n, i, name, value);
                } else {
                    return eEvalChild(n, i, name, value);
                }
            }
        }

        return false;
    }

    /**
     * Evaluate an universal predicate
     */

    private static boolean uEvalAttribute(XMLTag n, String r, String name, String value) {
        for (int i = 0; i < n.attributes(); i++) {
            XMLTag.Attribute a = n.getAttribute(i);
            if (a.getName().equals(name) && !Evaluation.evaluator(r, a.getValue(), value)) return false;
        }
        return true;
    }

    /**
     * Evaluate an existential predicate
     */

    private static boolean eEvalAttribute(XMLTag n, String r, String name, String value) {
        for (int i = 0; i < n.attributes(); i++) {
            XMLTag.Attribute a = n.getAttribute(i);
            if (a.getName().equals(name) && Evaluation.evaluator(r, a.getValue(), value)) return true;
        }
        return false;
    }

    /**
     * Evaluate attribute expression
     */

    private static boolean evaluateAttribute(XMLTag n, String expression) {
        for (String i : Evaluation.R) {

            if (expression.contains(i)) {
                int index = expression.indexOf(i);
                int sum = i.length() - 1;
                char[] chars = expression.toCharArray();

                String name = String.valueOf(chars, 1, index - 1).trim();
                String value = String.valueOf(chars, index + 2 + sum, chars.length - index - 3 - sum).trim();

                if (i.equals("!=")) {
                    return uEvalAttribute(n, i, name, value);
                } else {
                    return eEvalAttribute(n, i, name, value);
                }
            }
        }

        return false;
    }

    /**
     * Evaluate a universal predicate
     */

    private static boolean uEvalFunction(XMLTag tag, String r, String name, String value) {
        if (name.equals(FUNCTION[0])) return Evaluation.evaluator(r, tag.getText(), value);
        if (name.equals(FUNCTION[1])) return Evaluation.evaluateDate(r, tag.getText(), value);
        return true;
    }

    /**
     * Evaluate an existential predicate
     */

    private static boolean eEvalFunction(XMLTag tag, String r, String name, String value) {
        if (name.equals(FUNCTION[0])) return Evaluation.evaluator(r, tag.getText(), value);
        if (name.equals(FUNCTION[1])) return Evaluation.evaluateDate(r, tag.getText(), value);
        return false;
    }

    /**
     * Evaluate a function
     */

    private static boolean evaluateFunction(XMLTag tag, String expression) {
        for (String i : Evaluation.R) {

            if (expression.contains(i)) {
                int index = expression.indexOf(i);
                int sum = i.length() - 1;
                char[] chars = expression.toCharArray();

                String name = String.valueOf(chars, 0, index).trim();
                String value = String.valueOf(chars, index + 2 + sum, chars.length - index - 3 - sum).trim();

                if (i.equals("!=")) {
                    return uEvalFunction(tag, i, name, value);
                } else {
                    return eEvalFunction(tag, i, name, value);
                }
            }
        }

        return false;
    }

    /**
     * Compare the given Tag with the given expression
     */

    private static boolean compare(XMLTag tag, String expr) {
        for (String i : BOOL_LOGIC) {

            if (expr.contains(i)) {
                String[] split = expr.split(i, 2);
                String p1 = split[0].trim();
                String p2 = split[1].trim();

                if (i.equals(" or ")) return compare(tag, p1) || compare(tag, p2);
                if (i.equals(" and ")) return compare(tag, p1) && compare(tag, p2);
            }
        }

        if (isNumberExpression(expr)) return evaluateNumber(tag, expr);
        if (isAttribute(expr)) return evaluateAttribute(tag, expr);
        if (isFunction(expr)) return evaluateFunction(tag, expr);
        // maybe the expression is a child
        return evaluateChild(tag, expr);
    }

    /**
     * @return true if the given Tag matches this regex
     */

    public boolean match(XMLTag tag) {
        if (name.equals(tag.getName()) || name.equals("*")) {
            if (expression == null) return true;
            return compare(tag, expression);
        }
        return false;
    }

    @Override
    public String toString() {
        return name + '[' + expression + ']';
    }

    /**
     * @return the name of the xml object to control (the left assignment part)
     */

    public String getName() {
        return name;
    }

    /**
     * @return if exists the expression between square brackets
     */

    public String getExpression() {
        return expression;
    }

    /**
     * @return true if the expression is a number
     */

    private static boolean isNumberExpression(String expression) {
        return Utils.isNumber(expression);
    }

    /**
     * @return true if the expression specifies an attribute
     */

    private static boolean isAttribute(String expression) {
        return expression != null && expression.charAt(0) == '@';
    }

    /**
     * @return true if the expression is a function
     */

    private static boolean isFunction(String expression) {
        return expression != null && expression.contains("()");
    }

    /**
     * Create a new xRegex
     *
     * @param in a not null String to compute
     */

    public static xRegex create(String in) {
        xRegex regex = new xRegex();

        if (in.charAt(in.length() - 1) == ']') {
            char[] chars = in.toCharArray();

            int i = chars.length - 2;
            while (chars[i] != '[') i--;

            regex.name = String.valueOf(chars, 0, i);
            regex.expression = String.valueOf(chars, i + 1, chars.length - i - 2);
        } else {
            regex.name = in;
            regex.expression = null;
        }

        return regex;
    }

    /**
     * The Evaluation class is a container of evaluation functions
     */

    public static class Evaluation {
        public static final String[] R = {">=", "<=", ">", "<", "!=", "="};

        /**
         * Evaluate aRb according to the given relation
         *
         * @param r the relation; {@link #R}
         * @param a the left predicate
         * @param b the right predicate
         */

        public static boolean evaluate(String r, String a, String b) {
            return a != null && b != null && ((r.equals(R[0]) && a.compareTo(b) >= 0)
                    || (r.equals(R[1]) && a.compareTo(b) <= 0)
                    || (r.equals(R[2]) && a.compareTo(b) > 0)
                    || (r.equals(R[3]) && a.compareTo(b) < 0)
                    || (r.equals(R[4]) && !a.equals(b))
                    || (r.equals(R[5]) && a.equals(b)));
        }

        /**
         * Evaluate aRb according to the given relation
         *
         * @param r the relation; {@link #R}
         * @param a the left predicate
         * @param b the right predicate
         */

        public static boolean evaluate(String r, int a, int b) {
            return (r.equals(R[0]) && a >= b)
                    || (r.equals(R[1]) && a <= b)
                    || (r.equals(R[2]) && a > b)
                    || (r.equals(R[3]) && a < b)
                    || (r.equals(R[4]) && a != b)
                    || (r.equals(R[5]) && a == b);
        }

        /**
         * Evaluate aRb according to the given relation
         *
         * @param r the relation; {@link #R}
         * @param a the left predicate
         * @param b the right predicate
         */

        public static boolean evaluate(String r, float a, float b) {
            return (r.equals(R[0]) && a >= b)
                    || (r.equals(R[1]) && a <= b)
                    || (r.equals(R[2]) && a > b)
                    || (r.equals(R[3]) && a < b)
                    || (r.equals(R[4]) && a != b)
                    || (r.equals(R[5]) && a == b);
        }

        /**
         * Evaluate the two given dates according to the given relation
         *
         * @param r the relation; {@link #R}
         * @param a the left predicate
         * @param b the right predicate
         */

        public static boolean evaluateDate(String r, String a, String b) {
            Date date1 = Date.create(a);
            Date date2 = Date.create(b);
            if (date1 != null && date2 != null) {
                int c = date1.compareDate(date2);
                return (r.equals(R[0]) && c >= 0)
                        || (r.equals(R[1]) && c <= 0)
                        || (r.equals(R[2]) && c > 0)
                        || (r.equals(R[3]) && c < 0)
                        || (r.equals(R[4]) && c != 0)
                        || (r.equals(R[5]) && c == 0);
            }
            return false;
        }

        /**
         * Evaluate the given inputs. According to the given input, will be chosen one method
         * between {@link #evaluate(String, int, int)} or {@link #evaluate(String, String, String)}.
         *
         * @param r the relation; {@link #R}
         * @param a the left predicate
         * @param b the right predicate
         */

        public static boolean evaluator(String r, String a, String b) {
            if (isNumber(a) && isNumber(b)) {
                return evaluate(r, Float.parseFloat(a), Float.parseFloat(b));
            } else {
                return evaluate(r, a, b);
            }
        }
    }
}
