package solution;

import scotlandyard.*;
import java.io.*;
import java.util.Date;
import javax.swing.JOptionPane;


public class Save {
    ScotlandYardModel model;
    int counter;

    // Initialises the model in the constructor.
    public Save(ScotlandYardModel model)
    {
        this.model = model;

    }

    // Saves the data needed to recreate a game state to file.
    public void saveGame()
    {

        // A dialogue box which confirms if the player wants to save the game or not.
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to save this game?", "Save Game",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        // If the player wants to save the game.
        if (response == JOptionPane.YES_OPTION) {
            try{

                // Creates new file in the Saved Games folder.
                Date date = new Date();
                File file = new File("solution/SavedGames/" + date + " SY.txt");
                if (!file.exists()) {
                    file.createNewFile();
                }

                // Output stream created to write to file.
                FileWriter writer = new FileWriter(file);
                BufferedWriter out = new BufferedWriter(writer);


                out.write(model.getRound() + "\n"); // Writes the round number to file.
                out.write(model.getNumberOfDetectives()+"\n"); // Writes the number of detectives to file.
                out.write(model.getPlayerLocation(Colour.Black) + "\n"); // Writes Mr. X's Last Location to file.


                // Writes a list of each player's last known location to file in the order of play.
                counter = 0;
                for (Colour colour : model.orderOfPlay)
                {

                    if (counter < model.orderOfPlay.size() - 1) {
                        out.write(model.playerWithColour(colour).getLocation() + (","));
                        counter += 1;

                    } else
                    {
                        out.write(model.playerWithColour(colour).getLocation() + "");
                    }

                }
                out.write("\n");


                // Writes the list of player colours to file in order of play.
                counter = 0;
                for (Colour colour : model.orderOfPlay)
                {

                    if (counter < model.orderOfPlay.size() - 1) {
                        out.write(colour + (","));
                        counter += 1;

                    } else
                    {
                        out.write(String.valueOf(colour));
                    }

                }
                out.write("\n");


                out.write(model.getCurrentPlayerIndex() + "\n"); // Writes the current player index to file.
                out.write(model.spectatorList.size() + "\n"); // Writes the number of spectators to file.


                // Iterates through all players in the order of play.
                for (Colour colour : model.orderOfPlay)
                {
                    // Writes the list of all visited locations for each player to file.
                    counter = 0;
                    for(Integer location : model.playerWithColour(colour).getVisitedLocations())
                    {
                        if (counter < model.playerWithColour(colour).getVisitedLocations().size() - 1) {
                            out.write(location + (","));
                            counter +=1;
                        }
                        else{
                            out.write(location + "");
                        }
                    }
                    out.write("\n");


                    // Write the list of all tickets used by each player to file.
                    counter = 0;
                    for(Integer ticket : model.playerWithColour(colour).getTicketList())
                    {
                        if (counter < model.playerWithColour(colour).getTicketList().size() - 1) {
                            out.write(ticket + (","));
                            counter += 1;
                        }
                        else{
                            out.write(ticket + "");
                        }
                    }
                    out.write("\n");
                }

                // Closes the output stream.
                out.close();

            }catch (Exception IO){
                System.err.println("Error: " + IO.getMessage());
            }
        }

    }

}
