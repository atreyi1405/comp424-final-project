package student_player;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

public class MyTools {
    //Get all the moves that don't result in an immediate capture
    public static List<TablutMove> getSafeMoves(TablutBoardState bs, List<TablutMove> moves) {
        ArrayList<TablutMove> safeMoves = new ArrayList<>();
        for (TablutMove currentMove : moves) {
            TablutBoardState cloneBS = (TablutBoardState) bs.clone();
            int oldNumberOfPlayerPieces = cloneBS.getNumberPlayerPieces(bs.getTurnPlayer());
            cloneBS.processMove(currentMove);
            List<TablutMove> opponentMoves = getAllNeighbouringOpponentMoves(cloneBS);
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
                safeMoves.add(currentMove);
            }
            didCapture = false;

        }
        return safeMoves;
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

    public static List<TablutMove> getAllNeighbouringOpponentMoves(TablutBoardState bs) {
        List<TablutMove> neighbouringMoves = new ArrayList<>();
        HashSet<Coord> opponentCoords = bs.getOpponentPieceCoordinates();
        Hashtable<String, Coord> neighbourCoords = new Hashtable<>();
        for (Coord coord : opponentCoords) {
            for (Coord neighbourCoord: Coordinates.getNeighbors(coord)) {
                neighbourCoords.put(String.format("%d%d", neighbourCoord.x, neighbourCoord.y), neighbourCoord);
            }
        }
        for (TablutMove move: bs.getAllLegalMoves()) {
            Coord coord = move.getEndPosition();
            if (neighbourCoords.containsKey(String.format("%d%d", coord.x, coord.y))) {
                neighbouringMoves.add(move);
            }
        }

        return neighbouringMoves;
    }
}
