package student_player;

import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Node {
    TablutBoardState bs;
    TablutMove move;
    Node parent;
    List<Node> children;
    int wins;
    int losses;
    int visitCount;
    double score;
    Random random = new Random();

    public Node(TablutBoardState bs, Node parent, TablutMove move) {
        this.children = new ArrayList<>();
        this.bs = bs;
        this.parent = parent;
        this.move = move;
        this.wins = 0;
        this.losses = 0;
        this.visitCount = 0;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void addScore(double score) {
        this.score += score;
    }

    public void addVisit() {
        this.visitCount++;
    }

    public int getVisits() {
        return this.visitCount;
    }

    public int getWins() {
        return wins;
    }

    public void addWin() {
        this.wins++;
    }

    public int getLoses() {
        return losses;
    }

    public void addLoss() {
        this.losses++;
    }

    public Node getRandomChildNode() {
        return this.children.get(random.nextInt(this.children.size()));
    }

    public void addChild(Node child) {
        this.children.add(child);
    }

    public Node getChildWithMaxScore() {
        Node maxNode = this.children.get(0);
        double max = Integer.MIN_VALUE;
        for (Node node : this.children) {
            if(node.getScore() > max) {
                maxNode = node;
                max = node.getScore();
            }
        }

        return maxNode;
    }
}

