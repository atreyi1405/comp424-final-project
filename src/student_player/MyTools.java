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
    public static List<Coord> getCutoffCorners(TablutBoardState bs) {
        List<Coord> cutOffCorners = new ArrayList<>();

        for (int i = 0; i < 4; i ++) {
            int x = i/2;
            int y = i % 2;
            if (bs.getPieceAt(Coordinates.get(x == 0 ? 2 : 6, y == 0 ? 0 : 8)) == TablutBoardState.Piece.BLACK &&
                bs.getPieceAt(Coordinates.get(x == 0 ? 1 : 7, y == 0 ? 1 : 7)) == TablutBoardState.Piece.BLACK &&
                bs.getPieceAt(Coordinates.get(x == 0 ? 0 : 8, y == 0 ? 2 : 6)) == TablutBoardState.Piece.BLACK &&
                bs.getPieceAt(Coordinates.get(x == 0 ? 0 : 8, y == 0 ? 1 : 7)) == TablutBoardState.Piece.EMPTY &&
                bs.getPieceAt(Coordinates.get(x == 0 ? 1 : 7, y == 0 ? 0 : 8)) == TablutBoardState.Piece.EMPTY) {
                cutOffCorners.add(Coordinates.get(x==0 ? 0 : 8, y == 0 ? 0 : 8));
            }
        }

        return cutOffCorners;
    }

    public static List<Coord> getFreeCorners(TablutBoardState bs) {
        List<Coord> freeCorners = new ArrayList<>();

        for (int i = 0; i < 4; i ++) {
            int x = i/2;
            int y = i % 2;
            if (!(bs.turnPlayerCanMoveFrom(Coordinates.get(x == 0 ? 2 : 6, y == 0 ? 0 : 8)) &&
                    bs.turnPlayerCanMoveFrom(Coordinates.get(x == 0 ? 1 : 7, y == 0 ? 1 : 7)) &&
                    bs.turnPlayerCanMoveFrom(Coordinates.get(x == 0 ? 0 : 8, y == 0 ? 2 : 6)))) {
                freeCorners.add(Coordinates.get(x==0 ? 0 : 8, y == 0 ? 0 : 8));
            }
        }

        return freeCorners;
    }

    public static List<Coord> getMuscovitePowerPositions(TablutBoardState bs) {
        List<Coord> powerPositions = new ArrayList<>();

        if(bs.getPieceAt(1, 1) == TablutBoardState.Piece.BLACK)
            powerPositions.add(Coordinates.get(1, 1));

        if(bs.getPieceAt(1, 7) == TablutBoardState.Piece.BLACK)
            powerPositions.add(Coordinates.get(1, 7));

        if(bs.getPieceAt(7, 1) == TablutBoardState.Piece.BLACK)
            powerPositions.add(Coordinates.get(7, 1));

        if(bs.getPieceAt(7, 7) == TablutBoardState.Piece.BLACK)
            powerPositions.add(Coordinates.get(7, 7));

        return powerPositions;
    }

    public static boolean isCoordPowerPosition(Coord coord) {
        if(coord.maxDifference(Coordinates.get(1, 1)) == 0)
            return true;

        if(coord.maxDifference(Coordinates.get(1, 7)) == 0)
            return true;

        if(coord.maxDifference(Coordinates.get(7, 1)) == 0)
            return true;

        if(coord.maxDifference(Coordinates.get(7, 7)) == 0)
            return true;

        return false;
    }

    public static List<Coord> getMuscovitePartialCutoffCornerCoords(TablutBoardState bs, List<Coord> powerPositions) {
        List<Coord> partialCutoffCoords = new ArrayList<>();
        for(Coord coord: powerPositions) {
            if(coord.x == 1 && coord.y == 1) {
                if (bs.getPieceAt(0, 2) == TablutBoardState.Piece.BLACK)
                    partialCutoffCoords.add(Coordinates.get(0, 2));

                if (bs.getPieceAt(2, 0) == TablutBoardState.Piece.BLACK)
                    partialCutoffCoords.add(Coordinates.get(2, 0));
            }

            if(coord.x == 7 && coord.y == 1) {
                if (bs.getPieceAt(6, 0) == TablutBoardState.Piece.BLACK )
                    partialCutoffCoords.add(Coordinates.get(6, 0));

                if (bs.getPieceAt(8, 2) == TablutBoardState.Piece.BLACK)
                    partialCutoffCoords.add(Coordinates.get(8, 2));
            }

            if(coord.x == 1 && coord.y == 7) {
                if (bs.getPieceAt(0, 6) == TablutBoardState.Piece.BLACK)
                    partialCutoffCoords.add(Coordinates.get(0, 6));

                if(bs.getPieceAt(2, 8) == TablutBoardState.Piece.BLACK)
                    partialCutoffCoords.add(Coordinates.get(2, 8));
            }

            if(coord.x == 7 && coord.y == 7) {
                if (bs.getPieceAt(6, 8) == TablutBoardState.Piece.BLACK )
                    partialCutoffCoords.add(Coordinates.get(6, 8));

                if (bs.getPieceAt(8, 6) == TablutBoardState.Piece.BLACK)
                    partialCutoffCoords.add(Coordinates.get(8, 6));
            }
        }

        return partialCutoffCoords;
    }
}
