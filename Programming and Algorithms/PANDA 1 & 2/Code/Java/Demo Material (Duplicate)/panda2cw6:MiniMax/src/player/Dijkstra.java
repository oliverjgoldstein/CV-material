package player;

import scotlandyard.Edge;
import scotlandyard.Graph;
import scotlandyard.Node;

import java.util.*;

public class Dijkstra {
    Graph graph = null;
    Integer mrXLocation = null;
// This is the constructor that takes the location of MrX from which it calculates everything ane the graph.
    public Dijkstra(Graph graph, Integer mrXLocation) {
        this.graph = graph;
        this.mrXLocation = mrXLocation;
    }

// This calculates the paths from the source to every other node within the graph.
    public void calculatePaths(ComparableNode source)
    {
        PriorityQueue<ComparableNode> nodeQueue = new PriorityQueue<ComparableNode>();
        source.minimumDistance = 0.0;
        nodeQueue.add(source);

        while (!nodeQueue.isEmpty()) {

            ComparableNode node = nodeQueue.poll();

            for (ComparableEdge edge : node.connections)
            {
                ComparableNode nodeToCompare = edge.target;
                double weight = edge.weight;
                double distanceThoroughNodes = node.minimumDistance + weight;

                if (distanceThoroughNodes < nodeToCompare.minimumDistance) {
                    nodeQueue.remove(nodeToCompare);

                    nodeToCompare.minimumDistance = distanceThoroughNodes ;
                    nodeToCompare.preceedingNode = node;

                    nodeQueue.add(nodeToCompare);
                }
            }
        }
    }

// This gets the neighbours of one of the nodes such that it choses the other value associated with the edge.
// This traverses through all graph edges.
    public Set<Node> getNeighbours(Node u) {

        Integer nodeValue = Integer.parseInt(u.toString());
        Set<Edge> edgesToNode = graph.getEdgesFrom(nodeValue);
        Set<Node> neighbours = new HashSet<Node>();

        for(Edge edge: edgesToNode) {

            Integer source = Integer.parseInt(edge.source().toString());
            Integer target = Integer.parseInt(edge.target().toString());

            if (source.equals(nodeValue)) {

                Node node = graph.getNode(edge.target());
                neighbours.add(node);

            }
            else if (target.equals(nodeValue)) {

                Node node = graph.getNode(edge.source());
                neighbours.add(node);

            }
        }
        return neighbours;
    }
// This finds the node associated with a given nodeValue.
    public ComparableNode findComparableNodeEquivalent(Integer nodeValue, List<ComparableNode> nodeList) {
        for(ComparableNode node:nodeList) {
            if(node.nodeValue.equals(nodeValue)) {
                return node;
            }
        }
        return null;
    }
// This returns the shortest path to a node.
    public List<ComparableNode> getShortestPathTo(ComparableNode target) {
        List<ComparableNode> path = new ArrayList<ComparableNode>();

        for (ComparableNode node = target; node != null; node = node.preceedingNode) {
            path.add(node);
        }

        return path;
    }


// This finds the next location to move to that is the shortest route to a given location.
    public Integer getPath(Integer detectiveLocation)
    {
        double distanceBetweenNodes = 0;
        Set<Node> nodeSet = graph.getNodes();

        List<ComparableNode> nodeList = new ArrayList<ComparableNode>(nodeSet.size());
        for(Node node:nodeSet) {
            Integer nodeValue = Integer.parseInt(node.toString());
            ComparableNode newNode = new ComparableNode(nodeValue);
            nodeList.add(newNode);
        }

        for(ComparableNode node:nodeList) {
            Set<Node> neighbourNodes = getNeighbours(graph.getNode(node.nodeValue));
            ComparableEdge[] edgeList = new ComparableEdge[neighbourNodes.size()];
            int index = 0;
            for (Node neighbour : neighbourNodes) {
                ComparableNode comparableNode = findComparableNodeEquivalent(Integer.parseInt(neighbour.toString()), nodeList);
                edgeList[index] = new ComparableEdge(comparableNode);
                index++;
            }
            node.connections = edgeList;
        }

        calculatePaths(findComparableNodeEquivalent(mrXLocation, nodeList));

        List<ComparableNode> path = new ArrayList<ComparableNode>();
        for (ComparableNode v : nodeList) {
            if (detectiveLocation.equals(v.nodeValue)) {
                distanceBetweenNodes += v.minimumDistance;
                path = getShortestPathTo(v);
            }
        }
        if (path.size() == 1)
        {
            Integer nextMove = path.get(0).nodeValue;
            return nextMove;
        }
        else{
            Integer nextMove = path.get(1).nodeValue;
            return nextMove;
        }
    }




// Traverses through the detectives and adds up all the distances in the list if resolution is false.
// It the resolution is true it returns the closest detective distance - used in the score method.
    public double findDistances(List<Integer> detectiveDistances, boolean resolution)
    {
        Set<Node> nodeSet = graph.getNodes();

        List<ComparableNode> nodeList = new ArrayList<ComparableNode>(nodeSet.size());
        for(Node node:nodeSet) {
            Integer nodeValue = Integer.parseInt(node.toString());
            ComparableNode newNode = new ComparableNode(nodeValue);
            nodeList.add(newNode);
        }

        for(ComparableNode node:nodeList) {
            Set<Node> neighbourNodes = getNeighbours(graph.getNode(node.nodeValue));
            ComparableEdge[] edgeList = new ComparableEdge[neighbourNodes.size()];
            int index = 0;
            for (Node neighbour : neighbourNodes) {
                ComparableNode comparableNode = findComparableNodeEquivalent(Integer.parseInt(neighbour.toString()), nodeList);
                edgeList[index] = new ComparableEdge(comparableNode);
                index++;
            }
            node.connections = edgeList;
        }

        calculatePaths(findComparableNodeEquivalent(mrXLocation, nodeList));

        double distanceBetweenNodes = 0;
        if(resolution == true) {
            distanceBetweenNodes = Integer.MAX_VALUE;
            for (ComparableNode v : nodeList) {
                if (detectiveDistances.contains(v.nodeValue)) {
                    if (distanceBetweenNodes > v.minimumDistance) {
                        distanceBetweenNodes = v.minimumDistance;
                    }
                }
            }
        }
        else {
            for (ComparableNode v : nodeList) {
                if (detectiveDistances.contains(v.nodeValue)) {
                    distanceBetweenNodes += v.minimumDistance;
                }
            }
        }
        return distanceBetweenNodes;
    }

}
