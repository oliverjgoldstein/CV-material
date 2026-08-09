package solution;

import scotlandyard.*;

import java.util.*;
import java.io.IOException;

public class ScotlandYardModel extends ScotlandYard {

    private int roundNumber;
    private int numberOfDetectives;
    private int mrXLastLocation;
    List<Colour> orderOfPlay;
    private Colour currentPlayer;
    private int currentPlayerIndex;
    List<Boolean> rounds;
    List<PlayerInfo> playerList;
    Graph<Integer, Route> mainGraph;
    String graphName;
    Map<Integer, List<TargetAndTicket>> adjacencyMatrix;
    List<Spectator> spectatorList;


    //Initialises variables in the constructor.
    public ScotlandYardModel(int numberOfDetectives, List<Boolean> rounds, String graphFileName) {

        // Good practice to call parents constructor.
        super(numberOfDetectives, rounds, graphFileName);
        graphName = graphFileName;

        // Setting up the order of play and the rounds.
        orderOfPlay = new ArrayList<Colour>();
        this.rounds = new ArrayList<Boolean>(rounds);
        roundNumber = 0;

        // Initialise the detectives:
        this.numberOfDetectives = numberOfDetectives;

        // Initialise MrX as current player and last seen as 0. Index is set to Black.
        mrXLastLocation = 0;
        currentPlayerIndex = 0;

        // The roundNumber is 0 as the game starts.
        playerList = new ArrayList<PlayerInfo>(numberOfDetectives+1);

        // Setup the spectator.
        spectatorList = new ArrayList<Spectator>();

        // Capture the mainGraph in a graph.
        try
        {
            ScotlandYardGraphReader reader = new ScotlandYardGraphReader();
            mainGraph = reader.readGraph(graphFileName);
        }
        catch (IOException e)
        {
            System.out.println("Wrong filename.");
            System.exit(1);
        }

        // Creates the adjacency matrix of locations.
        adjacencyMatrix = findNeighbours();

    }


    // Returns true if the game is ready to be played.
    @Override
    public boolean isReady()
    {
        // Checks to see if all the detectives have joined the game.
        if((playerList.size()-1) == numberOfDetectives)
        {
            return true;
        }
        return false;
    }


    // Returns the move the player wants to make.
    @Override
    protected Move getPlayerMove(Colour colour) {

        // Finds the valid moves associated with the colour and waits on the player to select one.
        PlayerInfo playerInfo = playerWithColour(colour);
        List<Move> listOfMoves = validMoves(colour);
        Move move = playerInfo.notify(playerInfo.getLocation(), listOfMoves);
        return move;
    }


    // Returns the index of the next player to play the in order of play.
    private int getNextPlayerIndex()
    {
        currentPlayerIndex++;
        currentPlayerIndex = (currentPlayerIndex % (playerList.size()));
        return currentPlayerIndex;
    }


    // Returns the next player in the order of play.
    @Override
    protected void nextPlayer()
    {
        currentPlayer = orderOfPlay.get(getNextPlayerIndex());
    }



    // Plays a single move of type MoveTicket.
    @Override
    protected void play(MoveTicket move) {

        PlayerInfo player = playerWithColour(move.colour); // Gets the player that is making the move.
        player.addTicket(move.ticket); // Adds the ticket to the player's list of used tickets.
        player.addLocation(move.target); // Adds the tickets target location to the player's list of visited locations.


        if(!(orderOfPlay.get(0).equals(player.getColour()) && roundNumber==(rounds.size()))) {

            // The player is moved to the target location.
            player.setLocation(move.target);


            // Decrement the players number of tickets.
            int numberOfTickets = player.getTickets().get(move.ticket);
            numberOfTickets--;
            player.getTickets().put(move.ticket, numberOfTickets);


            // Adds tickets used by detectives to Mr. X's tickets.
            if (!(move.colour.equals(Colour.Black))) {
                PlayerInfo mrX = playerWithColour(Colour.Black);
                int currentTicketNumber = mrX.getTickets().get(move.ticket);
                currentTicketNumber++;
                mrX.getTickets().put(move.ticket, currentTicketNumber);
            }
        }

        // If the round is a visible round, reveal Mr. X's location.
        if (rounds.get(roundNumber) == true) {
            if (move.colour.equals(Colour.Black)) {
                mrXLastLocation = player.getLocation();
            }
        }

        // Notifies all spectators of the move that was made.
        for(Spectator spectator: spectatorList)
        {
            if(move.colour.equals(Colour.Black) && (rounds.get(roundNumber) == false))
            {
                MoveTicket tempMove = new MoveTicket(move.colour, mrXLastLocation, move.ticket);
                spectator.notify(tempMove);
            }
            else {
                spectator.notify(move);
            }
        }


        // Increments round after MrX plays.
        if (move.colour.equals(Colour.Black)) {
            ++roundNumber;
        }

    }

