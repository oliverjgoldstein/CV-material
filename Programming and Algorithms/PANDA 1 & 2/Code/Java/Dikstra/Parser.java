public class Parser
{
    private final String comma_delimiter = ",";
    private final String space_delimiter = " ";
    private String[] node_information;
    public Node[] node_list;
    private String[] test_length;

    public Parser(String node_information)
    {
        test_length = node_information.split(comma_delimiter);
        node_list = new Node[test_length.length];
    }
    public void parse(String node_information)
    {
        this.node_information = node_information.split(comma_delimiter);

        // For each item in the main parameter list.
        for(int i = 0;i<this.node_information.length;i++)
        {
            int node_number = 0;
            String[] individual_argument = this.node_information[i].split(space_delimiter);
            try
            {
                node_number = Integer.parseInt(individual_argument[0]);
            }
            catch (Exception e)
            {
                System.err.println("Please enter correct arguments.");
                System.exit(1);
            }
            int[] outgoing_node_connections = new int[5];
            int[] outgoing_node_weights = new int[5];
            int outgoing_node_array_count = 0;
            int outgoing_weight_array_count = 0;
            // For each item in the individual parameter list.
            for(int j = 1;j<individual_argument.length;j++)
            {
                if((j&1) != 0)
                {
                    outgoing_node_connections[outgoing_node_array_count] = Integer.parseInt(individual_argument[j]);
                    outgoing_node_array_count++;
                }
                else
                {
                    outgoing_node_weights[outgoing_weight_array_count] = Integer.parseInt(individual_argument[j]);
                    outgoing_weight_array_count++;
                }
            }

            // We now have the outgoing node, the connected nodes and their weights. Let's pass it to Node
            Node new_node = new Node(node_number,outgoing_node_connections,outgoing_node_weights);
            node_list[i] = new_node;
        }
    }

    public Node[] pass_to_main()
    {
        return node_list;
    }
}