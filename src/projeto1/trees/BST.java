package projeto1.trees;

public class BST<T extends Comparable<T>> {

    private Node root;

    private class Node {
        T key;
        Node left, right;

        Node(T key) {
            this.key = key;
        }
    }

    // Inserção
    public void insert(T key) {
        root = insertRec(root, key);
    }

    private Node insertRec(Node node, T key) {
        if (node == null) return new Node(key);
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = insertRec(node.left, key);
        else if (cmp > 0) node.right = insertRec(node.right, key);
        return node;
    }

    // Busca
    public boolean search(T key) {
        return searchRec(root, key);
    }

    private boolean searchRec(Node node, T key) {
        if (node == null) return false;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return true;
        if (cmp < 0) return searchRec(node.left, key);
        return searchRec(node.right, key);
    }

    // Remoção
    public void remove(T key) {
        root = removeRec(root, key);
    }

    private Node removeRec(Node node, T key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = removeRec(node.left, key);
        } else if (cmp > 0) {
            node.right = removeRec(node.right, key);
        } else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node min = findMin(node.right);
            node.key = min.key;
            node.right = removeRec(node.right, min.key);
        }
        return node;
    }

    private Node findMin(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    // Altura
    public int height() {
        return heightRec(root);
    }

    private int heightRec(Node node) {
        if (node == null) return -1;
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }

    public void clear() {
        root = null;
    }
}