    // Plays a double move.
    @Override
    protected void play(MoveDouble move) {

        playerWithColour(move.colour).addTicket(move); // Gets the player that is making the move.
        play((MoveTicket) move.moves.get(0)); // Plays the first move of the double move.

        // Notifies all spectators of the first move.
        for(Spectator spectator: spectatorList)
        {
            spectator.notify(move);
        }

        // Plays the second move.
        PlayerInfo player = playerWithColour(move.colour);
        play((MoveTicket) move.moves.get(1));

        // Decrements the number of double move tickets the player has.
        int numberOfTickets = player.getTickets().get(Ticket.DoubleMove);
        numberOfTickets--;
        player.getTickets().put(Ticket.DoubleMove, numberOfTickets);

    }


    // Plays a pass move.
    @Override
    protected void play(MovePass move) {

        // Notifies all the spectators of the move that was made.
        for(Spectator spectator: spectatorList)
        {
            spectator.notify(move);
        }

        // Adds a 'pass' to the list of tickets used by the player.
        playerWithColour(move.colour).addTicket(move);

    }


    // Generates the list of valid moves a player can make from their current location.
    protected List<Move> validMoves(Colour player)
    {
        // Variable to store the Player's list of moves.
        List<Move> moveList = new ArrayList<Move>();
        List<TargetAndTicket> possibleLocations;
        PlayerInfo playerInfos = playerWithColour(player);
        MoveTicket singleMove;
        MoveDouble doubleMove;

        // Finds the adjacency matrix associated with player.
        possibleLocations = adjacencyMatrix.get(playerInfos.getLocation());

        Boolean detectivePresent;
        Boolean hasTicket;

        for(TargetAndTicket location:possibleLocations)
        {
            // Variable will be set to true if a detective is at the target location.
            detectivePresent = false;

            for(PlayerInfo playerInfo : playerList)
            {
                if (location.getTarget() == playerInfo.getLocation() && !(playerInfo.getColour().equals(Colour.Black)))
                {
                    detectivePresent = true;
                }
            }

            // Variable will be set to false if the player cannot access target due to lack of tickets.
            hasTicket = true;

            if(!(playerInfos.getTickets().get(location.getTicket()) > 0))
            {
                hasTicket = false;
            }

            /*
                In order to move:
                   1) A detective must not be at the target location.
                   2) The player must have a ticket.
             */
            if((detectivePresent == false) && (hasTicket == true))
            {
                singleMove = new MoveTicket(player, location.getTarget(), location.getTicket());
                moveList.add(singleMove);

                // If the player is Mr. X and he has double move tickets the following occurs:
                if(player.equals(Colour.Black) && !(playerInfos.getTickets().get(Ticket.DoubleMove) == 0))
                {
                    List<Move> secondMoveList = validMovesHelper(singleMove);
                    for (Move secondMove : secondMoveList) {
                        MoveTicket tempMove2 = (MoveTicket) secondMove;
                        doubleMove = new MoveDouble(player, singleMove, tempMove2);
                        moveList.add(doubleMove);
                    }

                }

                //If Mr X has secret moves left, this adds them to the list of valid moves
                if(player.equals(Colour.Black) && (playerInfos.getTickets().get(Ticket.SecretMove) > 0) )
                {
                    singleMove = new MoveTicket(player, location.getTarget(), Ticket.SecretMove);
                    moveList.add(singleMove);
                    if(!(playerInfos.getTickets().get(Ticket.DoubleMove) == 0))
                    {
                        List<Move> secondMoveList = validMovesHelper(singleMove);
                        for (Move secondMove : secondMoveList)
                        {
                            MoveTicket tempMove2 = (MoveTicket) secondMove;
                            doubleMove = new MoveDouble(player,singleMove,tempMove2);
                            moveList.add(doubleMove);
                        }

                    }
                }
            }
        }

        // If the player is a detective and has no valid moves, a single Pass Move is added to the Move List
        if((moveList.size() == 0) && !(player.equals(Colour.Black)))
        {
            Move move = new MovePass(player);
            moveList.add(move);
        }

        return moveList;
    }


