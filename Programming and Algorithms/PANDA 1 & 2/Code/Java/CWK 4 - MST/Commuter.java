import java.math.BigDecimal;

public class Commuter
{
    private double lrDisruptionPerKm = 0.2;
    private double mrDisruptionPerKm = 0.5;
    private double uDisruptionPerKm = 1.0;

    private Graph graph;
    private Graph commuterGraph;

    public Commuter(Graph graph)
    {
        this.graph = graph;
        commuterGraph = new Graph();
    }

    public Graph calculate_commuter_graph()
    {
        for(Node node: graph.nodes())
        {
            commuterGraph.add(node);
        }
        for(Edge edge: graph.edges())
        {
            double newWeight = 0;
            if(edge.type().equals(Edge.EdgeType.LocalRoad))
            {
                newWeight += edge.weight()*lrDisruptionPerKm;
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                commuterGraph.add(newEdge);
            }
            if(edge.type().equals(Edge.EdgeType.MainRoad))
            {
                newWeight  += edge.weight()*mrDisruptionPerKm;
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                commuterGraph.add(newEdge);
            }
            if(edge.type().equals(Edge.EdgeType.Underground))
            {
                newWeight += edge.weight();
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                commuterGraph.add(newEdge);
            }
        }
        return commuterGraph;
    }
}