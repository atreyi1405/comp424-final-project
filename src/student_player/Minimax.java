package student_player;

import boardgame.Move;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minimax {
    public static Move getBestMove(TablutBoardState bs, int playerID, int depth) {
        double maxValue = Integer.MIN_VALUE;

        List<TablutMove> bestMoves = new ArrayList<>();

        for (TablutMove move : bs.getAllLegalMoves()) {
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();
            cloneBS.processMove(move);
            Node newNode = new Node(cloneBS, null, move);
            double value = minimax(newNode, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, playerID);
            if (value > maxValue) {
                maxValue = value;
                //clear best moves, we have a better group
                bestMoves.clear();
                bestMoves.add(move);
            }
            //If move is equally as good, add to list
            if( value == maxValue) {
                bestMoves.add(move);
            }
        }

        //If multiple best moves, chose a random one
        if (bestMoves.size() > 0) {
            Random random = new Random();
            return bestMoves.get(random.nextInt(bestMoves.size()));
        } else {
            return bs.getRandomMove();
        }
    }

    private static double minimax (Node node, int depth, double alpha, double beta, int maximizingPlayer) {
        if (depth == 0 || node.bs.gameOver()) {
            double value;
            if (maximizingPlayer == TablutBoardState.SWEDE) {
                value = SwedeDefender.evaluatePosition(node.bs, node.move);
            } else {
                value = MuscoviteAttacker.evaluatePosition(node.bs, node.move);
            }
            node.value = value;
            return value;
        }

        if (node.bs.getTurnPlayer() == maximizingPlayer) {
            double bestValue = Integer.MIN_VALUE;

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

            node.value = bestValue;
            return  bestValue;
        } else {
            double bestValue = Integer.MAX_VALUE;
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

            node.value = bestValue;
            return bestValue;
        }
    }
}