    public List<Move> validMovesHelper (MoveTicket move)
    {
        List<TargetAndTicket> possibleLocations;
        PlayerInfo player = playerWithColour(Colour.Black);
        List<Move> secondMoveList = new ArrayList<Move>();
        MoveTicket singleMove;
        possibleLocations = adjacencyMatrix.get(move.target);
        Boolean detectivePresent;
        Boolean hasTicket;


        for(TargetAndTicket location:possibleLocations)
        {
            // Variable will be set to true if a detective is at the target location.
            detectivePresent = false;

            for(PlayerInfo playerInfo : playerList)
            {
                if ((location.getTarget() == playerInfo.getLocation()) && !(playerInfo.getColour().equals(Colour.Black)))
                {
                    detectivePresent = true;
                }
            }

            // Variable will be set to false if the player cannot access target due to lack of tickets.
            hasTicket = true;

            if((player.getTickets().get(location.getTicket()) <= 1) && (location.getTicket().equals(move.ticket)))
            {
                hasTicket = false;
            }


            /*
                In order to move:
                   1) A detective must not be at the target location.
                   2) The player must have a ticket.
             */
            if((detectivePresent == false) && (hasTicket == true))
            {
                singleMove = new MoveTicket(move.colour, location.getTarget(), location.getTicket());
                secondMoveList.add(singleMove);

                // If Mr. X has secret move tickets left, add a corresponding secret move to the list of second moves.
                if(!(player.getTickets().get(Ticket.SecretMove) <= 1 && location.getTicket().equals(Ticket.SecretMove)))
                {
                    singleMove = new MoveTicket(move.colour, location.getTarget(), Ticket.SecretMove);
                    secondMoveList.add(singleMove);
                }
            }
        }

        return secondMoveList;
    }

    // This creates an adjacency matrix for each node.
    // It goes through each node and finds the edges connected to them.
    // For each edge it finds the name of the node that is not the node it is currently traversing.
    // It then looks at the edge type of that edge and the other node name and creates a target and ticket.
    // For each node it has a list of these and we return a map of these at the end.
    public Map<Integer, List<TargetAndTicket>> findNeighbours()
    {
        Map<Integer, List<TargetAndTicket>> adjacencyMatrix = new HashMap<Integer, List<TargetAndTicket>>();
        for(Node n: mainGraph.getNodes())
        {
            List<TargetAndTicket> tempList = new ArrayList<TargetAndTicket>();
            for(Edge e: mainGraph.getEdges())
            {
                if((e.source().toString()).equals(n.toString()))
                {
                    TargetAndTicket element = new TargetAndTicket((Integer)e.other(e.source()), Ticket.fromRoute((Route)e.data()));
                    tempList.add(element);
                }
                else if((e.target().toString()).equals(n.toString()))
                {
                    TargetAndTicket element = new TargetAndTicket((Integer)e.other(e.target()), Ticket.fromRoute((Route)e.data()));
                    tempList.add(element);
                }
            }
            adjacencyMatrix.put(Integer.parseInt(n.toString()),tempList);
        }
        return adjacencyMatrix;
    }

    // Adds a new spectator to the list of spectators.
    @Override
    public void spectate(Spectator spectator) {
        spectatorList.add(spectator);
    }

    // Joins a new player to the game.
    @Override
    public boolean join(Player player, Colour colour, int location, Map<Ticket, Integer> tickets)
    {
        playerList.add(new PlayerInfo(player, colour, location, tickets));

        // Checks to see if the game is full.
        if(playerList.size() <= numberOfDetectives+1)
        {

            // Adds the player's colour to order of play.
            if(colour.equals(Colour.Black))
            {
                orderOfPlay.add(0,colour);
                currentPlayer = orderOfPlay.get(0);
            }

            else
            {
                orderOfPlay.add(colour);
            }

        }
        else
        {
            return false;
        }
        return true;
    }

    // Returns the colours of all the players.
    @Override
    public List<Colour> getPlayers()
    {
        return orderOfPlay;
    }

