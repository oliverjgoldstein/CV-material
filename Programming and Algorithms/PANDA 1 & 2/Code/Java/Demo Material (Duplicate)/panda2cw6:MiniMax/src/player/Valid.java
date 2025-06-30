package player;

import scotlandyard.*;
import java.util.*;

public class Valid {

    ScotlandYardView view;
    Graph graph;
    Map<Integer, List<TargetAndTicket>> adjacencyMatrix;
    Set<Integer> allLocationsInReach;
    List<Integer> detectiveLocations;
    double miniMaxLocation;
    long mStartTime;
    boolean timeFlag = true;


    // Initialises a new object Valid.
    public Valid(ScotlandYardView view, Graph graph) {
        this.view = view;
        this.graph = graph;
        allLocationsInReach = new HashSet<Integer>();
        adjacencyMatrix = findNeighbours();
        detectiveLocations = new ArrayList<Integer>();
        miniMaxLocation = 0.0;

    }

    // Produces a score associated with a current game state.
    public Integer score(Set<Move> moves, int location, List<Integer> detectiveLocations) {
        // The amount of moves a player can make is subtracted from score.
        // This hinders Mr. X from going to locations that have many conenctions and are thus easily accessible by detectives.
        Integer score = -(moves.size());

        Dijkstra dijkstra = new Dijkstra(graph, location);

        // Produces a value associated with the distance of the detective closest to Mr. X that is added to score.
        Double one = dijkstra.findDistances(detectiveLocations,true);
        one = one*200;
        score += one.intValue();

        // Produces a value associated with the total distance of all detectives from Mr. X that is added to score.
        Double all = dijkstra.findDistances(detectiveLocations,false);
        all = all*20;
        score += all.intValue();

        // Produces a value associated with Mr. X's general location on the board which is then added to score.
        FindMapLocation mapFinder = new FindMapLocation(location);
        Double additionToScore = mapFinder.findDistanceFromOrigin(location);
        score += additionToScore.intValue();

        return score;
    }





    // Finds the score which is the minimized maximum loss using alpha beta pruning.
    // To get the location corresponding to this score, call the getMiniMaxLocation() method.
    public double miniMax (Integer mrXLocation,  List<Integer> detectiveLocations, int depth, double alpha,double beta ,boolean bool)
    {
        // Generates the list of valid moves Mr X can make and in doing so a set of his possible locations.
        validMoves(Colour.Black, mrXLocation , detectiveLocations);

        // Stores set of Mr X's possible locations.
        Set<Integer> mrXPossibleMoves = allLocationsInReach;

        // Will only be true when first minimax call is made.
        if(timeFlag == true)
        {
            // Records the start time of the function.
            mStartTime = System.currentTimeMillis();
            timeFlag = false;
        }

        // Halts recursion if incomplete after 11 seconds has passed and returns the best evaluated move thus far.
        if(System.currentTimeMillis() - mStartTime > 10000)
        {
            return alpha;
        }

        double value;


        List<List<Integer>> possibleDetectiveLocations = new ArrayList<List<Integer>>();
        possibleDetectiveLocations.add(optimalDetec(mrXLocation,detectiveLocations));

        // If a terminal game state, assess the score of that state and return it.
        if (depth == 0)
        {
            Set<Move> movelist = new HashSet<Move>(validMoves(Colour.Black, mrXLocation, detectiveLocations));
            value = score(movelist, mrXLocation, detectiveLocations);
            return value;
        }

        // If the state is not terminal.
        else
        {
            // Minimisation step of minimax where the lowest of the terminal states is found and returned.
            if (bool == false) {
                value = Double.POSITIVE_INFINITY;
                for (Integer loc : mrXPossibleMoves) {
                    for (List<Integer> detecLocs : possibleDetectiveLocations) {
                        value = min(value, miniMax(loc, detecLocs, depth - 1, alpha, beta, true));
                        beta = min(beta, value);

                        // if a terminal score is found that's worse than the current best, move on to the next location.
                        if (beta < alpha || beta == alpha) {
                            break;
                        }
                    }
                    break;
                }
                return value;
            } else {
                // Maximisation step of minimax where best of the worst case scenarios are chosen.
                value = Double.NEGATIVE_INFINITY;
                for (Integer loc : mrXPossibleMoves) {
                    for (List<Integer> detecLocs : possibleDetectiveLocations) {
                        value = max(value, miniMax(loc, detecLocs, depth - 1, alpha, beta, false));
                        // If a new terminal state is found that is better than our previous best the location is updated.
                        if(value > alpha)
                        {
                            miniMaxLocation = loc;
                        }

                        // The best score is updated if a new best is found.
                        alpha = max(alpha, value);
                        // if a terminal score is found that's worse than the current best, move on to the next location.
                        if (beta < alpha || beta == alpha) {
                            break;
                        }
                    }
                    if (beta < alpha || beta == alpha) {
                        break;
                    }
                }
                return value;
            }
        }
    }

