/**
 * INFO ABOUT CLASS
 *
 * Date Created: 06/11/2024
 * Date Last Updated: 06/24/2024
 * */

public class Square {

    int heuristic;
    int user;
    int row;
    int col;

    public Square() {
        this.user = user;
        this.heuristic = -1;
    }

    public Square(int user) {
        this.user = user;
        this.heuristic = -1;
    }

    public Square(int row, int col, int user) {
        this.user = user;
        this.heuristic = -1;
        this.col = col;
        this.row = row;
    }


    public int getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
