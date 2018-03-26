package student_player;

import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.*;

public class Node {
    TablutBoardState bs;
    TablutMove move;
    Node parent;
    List<Node> children;
    double value;

    public Node(TablutBoardState bs, Node parent, TablutMove move) {
        this.children = new ArrayList<>();
        this.bs = bs;
        this.parent = parent;
        this.move = move;
        this.value = 0;
    }

    public void addChild(Node child) {
        this.children.add(child);
    }
}

