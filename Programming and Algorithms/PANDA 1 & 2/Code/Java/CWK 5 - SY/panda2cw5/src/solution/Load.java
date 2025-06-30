package solution;

import scotlandyard.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.File;


public class Load {


    private ScotlandYardModel loadedModel;
    int roundNumber;
    int numberOfDetectives;
    int mrXLastLocation;
    List<Integer> playerLocations;
    List<Colour> orderOfPlay;
    int currentPlayerIndex;
    int numberOfSpectators;
    List<List<Integer>> allVisitedLocations;
    List<List<Integer>> allUsedTickets;
    int flagError = 0;
    private FileInputStream in;


    // Initialises all variables.
    public Load()
    {
        roundNumber = 0;
        numberOfDetectives = 0;
        mrXLastLocation = 0;
        playerLocations = new ArrayList<Integer>();
        orderOfPlay = new ArrayList<Colour>();
        currentPlayerIndex = 0;
        numberOfSpectators = 0;
        allVisitedLocations = new ArrayList<List<Integer>>();
        allUsedTickets = new ArrayList<List<Integer>>();
    }


    // Recreates a model which replicates the state of a saved game using data from a file.
    public void loadGame(File file){

        try {
            in = new FileInputStream(file); // File is read in.
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        try {
            // These variables are obtained and set based on file data.
            roundNumber = Integer.parseInt(getWord());
            numberOfDetectives = Integer.parseInt(getWord());
            mrXLastLocation = Integer.parseInt(getWord());


            //Retrieves the Last Location of Each Player in Order of Play from File
            String[] temp = getWord().split(",");
            for(int i = 0; i < temp.length; i++)
            {
                if(!(temp[i] == null) && !(temp[i].equals("")))
                {
                    playerLocations.add(Integer.parseInt(temp[i]));
                }
            }


            //Retrieves the Colours in Order of Play from File
            temp = getWord().split(",");
            for(int i = 0; i < temp.length; i++)
            {
                if(!(temp[i] == null) && !(temp[i] == ""))
                {
                    if(temp[i].equals(Colour.Black.toString()))
                        orderOfPlay.add(Colour.Black);
                    else if(temp[i].equals(Colour.Blue.toString()))
                        orderOfPlay.add(Colour.Blue);
                    else if(temp[i].equals(Colour.Green.toString()))
                        orderOfPlay.add(Colour.Green);
                    else if(temp[i].equals(Colour.Red.toString()))
                        orderOfPlay.add(Colour.Red);
                    else if(temp[i].equals(Colour.White.toString()))
                        orderOfPlay.add(Colour.White);
                    else if(temp[i].equals(Colour.Yellow.toString()))
                        orderOfPlay.add(Colour.Yellow);
                }
            }


            // These variables are obtained and set based on file data.
            currentPlayerIndex = Integer.parseInt(getWord());
            numberOfSpectators = Integer.parseInt(getWord());


            // Stores the Lists of Visited Locations and Used Tickets for Each Player in Order of Play.
            List<Integer> tempList;
            for(Colour colour : orderOfPlay)
            {
                tempList = new ArrayList<Integer>();
                temp = getWord().split(",");
                for(int i = 0; i < temp.length; i++)
                {
                    if(!(temp[i] == null) && !(temp[i].equals("")))
                    {
                        tempList.add(Integer.parseInt(temp[i]));
                    }
                }
                allVisitedLocations.add(tempList);

                tempList = new ArrayList<Integer>();
                temp = getWord().split(",");

                for(int i = 0; i < temp.length; i++)
                {
                    if(!(temp[i] == null) && !(temp[i].equals("")))
                    {
                        tempList.add(Integer.parseInt(temp[i]));
                    }
                }
                allUsedTickets.add(tempList);
            }


        }catch (Exception e) {
            flagError = 1;
            e.printStackTrace();
        }

    }


    // Retrieves the data from the saved game file one line at a time.
    public String getWord() throws IOException
    {
        int c;
        StringBuffer buf = new StringBuffer();

        do {
            c = in.read();
            if (c == '\n')
                return buf.toString();
            else
                buf.append((char) c);
        } while (c != -1);

        return buf.toString();
    }


    // Returns a model with a game state that has been replicated from a saved file.
    public ScotlandYardModel loadGameModel()
    {
        // Recreates the game state of the model using data obtained from a saved game file.
        ModelInitialiser modelInitialiser = new ModelInitialiser(roundNumber, numberOfDetectives, mrXLastLocation, playerLocations,
                orderOfPlay, currentPlayerIndex, numberOfSpectators, allVisitedLocations, allUsedTickets);

        // Assigns the recreated model to loadedModel variable.
        loadedModel = modelInitialiser.returnModel();

        return loadedModel;
    }


    // Returns the list of players in the recreated model.
    public List<PlayerInfo> getPlayerList()
    {
        return loadedModel.playerList;
    }


}
