package student_player;

import java.util.Collections;
import java.util.Comparator;

public class UCT {
    public static double uctValue(int totalVisit, double numberWins, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }

        return ((double) numberWins / (double) nodeVisit)
                + 1.41 * Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    public static Node findBestNodeWithUCT(Node node) {
        int totalVisits = node.getVisits();
        return Collections.max(
                node.children,
                Comparator.comparing(c -> uctValue(totalVisits,
                        c.getWins(), c.getVisits())));
    }
}
