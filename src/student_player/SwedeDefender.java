package student_player;

import java.util.ArrayList;
import java.util.List;
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

    public static int evaluatePosition(TablutBoardState bs, TablutMove move) {
        if (bs.gameOver()) {
            if (bs.getWinner() == TablutBoardState.SWEDE) {
                return 100000 - bs.getTurnNumber();
            } else {
                return -100000;
            }
        }

        int value = 1000;
        value += bs.getNumberPlayerPieces(TablutBoardState.SWEDE) * 5;


        value -= bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) * 10;

        //Favour moves that move the king to a wall
        Coord kingPosition = bs.getKingPosition();
        if (kingPosition.x == 0 || kingPosition.x == 8 || kingPosition.y == 0 || kingPosition.y == 8) {
            value += 100;

        }

        //Slightly favour moves that move the king, especially in the early game
        if (move.getEndPosition().x == kingPosition.x && move.getEndPosition().y == kingPosition.y) {
            value += 15 + move.getStartPosition().distance(move.getEndPosition()) ;
        }
        List<Coord> freeCorners = MyTools.getFreeCorners(bs);

        for(Coord freeCorner: freeCorners) {
            //Move away from cutoff corners
            if (bs.getKingPosition().distance(freeCorner) < 5) {
                value += 30;
                continue;
            }
        }


        value -= Coordinates.distanceToClosestCorner(bs.getKingPosition());

        return value;
    }
}
