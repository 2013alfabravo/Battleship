package battleship.model;

public class Ship {
    private static int counter = 0;
    public final int id = counter++;
    public final String name;
    public final int size;
    private Cell[] body = null;
    private int hp;
    private boolean isSunk = false;

    public Ship(String name, int size) {
        this.name = name;
        this.size = size;
        hp = size;
    }

    public void setBody(Cell[] body) {
        this.body = body;
        for (Cell cell : this.body) cell.setOccupied(true);
    }

    public Cell[] getBody() {
        return body;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int value) {
        if (isSunk) return;
        hp = value;
        if (hp < 1) isSunk = true;
    }

    public boolean isSunk() {
        return isSunk;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Ship && this.id == ((Ship) obj).id;
    }
}
