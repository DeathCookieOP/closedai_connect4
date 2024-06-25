/**
 * INFO ABOUT CLASS
 *
 * Date Created: 06/24/2024
 * Date Last Updated: 06/24/2024
 * */

import java.util.Scanner;

public class Player {

    Board board;
    static final int ROWS = 6;
    static final int COLUMNS = 7;
    static Scanner scanner = new Scanner(System.in); // Alejandro's Notes: Added this line

    public Player(Board board) {
        this.board = board;
    }

    /**
     * The player's move
     * Updated this with a while loop to check to see if user input is valid
     * and if the column is full
     */
    public boolean playerMove() {
        // Scanner scanner = new Scanner(System.in); Commented this line out
        System.out.print("Choose a column: ");
        int userMove = scanner.nextInt();

        //was board[0][userMove - 1]
        while (userMove > COLUMNS || userMove < 1 || board.getSquareValue(0, userMove - 1) != -1) {
            if (userMove <= COLUMNS && userMove >= 1) {
                System.out.println("Column is full, choose a different column");
            } else {
                System.out.println("Invalid column");
            }
            System.out.print("Choose a column: ");
            userMove = scanner.nextInt();
        }

        board.placeMove(userMove - 1, 1);
        return board.checkForWin(userMove - 1, 1);
    }


}
