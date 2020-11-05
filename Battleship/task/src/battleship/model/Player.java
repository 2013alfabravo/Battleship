package battleship.model;

enum PlayerStatus { WINNER, UNDECIDED }

public class Player {
    public final String name;
    private PlayerStatus isWinner = PlayerStatus.UNDECIDED;

    public Player(String name) {
        this.name = name;
    }

    public void setIsWinner() {
        this.isWinner = PlayerStatus.WINNER;
    }

    public boolean isWinner() {
        return isWinner == PlayerStatus.WINNER;
    }
}
