package solution;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.border.Border;

import scotlandyard.Colour;


import java.io.File;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.*;

public class View extends JFrame {
    protected JFrame frame;
    protected JFrame loadingFrame;
    protected JButton startGameButton;
    protected JButton loadButton;
    protected List<Controller> controllerList;
    protected JTextField numberOfPlayers;
    protected JLabel loadingError;
    protected JLabel currentPlayer;
    protected JLabel currentPlayerLocation;
    protected JComboBox possibleMoves;
    protected JButton submitMove;
    protected JButton saveGame;
    protected JLabel taxiTickets;
    protected JLabel busTickets;
    protected JLabel undergroundTickets;
    protected JLabel secretTickets;
    protected JLabel doubleTickets;
    protected JLabel moveError;
    protected JLabel roundNumber;
    protected List<Integer> locationList;
    protected List<Colour> colourList;
    protected mapRenderer maprenderer;
    protected ImageIcon backgroundIcon;
    protected JLabel backLabel;
    private double width;
    private double height;
    ImageIcon taxiImageIcon;
    ImageIcon busImageIcon;
    ImageIcon undergroundImageIcon;
    ImageIcon secretImageIcon;
    JLabel rowOneMrXA;
    JLabel rowOneMrXB;
    JLabel rowTwoMrXA;
    JLabel rowTwoMrXB;
    JLabel rowThreeMrXA;
    JLabel rowThreeMrXB;
    JLabel rowFourMrXA;
    JLabel rowFourMrXB;
    JLabel rowFiveMrXA;
    JLabel rowFiveMrXB;
    JLabel rowSixMrXA;
    JLabel rowSixMrXB;
    JLabel rowSevenMrXA;
    JLabel rowSevenMrXB;
    JLabel rowEightMrXA;
    JLabel rowEightMrXB;
    JLabel rowNineMrXA;
    JLabel rowNineMrXB;
    JLabel rowTenMrXA;
    JLabel rowTenMrXB;
    JButton exitToMenu;
    JButton replayButton;
    JButton replayMove;
    List<JLabel> travelLabelList;
    Clip clip;


    // This is the constructor and it is fed a list of controllers it needs to update.
    public View()
    {
        controllerList = new ArrayList<Controller>();
    }


