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
        for (TablutMove move : legalMoves) {
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();
            cloneBS.processMove(move);
            Node newNode = new Node(cloneBS, null, move);
            int value = minimax(newNode, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, playerID);
            if (value > maxValue) {
                maxValue = value;
                best = move;
            }
        }
        return best;
    }

    private static int minimax (Node node, int depth, int alpha, int beta, int maximizingPlayer) {
        if (depth == 0) {
            int value;
            if (maximizingPlayer == TablutBoardState.SWEDE) {
                value = SwedeDefender.evaluatePosition(node.bs, node.move);
            } else {
                value = MuscoviteAttacker.evaluatePosition(node.bs, node.move);
            }
            node.score = value;
            return value;
        }

        if (node.bs.getTurnPlayer() == maximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;

            for (TablutMove move : node.bs.getAllLegalMoves()) {
                TablutBoardState cloneBS = (TablutBoardState) node.bs.clone();
                cloneBS.processMove(move);
                Node childNode = new Node(cloneBS, node, move);
                bestValue = Math.max(bestValue, minimax(childNode, depth - 1, alpha, beta, maximizingPlayer));
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break;
                }
                node.addChild(childNode);
            }

            node.score = bestValue;
            return  bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (TablutMove move : node.bs.getAllLegalMoves()) {
                TablutBoardState cloneBS = (TablutBoardState) node.bs.clone();
                cloneBS.processMove(move);
                Node childNode = new Node(cloneBS, node, move);
                bestValue = Math.min(bestValue, minimax(childNode, depth - 1, alpha, beta, maximizingPlayer));
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    break;
                }
                node.addChild(childNode);
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

