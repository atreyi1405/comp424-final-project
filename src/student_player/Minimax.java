package student_player;

import boardgame.Move;
import tablut.TablutBoard;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.List;

public class Minimax {
    public static Move getBestMove(TablutBoardState bs, int playerID) {

        int maxValue = Integer.MIN_VALUE;
        TablutMove best = (TablutMove) bs.getRandomMove();
        List<TablutMove> legalMoves = bs.getAllLegalMoves();
        Node bestNode = null;
        for (TablutMove move : legalMoves) {
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();
            cloneBS.processMove(move);
            Node newNode = new Node(cloneBS, null, move);
            int value = minimax(newNode, 1, playerID);
            if (value > maxValue) {
                maxValue = value;
                best = move;
                bestNode = newNode;
            }
        }
        return best;
    }

    private static int minimax (Node node, int depth, int maximizingPlayer) {
        if (depth == 0) {
            int value = 0;
            if (node.bs.getTurnPlayer() == TablutBoardState.SWEDE) {
                value = SwedeDefender.evaluatePosition(node.bs);
            } else {
                value = MuscoviteAttacker.evaluatePosition(node.bs);
            }
            node.score = value;
            return value;
        }

        if (node.bs.getTurnPlayer() == maximizingPlayer) {
            for (TablutMove move : node.bs.getAllLegalMoves()) {
                TablutBoardState cloneBS = (TablutBoardState) node.bs.clone();
                cloneBS.processMove(move);
                Node newNode = new Node(cloneBS, node, move);
                node.addChild(newNode);
            }

            int bestValue = Integer.MIN_VALUE;
            for(Node childNode : node.children) {
                int value = minimax(childNode, depth - 1, maximizingPlayer);
                bestValue = Math.max(bestValue, value);
            }
            node.score = bestValue;
            return  bestValue;
        } else {
            for (TablutMove move : node.bs.getAllLegalMoves()) {
                TablutBoardState cloneBS = (TablutBoardState) node.bs.clone();
                cloneBS.processMove(move);
                Node newNode = new Node(cloneBS, node, move);
                node.addChild(newNode);
            }

            int bestValue = Integer.MAX_VALUE;
            for(Node childNode: node.children) {
                int value = minimax(childNode, depth - 1, maximizingPlayer);
                bestValue = Math.min(bestValue, value);
            }
            node.score = bestValue;
            return bestValue;
        }
    }

    public List<TablutMove> getCandidateMoves(TablutBoardState bs) {
        if (bs.getTurnPlayer() == TablutBoardState.SWEDE) {
            return SwedeDefender.getCandidateMoves(bs);
        } else {
            return MuscoviteAttacker.getCandidateMoves(bs);
        }
    }
}

