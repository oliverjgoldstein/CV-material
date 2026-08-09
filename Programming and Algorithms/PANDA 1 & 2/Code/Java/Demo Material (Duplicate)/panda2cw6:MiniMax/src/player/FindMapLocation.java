package player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class FindMapLocation {
    protected Map<Integer, List<Integer>> mapNodeToCoordinates;
    protected List<Integer> mrXCoordinates;

    public FindMapLocation(Integer mrXLocation) {
        mapNodeToCoordinates = findCoordinates();
        mrXCoordinates = findCoordinatesGivenNode(mrXLocation);
    }

    protected Map<Integer, List<Integer>> findCoordinates() {
        File file = new File("resources/pos.txt");

        Scanner in = null;

        try {
            in = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }

        Map<Integer, List<Integer>> tempMapNodeToCoordinates = new HashMap<Integer, List<Integer>>();

        String topLine = in.nextLine();
        int numberOfNodes = Integer.parseInt(topLine);

        for (int i = 0; i < numberOfNodes; i++) {
            String line = in.nextLine();
            String[] parts = line.split(" ");
            List<Integer> pos = new ArrayList<Integer>();

            Integer key = Integer.parseInt(parts[0]);
            pos.add(Integer.parseInt(parts[1]));
            pos.add(Integer.parseInt(parts[2]));

            tempMapNodeToCoordinates.put(key, pos);
        }
        return tempMapNodeToCoordinates;
    }

    protected List<Integer> findCoordinatesGivenNode(Integer mrXLocation) {
        List<Integer> coordinates = mapNodeToCoordinates.get(mrXLocation);
        return coordinates;
    }

    protected double findDistanceFromOrigin(Integer mrXLocation) {
        Integer midX = 509;
        Integer midY = 405;
        List<Integer> mrXCoordinates = findCoordinatesGivenNode(mrXLocation);
        Integer mrXXcoord = mrXCoordinates.get(0);
        Integer mrXYcoord = mrXCoordinates.get(1);
        Integer differenceX = (mrXXcoord - midX);
        Integer differenceY = (mrXYcoord - midY);
        if(differenceX < 0) {
            differenceX = differenceX*(-1);
        } if(differenceY < 0) {
            differenceY = differenceY*(-1);
        }

        double distance = Math.sqrt((differenceX*differenceX)+(differenceY*differenceY));
        double maxDistance = Math.sqrt((midX*midX)+(midY*midY));
        distance = distance/maxDistance;
        double valueToReturn = 100-Math.round((distance*100));
        return valueToReturn % 50;
    }

}
