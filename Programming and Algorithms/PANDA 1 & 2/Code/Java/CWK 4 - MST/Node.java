/**
 * Class representing a node of the graph
 */
public class Node
{
    private String name;
    private double distanceFromTree;    
    private boolean visited;
    private String idToReach;

    /**
     * Node constructor
     * @param n Name that will be given to the node
     */
    Node(String n)
    {
        name = n;
        distanceFromTree = Double.POSITIVE_INFINITY;
        visited = false;
    }

    /**
     * Function that gets the name that has been assigned to the node
     * @return
     */
    String name()
    {
        return name;
    }
    
    public String toString() 
    {
    	return name;
    }

    public void set_distanceFromTree(double distanceFromTree)
    {
        this.distanceFromTree = distanceFromTree;
    }
    
    public double get_distanceFromTree()
    {
        return distanceFromTree;
    }

    public void set_visited(boolean visited)
    {
        this.visited = visited;
    }
    
    public boolean get_visited()
    {
        return visited;
    }

    public void set_idToReach(String id)
    {
        idToReach = id;
    }
    
    public String get_idToReach()
    {
        return idToReach;
    }

    @Override
    public boolean equals(Object o){
        Node comp = (Node) o;
        return comp.name().equals(name());

    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

}