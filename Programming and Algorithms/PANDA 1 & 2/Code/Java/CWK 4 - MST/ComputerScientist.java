import java.math.BigDecimal;
import java.util.Calendar;
import java.text.Format;
import java.text.SimpleDateFormat;


public class ComputerScientist
{
    private double kmOfLayingLrPerDay = 0.2;
    private double kmOfLayingMrPerDay = 0.6;
    private double kmOfLayingUPerDay = 0.9;

    private Graph graph;
    private Graph computerScientistGraph;

    public ComputerScientist(Graph graph)
    {
        this.graph = graph;
        computerScientistGraph = new Graph();
    }

    public Graph calculate_computer_scientist_graph()
    {
        for(Node node: graph.nodes())
        {
            computerScientistGraph.add(node);
        }
        for(Edge edge: graph.edges())
        {
            double newWeight = 0;
            if(edge.type().equals(Edge.EdgeType.LocalRoad))
            {
                newWeight += edge.weight()/kmOfLayingLrPerDay;
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                computerScientistGraph.add(newEdge);
            }
            if(edge.type().equals(Edge.EdgeType.MainRoad))
            {
                newWeight  += edge.weight()/kmOfLayingMrPerDay;
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                computerScientistGraph.add(newEdge);
            }
            if(edge.type().equals(Edge.EdgeType.Underground))
            {
                newWeight += edge.weight()/kmOfLayingUPerDay;
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                computerScientistGraph.add(newEdge);
            }
        }
        return computerScientistGraph;
    }

    public String get_date(BigDecimal timeTaken)
    {
        double numberOfDays = timeTaken.doubleValue();
        int hourNumber = (int)(numberOfDays*24);
        numberOfDays = (numberOfDays*24)-hourNumber;
        int hourAdd = (int)(numberOfDays);
        numberOfDays = (numberOfDays*60)-hourAdd;
        int minuteNumber = (int)(numberOfDays);
        Format formatter = new SimpleDateFormat("EEE d MMMM YYYY HH:mm");

        Calendar cal = Calendar.getInstance();
        cal.set(2014, 1, 15);
        cal.set(Calendar.HOUR_OF_DAY,00);
        cal.set(Calendar.MINUTE,00);
        cal.set(Calendar.SECOND,00);
        cal.set(Calendar.MILLISECOND,00);
        cal.add(Calendar.HOUR_OF_DAY, hourNumber);  
        cal.add(Calendar.HOUR_OF_DAY, hourAdd);  
        cal.add(Calendar.MINUTE, minuteNumber);
        String fullDate = formatter.format(cal.getTime());
        
        return fullDate;

    }
}