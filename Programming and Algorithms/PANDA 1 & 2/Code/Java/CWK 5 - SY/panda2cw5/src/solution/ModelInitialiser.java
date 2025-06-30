package solution;

import scotlandyard.Colour;
import scotlandyard.Ticket;
import java.util.*;


public class ModelInitialiser {

    ScotlandYardModel model;
    List<PlayerInfo> detectives;
    List<Colour> playerColours;
    private List<Boolean> rounds = new ArrayList<Boolean>(Arrays.asList(false,false,true,false,false,false,false,true,false,false,false,false,true,false,false,false,false,false,true,false));
    private String graphFileName = "solution/resources/graph.txt";


    // Recreates the game state of the model using data obtained from a saved game file.
    public ModelInitialiser (int roundNum, int numDetec, int mrXLast, List<Integer> playLoc,
                             List<Colour> oOP, int cPI, int nOS,
                             List<List<Integer>> aVL, List<List<Integer>> aUT)
    {

        model = new ScotlandYardModel(numDetec, rounds, graphFileName);
        model.setRoundNumber(roundNum); // Sets the round number.
        model.setNumberOfDetectives(numDetec); // Sets the number of detectives.
        model.setMrXLastLocation(mrXLast);  // Sets Mr X Last Location.
        model.setCurrentPlayerIndex(cPI);   // Sets current player index.


        // Adds MrX to the game.
        model.join(new PlayerInfo(), oOP.get(0), playLoc.get(0), MrXMap(aUT));


        // Adds the Detectives to the game.
        for(int i = 1; i <= numDetec; i++)
        {
            model.join(new PlayerInfo(), oOP.get(i), playLoc.get(i), DetectiveMap(aUT.get(i)));
        }


        // The lists of all tickets used  and all visited locations are set for each player.
        for(int i = 0; i < model.playerList.size(); i ++)
        {
            model.playerList.get(i).setTicketList(aUT.get(i));
            model.playerList.get(i).setVisitedLocations(aVL.get(i));
        }

    }

    // Initialises a model to a new game state.
    public ModelInitialiser(int numberOfDetectives) {


        model = new ScotlandYardModel(numberOfDetectives, rounds, graphFileName);
        List<Integer> detectiveSpawnLocationList = new ArrayList<Integer>(Arrays.asList(13, 26, 29, 34, 50, 53, 91, 94, 103, 112, 117, 132, 138, 141, 155, 174, 197, 198));
        List<Integer> mrXSpawnLocationList = new ArrayList<Integer>(Arrays.asList(5, 15, 63, 67, 98, 121, 140));


        // Creates a list of all possible detective colours.
        playerColours = new ArrayList<Colour>();
        playerColours.add(0, Colour.White);
        playerColours.add(1, Colour.Green);
        playerColours.add(2, Colour.Red);
        playerColours.add(3, Colour.Blue);
        playerColours.add(4, Colour.Yellow);


        // Adds Mr. X to the game.
        PlayerInfo mrX = new PlayerInfo();
        model.join(mrX, Colour.Black, getRandomElement(mrXSpawnLocationList), MrXMap());


        // Adds the detectives to the game.
        detectives = new ArrayList<PlayerInfo>(numberOfDetectives);
        for (int i = 0; i < numberOfDetectives; i++) {
            detectives.add(i, new PlayerInfo());
            model.join(detectives.get(i), getRandomElement(playerColours), getRandomElement(detectiveSpawnLocationList), DetectiveMap());
        }
    }


    // Returns the initialised model.
    public ScotlandYardModel returnModel()
    {
        return model;
    }


    // Creates the ticket map for a detective for a new game.
    public Map<Ticket, Integer> DetectiveMap()
    {
        Map<Ticket, Integer> detectiveTicketMap = new LinkedHashMap<Ticket, Integer>();
        detectiveTicketMap.put(Ticket.Taxi, 11);
        detectiveTicketMap.put(Ticket.Bus, 8);
        detectiveTicketMap.put(Ticket.Underground, 4);
        detectiveTicketMap.put(Ticket.DoubleMove, 0);
        detectiveTicketMap.put(Ticket.SecretMove, 0);
        return detectiveTicketMap;
    }


