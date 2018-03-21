package student_player;

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
        List<TablutMove> safeMoves = MyTools.getSafeMoves(bs, bs.getAllLegalMoves());
        Move captureMove = MyTools.getCaptureMove(bs, safeMoves);

        if (captureMove != null) {
            return captureMove;
        }

        return safeMoves.get(random.nextInt(safeMoves.size()));
    }
}
