package player;

public class ComparableEdge
{
    // This is the weight for the edge of the graph. They are unweighted so assigned 1.
    protected double weight = 1.00;
    // This is the target of the edge.
    protected ComparableNode target;

    protected ComparableEdge(ComparableNode argTarget) {
        target = argTarget;
    }
}