    // Returns the lesser of two values.
    public double min( double a, double b){
        if(a < b)
            return a;
        else
            return b;
    }

    // Returns the greater of two values.
    public double max( double a, double b){
        if(a > b)
            return a;
        else
            return b;
    }

    // Returns the optimal move based on minimax.
    public double getMiniMaxLocation()
    {
        return miniMaxLocation;
    }






    // Produces the list of all valid moves a player can make.
    protected List<Move> validMoves(Colour player, Integer playerLocation, List<Integer> detectiveLocations)
    {
        this.detectiveLocations = detectiveLocations;
        Set<Integer> locationsInReach = new HashSet<Integer>();
        List<Move> moveList = new ArrayList<Move>();
        List<TargetAndTicket> possibleLocations;
        MoveTicket singleMove;
        MoveDouble doubleMove;

        // Gets the list of players fom the view.
        List<Colour> playerList = view.getPlayers();

        // Finds the adjacency matrix associated with player.
        possibleLocations = adjacencyMatrix.get(playerLocation);

        Boolean detectivePresent;
        Boolean hasTicket;

        for(TargetAndTicket location:possibleLocations)
        {
            // Variable will be set to true if a detective is at the target location.
            detectivePresent = false;

            for(Colour playerInfo : playerList)
            {
                if ((location.getTarget() == playerLocation && !(playerInfo.equals(Colour.Black))) || detectiveLocations.contains(location.getTarget()))
                {
                    detectivePresent = true;
                }
            }

            // Variable will be set to false if the player cannot access target due to lack of tickets.
            hasTicket = true;


            if(!(view.getPlayerTickets(player, location.getTicket()) >0))
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
                singleMove = MoveTicket.instance(player, location.getTicket(), location.getTarget());
                moveList.add(singleMove);
                locationsInReach.add(location.getTarget());

                // If the player is Mr. X and he has double move tickets the following occurs:
                if(player.equals(Colour.Black) && !(view.getPlayerTickets(player, Ticket.Double) == 0))
                {
                    List<Move> secondMoveList = validMovesHelper(singleMove);
                    for (Move secondMove : secondMoveList) {
                        MoveTicket tempMove2 = (MoveTicket) secondMove;
                        doubleMove = MoveDouble.instance(player, singleMove, tempMove2);
                        moveList.add(doubleMove);
                        locationsInReach.add(((MoveTicket) secondMove).target);
                    }

                }

                //If Mr X has secret moves left, this adds them to the list of valid moves
                if(player.equals(Colour.Black) && (view.getPlayerTickets(player, Ticket.Secret) > 0) )
                {

                    singleMove = MoveTicket.instance(player, Ticket.Secret, location.getTarget());
                    moveList.add(singleMove);
                    if(!(view.getPlayerTickets(player, Ticket.Double) == 0))
                    {
                        List<Move> secondMoveList = validMovesHelper(singleMove);
                        for (Move secondMove : secondMoveList)
                        {
                            MoveTicket tempMove2 = (MoveTicket) secondMove;
                            doubleMove = MoveDouble.instance(player, singleMove, tempMove2);
                            moveList.add(doubleMove);
                        }

                    }
                }
            }
        }

