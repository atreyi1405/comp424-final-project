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
        return Minimax.getBestMove(bs, TablutBoardState.MUSCOVITE, 2);
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
        //Muscovites reward keeping our own pieces rather than taking the Swedes
		value += bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) * 30;
		value -= bs.getNumberPlayerPieces(TablutBoardState.SWEDE) * 20;

		//Get corners that are cutoff
		List<Coord> cutoffCorners = MyTools.getCutoffCorners(bs);

        value += cutoffCorners.size() * 30;

		//Get power positions
        List<Coord> powerPositions = MyTools.getMuscovitePowerPositions(bs);


        for(Coord coord : powerPositions) {
            //Reward power positions, even greater if they are near the king
            value += 10;
            if (coord.distance(bs.getKingPosition()) <= 3) {
                value += 40;
            }
        }

        //Reward the muscovites for building partial barricades, enroute to entirely barricading a corner
        value += 7 * getPartialCutoffCornerCount(bs, powerPositions);


        //Don't let the king get to a a wall
        Coord kingPosition = bs.getKingPosition();
        if (kingPosition.x == 0 || kingPosition.x == 8 || kingPosition.y == 0 || kingPosition.y == 8)
            value -= 100;

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
