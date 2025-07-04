import judge.JudgedScotlandYard;
import judge.ScotlandYardJudge;
import messages.TcpMessenger;
import scotlandyard.Colour;
import scotlandyard.Ticket;

import java.io.IOException;
import java.util.*;

/**
 * The JudgeService uses a judge that decides which
 * game to play. It judges the moves that are given to it
 * to make sure that nobody is cheating.
 * The judge service is started with the following command,
 * which takes the host, the port, and a game number,
 * that identifies the game:
 * <pre>
 *   java JudgeService localhost 8123 1337
 * </pre>
 * This code does not need to be modified.
 */
public class JudgeService {
    public static void main(String[] args) {

        String host   = args[0];
        int    port   = Integer.parseInt(args[1]);
        int    gameId = Integer.parseInt(args[2]);

        List<Boolean> rounds = Arrays.asList(
                false,
                false, false,
                true,
                false, false, false, false,
                true,
                false, false, false, false,
                true,
                false, false, false, false,
                true,
                false, false, false, false, false,
                true);

        JudgedScotlandYard game = new JudgedScotlandYard(4, rounds, "resources/graph.txt");


        Map<Colour, Integer> locations = new HashMap<Colour, Integer>();

        List<Integer> mrXStart = Arrays.asList(35, 45, 51, 71, 78, 104, 106, 132, 127, 146, 166, 170, 172);
        Collections.shuffle(mrXStart);
        List<Integer> detStart = Arrays.asList(26, 34, 50, 53, 91, 94, 103, 112, 117, 13, 123, 138, 141, 155, 174, 29);
        Collections.shuffle(detStart);


        locations.put(Colour.Black, mrXStart.get(0));

        locations.put(Colour.Red,    detStart.get(0));
        locations.put(Colour.Blue,   detStart.get(1));
        locations.put(Colour.Green,  detStart.get(2));
        locations.put(Colour.White,  detStart.get(3));
        locations.put(Colour.Yellow, detStart.get(4));

        Map<Colour, Map<Ticket, Integer>> tickets = new HashMap<Colour, Map<Ticket, Integer>>();
        tickets.put(Colour.Red,    getTickets(false));
        tickets.put(Colour.Blue,   getTickets(false));
        tickets.put(Colour.Green,  getTickets(false));
        tickets.put(Colour.Yellow, getTickets(false));
        tickets.put(Colour.White,  getTickets(false));
        tickets.put(Colour.Black,  getTickets(true));

        // A messenger allows messages to be communicated over
        // a transportation medium. In this case TCP.
        TcpMessenger messenger = null;
        try {
            messenger = new TcpMessenger(host, port);
        }
        catch (IOException e) {
            e.printStackTrace();
        }


        // This sets up a new judge with the given gameId, and that
        // will wait 15000 milliseconds between each move.
        ScotlandYardJudge judge = new ScotlandYardJudge(gameId, 15000, messenger,
                game, "resources/graph.txt", locations, tickets);
        judge.initialiseGame();
        judge.interpretMessages();
    }

    public final static int[] mrXTicketNumbers = {4, 3, 3, 2, 5};
    public final static int[] detectiveTicketNumbers = {8, 11, 4, 0, 0};

    public static Map<Ticket, Integer> getTickets(boolean mrX) {
        Map<Ticket, Integer> tickets = new HashMap<Ticket, Integer>();
        int count = 0;
        for (Ticket ticket : Ticket.values()) {
            if (mrX)
                tickets.put(ticket, mrXTicketNumbers[count]);
            else
                tickets.put(ticket, detectiveTicketNumbers[count]);

            count++;
        }
        return tickets;
    }
}
