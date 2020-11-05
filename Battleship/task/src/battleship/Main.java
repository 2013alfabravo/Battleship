package battleship;

import battleship.controller.Game;
import battleship.model.Player;
import battleship.model.Ship;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Ship> ships1 = List.of(
                new Ship("Aircraft Carrier", 5),
                new Ship("Battleship", 4),
                new Ship("Submarine", 3),
                new Ship("Cruiser", 3),
                new Ship("Destroyer", 2)
        );

        List<Ship> ships2 = List.of(
                new Ship("Aircraft Carrier", 5),
                new Ship("Battleship", 4),
                new Ship("Submarine", 3),
                new Ship("Cruiser", 3),
                new Ship("Destroyer", 2)
        );

        Player playerOne = new Player("Player 1");
        Player playerTwo = new Player("Player 2");

        new Game(playerOne, playerTwo, ships1, ships2).run();
    }
}
