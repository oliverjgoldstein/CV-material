public class Node extends NodeProperties
{
    int node_number;
    int[] outgoing_connections= new int[100];
    int[] outgoing_weights= new int[100];
    int shortest_node_number;
    int largest_comparison_value = 50000;
    int outgoing_array_length;

    // When visited nodes = node_count we finish.
    public static int node_count = 0;

    public Node(int node_number, int[] outgoing_connections, int[] outgoing_weights)
    {
        this.node_number = node_number;
        this.outgoing_connections = outgoing_connections;
        this.outgoing_weights = outgoing_weights;
        node_count++;
    }
    public void find_outgoing_array_length()
    {
        int index = 0;
        while(outgoing_connections[index] != 0)
        {
            index++;
        }
        outgoing_array_length = index;
    }
    public void find_shortest_route()
    {
        for(int index = 0;index < outgoing_array_length;index++)
        {
            if(outgoing_weights[index] < largest_comparison_value)
            {
                largest_comparison_value = outgoing_weights[index];
                shortest_node_number = index;
            }
        }
        System.out.println("Shortest Node Array Index: "+shortest_node_number);
    }
}