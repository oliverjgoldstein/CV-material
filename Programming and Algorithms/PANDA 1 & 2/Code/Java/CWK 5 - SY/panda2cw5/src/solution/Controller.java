package solution;

import scotlandyard.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class Controller implements Spectator {

    protected ScotlandYardModel model;
    protected View view;
    protected List<PlayerInfo> playerList;
    protected List<Move> allMoves;
    int replayMoveIndex;

    // This constructs the controller with the view.
    // The model will be created later by model initialiser.
    public Controller(View view)
    {
        this.view = view;
        view.spectate(this);
        view.initialiseLoadingFrame();
    }

    // This notifies the controller of moves made by the model.
    public void notify(Move move)
    {
        // This is empty as the MVC principle is implemented differently.
    }

    // This tells the loading screen if they file they chose is a valid txt file.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.txt)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    // This creates a map for MrX for his tickets.
    // It initialises the values to a specific prechosen list.
    public Map<Ticket, Integer> MrXMap()
    {
        Map<Ticket, Integer> mrXTicketMap = new LinkedHashMap<Ticket, Integer>();
        mrXTicketMap.put(Ticket.Taxi, 4);
        mrXTicketMap.put(Ticket.Bus, 3);
        mrXTicketMap.put(Ticket.Underground, 3);
        mrXTicketMap.put(Ticket.DoubleMove, 2);
        mrXTicketMap.put(Ticket.SecretMove, 5);
        return mrXTicketMap;
    }

    // This is the same for the detectives.
    // There are 0 double/secret tickets.
    public Map<Ticket, Integer> DetectiveMap()
    {
        Map<Ticket, Integer> detectiveTicketMap = new LinkedHashMap<Ticket, Integer>();
        detectiveTicketMap.put(Ticket.Taxi, 11);
        detectiveTicketMap.put(Ticket.Bus, 8);
        detectiveTicketMap.put(Ticket.Underground, 4);
        detectiveTicketMap.put(Ticket.DoubleMove, 0);
        detectiveTicketMap.put(Ticket.SecretMove, 0);
        return detectiveTicketMap;
    }

    // This is what view calls to notify the view of any changes.
    // The methods look at the source of each of the actions e.g. submit button and take relevant action.
    public void notify(ActionEvent e)
    {
        Object source = e.getSource();

        if(source == view.startGameButton)
        {
            boolean numberFormatException = false;
            int numberOfPlayers = 0;
            try
            {
                numberOfPlayers = view.getPlayerNumber();
            }
            catch(NumberFormatException exception)
            {
                numberFormatException = true;
                view.setLoadingError("Please enter a number.");
            }

            // Now the user has entered a number.
            if(numberFormatException == false)
            {
                if((numberOfPlayers < 6 && numberOfPlayers >= 1)) {
                    initialiseModel(view.getPlayerNumber());
                    model.spectate(this);

                    // Start the game if the model is ready.
                    if (model.isReady()) {
                        passLocationsToView();
                        view.initialiseMainFrame();
                        view.startMainPanel();
                        view.setCurrentPlayer(model.getCurrentPlayer().toString());
                        view.setCurrentPlayerLocation(model.getPlayerLocation(model.getCurrentPlayer()));
                        view.setTaxiTickets(model.getPlayerTickets(model.getCurrentPlayer(),Ticket.Taxi));
                        view.setBusTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Bus));
                        view.setUndergroundTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Underground));
                        view.setSecretTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.SecretMove));
                        view.setDoubleTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.DoubleMove));
                        view.setRoundNumber(model.getRound());
                        String[] movesToPass = new String[model.validMoves(model.getCurrentPlayer()).size()];
                        int i = 0;
                        for (Move move : model.validMoves(model.getCurrentPlayer())) {
                            movesToPass[i] = i+". "+move.toString();
                            i++;
                        }
                        view.setPossibleMoves(movesToPass);
                        view.replayMove.setEnabled(false);
                    }
                }
                else
                {
                    view.setLoadingError("Please enter a number between 1 and 5 inclusive.");
                }
            }
        }


        // Load the game and then update the view.
        if(source == view.loadButton)
        {
            File file = null;
            boolean validFile = true;
            final JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(view);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                if(!(accept(file)))
                {
                    validFile = false;
                    view.setLoadingError("Please choose a txt file.");
                }
            }
            else {
                validFile = false;
                view.setLoadingError("Please select a file.");
            }

            if(validFile) {
                Load load = new Load();
                load.loadGame(file);
                if (!(load.flagError == 1)) {
                    view.loadingFrame.setVisible(false);

                    model = load.loadGameModel();
                    playerList = load.getPlayerList();
                    model.spectate(this);
                    passLocationsToView();
                    view.initialiseMainFrame();
                    view.startMainPanel();
                    view.setCurrentPlayer(model.getCurrentPlayer().toString());
                    view.setCurrentPlayerLocation(model.getPlayerLocation(model.getCurrentPlayer()));
                    view.setTaxiTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Taxi));
                    view.setBusTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Bus));
                    view.setUndergroundTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Underground));
                    view.setSecretTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.SecretMove));
                    view.setDoubleTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.DoubleMove));
                    view.setRoundNumber(model.getRound());
                    String[] movesToPass = new String[model.validMoves(model.getCurrentPlayer()).size()];
                    int i = 0;
                    for (Move move : model.validMoves(model.getCurrentPlayer())) {
                        movesToPass[i] = i + ". " + move.toString();
                        i++;
                    }
                    view.setPossibleMoves(movesToPass);
                    view.replayMove.setEnabled(false);
                    model.playerWithColour(Colour.Black).generateMoves();
                    List<Move> list = model.playerWithColour(Colour.Black).getMoves();
                    i = 0;

                    // These update the rows of tickets on the view.
                    // It updates it using ticket and row indices.
                    for (Move move : list) {
                        if (move instanceof MoveTicket) {
                            MoveTicket moveT = (MoveTicket) move;
                            if (moveT.ticket.equals(Ticket.Taxi)) {
                                view.setTicketImage(0, i);
                            } else if (moveT.ticket.equals(Ticket.Bus)) {
                                view.setTicketImage(1, i);
                            } else if (moveT.ticket.equals(Ticket.Underground)) {
                                view.setTicketImage(2, i);
                            } else if (moveT.ticket.equals(Ticket.SecretMove)) {
                                view.setTicketImage(3, i);
                            } else {
                                view.setTicketImage(0, i);
                            }
                            i++;

                        } else if (move instanceof MoveDouble && !(move instanceof MoveTicket)) {
                            MoveDouble moveTD = (MoveDouble) move;
                            MoveTicket moveTD1 = (MoveTicket) moveTD.moves.get(0);
                            MoveTicket moveTD2 = (MoveTicket) moveTD.moves.get(1);

                            if (moveTD1.ticket.equals(Ticket.Taxi)) {
                                view.setTicketImage(0, i);
                            } else if (moveTD1.ticket.equals(Ticket.Bus)) {
                                view.setTicketImage(1, i);
                            } else if (moveTD1.ticket.equals(Ticket.Underground)) {
                                view.setTicketImage(2, i);
                            } else if (moveTD1.ticket.equals(Ticket.SecretMove)) {
                                view.setTicketImage(3, i);
                            } else {

                            }
                            i++;

                            if (moveTD2.ticket.equals(Ticket.Taxi)) {
                                view.setTicketImage(0, i);
                            } else if (moveTD2.ticket.equals(Ticket.Bus)) {
                                view.setTicketImage(1, i);
                            } else if (moveTD2.ticket.equals(Ticket.Underground)) {
                                view.setTicketImage(2, i);
                            } else if (moveTD2.ticket.equals(Ticket.SecretMove)) {
                                view.setTicketImage(3, i);
                            }
                            i++;
                        }
                    }
