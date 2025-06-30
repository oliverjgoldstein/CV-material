package player;

import scotlandyard.*;

import java.io.IOException;
import java.util.*;

/**
 * The RandomPlayer class is an example of a very simple AI that
 * makes a random move from the given set of moves. Since the
 * RandomPlayer implements Player, the only required method is
 * notify(), which takes the location of the player and the
 * list of valid moves. The return value is the desired move,
 * which must be one from the list.
 */

public class MyAIPlayer implements Player {
    ScotlandYardView view;
    Graph graph;
    Valid valid;

    // Constructor initialises all variables.
    public MyAIPlayer(ScotlandYardView view, String graphFilename) {
        this.view = view;
        try {
            ScotlandYardGraphReader reader = new ScotlandYardGraphReader();
            Graph graph = reader.readGraph(graphFilename);
            this.graph = graph;
        } catch (IOException e) {
            System.out.println("Wrong filename.");
            System.exit(1);
        }
        valid = new Valid(view, graph);
    }

    @Override
    public Move notify(int location, Set<Move> moves) {
        // If the player is Mr X
        if(view.getCurrentPlayer().equals(Colour.Black))
        {
            // Used to store the initial locations of the detectives.
            List<Integer> initialLocations = new ArrayList<Integer>();
            for(Colour colour : view.getPlayers())
            {
                if (!colour.equals(Colour.Black))
                {
                    initialLocations.add(view.getPlayerLocation(colour));
                }
            }

            // Represents how deep we want to search in the game tree.
            int depth = 8;

            // Value that will be maximised.
            double alpha = Double.NEGATIVE_INFINITY;

            // Value that will be minimised.
            double beta = Double.POSITIVE_INFINITY;

            // Stores the score associated with the best move in minimax.
            double miniMax = valid.miniMax(location, initialLocations, depth, alpha, beta, true);

            // Stores the location of the best move in minimax
            int maxLoc = (int) valid.getMiniMaxLocation();

            // Resets the time flag for the next time minimax is called.
            valid.resetTimeFlag();

            // Generates a list of moves to the minimax location.
            List<Move> movesToChooseFrom = valid.validMovesFilter(Colour.Black, location, maxLoc);

            // Randomly chooses a move from the generated list.
            int choice = new Random().nextInt(movesToChooseFrom.size());
            for (Move move : movesToChooseFrom) {
                if (choice == 0) {
                    return move;
                }
                choice--;
            }

        }
      // If the Player is a detective
      else {
            // Variable to store Mr X's last known location.
            Integer lastKnown = view.getPlayerLocation(Colour.Black);

            // If Mr. X has not revealed himself yet, a random move is returned.
            if(lastKnown == 0) {
                int choice = new Random().nextInt(moves.size());
                for (Move move : moves) {
                    if (choice == 0) {
                        return move;
                    }
                    choice--;
                }
            }

            // The minimum path between the Detective and Mr. X's last known location is found.
            Dijkstra dijkstra = new Dijkstra(graph, lastKnown);

            // A list of moves is generated to the location that will bring the detective one step closer to Mr. X.
            Integer nextMove = dijkstra.getPath(location);
            List<Move> movesToChooseFrom = valid.validMovesFilter(view.getCurrentPlayer(), location, nextMove);

            // Variable set to true if another detective is at Mr X's last known location or the location the detective wants to move to.
            Boolean occupied = false;
            for(Colour player : view.getPlayers())
            {
                if(!player.equals(view.getCurrentPlayer()) && !player.equals(Colour.Black))
                {
                    if (view.getPlayerLocation(player) == lastKnown || view.getPlayerLocation(player) == nextMove) {
                        occupied = true;
                    }
                }
            }

            // Returns a random move if a detective is at Mr. X's last known location or the location we originally wanted to move to.
            if((movesToChooseFrom.size() == 1 && movesToChooseFrom.get(0) instanceof MovePass) || occupied == true)
            {
                int choice = new Random().nextInt(moves.size());
                for (Move move : moves) {
                    if (choice == 0) {
                        return move;
                    }
                    choice--;
                }
            }
            // Randomly chooses from among the moves that will bring the detective closer to Mr. X's last known location.
            else
            {
                int choice = new Random().nextInt(movesToChooseFrom.size());
                for (Move move : movesToChooseFrom) {
                    if (choice == 0) {
                        return move;
                    }
                    choice--;
                }
            }

        }
        return null;
    }


}