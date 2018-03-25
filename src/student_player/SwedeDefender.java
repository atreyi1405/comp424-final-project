package student_player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleEntry;
import java.util.Random;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;

public class SwedeDefender {
	public static Move getOpeningMove(TablutBoardState bs) {
		Move currentMove = bs.getRandomMove();
        List<TablutMove> allMoves = bs.getAllLegalMoves();

        Move captureMove = MyTools.getCaptureMove(bs, allMoves);

        if (captureMove != null) {
            return captureMove;
        }

        List<TablutMove> safeMoves = MyTools.getSafeMoves(bs, allMoves);

		for (TablutMove move : safeMoves ) {
		    Coord start = move.getStartPosition();

			if (Coordinates.isCenterOrNeighborCenter(start) && start.distance(move.getEndPosition()) == 2) {
				currentMove = move;
			}
		}
		
		return currentMove;
	}

	public static Move getMove(TablutBoardState bs) {
	    Random random = new Random();

        List<TablutMove> filteredKingMoves = MyTools.getSafeMoves(bs, bs.getLegalMovesForPosition(bs.getKingPosition()));
        int shortestManhattan = Integer.MAX_VALUE;
        int longestTrip = Integer.MIN_VALUE;

        TablutMove longestTripMove = (TablutMove) bs.getRandomMove();
        TablutMove shortestManhattanMove = (TablutMove) bs.getRandomMove();

        for (TablutMove move : filteredKingMoves) {
            //Always choose win if possible
            if (Coordinates.isCorner(move.getEndPosition())) {
                return move;
            }
            int distance = move.getStartPosition().distance(move.getEndPosition());
            if ( distance > longestTrip) {
                longestTripMove = move;
                longestTrip = distance;
            }

            if (Coordinates.distanceToClosestCorner(move.getEndPosition()) < shortestManhattan) {
                shortestManhattanMove = move;
                shortestManhattan = Coordinates.distanceToClosestCorner(move.getEndPosition());
            }
        }

        List<TablutMove> safeMoves = MyTools.getSafeMoves(bs, bs.getAllLegalMoves());

        Move captureMove = MyTools.getCaptureMove(bs, safeMoves);
        if (captureMove != null) {
            return captureMove;
        }
        if (random.nextInt(1) == 0 && filteredKingMoves.size() > 0) {
            if (random.nextInt(1) == 0 ) {
                return shortestManhattanMove;
            } else {
                return longestTripMove;
            }
        } else if(safeMoves.size() > 0) {
            return safeMoves.get(random.nextInt(safeMoves.size()));
        } else {
            return bs.getRandomMove();

        }
    }

    public static List<TablutMove> getCandidateMoves(TablutBoardState bs) {
	    List<TablutMove> candidateMoves = new ArrayList<>();
        List<TablutMove> filteredKingMoves = MyTools.getSafeMoves(bs, bs.getLegalMovesForPosition(bs.getKingPosition()));

        for (TablutMove kingMove : filteredKingMoves) {

            //Always choose win if possible
            if (Coordinates.isCorner(kingMove.getEndPosition())) {
                candidateMoves.add(kingMove);
                return candidateMoves;
            }
        }

        Move captureMove = MyTools.getCaptureMove(bs, bs.getAllLegalMoves());
        if (captureMove != null) {
            candidateMoves.add((TablutMove) captureMove);
            return candidateMoves;
        }

        List<TablutMove> safeMoves = MyTools.getSafeMoves(bs, bs.getAllLegalMoves());
        if(safeMoves.size() > 0) {
            return MyTools.getSafeMoves(bs, bs.getAllLegalMoves());
        } else {
            return bs.getAllLegalMoves();
        }
    }

    public static int evaluatePosition(TablutBoardState bs, TablutMove move) {
	    int value = 100;
	    value += bs.getNumberPlayerPieces(TablutBoardState.SWEDE) * 5;

        if (bs.gameOver()) {
            if (bs.getWinner() == TablutBoardState.SWEDE) {
                return Integer.MAX_VALUE;
            } else {
                return Integer.MIN_VALUE;
            }
        }

        value -= bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) * 10;
        Coord kingPosition = bs.getKingPosition();
        if (kingPosition.x == 0 || kingPosition.x == 8 || kingPosition.y == 0 || kingPosition.y == 8) {
            value += 100;
            //Slightly favour moves that move the king, even if its to another wall
            if (move.getEndPosition().x == kingPosition.x && move.getEndPosition().y == kingPosition.y) {
                value++;
            }
        }

        value -= MyTools.cutOffCornerCount(bs) * 10;
        value -= Coordinates.distanceToClosestCorner(bs.getKingPosition());

        return value;
    }
}