//                }
                }
                else
                {
                    view.setLoadingError("Please give a valid model file.");
                }
            }

        }

        // No need to destruct anything. Garbage collector will do this for us.
        if(source == view.exitToMenu)
        {
            view.loadingFrame.setVisible(true);
            view.frame.setVisible(false);
        }

        // This is when they want to restart the game. Reinitialise everything and on replayMove play the move.
        // We disable the submit button in this process.
        // ReplayMoveIndex is where we are in the list of moves o be played.
        if(source == view.replayButton)
        {
            for(PlayerInfo player : model.playerList)
            {
                player.setLocation(player.getVisitedLocations().get(0)); // Each player is returned to their starting location.
                if(!(player.getColour().equals(Colour.Black))) {
                    player.setTicketMap(DetectiveMap());
                }
                else
                {
                    player.setTicketMap(MrXMap());
                }
            }

            model.setCurrentPlayerIndex(0);
            model.setRoundNumber(0);
            model.setMrXLastLocation(0);
            Replay replay = new Replay(model);
            allMoves = replay.replayGame(); //Returns the list of all moves played in the game in order.
            passLocationsToView();
            view.setCurrentPlayer(Colour.Black.toString());
            view.setCurrentPlayerLocation(model.getPlayerLocation(model.getCurrentPlayer()));
            view.setTaxiTickets(model.getPlayerTickets(model.getCurrentPlayer(),Ticket.Taxi));
            view.setBusTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Bus));
            view.setUndergroundTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Underground));
            view.setSecretTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.SecretMove));
            view.setDoubleTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.DoubleMove));
            view.setRoundNumber(model.getRound());
            view.updateImage();
            for(JLabel label:view.travelLabelList)
            {
                label.setIcon(null);
                label.revalidate();
            }
            view.replayMove.setEnabled(true);
            view.submitMove.setEnabled(false);
            replayMoveIndex = 0;
        }

        // Upon submit move we find the index of the move to play by looking at the string given by the dropdown.
        // We then find that as an index in the valid moves.
        // We then play that to move loop.
        // If the game is over display the moves and disable the submit button.
        if (source == view.submitMove) {
            String x = String.valueOf(view.possibleMoves.getSelectedItem());
            if(!(x.equals("Choose Move:"))) {
                String[] words = x.split("\\.");
                Integer moveIndex = Integer.parseInt(words[0]);
                Move move = model.validMoves(model.getCurrentPlayer()).get(moveIndex);
                moveLoop(move);
                view.replayMove.setEnabled(false);
                if(model.isGameOver())
                {
                    if(model.getRound() == 19 && model.getCurrentPlayer().equals(Colour.Black)) {
                        view.setRoundNumber(model.getRound() + 1);
                    }
                    view.setMoveError("Winners: "+model.getWinningPlayers().toString());
                    view.submitMove.setEnabled(false);
                }
            }
            else
            {
                view.setMoveError("Please enter a valid move.");
            }
        }

        // Call the saveGame model. As simple as that.
        if(source == view.saveGame)
        {
            Save save = new Save(model);
            save.saveGame();
        }

        // If we do replay move we call moveloop with the move from the replayIndex.
        if(source == view.replayMove)
        {
            if(replayMoveIndex != allMoves.size()) {
                Move move = allMoves.get(replayMoveIndex);
                moveLoop(move);
                replayMoveIndex++;
            }
            else
            {
                view.replayMove.setEnabled(false);
                view.setMoveError("No more moves in the replay list.");
                view.submitMove.setEnabled(true);
            }
        }

    }

    // Moveloop calls model.turn and updates the view.
    public void moveLoop(Move startMove)
    {
        Move move = startMove;
        if (move.colour.equals(Colour.Black)) {
            if (move instanceof MoveTicket) {
                MoveTicket moveT = (MoveTicket) move;
                int round = model.getRound();
                round++;
                if (moveT.ticket.equals(Ticket.Taxi)) {
                    view.setTicketImage(0, round-1);
                } else if (moveT.ticket.equals(Ticket.Bus)) {
                    view.setTicketImage(1, round-1);
                } else if (moveT.ticket.equals(Ticket.Underground)) {
                    view.setTicketImage(2, round-1);
                } else if (moveT.ticket.equals(Ticket.SecretMove)) {
                    view.setTicketImage(3, round-1);
                } else {
                    view.setTicketImage(0, round-1);
                }

            }

            else if (move instanceof MoveDouble && !(move instanceof MoveTicket)) {
                MoveDouble moveTD = (MoveDouble) move;
                MoveTicket moveTD1 = (MoveTicket) moveTD.moves.get(0);
                MoveTicket moveTD2 = (MoveTicket) moveTD.moves.get(1);
                int round = model.getRound();
                round++;

                if (moveTD1.ticket.equals(Ticket.Taxi)) {
                    view.setTicketImage(0, round-1);
                } else if (moveTD1.ticket.equals(Ticket.Bus)) {
                    view.setTicketImage(1, round-1);
                } else if (moveTD1.ticket.equals(Ticket.Underground)) {
                    view.setTicketImage(2, round-1);
                } else if (moveTD1.ticket.equals(Ticket.SecretMove)) {
                    view.setTicketImage(3, round-1);
                } else {

                }


                if (moveTD2.ticket.equals(Ticket.Taxi)) {
                    view.setTicketImage(0, round);
                } else if (moveTD2.ticket.equals(Ticket.Bus)) {
                    view.setTicketImage(1, round);
                } else if (moveTD2.ticket.equals(Ticket.Underground)) {
                    view.setTicketImage(2, round);
                } else if (moveTD2.ticket.equals(Ticket.SecretMove)) {
                    view.setTicketImage(3, round);
                } else {

                }
            }
            else {

            }
        }
        for(PlayerInfo player: playerList)
        {
            if(player.getColour().equals(model.getCurrentPlayer()))
            {
                player.setMove(startMove);
            }
        }
        model.turn();

        view.setCurrentPlayer(model.getCurrentPlayer().toString());
        view.setCurrentPlayerLocation(model.getPlayerLocation(model.getCurrentPlayer()));
        view.setTaxiTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Taxi));
        view.setBusTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Bus));
        view.setUndergroundTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.Underground));
        view.setSecretTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.SecretMove));
        view.setDoubleTickets(model.getPlayerTickets(model.getCurrentPlayer(), Ticket.DoubleMove));
        view.setRoundNumber(model.getRound());
        view.possibleMoves.removeAllItems();
        String[] movesToPass = new String[model.validMoves(model.getCurrentPlayer()).size()];
        int i = 0;
        for (Move moves : model.validMoves(model.getCurrentPlayer())) {
            movesToPass[i] = i+". "+moves.toString();
            i++;
        }
        view.setPossibleMoves(movesToPass);
        passLocationsToView();
        view.updateImage();
    }

    // This retrieves the model to give to the controller.
    private void initialiseModel(int numberOfDetectives)
    {
        ModelInitialiser modelInitialiser = new ModelInitialiser(numberOfDetectives);
        model = modelInitialiser.returnModel();
        playerList = model.playerList;
    }

    // we gather all of the current locations and the colours of the players and give it to the view.
    // The view will then get the background rendered.
    private void passLocationsToView()
    {
        List<Integer> locationList = new ArrayList<Integer>(playerList.size());
        List<Colour> colourList = new ArrayList<Colour>(playerList.size());
        for(PlayerInfo player:model.playerList)
        {
            if(player.getColour().equals(Colour.Black)) {
                locationList.add(model.getPlayerLocation(Colour.Black));
            }
            else
            {
                locationList.add(player.getLocation());
            }
            colourList.add(player.getColour());
        }

        view.setLocationsAndColour(locationList,colourList);
    }
}
