package battleship.model;

public class Cell {
    public final int x;
    public final int y;
    private boolean isHidden = false;
    private boolean isOccupied = false;
    private boolean isHit = false;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setOccupied(boolean occupied) {
        isOccupied = occupied;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
        isHidden = false;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden && !isHit;
    }

}
