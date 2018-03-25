package student_player;

import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.*;

public class Node {
    TablutBoardState bs;
    TablutMove move;
    Node parent;
    List<Node> children;
    int wins;
    int visitCount;
    double score;
    Random random = new Random();

    public Node(TablutBoardState bs, Node parent, TablutMove move) {
        this.children = new ArrayList<>();
        this.bs = bs;
        this.parent = parent;
        this.move = move;
        this.wins = 0;
        this.visitCount = 0;
    }

    public void incrementVisit() {
        this.visitCount++;
    }

    public int getVisits() {
        return this.visitCount;
    }

    public int getWins() {
        return wins;
    }

    public void incrementWin() {
        this.wins++;
    }

    public Node getRandomChildNode() {
        return this.children.get(random.nextInt(this.children.size()));
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public Node getChildWithMaxScore() {
        return Collections.max(
                this.children,
                Comparator.comparing(c -> c.getVisits() > 0 ? c.getWins()/c.getVisits() : 0));
    }
}

