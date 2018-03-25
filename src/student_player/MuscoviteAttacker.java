package student_player;

import java.util.ArrayList;
import java.util.List;

import boardgame.Move;
import coordinates.Coord;
import coordinates.Coordinates;
import tablut.TablutBoardState;
import tablut.TablutMove;

public class MuscoviteAttacker {
	public static Move getOpeningMove(TablutBoardState bs) {
		List<TablutMove> openerMoves = bs.getLegalMovesForPosition(Coordinates.get(4, 1));
		for (TablutMove move : openerMoves) {
			if (move.getEndPosition().x == 2 && move.getEndPosition().y == 1) {
				return move;
			}
		}
		
		return null;
	}

	public static Move getMove(TablutBoardState bs) {
		return bs.getRandomMove();
	}

	public static List<TablutMove>  getCandidateMoves(TablutBoardState bs) {
		List<TablutMove> candidateMoves = new ArrayList<>();
		candidateMoves.add((TablutMove) bs.getRandomMove());
		return candidateMoves;
	}


	public static int evaluatePosition(TablutBoardState bs, TablutMove move) {
		int value = 100;
		value += bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) * 10;

		value -= bs.getNumberPlayerPieces(TablutBoardState.SWEDE) * 5;

		List<Coord> cutoffCorners = MyTools.getCutoffCorners(bs);

        List<Coord> powerPositions = MyTools.getMuscovitePowerPositions(bs);

        for(Coord coord : powerPositions) {
            if (coord.distance(bs.getKingPosition()) < 3) {
                value += 100;
            }
        }

		value += cutoffCorners.size() * 100;
		if (bs.gameOver()) {
			if (bs.getWinner() == TablutBoardState.MUSCOVITE) {
				value += 100000;
			} else {
				value += 100000;
			}
		}
		return value;
	}
}
