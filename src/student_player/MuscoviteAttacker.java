package student_player;

import java.util.ArrayList;
import java.util.List;

import boardgame.Move;
import coordinates.Coordinates;
import tablut.TablutBoard;
import tablut.TablutBoardState;
import tablut.TablutMove;

public class MuscoviteAttacker {
	public static Move getOpeningMove(TablutBoardState bs) {
		List<TablutMove> openerMoves = bs.getLegalMovesForPosition(Coordinates.get(1, 4));
		for (TablutMove move : openerMoves) {
			if (move.getEndPosition().x == 2 && move.getEndPosition().y == 4) {
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

		value += MyTools.cutOffCornerCount(bs) * 10;
		if (bs.gameOver()) {
			if (bs.getWinner() == TablutBoardState.MUSCOVITE) {
				return Integer.MAX_VALUE;
			} else {
				return Integer.MIN_VALUE;
			}
		}
		return value;
	}
}
