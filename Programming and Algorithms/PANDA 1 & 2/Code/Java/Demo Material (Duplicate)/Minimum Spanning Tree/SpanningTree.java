import java.math.BigDecimal;
import java.math.RoundingMode;

// Change of code styling convention.
public class SpanningTree
{
    private static boolean debugFlag = true;
    public static Graph initialisation(String filename)
    {
        Reader reader = new Reader();
        try
        {
            reader.read(filename);
        }
        catch(Exception e)
        {
            System.err.println("IOException Thrown.");
            System.exit(1);
        }
        return reader.graph();
    }

    public static void main(String[] args)
    {
        if(args.length != 2)
        {
            System.err.println("Proper Usage is: java program filename.");
            System.exit(1);
        }

        Graph newGraph = initialisation(args[1]);
        Graph secondNewGraph = initialisation(args[1]);
        Graph thirdNewGraph = initialisation(args[1]);

        if(args[0].equals("-p1"))
        {
            BigDecimal total = edge_weights(newGraph);
            BigDecimal thousand = new BigDecimal(1000);
            total = total.multiply(thousand);
            total = total.setScale(2, RoundingMode.HALF_DOWN);
            System.out.println("Total Cable Needed: "+total+"m");
        }

        if(args[0].equals("-p2"))
        {
            Government government = new Government(newGraph);
            Graph governmentGraph = government.calculate_government_graph();
            BigDecimal cost = edge_weights(governmentGraph);
            cost = cost.setScale(2, RoundingMode.HALF_DOWN);
            System.out.println("Price: "+cost);

            Commuter commuter = new Commuter(newGraph);
            Graph commuterGraph = commuter.calculate_commuter_graph();
            BigDecimal disruption = edge_weights(commuterGraph);
            disruption = disruption.setScale(2, RoundingMode.HALF_DOWN);
            System.out.println("Hours of Disrupted Travel: "+disruption+"h");

            ComputerScientist computerScientist = new ComputerScientist(newGraph);
            Graph computerScientistGraph = computerScientist.calculate_computer_scientist_graph();
            BigDecimal timeTaken = edge_weights(computerScientistGraph);
            String date = computerScientist.get_date(timeTaken);
            System.out.println("Completion Date: "+date);
        }

        if(args[0].equals("-p3"))
        {
            Government government = new Government(thirdNewGraph);
            Graph governmentGraph = government.calculate_government_graph().span();

            BigDecimal cost = edge_weights(governmentGraph);
            cost = cost.setScale(2, RoundingMode.HALF_DOWN);
            System.out.println("Price: "+cost);

             Commuter commuter = new Commuter(secondNewGraph);
            Graph commuterGraph = commuter.calculate_commuter_graph().span();

            BigDecimal disruption = edge_weights(commuterGraph);
            disruption = disruption.setScale(2, RoundingMode.HALF_DOWN);
            System.out.println("Hours of Disrupted Travel: "+disruption+"h");

            ComputerScientist computerScientist = new ComputerScientist(newGraph);
            Graph computerScientistGraph = computerScientist.calculate_computer_scientist_graph().span();

            BigDecimal timeTaken = edge_weights(computerScientistGraph);
            String date = computerScientist.get_date(timeTaken);
            System.out.println("Completion Date: "+date);


        }
    }

    public static BigDecimal edge_weights(Graph graph)
    {
        BigDecimal accumulator = new BigDecimal(0);
        BigDecimal convertToBigDecimal = new BigDecimal(0);
        for(Edge edge:graph.edges())
        {
            convertToBigDecimal = convertToBigDecimal.valueOf(edge.weight());
            accumulator = accumulator.add(convertToBigDecimal);
        }
        return accumulator;
    }
}