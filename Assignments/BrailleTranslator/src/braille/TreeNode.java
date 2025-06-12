package braille;

public class TreeNode {
    private Symbol data;
    private TreeNode left;
    private TreeNode right;

    public TreeNode(Symbol data, TreeNode left, TreeNode right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    public Symbol getSymbol() {
        return this.data;
    }

    public void setSymbol(Symbol data) {
        this.data = data;
    }

    public TreeNode getLeft() {
        return this.left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return this.right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
