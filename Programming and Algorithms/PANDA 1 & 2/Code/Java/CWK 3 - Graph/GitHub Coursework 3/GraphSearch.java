        // Node original_node = graph.find(1);
        // Set<Node> original_node_neighbours_set = original_node.neighbours();
        // int set_size = original_node_neighbours_set.size();

        // for(int counter_of_neighbours = 0;counter_of_neighbours<set_size;counter_of_neighbours++)
        // {
        //     for(int optimised_counter = counter_of_neighbours;optimised_counter<(set_size-1);optimised_counter++)
        //     {
        //         Node comparison_node = graph.find(optimised_counter);
        //         Set<Node> comparison_node_neighbours = comparison_node.neighbours();
        //         for (optimised_counter += 1;optimised_counter<(set_size-2);optimised_counter++)
        //         {
        //             if comparison_node_neighbours.get(op)
        //         }
        //     }
        // }
import java.util.*;
import java.io.*;
import java.util.ArrayList.*;
import java.lang.Object.*;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class GraphSearch
{
    private static Reader reader_instance;
    private static Graph graph_instance;
    private static Set<Node> node_list;

    public static void main(String[] args)
    {
        set_reader(args[1]);
        set_graph(reader_instance.graph());
        set_list(graph_instance.nodes());

        if(args[0].equals("-p1"))
        {
            print_graph();
        }
        if(args[0].equals("-p2"))
        {
            int n = Integer.parseInt(args[2]);
            Set<Node> neighbours_greater_than_n = neighbour_search(graph_instance,n);
            System.out.println("Number of nodes with at least "+n+" neighbours: "+neighbours_greater_than_n.size());
        }
        if(args[0].equals("-p3"))
        {
            findFullyConnectedNeighbours(graph_instance);
        }
        if(args[0].equals("-p4"))
        {
           clique(graph_instance, Integer.parseInt(args[2]));
        }

    }

    public static void set_reader(String file_directory)
    {
        reader_instance = new Reader();
        try
        {
            reader_instance.read(file_directory);
        }
        catch (Exception e)
        {
            System.err.println("Error. ");
            System.exit(1);
        }
    }

    public static void set_graph(Graph reader_graph)
    {
        graph_instance = new Graph();
        graph_instance = reader_instance.graph();
    }

    public static void set_list(Set<Node> graph_nodes)
    {
        node_list = graph_nodes;
    }

    public static void print_graph()
    {
        int counter = 0;
        int[] neighbour_array;
        int neighbour_array_counter;

        for(Node n: node_list)
        {
            neighbour_array_counter = 0;
            counter++;
            Node in_order_node = graph_instance.find(counter);
            while(in_order_node == null)
            {
                counter++;
                in_order_node = graph_instance.find(counter);
            }

            System.out.print(in_order_node.name());
            Set<Node> neighbour_set = in_order_node.neighbours();


            neighbour_array = new int[neighbour_set.size()];
            for(Node neighbour: neighbour_set)
            {
                neighbour_array[neighbour_array_counter] = Integer.parseInt(neighbour.name());
                neighbour_array_counter++;
            }

            Arrays.sort(neighbour_array);

            for(int element: neighbour_array)
            {
                System.out.print(" "+element);
            }
            System.out.println();
        }
    }

    public static Set<Node> neighbour_search(Graph graph, int n)
    {
        // create the output list
        Set<Node> output = new HashSet<Node>();
        
        for(Node node: graph.nodes())
        {
            if(node.neighbours().size()>=n)
            {
                output.add(node);
            }
        }
    
        return output;
    }
    public static void findFullyConnectedNeighbours(Graph graph)
    {
        int number_of_fully_connected = 0;
        for(Node node:graph.nodes())
        {
            List<Integer> set = new ArrayList<Integer>();
            Set<Node> neighbours = node.neighbours();
            for(Node neighbour:neighbours)
            {
                for(Node n_neighbour:neighbour.neighbours())
                {
                    set.add(Integer.parseInt(n_neighbour.name()));
                }
            }
            int c = neighbours.size();
            int counter = 0;
            for(Node n:neighbours)
            {
                for(int j = 0;j<(set.size());j++)
                {
                    if(set.get(j) == Integer.parseInt(n.name()))
                    {
                        counter++;
                    }
                }
            }
            if(counter == c*(c-1))
            {
                number_of_fully_connected++;
            }
            set.clear();
        }
        System.out.println("Number of nodes with fully connected neighbours: "+number_of_fully_connected);
    }

    public static void clique(Graph graph, int cliqueSize)
    {
        Set<Integer> set = new HashSet<Integer>();
        for(Node node:graph.nodes())
        {
            set.add(Integer.parseInt(node.name()));
            Set<Node> neighbours = node.neighbours();
            for(Node neighbour:neighbours)
            {
                for(Node n_neighbour:neighbour.neighbours())
                {
                    set.add(Integer.parseInt(n_neighbour.name()));
                }
            }
        }
        Set<Set<Integer>> pSet = powerSet(set);
         Set<Set<Integer>> cliques = new HashSet<Set<Integer>>();
        for (Set<Integer> x : pSet)
        {
             System.out.println(x);
        }
        for (Set<Integer> x : pSet)
        {
            System.out.println("Test: x = "+x); 
           List<Integer> list = new ArrayList<Integer>();
           Set<Node> testNodes = new HashSet<Node>();
           for (Integer element : x)
           {
              Node temp = graph.find(element);
              testNodes.add(temp);
              Set<Node> neighbours = temp.neighbours();
              for(Node neighbour:neighbours)
              {
                  for(Node n_neighbour:neighbour.neighbours())
                  {
                    list.add(Integer.parseInt(n_neighbour.name()));
                  }
              }
              System.out.println("Test list: "+list);


            int c = x.size();
            int counter = 0;
            for(Node n:neighbours)
            {
                for(int j = 0;j<(list.size());j++)
                {
                    if(list.get(j) == Integer.parseInt(n.name()))
                    {
                        counter++;
                    }
                }
            }
            if(counter == c*(c-1))
            {
                System.out.println("Counter Test: "+x);
                cliques.add(x);
            }
            list.clear();
           }

           for (Node node : testNodes)
            {
                System.out.println("Test: "+node.name());

            }
           
        }
        System.out.println("The number of cliques in the graph is "+cliques.size());
        for (Set<Integer> clique : cliques)
            {
                System.out.println(clique);

            }
    }

    public static <T> Set<Set<T>> powerSet(Set<T> originalSet) 
    {
        Set<Set<T>> sets = new HashSet<Set<T>>();
        if (originalSet.isEmpty()) 
        {
            sets.add(new HashSet<T>());
            return sets;
        }
        List<T> list = new ArrayList<T>(originalSet);
        T head = list.get(0);
        Set<T> rest = new HashSet<T>(list.subList(1, list.size())); 
        for (Set<T> set : powerSet(rest)) 
        {
            Set<T> newSet = new HashSet<T>();
            newSet.add(head);
            newSet.addAll(set);
            sets.add(newSet);
            sets.add(set);
        }       
        return sets;
    }

  
}