    // This creates the loading frame with a variety of characteristcs.
    public void initialiseLoadingFrame()
    {
        loadingFrame = new JFrame("Loading Screen");
        JPanel loadingPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        ImageIcon loading = new ImageIcon("solution/resources/loadingScreen.jpg");
        Image img = loading.getImage();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        Image newimg = img.getScaledInstance((int)(width/1.5), (int)(height/1.5),  java.awt.Image.SCALE_SMOOTH);
        loading = new ImageIcon(newimg);
        JLabel loadingBackground = new JLabel(loading);
        startGameButton = new JButton();
        loadButton = new JButton();
        numberOfPlayers = new JTextField("Enter number of detectives: ");
        loadingError = new JLabel();
        Font font = new Font("Andale Mono", Font.BOLD, 15);
        numberOfPlayers.setFont(font);

        PlaySound();
        numberOfPlayers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                numberOfPlayers.setText("");
            }
        });

        //Todo: decide if we want to implement the code below or not (mute button also on loading screen)

        ButtonEvent startGameClicked = new ButtonEvent();
        ButtonEvent loadButtonEvent = new ButtonEvent();

        startGameButton.setPreferredSize(new Dimension(100, 30));
        loadButton.setPreferredSize(new Dimension(100, 30));
        numberOfPlayers.setPreferredSize(new Dimension(300,40));
        startGameButton.addActionListener(startGameClicked);
        loadButton.addActionListener(loadButtonEvent);

        Border emptyBorder = BorderFactory.createEmptyBorder();
        loadButton.setBorder(emptyBorder);
        loadButton.setIcon(new ImageIcon("solution/resources/load.jpg"));
        startGameButton.setBorder(emptyBorder);
        startGameButton.setIcon(new ImageIcon("solution/resources/newgame.jpg"));

        loadingError.setForeground(Color.red);

        loadingFrame.setUndecorated(true);
        loadingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadingFrame.getContentPane().add(loadingPanel);
        loadingFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        loadingFrame.setLocationRelativeTo(null);
        loadingFrame.setResizable(false);

        eastPanel.setBackground(Color.black);
        eastPanel.setLayout(new GridLayout(10,1));
        loadingPanel.setLayout(new BorderLayout());
        loadingPanel.setBackground(Color.BLACK);
        loadingPanel.add(loadingBackground, BorderLayout.WEST);
        loadingPanel.add(eastPanel, BorderLayout.EAST);
        eastPanel.add(loadButton);
        eastPanel.add(startGameButton);
        eastPanel.add(numberOfPlayers);
        eastPanel.add(loadingError);


        loadingFrame.setVisible(true);
    }

    // This initialises the main frame.
    public void initialiseMainFrame()
    {
        frame = new JFrame("Scotland Yard Game");
        JPanel mainPanel = new JPanel();
        JPanel eastPanel = new JPanel();
        JPanel westPanel = new JPanel();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        width = screenSize.getWidth();
        height = screenSize.getHeight();

        currentPlayer = new JLabel();
        currentPlayerLocation = new JLabel();
        taxiTickets = new JLabel();
        busTickets = new JLabel();
        undergroundTickets = new JLabel();
        secretTickets = new JLabel();
        doubleTickets = new JLabel();
        String labels[] = {"Choose Move:"};
        possibleMoves = new JComboBox(labels);
        moveError = new JLabel();
        roundNumber = new JLabel();
        submitMove = new JButton();
        saveGame = new JButton();

        final JToggleButton muteGame = new JToggleButton("Pause Music");
        muteGame.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (muteGame.isSelected()) {
                    clip.stop();
                    muteGame.setText("Play Music");
                } else {
                    clip.loop(-1);
                    clip.start();
                    muteGame.setText("Pause Music");
                }
            }
        } );

        final JButton rules = new JButton("Game Rules");
        rules.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                rules();
            }
        });

        replayMove = new JButton("Replay Move");


        taxiImageIcon = new ImageIcon("solution/resources/taxiLarge.png");
        Image taxiImage = taxiImageIcon.getImage();
        Image resizedTaxiImage = taxiImage.getScaledInstance(150, 25,  java.awt.Image.SCALE_SMOOTH);
        taxiImageIcon = new ImageIcon(resizedTaxiImage);

        busImageIcon = new ImageIcon("solution/resources/busLarge.png");
        Image busImage = busImageIcon.getImage();
        Image resizedBusImage = busImage.getScaledInstance(150, 25,  java.awt.Image.SCALE_SMOOTH);
        busImageIcon = new ImageIcon(resizedBusImage);

        undergroundImageIcon = new ImageIcon("solution/resources/trainLarge.png");
        Image undergroundImage = undergroundImageIcon.getImage();
        Image resizedUndergroundImage = undergroundImage.getScaledInstance(150, 25,  java.awt.Image.SCALE_SMOOTH);
        undergroundImageIcon = new ImageIcon(resizedUndergroundImage);

        secretImageIcon = new ImageIcon("solution/resources/secretLarge.png");
        Image secretImage = secretImageIcon.getImage();
        Image resizedSecretImage = secretImage.getScaledInstance(150, 25,  java.awt.Image.SCALE_SMOOTH);
        secretImageIcon = new ImageIcon(resizedSecretImage);

        Color lightblue = new Color(51,199,255);
        rowOneMrXA = new JLabel();
        rowOneMrXB = new JLabel();
        rowTwoMrXA = new JLabel();
        rowTwoMrXB = new JLabel();
        rowThreeMrXA = new JLabel();
        rowThreeMrXB = new JLabel();
        rowFourMrXA = new JLabel();
        rowFourMrXB = new JLabel();
        rowFiveMrXA = new JLabel();
        rowFiveMrXB = new JLabel();
        rowSixMrXA = new JLabel();
        rowSixMrXB = new JLabel();
        rowSevenMrXA = new JLabel();
        rowSevenMrXB = new JLabel();
        rowEightMrXA = new JLabel();
        rowEightMrXB = new JLabel();
        rowNineMrXA = new JLabel();
        rowNineMrXB = new JLabel();
        rowTenMrXA = new JLabel();
        rowTenMrXB = new JLabel();
        exitToMenu = new JButton("Exit to menu.");
        replayButton = new JButton("Replay current game.");

        replayButton.setBackground(new Color(58,55,127));
        replayButton.setForeground(Color.WHITE);
        replayButton.setOpaque(true);
        replayButton.setBorderPainted(false);

        muteGame.setBackground(new Color(55,85,127));
        muteGame.setForeground(Color.WHITE);
        muteGame.setOpaque(true);
        muteGame.setBorderPainted(false);

        rules.setBackground(new Color(51,102,116));
        rules.setForeground(Color.WHITE);
        rules.setOpaque(true);
        rules.setBorderPainted(false);

        exitToMenu.setBackground(new Color(30,64,101));
        exitToMenu.setForeground(Color.WHITE);
        exitToMenu.setOpaque(true);
        exitToMenu.setBorderPainted(false);

        replayMove.setBackground(new Color(51,91,116));
        replayMove.setForeground(Color.WHITE);
        replayMove.setOpaque(true);
        replayMove.setBorderPainted(false);


        travelLabelList = new ArrayList<JLabel>();
        travelLabelList.add(0, rowOneMrXA);
        travelLabelList.add(1, rowTwoMrXA);
        travelLabelList.add(2, rowThreeMrXA);
        travelLabelList.add(3, rowFourMrXA);
        travelLabelList.add(4, rowFiveMrXA);
        travelLabelList.add(5, rowSixMrXA);
        travelLabelList.add(6, rowSevenMrXA);
        travelLabelList.add(7, rowEightMrXA);
        travelLabelList.add(8, rowNineMrXA);
        travelLabelList.add(9, rowTenMrXA);
        travelLabelList.add(10, rowOneMrXB);
        travelLabelList.add(11, rowTwoMrXB);
        travelLabelList.add(12, rowThreeMrXB);
        travelLabelList.add(13, rowFourMrXB);
        travelLabelList.add(14, rowFiveMrXB);
        travelLabelList.add(15, rowSixMrXB);
        travelLabelList.add(16, rowSevenMrXB);
        travelLabelList.add(17, rowEightMrXB);
        travelLabelList.add(18, rowNineMrXB);
        travelLabelList.add(19, rowTenMrXB);

        ButtonEvent submitMoveButtonEvent = new ButtonEvent();
        ButtonEvent saveGameButtonEvent = new ButtonEvent();
        ButtonEvent exitToMenu = new ButtonEvent();
        ButtonEvent replayButton = new ButtonEvent();
        ButtonEvent replayMoveButton = new ButtonEvent();
        submitMove.addActionListener(submitMoveButtonEvent);
        saveGame.addActionListener(saveGameButtonEvent);
        this.exitToMenu.addActionListener(exitToMenu);
        this.replayButton.addActionListener(replayButton);
        replayMove.addActionListener(replayMoveButton);

        currentPlayer.setForeground(Color.white);
        currentPlayerLocation.setForeground(Color.white);
        taxiTickets.setForeground(Color.yellow);
        busTickets.setForeground(Color.green);
        undergroundTickets.setForeground(Color.red);
        secretTickets.setForeground(Color.lightGray);
        doubleTickets.setForeground(lightblue);
        roundNumber.setForeground(Color.white);

        Border emptyBorder = BorderFactory.createEmptyBorder();
        ImageIcon image = new ImageIcon("solution/resources/submit.jpg");
        ImageIcon saveGameImage = new ImageIcon("solution/resources/save.jpg");
        submitMove.setBorder(emptyBorder);
        saveGame.setBorder(emptyBorder);
        Image img = image.getImage();
        Image tempimg = saveGameImage.getImage();
        Image newimg = img.getScaledInstance( 150, 35,  java.awt.Image.SCALE_SMOOTH);
        Image temp = tempimg.getScaledInstance( 150, 35,  java.awt.Image.SCALE_SMOOTH);
        image = new ImageIcon( newimg );
        ImageIcon newone = new ImageIcon( temp );
        submitMove.setIcon(image);
        saveGame.setIcon(newone);


        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.setLocationRelativeTo(null);


        backgroundIcon = findImage(width, height);
        backLabel = new JLabel();
        backLabel.setIcon(backgroundIcon);
        JScrollPane jsp = new JScrollPane(backLabel);

        roundNumber.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        Color darkGrey = new Color(20, 25, 21);

        GridLayout layout = new GridLayout(1,2);
        JPanel row1 = new JPanel(layout);
        JPanel row2 = new JPanel(layout);
        JPanel row3 = new JPanel(layout);
        JPanel row4 = new JPanel(layout);
        JPanel row5 = new JPanel(layout);
        JPanel row6 = new JPanel(layout);
        JPanel row7 = new JPanel(layout);
        JPanel row8 = new JPanel(layout);
        JPanel row9 = new JPanel(layout);
        JPanel row10 = new JPanel(layout);
        JPanel row11 = new JPanel(new GridLayout(1,3));
        JPanel row12 = new JPanel(new GridLayout(1,2));
        row1.setBackground(darkGrey);
        row2.setBackground(darkGrey);
        row3.setBackground(darkGrey);
        row4.setBackground(darkGrey);
        row5.setBackground(darkGrey);
        row6.setBackground(darkGrey);
        row7.setBackground(darkGrey);
        row8.setBackground(darkGrey);
        row9.setBackground(darkGrey);
        row10.setBackground(darkGrey);
        row11.setBackground(darkGrey);
        row12.setBackground(darkGrey);
        Font font = new Font("Andale Mono", Font.BOLD, 14);
        Font roundNumberFont = new Font("Arial", Font.BOLD, 20);
        westPanel.setLayout(new GridLayout(1,1));
        eastPanel.setLayout(new GridLayout(23,1));
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(westPanel, BorderLayout.WEST);
        westPanel.add(jsp);
        mainPanel.add(eastPanel, BorderLayout.EAST);
        mainPanel.add(roundNumber, BorderLayout.NORTH);
        eastPanel.add(possibleMoves);
        eastPanel.add(submitMove);
        eastPanel.add(currentPlayerLocation);
        eastPanel.add(currentPlayer);
        eastPanel.add(taxiTickets);
        eastPanel.add(busTickets);
        eastPanel.add(undergroundTickets);
        eastPanel.add(secretTickets);
        eastPanel.add(doubleTickets);
        eastPanel.add(saveGame);
        eastPanel.add(moveError);
        row1.add(rowOneMrXA);
        row1.add(rowOneMrXB);
        row2.add(rowTwoMrXA);
        row2.add(rowTwoMrXB);
        row3.add(rowThreeMrXA);
        row3.add(rowThreeMrXB);
        row4.add(rowFourMrXA);
        row4.add(rowFourMrXB);
        row5.add(rowFiveMrXA);
        row5.add(rowFiveMrXB);
        row6.add(rowSixMrXA);
        row6.add(rowSixMrXB);
        row7.add(rowSevenMrXA);
        row7.add(rowSevenMrXB);
        row8.add(rowEightMrXA);
        row8.add(rowEightMrXB);
        row9.add(rowNineMrXA);
        row9.add(rowNineMrXB);
        row10.add(rowTenMrXA);
        row10.add(rowTenMrXB);
        row11.add(muteGame);
        row11.add(rules);
        row11.add(replayMove);
        row12.add(this.exitToMenu);
        row12.add(this.replayButton);
        eastPanel.add(row1);
        eastPanel.add(row2);
        eastPanel.add(row3);
        eastPanel.add(row4);
        eastPanel.add(row5);
        eastPanel.add(row6);
        eastPanel.add(row7);
        eastPanel.add(row8);
        eastPanel.add(row9);
        eastPanel.add(row10);
        eastPanel.add(row11);
        eastPanel.add(row12);
        westPanel.setBackground(darkGrey);
        eastPanel.setBackground(darkGrey);
        mainPanel.setBackground(darkGrey);
        currentPlayerLocation.setHorizontalAlignment(JLabel.CENTER);
        possibleMoves.setPreferredSize(new Dimension(((int) width - (backgroundIcon.getIconWidth()) - 40), 20));
        currentPlayer.setHorizontalAlignment(JLabel.CENTER);
        taxiTickets.setHorizontalAlignment(JLabel.CENTER);
        busTickets.setHorizontalAlignment(JLabel.CENTER);
        undergroundTickets.setHorizontalAlignment(JLabel.CENTER);
        secretTickets.setHorizontalAlignment(JLabel.CENTER);
        doubleTickets.setHorizontalAlignment(JLabel.CENTER);
        moveError.setHorizontalAlignment(JLabel.CENTER);
        roundNumber.setHorizontalAlignment(JLabel.CENTER);

        frame.setUndecorated(true);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize());

        currentPlayerLocation.setFont(font);
        currentPlayer.setFont(font);
        taxiTickets.setFont(font);
        busTickets.setFont(font);
        undergroundTickets.setFont(font);
        secretTickets.setFont(font);
        doubleTickets.setFont(font);
        moveError.setFont(font);
        moveError.setForeground(new Color(126,19,255));
        roundNumber.setFont(roundNumberFont);
        jsp.getViewport().setBackground(darkGrey);
        jsp.setBorder(BorderFactory.createEmptyBorder());
    }

    // This re renders the background image with the width and height of the screen over 1.4 and 1.2.
    public ImageIcon findImage(double width, double height)
    {
        maprenderer = new mapRenderer();
        maprenderer.render(locationList, colourList);
        ImageIcon backgroundIcons = new ImageIcon(maprenderer.returnImage());
        Image w = backgroundIcons.getImage();
        Image n = w.getScaledInstance((int)(width/1.4), (int)(height/1.2),  java.awt.Image.SCALE_SMOOTH);
        backgroundIcons = new ImageIcon(n);
        return backgroundIcons;
    }

    // This is used for loading players.
    public int getPlayerNumber() throws NumberFormatException
    {
        return Integer.parseInt(numberOfPlayers.getText());
    }


    // Sets loading error on loading screen.
    public void setLoadingError(String error)
    {
        loadingError.setText(error);
    }


    // The following are somewhat self explanatory.
    public void setCurrentPlayer(String colour)
    {
        currentPlayer.setText("Current Player: "+colour);
    }
    public void setCurrentPlayerLocation(Integer location)
    {
        currentPlayerLocation.setText("Location: "+location.toString());
    }

    public void setTaxiTickets(Integer ticketNumber)
    {
        taxiTickets.setText("Number of taxi tickets left: "+ticketNumber.toString());
    }

    public void setBusTickets(Integer ticketNumber)
    {
        busTickets.setText("Number of bus tickets left: "+ticketNumber.toString());
    }

    public void setUndergroundTickets(Integer ticketNumber)
    {
        undergroundTickets.setText("Number of underground tickets left: "+ticketNumber.toString());
    }

    public void setSecretTickets(Integer ticketNumber)
    {
        secretTickets.setText("Number of secret tickets left: "+ticketNumber.toString());
    }

    public void setDoubleTickets(Integer ticketNumber)
    {
        doubleTickets.setText("Number of double tickets left: "+ticketNumber.toString());
    }

    // Displays a difference if the round is zero and the game has not started.
    public void setRoundNumber(Integer round)
    {
        if(round == 0)
        {
            roundNumber.setText("Play a move to start the game: ");
        }
        else {
            roundNumber.setText("Round Number: " + round.toString() + "/20");
        }
    }

    public void setMoveError(String error)
    {
        moveError.setText(error);
    }

    public void setPossibleMoves(String[] Moves)
    {
        for(String move: Moves)
        {
            possibleMoves.addItem(move);
        }
    }

    // This is set from the view to tell the map who to render where.
    public void setLocationsAndColour(List<Integer> locationList, List<Colour> colourList)
    {
        this.locationList = locationList;
        this.colourList = colourList;
    }

    // This is called to re render the image.
    public void updateImage()
    {
        maprenderer.render(locationList, colourList);
        backgroundIcon = new ImageIcon(maprenderer.returnImage());
        Image w = backgroundIcon.getImage();
        Image n = w.getScaledInstance((int)(width/1.4), (int)(height/1.2),  java.awt.Image.SCALE_SMOOTH);
        backgroundIcon = new ImageIcon(n);
        backgroundIcon.getImage().flush();
        backLabel.setIcon(backgroundIcon);
    }

    // This is told what icon to display by the view and it is given which row it should use.
    // The rows are held in an arraylist. The ticket index is from 0 to 3.
    public void setTicketImage(int ticketIndex, int rowIndex)
    {
        ImageIcon image;
        if(ticketIndex == 0)
        {
            image = taxiImageIcon;
        }
        else if(ticketIndex == 1)
        {
            image = busImageIcon;
        }
        else if(ticketIndex == 2)
        {
            image = undergroundImageIcon;
        }
        else if(ticketIndex == 3)
        {
            image = secretImageIcon;
        }
        else
        {
            image = taxiImageIcon;
        }
        JLabel change = travelLabelList.get(rowIndex);
        change.setIcon(image);
    }

    // This is called by the controller after start/load game.
    public void startMainPanel()
    {
        loadingFrame.setVisible(false);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    // This is called when the an action is made by the player.
    public void controllerUpdate(ActionEvent e)
    {
        for(Controller controller: controllerList)
        {
            controller.notify(e);
        }
    }

    // This spectate adds the controllers it needs to update to a list.
    public void spectate(Controller controller)
    {
        controllerList.add(controller);
    }


    // This implements all the action listeners for the JButtons.
    public class ButtonEvent implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(!(e.getSource() == exitToMenu))
            {
                controllerUpdate(e);
            }
            else {
                int n = JOptionPane.showConfirmDialog(
                        frame,
                        "Are you sure you want to quit?",
                        "",
                        JOptionPane.YES_NO_OPTION);
                if(n==JOptionPane.YES_OPTION)
                {
                    controllerUpdate(e);
                }

            }
        }
    }

    // This plays music.
    public  void PlaySound() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("solution/resources/Naruto-Girei.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }

    }

    // This creates the rule box window.
    public void rules()
    {
        final JFrame f = new JFrame("Scotland Yard Rules");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel gui = new JPanel(new BorderLayout());
        final JEditorPane document = new JEditorPane();

        gui.add(new JScrollPane(document), BorderLayout.CENTER);
        File file = new File("solution/resources/Rules.txt");

        try {
            document.setPage(file.toURI().toURL());
        } catch(Exception e) {
            e.printStackTrace();
        }

        f.setContentPane(gui);
        f.pack();
        f.setSize(700,600);
        f.setLocationByPlatform(true);
        f.setVisible(true);
    }
}