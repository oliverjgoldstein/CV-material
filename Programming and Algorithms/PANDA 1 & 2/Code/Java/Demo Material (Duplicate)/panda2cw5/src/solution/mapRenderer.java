package solution;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import scotlandyard.Colour;

public class mapRenderer {
    private String filePath = "solution/resources/map.jpg";
    private Map<Integer, List<Integer>> coordinateMap;
    private List<Integer> posLocations;
    private List<Colour> colourList;
    private List<Integer> locationList;
    protected BufferedImage bufferedImage;

    // This renders the map.
    // It takes in a list of locations and colours
    // It then draws to that image and rewrites it.
    // It takes in coordinates from pos.txt
    // It then traverses the colour list and switches them corresponding to the coordinates.
    public void render(List<Integer> locations, List<Colour> colours) {
        this.colourList = colours;
        this.locationList = locations;
        coordinateMap = findCoordinates();
        Image img = new ImageIcon(filePath).getImage();
        bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.drawImage(img, 0, 0, null);

        int i = 0;
        for (Integer location : locationList) {
            if (location != 0) {
                if (coordinateMap.get(location) != null) {
                    List<Integer> coords = coordinateMap.get(location);
                    Integer y = coords.get(0);
                    Integer x = coords.get(1);
                    switch (colourList.get(i)) {
                        case Black:
                            g2d.setColor(Color.black);
                            break;
                        case White:
                            g2d.setColor(Color.white);
                            break;
                        case Yellow:
                            g2d.setColor(Color.yellow);
                            break;
                        case Green:
                            g2d.setColor(Color.green);
                            break;
                        case Blue:
                            g2d.setColor(Color.blue);
                            break;
                                case Red:
                                    g2d.setColor(Color.red);
                                    break;
                            }
                            g2d.fill(new Ellipse2D.Float((y - 12), (x - 12), 25, 25));
                        }
                    }
                    i++;
                }

        g2d.dispose();
        try {
            ImageIO.write(bufferedImage, "jpg", new File("solution/resources/MapRendered.jpg"));
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    // This returns the image to the view.
    public Image returnImage() {
        return (Image) bufferedImage;
    }

    // This returns the coordinates and puts them in a Map corresponding to a location to a pair of x,y coordinates.
    public Map<Integer, List<Integer>> findCoordinates()
    {
        File file = new File("solution/resources/pos.txt");

        Scanner in = null;
        try {
            in = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }

        Map<Integer, List<Integer>> tempCoordinateMap = new HashMap<Integer, List<Integer>>();
        String topLine = in.nextLine();
        int numberOfNodes = Integer.parseInt(topLine);
        posLocations = new ArrayList<Integer>();

        for (int i = 0; i < numberOfNodes; i++)
        {
            String line = in.nextLine();
            String[] parts = line.split(" ");
            List<Integer> pos = new ArrayList<Integer>();
            pos.add(Integer.parseInt(parts[1]));
            pos.add(Integer.parseInt(parts[2]));
            String key = parts[0];
            tempCoordinateMap.put(Integer.parseInt(key), pos);
            posLocations.add(i, Integer.parseInt(key));
        }
        return tempCoordinateMap;
    }
}