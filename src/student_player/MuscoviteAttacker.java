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

	public static double evaluatePosition(TablutBoardState bs, TablutMove move) {
        if (bs.gameOver()) {
            if (bs.getWinner() == TablutBoardState.MUSCOVITE) {
                return 100000 - bs.getTurnNumber();
            } else {
                return -100000;
            }
        }

	    double value = 1000;
        //Muscovites reward keeping our own pieces rather than taking the Swedes
		value += bs.getNumberPlayerPieces(TablutBoardState.MUSCOVITE) * 30;
		value -= bs.getNumberPlayerPieces(TablutBoardState.SWEDE) * 20;

		//Get corners that are cutoff
		List<Coord> cutoffCorners = MyTools.getCutoffCorners(bs);

        value += cutoffCorners.size() * 30;

		//Get power positions
        List<Coord> powerPositions = MyTools.getMuscovitePowerPositions(bs);
        Coord kingPosition = bs.getKingPosition();


        for(Coord coord : powerPositions) {
            //Reward power positions, even greater if they are near the king
            value += 10;
            if (coord.distance(bs.getKingPosition()) <= 3) {
                value += 40;
            }
        }

        //Reward the muscovites for building partial barricades, enroute to entirely barricading a corner
        List<Coord> partialCutOffCornerCoords = MyTools.getMuscovitePartialCutoffCornerCoords(bs, powerPositions);
        value += 7 * partialCutOffCornerCoords.size();

        //Further reward building barricades near the king
        for(Coord coord: partialCutOffCornerCoords) {
            if (kingPosition.maxDifference(coord) <= 3) {
                value += 5;
            }
        }

        //Don't let the king get to a a wall
        if (kingPosition.x == 0 || kingPosition.x == 8 || kingPosition.y == 0 || kingPosition.y == 8)
            value -= 100;

		return value;
	}
}
