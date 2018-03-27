package autoplay;

import boardgame.Server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

//Author: Lilly Tong, Eric Crawford
//
// Assumes all the code in ``src`` has been compiled, and the resulting
// class files were stored in ``bin``.
//
// From the root directory of the project, run
//
//     java -cp bin autoplay.Autoplay n_games
//
// Note: The script is currently set up to have the StudentPlayer play against
// RandomHusPlayer. In order to have different players participate, you need
// to change the variables ``client1_line`` and ``client2_line``. Make sure
// that in those lines, the classpath and the class name is set appropriately
// so that java can find and run the compiled code for the agent that you want
// to test. For example to have StudentPlayer play against itself, you would
// change ``client2_line`` to be equal to ``client1_line``.
//
public class Autoplay {
    public static void main(String args[]) {
        File logs = new File("/Users/callum/projects/comp424-final-project/" + Server.log_dir);
        deleteDir(logs);
        int n_games;

        try {
            n_games = Integer.parseInt(args[0]);
            if (n_games < 1) {
                throw new Exception();
            }
        } catch (Exception e) {
            System.err.println(
                    "First argument to Autoplay must be a positive int " + "giving the number of games to play.");
            return;
        }

        try {
            ProcessBuilder server_pb = new ProcessBuilder("java", "-cp", "bin", "boardgame.Server", "-ng", "-k", "-t", "2000", "-p", "8125");
            server_pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            Process server = server_pb.start();

            ProcessBuilder client1_pb = new ProcessBuilder("java", "-cp", "bin", "-Xms520m", "-Xmx520m",
                    "boardgame.Client", "student_player.StudentPlayer", "localhost", "8125");
            client1_pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            ProcessBuilder client2_pb = new ProcessBuilder("java", "-cp", "bin", "-Xms520m", "-Xmx520m",
                    "boardgame.Client", "student_player.StudentPlayer", "localhost", "8125");
            client2_pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);

            for (int i = 0; i < n_games; i++) {
                System.out.println("Game " + i);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                Process client1 = ((i % 2 == 0) ? client1_pb.start() : client2_pb.start());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

                Process client2 = ((i % 2 == 0) ? client2_pb.start() : client1_pb.start());

                try {
                    client1.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    client2.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            server.destroy();

        } catch (IOException e) {
            e.printStackTrace();
        }

        List<String> lines = null;
        try {
            lines = Files.readAllLines(Paths.get("/Users/callum/projects/comp424-final-project/"+ Server.log_dir+"/outcomes.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        double whiteWins = 0;
        double blackWins = 0;
        double draws = 0;

        for(String line : lines){
            String win = line.split(",")[3];
            switch(win) {
                case "0":
                    blackWins++;
                    break;
                case "1":
                    whiteWins++;
                    break;
                default:
                    draws++;
                    break;
            }
        }

        System.out.println("Black wins: " + blackWins/lines.size() );
        System.out.println("White wins: " + whiteWins/lines.size() );
        System.out.println("Draws: " + draws/lines.size() );
    }

    static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}