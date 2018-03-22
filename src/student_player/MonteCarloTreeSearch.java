package student_player;

import boardgame.Move;
import tablut.TablutBoard;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.List;

public class MonteCarloTreeSearch {
    static final int WIN_SCORE = 10;

    public Move findNextMove(TablutBoardState bs, long timeout) {
        long end = System.currentTimeMillis() + timeout;
        Tree tree = new Tree((TablutBoardState) bs.clone() );
        Node rootNode = tree.getRoot();

        while (System.currentTimeMillis() < timeout) {
            Node promisingNode = selectPromisingNode(rootNode);
            if (!promisingNode.bs.gameOver()) {
                expandNode(promisingNode);
            }
            Node nodeToExplore = promisingNode;
            if (promisingNode.children.size() > 0) {
                nodeToExplore = promisingNode.getRandomChildNode();
            }
            int playoutResult = simulateRandomPlayout(nodeToExplore);
            backPropogation(nodeToExplore, playoutResult);
        }

        Node winnerNode = rootNode.getChildWithMaxScore();
        tree.setRoot(winnerNode);
        return winnerNode.move;
    }

    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;
        while (node.children.size() != 0) {
            node = UCT.findBestNodeWithUCT(node);
        }
        return node;
    }

    private void expandNode(Node node) {
        List<TablutMove> possibleMoves = node.bs.getAllLegalMoves();
        possibleMoves.forEach(move-> {
            TablutBoardState cloneBS = (TablutBoardState) node.bs.clone();
            cloneBS.processMove(move);
            Node newNode = new Node(cloneBS, node, move);
            node.addChild(newNode);
        });
    }

    private int simulateRandomPlayout(Node node) {
        TablutBoardState cloneBS = (TablutBoardState) node.bs.clone();
        if (cloneBS.gameOver()) {
            node.parent.setScore(Integer.MIN_VALUE);
            return cloneBS.getWinner();
        }
        while (!cloneBS.gameOver()) {
            TablutMove move = (TablutMove) cloneBS.getRandomMove();
            cloneBS.processMove(move);
        }
        return cloneBS.getWinner();
    }

    private void backPropogation(Node nodeToExplore, int playerNumber) {
        Node tempNode = nodeToExplore;
        while(tempNode != null) {
            tempNode.addVisit();
            if(tempNode.bs.getTurnPlayer() == playerNumber) {
                tempNode.addScore(WIN_SCORE);
            }
            tempNode = tempNode.parent;
        }
    }

}
