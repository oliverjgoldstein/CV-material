public class Graph
{
    public Node[] node_list;
    int source_node;
    int dest_node;
    int[] shortest_route = new int[100]; // The shortest route of outgoing connections.
    private int largest_weight_comparison = 50000;

    public Graph(Node[] node_list, int source_node, int dest_node)
    {
        this.source_node = source_node;
        this.dest_node = dest_node;
        this.node_list = node_list;
    }
    public void prepare_graph()
    {
        for(Node node:node_list)
        {
            node.find_outgoing_array_length();
            node.find_shortest_route();
        }
    }

}