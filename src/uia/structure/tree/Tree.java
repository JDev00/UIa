package uia.structure.tree;

import static java.lang.Math.*;

/**
 * Abstract Tree representation
 */

public abstract class Tree<T> {
    private Node<T> root;

    public Tree() {
        root = null;
    }

    /**
     * Sets the root of this tree
     */

    public final void setRoot(Node<T> n) {
        root = n;
    }

    /**
     * @return the tree root
     */

    public final Node<T> getRoot() {
        return root;
    }

    /**
     * Clears all nodes
     */

    public void clear() {
        root = null;
    }

    /**
     * Adds a new key and a value associated to that key to this tree
     *
     * @param k   the kye
     * @param val the value associated to the key
     */

    public abstract void add(int k, T val);

    /**
     * Removes a key and ots value from this tree
     */

    public abstract void remove(int k);

    /**
     * Stamps this tree with the polish notation
     */

    public static <T> void show(Node<T> n) {
        if (n != null) {
            System.out.print(n.key + ":" + n.value.toString() + ":" + n.height + " ");
            show(n.left);
            show(n.right);
        } else {
            System.out.print("NULL ");
        }
    }

    /**
     * Stamp, starting from the given node, the keys inorder
     *
     * @param n the node used as starting point
     */

    public static <T> void inorder(Node<T> n) {
        if (n != null) {
            inorder(n.left);
            System.out.print(n.key + " ");
            inorder(n.right);
        }
    }

    /**
     * Stamp, starting from the given node, the keys in preorder
     *
     * @param n the node used as starting point
     */

    public static <T> void preorder(Node<T> n) {
        if (n != null) {
            System.out.print(n.key + " ");
            preorder(n.left);
            preorder(n.right);
        }
    }

    /**
     * Stamp, starting from the given node, the keys in postorder
     *
     * @param n the node used as starting point
     */

    public static <T> void postorder(Node<T> n) {
        if (n != null) {
            postorder(n.left);
            postorder(n.right);
            System.out.print(n.key + " ");
        }
    }

    /**
     * @return if the node height if it is non-null otherwise -1
     */

    public static <T> int height(Node<T> n) {
        return (n == null) ? 0 : n.height;
    }

    /**
     * @return if any, the node associated to the given key, otherwise null
     */

    public static <T> Node<T> search(Node<T> root, int key) {
        Node<T> ret = root;

        while (ret != null && ret.key != key) {

            if (key > ret.key) {
                ret = ret.right;
            } else {
                ret = ret.left;
            }
        }

        return ret;
    }

    /**
     * @return the node that contains the minimum key
     */

    public static <T> Node<T> minimum(Node<T> n) {
        while (n.left != null) {
            n = n.left;
        }

        return n;
    }

    /**
     * @return the node that contains the maximum key
     */

    public static <T> Node<T> maximum(Node<T> n) {
        while (n.right != null) {
            n = n.right;
        }

        return n;
    }

    /**
     * @return if any, the predecessor of the given node, otherwise null
     */

    public static <T> Node<T> predecessor(Node<T> n) {
        if (n.left != null) {
            return maximum(n.left);
        }

        Node<T> t = n.parent;

        while (t != null && n == t.left) {
            n = t;
            t = t.parent;
        }

        return t;
    }

    /**
     * @return if any, the successor of the given node, otherwise null
     */

    public static <T> Node<T> successor(Node<T> n) {
        if (n.right != null) {
            return minimum(n.right);
        }

        Node<T> t = n.parent;

        while (t != null && n == t.right) {
            n = t;
            t = t.parent;
        }

        return t;
    }

    /**
     * Tree Left rotation (x = root)
     */

    public void rotLeft(Node<T> x) {
        Node<T> p = x.parent;
        Node<T> z = x.left;
        Node<T> y = x.right;
        Node<T> yL = x.right.left;
        Node<T> yR = x.right.right;

        x.right = yL;
        if (yL != null) yL.parent = x;

        y.left = x;

        x.parent = y;
        y.parent = p;

        if (p == null) {
            setRoot(y);
        } else {

            if (p.left == x) {
                p.left = y;
            } else {
                p.right = y;
            }
        }

        x.height = max(height(yL), height(z)) + 1;
        y.height = max(height(yR), height(x)) + 1;
    }

    /**
     * Tree Right rotation (x = root)
     */

