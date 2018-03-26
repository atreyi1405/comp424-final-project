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
        if (bs.gameOver()) {
            if (bs.getWinner() == TablutBoardState.MUSCOVITE) {
                return 100000 - bs.getTurnNumber();
            } else {
                return -100000;
            }
        }

	    int value = 100;
		value += bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) * 30;

		value -= bs.getNumberPlayerPieces(TablutBoardState.SWEDE) * 20;

		List<Coord> cutoffCorners = MyTools.getCutoffCorners(bs);

        List<Coord> powerPositions = MyTools.getMuscovitePowerPositions(bs);


        for(Coord coord : powerPositions) {
            //Reward power positions, even greater if they are near the king
            value += 10;
            if (coord.distance(bs.getKingPosition()) <= 3) {
                value += 40;
            }
        }

        //Don't let the king get to a a wall
        Coord kingPosition = bs.getKingPosition();
        if (kingPosition.x == 0 || kingPosition.x == 8 || kingPosition.y == 0 || kingPosition.y == 8)
            value -= 100;

        value += 7 * getPartialCutoffCornerCount(bs, powerPositions);

		value += cutoffCorners.size() * 30;

		return value;
	}

	public static int getPartialCutoffCornerCount(TablutBoardState bs, List<Coord> powerPositions) {
	    int count = 0;
	    for(Coord coord: powerPositions) {
	        if(coord.x == 1 && coord.y == 1) {
	            if (bs.getPieceAt(0, 2) == TablutBoardState.Piece.BLACK) {
	                count++;
                }
                if (bs.getPieceAt(2, 0) == TablutBoardState.Piece.BLACK) {
	                count++;
                }
            }

            if(coord.x == 7 && coord.y == 1) {
                if (bs.getPieceAt(6, 0) == TablutBoardState.Piece.BLACK ) {
                    count++;
                }

                if (bs.getPieceAt(8, 2) == TablutBoardState.Piece.BLACK) {
                    count++;
                }
            }

            if(coord.x == 1 && coord.y == 7) {
                if (bs.getPieceAt(0, 6) == TablutBoardState.Piece.BLACK) {
                    count ++;
                }

                if(bs.getPieceAt(2, 8) == TablutBoardState.Piece.BLACK) {
                    count ++;
                }
            }

            if(coord.x == 7 && coord.y == 7) {
                if (bs.getPieceAt(6, 8) == TablutBoardState.Piece.BLACK ) {
                    count ++;
                }

                if (bs.getPieceAt(8, 6) == TablutBoardState.Piece.BLACK) {
                    count++;
                }
            }
        }

        return count;
    }
}