    // Creates the ticket map for a detective from a loaded game.
    public Map<Ticket, Integer> DetectiveMap(List<Integer> usedTickets)
    {
        // Initial ticket values.
        int taxi = 11;
        int bus = 8;
        int underground = 4;

        // Removes a ticket for each ticket in the player's list of used tickets.
        for(Integer ticket : usedTickets)
        {
            if(ticket == 0)
                taxi -= 1;
            else if (ticket == 1)
                bus -= 1;
            else if (ticket == 2)
                underground -= 1;
        }

        // Assigns the values to the ticket map.
        Map<Ticket, Integer> detectiveTicketMap = new LinkedHashMap<Ticket, Integer>();
        detectiveTicketMap.put(Ticket.Taxi, taxi);
        detectiveTicketMap.put(Ticket.Bus, bus);
        detectiveTicketMap.put(Ticket.Underground, underground);
        detectiveTicketMap.put(Ticket.DoubleMove, 0);
        detectiveTicketMap.put(Ticket.SecretMove, 0);
        return detectiveTicketMap;
    }


    // Creates the ticket map for Mr. X for a new game.
    public Map<Ticket, Integer> MrXMap()
    {
        Map<Ticket, Integer> mrXTicketMap = new LinkedHashMap<Ticket, Integer>();
        mrXTicketMap.put(Ticket.Taxi, 4);
        mrXTicketMap.put(Ticket.Bus, 3);
        mrXTicketMap.put(Ticket.Underground, 3);
        mrXTicketMap.put(Ticket.DoubleMove, 2);
        mrXTicketMap.put(Ticket.SecretMove, 5);
        return mrXTicketMap;
    }


    // Creates the ticket map for a detective from a loaded game.
    public Map<Ticket, Integer> MrXMap(List<List<Integer>> usedTickets)
    {
        // Initial ticket values.
        int taxi = 4;
        int bus = 3;
        int underground = 3;
        int doubleMove = 2;
        int secret = 5;

        // Adds the tickets used by the detectives to Mr. X.
        for(int i = 1; i < model.orderOfPlay.size(); i++)
        {
            for(Integer ticket : usedTickets.get(i))
            {
                if (ticket == 0)
                    taxi += 1;
                else if (ticket == 1)
                    bus += 1;
                else if (ticket == 2)
                    underground += 1;
            }
        }

        // Removes the tickets Mr. X used.
        for(Integer ticket : usedTickets.get(0))
        {
            if(ticket == 0)
                taxi -= 1;
            else if (ticket == 1)
                bus -= 1;
            else if (ticket == 2)
                underground -= 1;
            else if (ticket == 3)
                secret -= 1;
            else if (ticket == 4)
                doubleMove -= 1;
        }

        // Creates Mr. X's ticket map.
        Map<Ticket, Integer> mrXTicketMap = new LinkedHashMap<Ticket, Integer>();
        mrXTicketMap.put(Ticket.Taxi, taxi);
        mrXTicketMap.put(Ticket.Bus, bus);
        mrXTicketMap.put(Ticket.Underground, underground);
        mrXTicketMap.put(Ticket.DoubleMove, doubleMove);
        mrXTicketMap.put(Ticket.SecretMove, secret);
        return mrXTicketMap;

    }


    /*
        1. Selects a random element from a list.
        2. Removes that element from the list.
        3. Returns the selected element.
    */
    public <X> X getRandomElement(List<X> list)
    {
        Random randomGenerator = new Random();
        Integer item = randomGenerator.nextInt(list.size());
        X chosenItem = list.get(item);
        for (Iterator<X> iterator = list.iterator(); iterator.hasNext();)
        {
            X x = iterator.next();
            if (x.equals(chosenItem)) {
                iterator.remove();
            }
        }
        return chosenItem;
    }
}

