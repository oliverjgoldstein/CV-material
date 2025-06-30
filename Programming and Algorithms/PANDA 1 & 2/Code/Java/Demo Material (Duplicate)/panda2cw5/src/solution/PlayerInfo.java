package solution;

import scotlandyard.*;
import java.util.*;

public class PlayerInfo implements Player
{
    private static final Integer TAXI_KEY = 0;
    private static final Integer BUS_KEY = 1;
    private static final Integer UNDERGROUND_KEY = 2;
    private static final Integer SECRET_MOVE_KEY = 3;
    private static final Integer DOUBLE_MOVE_KEY = 4;
    private static final int PASS_MOVE_KEY = 5;
    private int location;
    private final Colour colour;
    private final Player player;
    private Map<Ticket, Integer> tickets;
    private Move justPlayed;
    private List<Integer> visitedLocations = new ArrayList<Integer>();
    private List<Integer> ticketList = new ArrayList<Integer>();
    private List<Move> movesMade = new ArrayList<Move>();

    // Initialises variables in the constructor.
    public PlayerInfo()
    {
        player = null;
        colour = null;
        location = 0;
        tickets = null;
    }


    // Initialises variables in the constructor.
    public PlayerInfo(Player player, Colour colour, int location, Map<Ticket, Integer> tickets) {
        this.player = player;
        this.colour = colour;
        this.location = location;
        this.tickets = tickets;
        visitedLocations.add(location);
    }


    @Override
    public Move notify(int locations, List<Move> list)
    {
        if(justPlayed != null)
        {
            return justPlayed;
        }
        return player.notify(locations, list);
    }

    // Returns the player's location.
    public int getLocation() {
        return location;
    }

    // Sets the moves that was just played.
    public void setMove(Move justPlayed)
    {
        this.justPlayed = justPlayed;
    }

    // Returns the player's colour.
    public Colour getColour() {
        return colour;
    }

    // Returns the player.
    public Player getPlayer()
    {
        return player;
    }

    // Returns the player's ticket map.
    public Map<Ticket, Integer> getTickets() {
        return tickets;
    }

    // Sets the player's location.
    public void setLocation(int location) {
        this.location = location;
    }

    // Adds a location to the list of the locations visited by the player.
    public void addLocation(int locations)
    {
        visitedLocations.add(locations);
    }


    // Creates a list of all moves made by a player based on their used tickets and visited locations.
    public void generateMoves()
    {

        Integer i = 1; // Index in visited locations list.
        Integer y = 0; // Index in used tickets list.

        // Checks to see if the player has made any moves.
        if (!(ticketList.isEmpty())) {

            // Goes through the list of used tickets and recreates the moves one by one using the visited location as the targets.
            do {
                if (ticketList.get(y) == TAXI_KEY) {
                    Move move = new MoveTicket(colour, visitedLocations.get(i), Ticket.Taxi);
                    y++;
                    i++;
                    movesMade.add(move);

                } else if (ticketList.get(y) == BUS_KEY) {
                    Move move = new MoveTicket(colour, visitedLocations.get(i), Ticket.Bus);
                    y++;
                    i++;
                    movesMade.add(move);

                } else if (ticketList.get(y) == UNDERGROUND_KEY) {
                    Move move = new MoveTicket(colour, visitedLocations.get(i), Ticket.Underground);
                    y++;
                    i++;
                    movesMade.add(move);

                } else if (ticketList.get(y) == SECRET_MOVE_KEY) {
                    Move move = new MoveTicket(colour, visitedLocations.get(i), Ticket.SecretMove);
                    y++;
                    i++;
                    movesMade.add(move);

                } else if (ticketList.get(y) == DOUBLE_MOVE_KEY) {

                    Move move1 = null;
                    Move move2 = null;

                    // Recreates the first move of the double move.
                    if (ticketList.get(y + 1) == 0)
                        move1 = new MoveTicket(colour, visitedLocations.get(i), Ticket.Taxi);

                    else if (ticketList.get(y + 1) == 1)
                        move1 = new MoveTicket(colour, visitedLocations.get(i), Ticket.Bus);

                    else if (ticketList.get(y + 1) == 2)
                        move1 = new MoveTicket(colour, visitedLocations.get(i), Ticket.Underground);

                    else if (ticketList.get(y + 1) == 3)
                        move1 = new MoveTicket(colour, visitedLocations.get(i), Ticket.SecretMove);


                    // Recreates the second move of the double move.
                    if (ticketList.get(y + 2) == 0)
                        move2 = new MoveTicket(colour, visitedLocations.get(i + 1), Ticket.Taxi);
                    else if (ticketList.get(y + 2) == 1)
                        move2 = new MoveTicket(colour, visitedLocations.get(i + 1), Ticket.Bus);
                    else if (ticketList.get(y + 2) == 2)
                        move2 = new MoveTicket(colour, visitedLocations.get(i + 1), Ticket.Underground);
                    else if (ticketList.get(y + 2) == 3)
                        move2 = new MoveTicket(colour, visitedLocations.get(i + 1), Ticket.SecretMove);

                    //Recreates the double move.
                    Move doubleMove = new MoveDouble(colour, move1, move2);
                    y += 3;
                    i += 2;
                    movesMade.add(doubleMove);

                } else if (ticketList.get(y) == PASS_MOVE_KEY) {
                    Move move = new MovePass(colour);
                    y++;
                    movesMade.add(move);
                }

            } while (y < ticketList.size()) ; // Loops until all moves made have be recreated.
        }

    }


    // Adds a ticket to the player's list of used tickets.
    public void addTicket(Move move)
    {
        if(move instanceof MoveDouble){
            ticketList.add(DOUBLE_MOVE_KEY);
        }

        else if (move instanceof MovePass){
            ticketList.add(PASS_MOVE_KEY);
        }
    }


    // Adds a ticket to the player's list of used tickets.
    public void addTicket(Ticket ticket)
    {
        if(ticket.equals(Ticket.Taxi)) {
            ticketList.add(TAXI_KEY);
        }

        else if(ticket.equals(Ticket.Bus)){
            ticketList.add(BUS_KEY);
        }

        else if(ticket.equals(Ticket.Underground)){
            ticketList.add(UNDERGROUND_KEY);
        }

        else if(ticket.equals(Ticket.SecretMove)){
            ticketList.add(SECRET_MOVE_KEY);
        }
    }

    // Returns the list of locations visited by the player.
    public List<Integer> getVisitedLocations()
    {
        return visitedLocations;
    }

    // Returns the player's ticket list.
    public List<Integer> getTicketList()
    {
        return ticketList;
    }

    // Sets the player's list of visited locations.
    public void setVisitedLocations(List<Integer> visitedLocations) {
        this.visitedLocations = visitedLocations;
    }

    // Sets the player's ticket list.
    public void setTicketList(List<Integer> ticketList) {
        this.ticketList = ticketList;
    }

    // Sets the player's ticket map.
    public void setTicketMap(Map<Ticket, Integer> map)
    {
        tickets.clear();
        tickets = map;
    }

    // Returns the list of all moves the player has made.
    public List<Move> getMoves()
    {
        return movesMade;
    }
}

