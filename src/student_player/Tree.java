package student_player;

import tablut.TablutBoardState;

public class Tree {
    Node root;

    public Tree(TablutBoardState bs) {
        this.root = new Node(bs, null, null);
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node node ){
        this.root = node;
    }
}