    // Returns the colours of the winning player(s).
    @Override
    public Set<Colour> getWinningPlayers() {

        List<Move> mrXMoves = validMoves(Colour.Black); // Generates a list of Mr. X's valid moves.

        // Creates a set containing Mr. X's colour.
        Set<Colour> mrX = new HashSet<Colour>();
        mrX.add(Colour.Black);

        // Creates a set for the Detectives' colours.
        Set<Colour> detectives = new HashSet<Colour>();

        // Set to true if Mr. X is caught.
        boolean mrXCaught = false;

        // Adds the colours of the detectives to the set and checks if Mr. X has been caught.
        for (PlayerInfo player : playerList) {
            if (!(player.getColour().equals(Colour.Black))) {
                detectives.add(player.getColour());
                if (player.getLocation() == playerWithColour(Colour.Black).getLocation()) {
                    mrXCaught = true;
                }
            }
        }

        // Returns the set of colours corresponding to the winners if the game is over.
        if (isGameOver())
        {

            if (mrXMoves.size() == 0) {
                mrXCaught = true;
            }

            if (mrXCaught == false) {
                return mrX;
            } else {
                return detectives;
            }

        }

        else
        {
            Set<Colour> gameNotOver = new HashSet<Colour>();
            return gameNotOver;
        }
    }

    // Returns the player's location.
    @Override
    public int getPlayerLocation(Colour colour)
    {
        // Returns Mr. X's last known location if his location is request but the round is false.
        if(colour.equals(Colour.Black) && (rounds.get(roundNumber) == false))
        {
            return mrXLastLocation;
        }

        PlayerInfo player = playerWithColour(colour);
        return player.getLocation();
    }

    // Returns a player's ticket map.
    @Override
    public int getPlayerTickets(Colour colour, Ticket ticket) {
        int ticketNumber;
        PlayerInfo player = playerWithColour(colour);
        ticketNumber = player.getTickets().get(ticket);
        return ticketNumber;
    }

    // Checks to see if the game is over or not.
    @Override
    public boolean isGameOver()
    {
        if(isReady())
        {
            for (PlayerInfo player : playerList) {
                if (!(player.getColour().equals(Colour.Black))) {
                    if (player.getLocation() == playerWithColour(Colour.Black).getLocation()) {
                        return true; // Returns true if Mr. X is caught.
                    }
                }
            }

            int numberOfTickets = 0;
            List<Move> totalMoves = new ArrayList<Move>();
            for (PlayerInfo player : playerList) {
                if (!(player.getColour().equals(Colour.Black))) {
                    numberOfTickets = numberOfTickets + player.getTickets().get(Ticket.Bus) + player.getTickets().get(Ticket.Taxi) + player.getTickets().get(Ticket.Underground);
                    List<Move> detectiveMoves = validMoves(player.getColour());
                    for (Move move: detectiveMoves)
                    {
                        if(move instanceof MovePass) {
                            totalMoves.add(move);
                        }
                    }
                }
            }

            if (numberOfTickets == 0 || totalMoves.size() == numberOfDetectives) {
                return true; // Returns true if the detectives have ran out of moves.
            }

            List<Move> mrXMoves = validMoves(Colour.Black);
            if(mrXMoves.isEmpty()) {
                return true; // Returns true if Mr. X cannot move.
            }
            if((rounds.size()==(roundNumber+1)) && (currentPlayer == orderOfPlay.get(0)))
            {
                return true; // Returns true if the rounds are up.
            }
        }

        return false; // Returns false if the game is not over.
    }

    // Returns the player associated with a given colour.
    public PlayerInfo playerWithColour(Colour colour)
    {
        for(PlayerInfo player: playerList)
        {
            if(player.getColour().equals(colour))
            {
                return player;
            }
        }
        return null;
    }

    // Returns the colour associated with the current player.
    @Override
    public Colour getCurrentPlayer()
    {
        return orderOfPlay.get(currentPlayerIndex);
    }

    // Returns the index of the colour in order of play associated with the current player.
    public int getCurrentPlayerIndex()
    {
        return currentPlayerIndex;
    }

    // Returns the round number.
    @Override
    public int getRound() {
        return roundNumber;
    }

    // Returns the list of rounds.
    @Override
    public List<Boolean> getRounds() {
        return rounds;
    }

    // Returns the number of detectives.
    public int getNumberOfDetectives()
    {
        return numberOfDetectives;
    }

    // Sets the round number.
    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    // Sets the number of detectives.
    public void setNumberOfDetectives(int numberOfDetectives) {
        this.numberOfDetectives = numberOfDetectives;
    }

    // Sets Mr. X's last location.
    public void setMrXLastLocation(int mrXLastLocation) {
        this.mrXLastLocation = mrXLastLocation;
    }

    // Sets the current player index.
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

}