    public void rotRight(Node<T> x) {
        Node<T> p = x.parent;
        Node<T> z = x.right;
        Node<T> y = x.left;
        Node<T> yL = x.left.left;
        Node<T> yR = x.left.right;

        x.left = yR;

        if (yR != null)
            yR.parent = x;

        y.right = x;

        x.parent = y;
        y.parent = p;

        if (p == null) {
            setRoot(y);
        } else {

            if (p.left == x) {
                p.left = y;
            } else {
                p.right = y;
            }
        }

        x.height = max(height(yR), height(z)) + 1;
        y.height = max(height(yL), height(x)) + 1;

        /*Node alpha = x.left.left;
        Node beta = x.left.right;
        Node y = x.left;
        Node z = x.right;
        Node w = x.parent;

        x.left = beta;
        if (beta != null) beta.parent = x;

        y.right = x;
        x.parent = y;

        y.parent = w;
        if (w != null) {
            if (w.left == x)
                w.left = y;
            else
                w.right = y;
        } else {
            setRoot(y);
        }

        //aggiorno le altezze
        x.height = max(height(beta), height(z)) + 1;
        y.height = max(height(x), height(alpha)) + 1;*/
    }

    /**
     * Tree node
     */

    public static class Node<T> {
        //values
        public int key;
        public T value;

        //properties
        public int height;

        //pointers
        public Node<T> parent;
        public Node<T> left;
        public Node<T> right;

        public Node(int k, T v) {
            key = k;
            value = v;
            height = -1;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "key=" + key +
                    ", value=" + value +
                    ", height=" + height +
                    '}';
        }
    }

    /**
     * Creates a new Binary Search Tree
     *
     * @return a new Tree instance
     */

    public static <T> Tree<T> createBST() {
        return new Tree<T>() {

            @Override
            public void add(int key, T data) {
                if (getRoot() == null) {
                    setRoot(new Node<>(key, data));
                } else {
                    Node<T> x = getRoot();
                    Node<T> y = null;

                    while (x != null) {
                        y = x;

                        if (key <= x.key) {
                            x = x.left;
                        } else {
                            x = x.right;
                        }
                    }

                    Node<T> node = new Node<>(key, data);
                    node.parent = y;

                    if (key <= y.key) {
                        y.left = node;
                    } else {
                        y.right = node;
                    }
                }
            }

            @Override
            public void remove(int k) {

            }
        };
    }

    /**
     * AVL Search tree implementation.
     * AVL is a special variant of BST. In particular AVL keeps the tree balanced.
     * <p>
     * Time required for every operation:
     * insert Theta(lg(n))
     * remove Theta(lg(n))
     * search Theta(lg(n))
     * min Theta(lg(n))
     * max Theta(lg(n))
     * successor   Theta(lg(n))
     * predecessor Theta(lg(n))
     *
     * @return a new Tree instance
     */

    public static <T> Tree<T> createAVL() {
        return new Tree<T>() {

            @Override
            public void add(int k, T val) {
                Node<T> y = null;
                Node<T> x = getRoot();
                Node<T> z = new Node<>(k, val);

                while (x != null) {
                    y = x;

                    if (k < x.key) {
                        x = x.left;
                    } else {
                        x = x.right;
                    }
                }

                if (y != null && y.key == k)
                    return;

                z.parent = y;
                z.height = 1;

                if (y == null) {
                    setRoot(z);
                } else if (z.key < y.key) {
                    y.left = z;
                } else {
                    y.right = z;
                }

                // okay
                calcHeight(z);
            }

            @Override
            public void remove(int k) {

            }

            private void calcHeight(Node<T> n) {
                if (n != null) {
                    int hL = height(n.left);
                    int hR = height(n.right);
                    n.height = Math.max(hL, hR) + 1;

                    if (abs(hR - hL) > 1)
                        fixUp(n);

                    calcHeight(n.parent);
                }
            }

            //to test --fix up
            private void fixUp(Node<T> n) {
                int hL = height(n.left);
                int hR = height(n.right);
                int dT = hR - hL;

                Node<T> i;
                int sHL;
                int sHR;

                if (dT > 1) {
                    i = n.right;
                    sHL = (i == null) ? 0 : height(i.left);
                    sHR = (i == null) ? 0 : height(i.right);

                    if (sHR - sHL < 0)
                        rotRight(i);

                    rotLeft(n);
                } else if (dT < -1) {
                    i = n.left;
                    sHL = (i == null) ? 0 : height(i.left);
                    sHR = (i == null) ? 0 : height(i.right);

                    if (sHR - sHL > 0)
                        rotLeft(i);

                    rotRight(n);
                }
            }
        };
    }

    /*
     *
     */

    /*public static void main(String[] args) {
        Tree<String> bst = createBST();

        int[] data = {20, 100, 2, 8, 15, 30};

        for (int i : data) {
            bst.add(i, "");
        }

        System.out.println("preorder: ");
        Tree.preorder(bst.getRoot());

        System.out.println("\ninorder: ");
        Tree.inorder(bst.getRoot());

        System.out.println("\npostorder: ");
        Tree.postorder(bst.getRoot());
    }*/
}
