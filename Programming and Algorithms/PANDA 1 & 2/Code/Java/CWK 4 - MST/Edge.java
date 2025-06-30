/**
 * Class that represents a graph edge. The edges 
 * define the structure of the graph as they constitute
 * the connections between nodes. 
 * 
 * In addition, they have both an edge type and an edge weight.
 * It is important that you understand this class.
 */
public class Edge implements Comparable<Edge> {

	/**
	 * Enum type which defines the different
	 * types of edge. (similar to the different types
	 * of transport in the game 'Scotland Yard')
	 */
	public enum EdgeType {
		LocalRoad, MainRoad, Underground;
	}
	
	
	private String id1;
	private String id2;
	private double weight;
	private EdgeType type;
	private int position;

	
	
	/**
	 * Returns the id of the first node that this edge connects
	 * @return The id of the first node
	 */
	public String id1()
	{
		return id1;
	}
	
	/**
	 * Returns the id of the second node that this edge connects
	 * @return The id of the second node
	 */
	public String id2()
	{
		return id2;
	}
		

	/**
	 * Function to get the weight of the edge
	 * @return The weight assigned to the edge
	 */
	public double weight()
	{
		return weight;
	}
	
	
	/**
	 * Function to get the type of the edge
	 * @return The type of the edge
	 */
	public EdgeType type()
	{
		return type;
	}
	
	public boolean contains_node(String id)
	{
		if(id1.equals(id) || id2.equals(id))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean edges_equal(String id1, String id2, double weight, EdgeType type)
	{
		if((this.id1.equals(id1)) && (this.id2.equals(id2)) && (this.weight == weight) && (this.type == type))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Class constructor. All of the information about the edge is set in 
	 * this function and cannot be changed afterwards
	 * @param id1 The id of the first node that this edge connects
	 * @param id2 The id of the second node that this edge connects
	 * @param weight The weight of the edge
	 * @param type The type of the edge
	 */
	public Edge(String id1, String id2, double weight, EdgeType type)
	{
		this.id1 = id1;
		this.id2 = id2;
		this.weight = weight;
		this.type = type;
	}

	public void set_position(int position)
	{
		this.position = position;
	}

	public int compareTo(Edge edge) {
        return 0 + edge.position;
    }
	
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.id1 + " " + this.id2 + " " + this.weight + " " + this.type;
	}

	@Override
	public boolean equals(Object o){
		Edge comp = (Edge) o;

		return comp.id1().equals(id1()) && comp.id2().equals(id2()) && comp.weight() == weight() && comp.type().equals(type());

	}
	
	
}
