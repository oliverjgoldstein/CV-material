import java.util.Set;
import java.util.HashSet;



/**
 * Class that represents a graph. This class is based around a
 * List of nodes and a List of edges. The nodes are very simple 
 * classes that only contain the name of the node. The edges are 
 * more important as they define the structure of the graph. 
 */
public class Graph
{
    private Set<Node> nodes;
    private Set<Edge> edges;

    /**
     * Graph constructor. This initialises the lists that 
     * will hold the nodes and edges
     */
    public Graph()
    {
        nodes = new HashSet<Node>();
        edges = new HashSet<Edge>();
    }

    /**
     * Function that returns the list of nodes from the graph
     * @return The list of nodes
     */
    public Set<Node> nodes()
    {
        return nodes;
    }
    
    /**
     * Function that returns the list of edges from the graph
     * @return The list of edges
     */
	public Set<Edge> edges() 
	{
		return edges;
	}

	/**
	 * Function to find a node in the graph given the nodes name. 
	 * This function will search through the list of nodes and check
	 * each of their names. If it finds a matching node, it will be 
	 * returned. If not, it will return null.
	 * @param name The name of the node that you wish to find
	 * @return The found node or null
	 */
    public Node find(int names)
    {
        String name = Integer.toString(names);
        for (Node n : nodes)
        {
            if (n.name().equals(name)) return n;
        }
        return null;
    }
    
    /**
     * Finds a node given an id. The id represents the position of the
     * node in the list of nodes
     * If the id is out of bounds, null is returned
     * @param index The index of the node
     * @return The desired node or null
    
    public Node find(int index)
    {
    	if(index < 0 || index >= nodeNumber())
    	{
    		return null;
    	}
    	return nodes.get(index);
    }
    */
    
    /**
     * Returns the number of nodes in the graph
     * @return The number of nodes in the graph
     */
    public int nodeNumber()
    {
    	return nodes.size();
    }
    
    
    /**
     * Function to add a new node to the graph
     * @param node The node you wish to add
     */
    public void add(Node node)
    {
        nodes.add(node);
    }
    
    /**
     * Function to add a new edge to the graph
     * @param edge The edge you wish to add
     */
    public void add(Edge edge)
    {
    	edges.add(edge);
    }

    public Graph span()
    {
        Graph spanGraph = new Graph(); // creates a new graph for the MST 
        Set<Node> graphNodes = new HashSet<Node>();

        graphNodes.addAll(nodes()); //adds all of the nodes from the graph to a set

        spanGraph.add(nodes().iterator().next()); // Selects a starting point for the MST

        while (graphNodes.size() >= 1) // while all of the nodes have not been added to the MST
        {
            Edge min = null;

            for (Edge anEdge : edges())
            {
                Node nodeid1 = new Node(anEdge.id1()); //Stores the first node connected to the edge
                Node nodeid2 = new Node(anEdge.id2()); //Stores the second node connected to the edge

                if(spanGraph.edges().contains(anEdge) == false)
                {
                    if(spanGraph.nodes().contains(nodeid1) || spanGraph.nodes().contains(nodeid2))
                    {
                        if((spanGraph.nodes().contains(nodeid1) && spanGraph.nodes().contains(nodeid2)) == false)
                        {
                            if (min == null || anEdge.weight() < min.weight())
                            {
                                min = anEdge;
                            }
                        }
                    }
                }
            }

           
            spanGraph.add(min);

            spanGraph.add(new Node(min.id1()));
            spanGraph.add(new Node(min.id2()));

            graphNodes.remove(new Node(min.id1()));
            graphNodes.remove(new Node(min.id2()));

        }

        return spanGraph;
    }
}

    

