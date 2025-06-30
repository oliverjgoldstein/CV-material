package player;

import gui.Gui;
import net.PlayerFactory;
import scotlandyard.Colour;
import scotlandyard.Player;
import scotlandyard.ScotlandYardView;
import scotlandyard.Spectator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The RandomPlayerFactory is an example of a PlayerFactory that
 * creates a series of random players. By default it assigns
 * a random AI to each of the colours except Blue, which
 * is controlled by the GUI.
 */
public class OurPlayerFactory implements PlayerFactory {
    protected Map<Colour, PlayerType> typeMap;

    public enum PlayerType {AI, GUI}

    String imageFilename;
    String positionsFilename;

    protected List<Spectator> spectators;
    Gui gui;

    public OurPlayerFactory() {
        typeMap = new HashMap<Colour, PlayerType>();
        typeMap.put(Colour.Black, OurPlayerFactory.PlayerType.AI);
        typeMap.put(Colour.Blue, OurPlayerFactory.PlayerType.AI);
        typeMap.put(Colour.Green, OurPlayerFactory.PlayerType.AI);
        typeMap.put(Colour.Red, OurPlayerFactory.PlayerType.AI);
        typeMap.put(Colour.White, OurPlayerFactory.PlayerType.AI);
        typeMap.put(Colour.Yellow, OurPlayerFactory.PlayerType.AI);

        positionsFilename = "resources/pos.txt";
        imageFilename     = "resources/map.jpg";

        spectators = new ArrayList<Spectator>();
    }

    public OurPlayerFactory(Map<Colour, PlayerType> typeMap, String imageFilename, String positionsFilename) {
        this.typeMap = typeMap;
        this.imageFilename = imageFilename;
        this.positionsFilename = positionsFilename;
        spectators = new ArrayList<Spectator>();
    }

    @Override
    public Player player(Colour colour, ScotlandYardView view, String mapFilename) {
        switch (typeMap.get(colour)) {
            case AI:
                return new MyAIPlayer(view, mapFilename);
            case GUI:
                return gui(view);
            default:
                return new MyAIPlayer(view, mapFilename);
        }
    }

    @Override
    public void ready() {
        if (gui != null) gui.run();
    }

    @Override
    public List<Spectator> getSpectators(ScotlandYardView view) {
        List<Spectator> specs = new ArrayList<Spectator>();
        specs.add(gui(view));
        return specs;
    }

    @Override
    public void finish() {
        if (gui != null) gui.update();
    }


    private Gui gui(ScotlandYardView view) {
        System.out.println("GUI");
        if (gui == null) try {
            gui = new Gui(view, imageFilename, positionsFilename);
            spectators.add(gui);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return gui;
    }
}
