package battleship.controller;

import battleship.model.Player;
import battleship.model.Ship;
import battleship.view.UI;
import java.util.ArrayList;
import java.util.List;

enum GameState { RUNNING, PLAYER1_WIN, PLAYER2_WIN }

public class Game {
    private final UI ui = new UI();
    private final Player playerOne;
    private final Player playerTwo;
    private final List<Ship> playerOneShips;
    private final List<Ship> playerTwoShips;
    private Player activePlayer = null;
    private final Field playerOneField = new Field();
    private final Field playerTwoField = new Field();
    private Field activePlayerField = null;
    private Field inactivePlayerField = null;
    private GameState status = GameState.RUNNING;

    public Game(Player playerOne, Player playerTwo, List<Ship> playerOneShips, List<Ship> playerTwoShips) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOneShips = playerOneShips;
        this.playerTwoShips = playerTwoShips;
    }

    public void run() {
        ui.print(playerOne.name + ", place your ships to the game field\n");
        placeShips(playerOneShips, playerOneField);
        ui.readInput("\nPress Enter and pass the move to another player...", "", "");
        ui.print(playerTwo.name + ", place your ships to the game field\n");
        placeShips(playerTwoShips, playerTwoField);

        ui.readInput("\nPress Enter and pass the move to another player...", "", "");
        activePlayer = playerOne;
        activePlayerField = playerOneField;
        inactivePlayerField = playerTwoField;
        String prompt = ", It's your turn:\n\n> ";
        String format = "[A-J]([1-9]|10)";
        String error = "\nError! You entered the wrong coordinates! Try again:";

        while (status != GameState.PLAYER1_WIN && status != GameState.PLAYER2_WIN) {
            inactivePlayerField.setFogOfWar(true);
            activePlayerField.setFogOfWar(false);

            ui.print(inactivePlayerField.getFieldImage());
            ui.print("-".repeat(21));
            ui.print(activePlayerField.getFieldImage());

            String input = ui.readInput("\n" + activePlayer.name + prompt, format, error);
            List<Integer> coordinates = parseCoordinates(input);

            ShootResult shot = inactivePlayerField.landShot(coordinates);

            switch (shot) {
                case REPEATED_HIT:
                case HIT:
                    ui.print("You hit a ship!");
                    break;
                case MISS:
                    ui.print("You missed!");
                    break;
                case SINK:
                    if (inactivePlayerField.ships.isEmpty()) {
                        activePlayer.setIsWinner();
                    } else {
                        ui.print("You sank a ship!");
                    }
                    break;
            }

            validateGameState();

            if (status == GameState.RUNNING) {
                ui.readInput("Press Enter and pass the move to another player...", "", "");
                switchPlayers();
            }
        }

        ui.print("You sank the last ship. You won. Congratulations!");
    }

    private void validateGameState() {
        if (playerOne.isWinner()) {
            status = GameState.PLAYER1_WIN;
        } else if (playerTwo.isWinner()) {
            status = GameState.PLAYER2_WIN;
        } else {
            status = GameState.RUNNING;
        }
    }

    private void switchPlayers() {
        if (activePlayer == playerOne) {
            activePlayer = playerTwo;
            activePlayerField = playerTwoField;
            inactivePlayerField = playerOneField;
        } else {
            activePlayer = playerOne;
            activePlayerField = playerOneField;
            inactivePlayerField = playerTwoField;
        }
    }

    private void placeShips(List<Ship> ships, Field field) {
        ui.print(field.getFieldImage());

        for (Ship ship : ships) {
            String input;
            List<Integer> coordinates;
            String prompt = "\nEnter the coordinates of the " + ship.name + " (" + ship.size + " cells):\n\n> ";
            String error = "Error! Wrong ship location! Try again:";
            String format = "[A-J]([1-9]|10)\\s[A-J]([1-9]|10)";

            while (true) {
                input = ui.readInput(prompt, format, error);
                coordinates = parseCoordinates(input);
                PlacementResult result = field.deployShip(ship, coordinates);

                if (result == PlacementResult.SUCCESS) {
                    ui.print(field.getFieldImage());
                    break;
                }

                switch (result) {
                    case WRONG_COORDINATES:
                        ui.print("Error! Wrong ship location! Try again:");
                        break;
                    case WRONG_SIZE:
                        ui.print("Error! Wrong length of the " + ship.name + "! Try again:");
                        break;
                    case TOO_CLOSE:
                        ui.print("Error! You placed it too close to another one. Try again:");
                        break;
                }
            }
        }
    }

    private static List<Integer> parseCoordinates(String input) {
        String[] tokens = input.split(" ");
        List<Integer> coordinates = new ArrayList<>();
        for (String token : tokens) {
            coordinates.add(Integer.parseInt(token.substring(1)) - 1);
            coordinates.add(token.charAt(0) - 'A');
        }
        return coordinates;
    }
}
