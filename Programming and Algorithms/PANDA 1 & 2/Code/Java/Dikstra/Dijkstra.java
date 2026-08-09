public class Dijkstra
{
    public static Node[] node_list;

    public static void create_nodes(Node[] node_list, String source_node_string, String dest_node_string)
    {
        int source_node = Integer.parseInt(source_node_string);
        int dest_node = Integer.parseInt(dest_node_string);
        Graph graph = new Graph(node_list, source_node, dest_node);
        graph.prepare_graph();
    }

    // Arguments need to be inserted as java Dijkstra "1 2 3 4 5 ,1 2 3 4 5" 1 2
    public static void main(String[] args)
    {
        Parser node_information = new Parser(args[0]);
        node_information.parse(args[0]);
        node_list = node_information.pass_to_main();
        create_nodes(node_list, args[1], args[2]);
    }
}