        // If the player is a detective and has no valid moves, a single Pass Move is added to the Move List
        if((moveList.size() == 0) && !(player.equals(Colour.Black)))
        {
            Move move = MovePass.instance(player);
            moveList.add(move);
        }

        allLocationsInReach = locationsInReach;
        return moveList;
    }


    public List<Move> validMovesHelper (MoveTicket move)
    {
        Colour player = view.getCurrentPlayer();
        List<TargetAndTicket> possibleLocations;
        List<Move> secondMoveList = new ArrayList<Move>();
        MoveTicket singleMove;
        possibleLocations = adjacencyMatrix.get(move.target);
        Boolean detectivePresent;
        Boolean hasTicket;
        List<Colour> playerList = view.getPlayers();


        for(TargetAndTicket location:possibleLocations)
        {
            // Variable will be set to true if a detective is at the target location.
            detectivePresent = false;

            for(Colour playerInfo : playerList)
            {
                if ((location.getTarget() == view.getPlayerLocation(playerInfo)) && !(playerInfo.equals(Colour.Black)) || detectiveLocations.contains(location.getTarget()))
                {
                    detectivePresent = true;
                }
            }

            // Variable will be set to false if the player cannot access target due to lack of tickets.
            hasTicket = true;

            if((view.getPlayerTickets(player,location.getTicket()) <= 1) && (location.getTicket().equals(move.ticket)))
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
                singleMove = MoveTicket.instance(move.colour, location.getTicket(), location.getTarget());
                secondMoveList.add(singleMove);

                // If Mr. X has secret move tickets left, add a corresponding secret move to the list of second moves.
                if(!(view.getPlayerTickets(player, Ticket.Secret) <= 1 && location.getTicket().equals(Ticket.Secret)))
                {
                    singleMove = MoveTicket.instance(move.colour, Ticket.Secret, location.getTarget());
                    secondMoveList.add(singleMove);
                }
            }
        }

        return secondMoveList;
    }



    // Produces a list of valid moves to only to the specified location.
    protected List<Move> validMovesFilter(Colour player, Integer playerLocation, Integer targetLocation)
    {
        List<Move> moveList = new ArrayList<Move>();
        List<TargetAndTicket> possibleLocations;
        MoveTicket singleMove;
        MoveDouble doubleMove;
        List<Colour> playerList = view.getPlayers();

        // Finds the adjacency matrix associated with player.
        possibleLocations = adjacencyMatrix.get(playerLocation);

        Boolean detectivePresent;
        Boolean hasTicket;

        for(TargetAndTicket location:possibleLocations)
        {
            // Variable will be set to true if a detective is at the target location.
            detectivePresent = false;

            for(Colour playerInfo : playerList)
            {
                if (location.getTarget() == playerLocation && !(playerInfo.equals(Colour.Black)))
                {
                    detectivePresent = true;
                }
            }

            // Variable will be set to false if the player cannot access target due to lack of tickets.
            hasTicket = true;


            if(!(view.getPlayerTickets(player, location.getTicket()) >0))
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
                singleMove = MoveTicket.instance(player, location.getTicket(), location.getTarget());
                if(location.getTarget().equals(targetLocation))
                {
                    moveList.add(singleMove);
                }
                // If the player is Mr. X and he has double move tickets the following occurs:
                if(player.equals(Colour.Black) && !(view.getPlayerTickets(player, Ticket.Double) == 0))
                {
                    List<Move> secondMoveList = validMovesFilterHelper(singleMove, targetLocation);
                    if(!secondMoveList.isEmpty())
                    {
                        for (Move secondMove : secondMoveList) {
                            MoveTicket tempMove2 = (MoveTicket) secondMove;
                            doubleMove = MoveDouble.instance(player, singleMove, tempMove2);
                            moveList.add(doubleMove);
                        }
                    }

                }

                //If Mr X has secret moves left, this adds them to the list of valid moves
                if(player.equals(Colour.Black) && (view.getPlayerTickets(player, Ticket.Secret) > 0) )
                {

                    singleMove = MoveTicket.instance(player, Ticket.Secret, location.getTarget());
                    if(location.getTarget() == targetLocation)
                    {
                        moveList.add(singleMove);
                    }
                    if(!(view.getPlayerTickets(player, Ticket.Double) == 0))
                    {
                        List<Move> secondMoveList = validMovesFilterHelper(singleMove, targetLocation);
                        if(!secondMoveList.isEmpty())
                        {
                            for (Move secondMove : secondMoveList) {
                                MoveTicket tempMove2 = (MoveTicket) secondMove;
                                doubleMove = MoveDouble.instance(player, singleMove, tempMove2);
                                moveList.add(doubleMove);
                            }
                        }
                    }
                }
            }
        }

        // If the player is a detective and has no valid moves, a single Pass Move is added to the Move List
        if((moveList.size() == 0) && !(player.equals(Colour.Black)))
        {
            Move move = MovePass.instance(player);
            moveList.add(move);
        }

        return moveList;
    }


    public List<Move> validMovesFilterHelper (MoveTicket move, Integer targetLocation)
    {
        Colour player = view.getCurrentPlayer();
        List<TargetAndTicket> possibleLocations;
        List<Move> secondMoveList = new ArrayList<Move>();
        MoveTicket singleMove;
        possibleLocations = adjacencyMatrix.get(move.target);
        Boolean detectivePresent;
        Boolean hasTicket;
        List<Colour> playerList = view.getPlayers();



        for(TargetAndTicket location:possibleLocations)
        {
            // Variable will be set to true if a detective is at the target location.
            detectivePresent = false;

            for(Colour playerInfo : playerList)
            {
                if ((location.getTarget() == view.getPlayerLocation(playerInfo)) && !(playerInfo.equals(Colour.Black)))
                {
                    detectivePresent = true;
                }
            }

            // Variable will be set to false if the player cannot access target due to lack of tickets.
            hasTicket = true;

            if((view.getPlayerTickets(player,location.getTicket()) <= 1) && (location.getTicket().equals(move.ticket)))
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
                singleMove = MoveTicket.instance(move.colour, location.getTicket(), location.getTarget());
                if(location.getTarget().equals(targetLocation))
                {
                    secondMoveList.add(singleMove);
                }

                // If Mr. X has secret move tickets left, add a corresponding secret move to the list of second moves.
                if(!(view.getPlayerTickets(player, Ticket.Secret) <= 1 && location.getTicket().equals(Ticket.Secret)))
                {
                    singleMove = MoveTicket.instance(move.colour, Ticket.Secret, location.getTarget());
                    if(location.getTarget() == targetLocation)
                    {
                        secondMoveList.add(singleMove);
                    }
                }
            }
        }

        return secondMoveList;
    }

    // creates an adjacency tree of nodes that are connected.
    public Map<Integer, List<TargetAndTicket>> findNeighbours()
    {
        Set<Edge> allEdges = graph.getEdges();
        Set<Node> allNodes = graph.getNodes();
        Map<Integer, List<TargetAndTicket>> adjacencyMatrix = new HashMap<Integer, List<TargetAndTicket>>();
        for(Node n: allNodes)
        {
            List<TargetAndTicket> tempList = new ArrayList<TargetAndTicket>();

            for(Edge e: allEdges)
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


    // Procudes a list of detective locations assuming they all take one step closer to Mr X.
    public List<Integer> optimalDetec(Integer mrXlocation, List<Integer> detectiveLocations )
    {
        List<Integer> newDetectiveLocations = new ArrayList<Integer>();
        Dijkstra dijkstra = new Dijkstra(graph, mrXlocation);
        Integer next = 0;
        for(int i = 0; i < detectiveLocations.size(); i++) {
            next = dijkstra.getPath(detectiveLocations.get(i));
            newDetectiveLocations.add(next);
        }

        return newDetectiveLocations;
    }


    // Resets the time flag to True.
    public void resetTimeFlag() {
        timeFlag = true;
        return;
    }



}
