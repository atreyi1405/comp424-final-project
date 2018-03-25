package student_player;

import boardgame.Move;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.List;

public class MonteCarloTreeSearch {
    int player;

    public MonteCarloTreeSearch(TablutBoardState bs, int player) {
        this.player = player;
    }

    public Move findNextMove(TablutBoardState bs, long timeout) {
        long end = System.currentTimeMillis() + timeout;
        Tree tree = new Tree((TablutBoardState) bs.clone() );
        Node rootNode = tree.getRoot();

        while (System.currentTimeMillis() < end) {
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
        List<TablutMove> possibleMoves;
        if (this.player == TablutBoardState.SWEDE) {
            possibleMoves = SwedeDefender.getCandidateMoves(node.bs);
        } else {
            possibleMoves = MuscoviteAttacker.getCandidateMoves(node.bs);
        }

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
            return cloneBS.getWinner();
        }

        while (!cloneBS.gameOver()) {
            if (this.player == TablutBoardState.SWEDE) {
                TablutMove move = (TablutMove) SwedeDefender.getMove(cloneBS);
                cloneBS.processMove(move);
            } else {
                TablutMove move = (TablutMove) cloneBS.getRandomMove();
                cloneBS.processMove(move);
            }
        }
        return cloneBS.getWinner();
    }

    private void backPropogation(Node nodeToExplore, int winningPlayer) {
        Node tempNode = nodeToExplore;
        while(tempNode != null) {
            tempNode.incrementVisit();
            if(winningPlayer == this.player) {
                tempNode.incrementWin();
            }
            tempNode = tempNode.parent;
        }
    }

}
