package student_player;

import boardgame.Move;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.ArrayList;
import java.util.List;

public class MyTools {
    //Get all the moves that don't result in an immediate capture
    public static List<TablutMove> getSafeMoves(TablutBoardState bs, List<TablutMove> moves) {
        ArrayList<TablutMove> filteredMoves = new ArrayList<>();
        for (TablutMove currentMove : moves) {
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();
            int oldNumberOfPlayerPieces = cloneBS.getNumberPlayerPieces(bs.getTurnPlayer());
            cloneBS.processMove(currentMove);
            List<TablutMove> opponentMoves = cloneBS.getAllLegalMoves();
            boolean didCapture = false;
            for (TablutMove opponentMove: opponentMoves) {
                TablutBoardState cloneBS2 = (TablutBoardState) cloneBS.clone();
                cloneBS2.processMove(opponentMove);
                int newNumberOfPlayerPieces = cloneBS2.getNumberPlayerPieces(bs.getTurnPlayer());
                if (newNumberOfPlayerPieces < oldNumberOfPlayerPieces) {
                    didCapture = true;
                }
            }
            if (!didCapture) {
                filteredMoves.add(currentMove);
            }
            didCapture = false;

        }
        return filteredMoves;
    }

    public static Move getCaptureMove(TablutBoardState bs, List<TablutMove> moves) {
        Move captureMove = null;
        int minNumberOfOpponentPieces = bs.getNumberPlayerPieces(bs.getOpponent());
        for (TablutMove currentMove : moves) {
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();
            cloneBS.processMove(currentMove);
            int newNumberOfOpponentPieces = cloneBS.getNumberPlayerPieces(bs.getOpponent());
            if (newNumberOfOpponentPieces < minNumberOfOpponentPieces) {
                captureMove = currentMove;
            }
        }

        return captureMove;
    }
}
