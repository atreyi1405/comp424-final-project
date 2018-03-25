package student_player;

import boardgame.Move;
import tablut.TablutBoardState;
import tablut.TablutPlayer;

/** A player file submitted by a student. */
public class StudentPlayer extends TablutPlayer {
    MonteCarloTreeSearch MCTS = null;
    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260564523");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(TablutBoardState bs) {
        // You probably will make separate functions in MyTools.
        // For example, maybe you'll need to load some pre-processed best opening
        // strategies...
        if (bs.getTurnNumber() == 1) {
            if (player_id == TablutBoardState.MUSCOVITE) {
                MCTS = new MonteCarloTreeSearch(bs, TablutBoardState.MUSCOVITE);
                return MuscoviteAttacker.getOpeningMove(bs);
            } else {
                MCTS = new MonteCarloTreeSearch(bs, TablutBoardState.SWEDE);
                return SwedeDefender.getOpeningMove(bs);
            }
        }

        if (player_id == TablutBoardState.MUSCOVITE) {
            return MuscoviteAttacker.getMove(bs);
        } else {
            return MCTS.findNextMove(bs, 2000);
//            return SwedeDefender.getMove(bs);
        }
    }
}