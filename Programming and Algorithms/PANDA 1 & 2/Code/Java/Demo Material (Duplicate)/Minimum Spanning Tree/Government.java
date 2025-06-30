import java.math.BigDecimal;

public class Government
{
    private Graph graph;
    private Graph governmentGraph;

    public Government(Graph graph)
    {
        this.graph = graph;
        governmentGraph = new Graph();
    }

    private double road_cost(BigDecimal roadLength, BigDecimal ratePerKm, BigDecimal fixedCost)
    {
        BigDecimal cost = new BigDecimal(0);
        ratePerKm = ratePerKm.multiply(roadLength);
        cost = cost.add(fixedCost);
        cost = cost.add(ratePerKm);
        double total = cost.doubleValue(); 
        return total;
    }

    public Graph calculate_government_graph()
    {
        BigDecimal lrRatePerKm = new BigDecimal(4500);
        BigDecimal lrFixedCost = new BigDecimal(5000);
        BigDecimal mrRatePerKm = new BigDecimal(4000);
        BigDecimal uRatePerKm = new BigDecimal(1000);
        BigDecimal zeroCost = new BigDecimal(0);

        for(Node node: graph.nodes())
        {
            governmentGraph.add(node);
        }
        for(Edge edge: graph.edges())
        {
            double newWeight = 0;
            BigDecimal edgeWeight = new BigDecimal(edge.weight());

            if(edge.type().equals(Edge.EdgeType.LocalRoad))
            {
                newWeight += road_cost(edgeWeight, lrRatePerKm, lrFixedCost);
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                governmentGraph.add(newEdge);
            }
            if(edge.type().equals(Edge.EdgeType.MainRoad))
            {
                newWeight += road_cost(edgeWeight, mrRatePerKm, zeroCost);
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                governmentGraph.add(newEdge);
            }
            if(edge.type().equals(Edge.EdgeType.Underground))
            {
                newWeight += road_cost(edgeWeight, uRatePerKm, zeroCost);
                Edge newEdge = new Edge(edge.id1(), edge.id2(), newWeight, edge.type());
                governmentGraph.add(newEdge);
            }
        }
        return governmentGraph;
    }

}