public class Node extends NodeProperties
{
    private int node_number;
    private int[] outgoing_connections;
    private int[] outgoing_weights;

    // When visited nodes = node_count we finish.
    public static int node_count = 0;

    public Node(int node_number, int[] outgoing_connections, int[] outgoing_weights)
    {
        this.node_number = node_number;
        this.outgoing_connections = outgoing_connections;
        this.outgoing_weights = outgoing_weights;
        node_count++;
    }

    public void insert()
    {

    }
}