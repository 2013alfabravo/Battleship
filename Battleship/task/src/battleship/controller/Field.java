package battleship.controller;

import battleship.model.Cell;
import battleship.model.Ship;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum PlacementResult { SUCCESS, WRONG_COORDINATES, WRONG_SIZE, TOO_CLOSE }
enum ShootResult { HIT, MISS, SINK, REPEATED_HIT }

public class Field {
    private final int SIZE = 10;
    private final Cell[][] field = new Cell[SIZE][SIZE];
    public final List<Ship> ships = new ArrayList<>();

    public Field() {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                field[i][j] = new Cell(j, i);
            }
        }
    }

    public String getFieldImage() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < field.length + 1; i++) {
            for (int j = 0; j < field[0].length + 1; j++) {
                if (i == 0 && j == 0) {
                    sb.append(" ");
                } else if (i == 0) {
                    sb.append(" ").append(j);
                } else if (j == 0) {
                    sb.append((char) ('A' + i - 1));
                } else {
                    sb.append(" ").append(createCellImage(field[i - 1][j - 1]));
                }
            }
            if (i < field.length) sb.append("\n");
        }
        return sb.toString();
    }

    private char createCellImage(Cell cell) {
        char img = '~';
        if (cell.isOccupied()) img = 'O';
        if (cell.isHit() && cell.isOccupied()) {
            img = 'X';
        } else if (cell.isHit()) {
             img = 'M';
        }

        if (cell.isHidden()) {
            return '~';
        } else {
            return img;
        }
    }

    public void setFogOfWar(boolean flag) {
        Arrays.stream(field).flatMap(Arrays::stream).forEach(it -> it.setHidden(flag));
    }

    public PlacementResult deployShip(Ship ship, List<Integer> coordinates) {
        int x1 = coordinates.get(0);
        int y1 = coordinates.get(1);
        int x2 = coordinates.get(2);
        int y2 = coordinates.get(3);

        if (y1 != y2 && x1 != x2) return PlacementResult.WRONG_COORDINATES;

        if (y1 == y2 && Math.abs(x1 - x2) != ship.size - 1 || x1 == x2 && Math.abs(y1 - y2) != ship.size - 1) {
            return PlacementResult.WRONG_SIZE;
        }

        boolean vertical = x1 == x2;
        Cell[] body = new Cell[ship.size];
        int start;

        if (vertical) {
            start = Math.min(y1, y2);
        } else {
            start = Math.min(x1, x2);
        }

        for (int i = 0; i < ship.size; i++) {
            if (vertical) {
                if (isAvailableCell(field[start + i][x1])) {
                    body[i] = field[start + i][x1];
                } else {
                    return PlacementResult.TOO_CLOSE;
                }
            } else {
                if (isAvailableCell(field[y1][start + i])) {
                    body[i] = field[y1][start + i];
                } else {
                    return PlacementResult.TOO_CLOSE;
                }
            }
        }

        ship.setBody(body);
        ships.add(ship);
        return PlacementResult.SUCCESS;
    }

    public ShootResult landShot(List<Integer> coordinates) {
        int x = coordinates.get(0);
        int y = coordinates.get(1);

        if (field[y][x].isHit()) return ShootResult.REPEATED_HIT;

        field[y][x].setHit(true);
        if (field[y][x].isOccupied()) {
            Ship ship = getShip(x, y);
            if (ship != null) {
                ship.setHp(ship.getHp() - 1);
                if (ship.isSunk()) {
                    ships.remove(ship);
                    return ShootResult.SINK;
                }
            }
            return ShootResult.HIT;
        }
        return ShootResult.MISS;
    }

    private Ship getShip(int x, int y) {
        for (Ship ship : ships) for (Cell cell : ship.getBody()) if (cell.x == x && cell.y == y) return ship;
        return null;
    }

    private boolean isAvailableCell(Cell cell) {
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                int x = cell.x + j;
                int y = cell.y + i;
                if (x < 0 || y < 0 || x > SIZE - 1 || y > SIZE - 1) continue;
                if (field[y][x].isOccupied()) return false;
            }
        }
        return true;
    }
}
