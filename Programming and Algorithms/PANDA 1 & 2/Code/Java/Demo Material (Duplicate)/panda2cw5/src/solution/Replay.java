package solution;

import scotlandyard.*;
import java.util.ArrayList;
import java.util.List;

public class Replay {

    ScotlandYardModel model;
    List<List<Move>> playerMoves;
    List<Move> allMoves;

    // Initialises variables in the constructor.
    public Replay (ScotlandYardModel model)
    {
        this.model = model;
        playerMoves = new ArrayList<List<Move>>();
        allMoves = new ArrayList<Move>();
    }

    // Returns a list of all the moves made in the game in the order they were made.
    public List<Move> replayGame()
    {

        for(PlayerInfo player : model.playerList)
        {
            player.setLocation(player.getVisitedLocations().get(0)); // Each player is returned to their starting location.
            player.generateMoves(); // A list of each player's moves is generated.
            playerMoves.add(player.getMoves()); // The list of the player's moves is brought in and stored in a List of Lists
        }

        // Iterates through the list of list of moves and produces a single list of all moves in the order in which they were played.
        int i = 1;
        while (i < model.playerList.size()){

            for(List<Move> list : playerMoves)
            {

                if(list.isEmpty())
                {
                    i++;
                }

                else{
                    allMoves.add(list.get(0));
                    list.remove(0);
                }

            }

        }

        return(allMoves); // Returns the list of all moves made in the game.
    }

}
