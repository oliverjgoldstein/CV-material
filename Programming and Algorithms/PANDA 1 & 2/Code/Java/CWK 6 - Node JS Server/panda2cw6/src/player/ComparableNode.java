package player;

public class ComparableNode implements Comparable<ComparableNode>
{
    // These are the connections to the nodes (neighbours).
    protected ComparableEdge[] connections;
    // This is the value of the node.
    protected Integer nodeValue;

    // This is the preceeding node that got there using dijkstra's.
    protected ComparableNode preceedingNode;

    // This is the constructor.
    protected ComparableNode(Integer argValue) { nodeValue = argValue; }

    // The minimum distance
    protected double minimumDistance = Double.POSITIVE_INFINITY;

    // This is just for queues to compare them.
    public int compareTo(ComparableNode other)
    {
        return Double.compare(minimumDistance, other.minimumDistance);
    }

}