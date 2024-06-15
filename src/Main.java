import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

    static int[][] board = new int[6][7];

    public static void main(String[] args) throws InterruptedException {

        emptyBoard(board);
        printBoard(board);
        boolean playerWin = false, aiWin = false;
        while (true) {
            playerWin = playerMove();
            printBoard(board);
            if (playerWin) {
                System.out.println("Player is winner!");
                break;
            }
            TimeUnit.SECONDS.sleep(1);
            aiWin = aiMove();
            printBoard(board);
            if (aiWin) {
                System.out.println("AI is winner!");
                break;
            }
        }
    }


    /**
     * The player's move
     */
    public static boolean playerMove() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Choose a column: ");
        int userMove = scanner.nextInt();

        //checks to see if user input is valid &
        //checks to see if column is full
        do {
            if (userMove > 7 || userMove < 1) {
                System.out.println("invalid column");
                System.out.print("Choose a column: ");
                userMove = scanner.nextInt();
            } else if (board[0][userMove - 1] != -1) {
                System.out.println("Column is full, choose a different row");
                userMove = scanner.nextInt();
            }
        } while ((userMove > 7 || userMove < 1) || board[0][userMove - 1] != -1);

        //1 cuz its userMove, so make it an x
        placeMove(userMove, 1);

        //check for win
        return checkForWin(userMove, 1);
    }

    /**
     * The AI's move (currently just picks a random int)
     */
    public static boolean aiMove() {
        Random random = new Random();
        int userMove = random.nextInt(7); //gets a random number 0-5

        do {
            if (board[0][userMove] != -1) userMove = random.nextInt(7);
        } while (board[0][userMove] != -1);
        System.out.println("AI chooses: " + (userMove + 1));

        placeMove(userMove, 0);

        return checkForWin(userMove, 0);
    }

    /**
     * Places the user's move
     *
     * @param column    is the column where the most recent piece was placed
     * @param user          is the user who played the move to see if their piece won
     */
    public static void placeMove(int column, int user) {
        //because user's input will be off by 1
        if (user == 1) column -= 1;

        int nextRow = getNextAvailableRow(column);
        board[nextRow][column] = user;

    }

    /**
     * Gets the next available row
     *
     * @param column    is the column where the most recent piece was placed
     *
     * @return          integer of the next available row
     */
    public static int getNextAvailableRow(int column) {
        int i = board.length - 1;

        while (i >= 0) {
            //move up a row if current row is taken
            if (board[i][column] != -1) {
                i = i - 1;
            } else {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks to see if there is a win
     *
     * @param columnPlaced  is the column where the most recent piece was placed
     * @param user          is the user who played the move to see if their piece won
     *
     * @return          true if there was any type of win
     */
    public static boolean checkForWin(int columnPlaced, int user) {
        if (user == 1) columnPlaced -= 1;

        return efficientVerticalWin(columnPlaced) ||
                efficientHorizontalWin(columnPlaced) ||
                checkDiagonalWin(columnPlaced);
    }

    /**
     * Prints the board to the console
     */
    public static void printBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            System.out.print("| ");
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 0) System.out.print("o | ");
                else if (board[i][j] == 1) System.out.print("x | ");
                else System.out.print("- | ");

            }
            System.out.println();
        }

        System.out.println("\n  1   2   3   4   5   6   7 ");
        System.out.println();
    }

    /**
     * Empties the board by setting all items to -1
     */
    public static void emptyBoard(int[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = -1;
            }
        }
    }

    /**
     * Checks to see if there is a vertical win
     *
     * @param columnPlaced  is the column where the most recent piece was placed
     *
     * @return          true if the most recent move resulted in a vertical win
     */
    public static boolean efficientVerticalWin(int columnPlaced) {
        /*
        //+1 cuz the last used row was +1 ahead
        int rowPlaced = getNextAvailableRow(columnPlaced) + 1;
        // <= 3 because that will mean that there is 4 columns under it that have been used up
        if (rowPlaced <= 3) {
            if (allEqual(board[rowPlaced][columnPlaced],
                    board[rowPlaced + 1][columnPlaced],
                    board[rowPlaced + 2][columnPlaced],
                    board[rowPlaced + 3][columnPlaced])) {
                return true;
            }
        }

        return false;
         */

        int ROWS = 6;
        int COLUMNS = 7;

        //last check at 4th row
        for (int colm = 0; colm < COLUMNS; colm++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if(board[row][colm] != -1 &&
                        allEqual(board[row][colm],
                                board[row + 1][colm],
                                board[row + 2][colm],
                                board[row + 3][colm])){
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Checks to see if there is a horizontal win
     *
     * @param columnPlaced  is the column where the most recent piece was placed
     *
     * @return          true if the most recent move resulted in a horizontal win
     */
    public static boolean efficientHorizontalWin(int columnPlaced) {
        /*
        //starts from 0
        int colFromR = board[0].length - 1 - columnPlaced;
        int currRow = getNextAvailableRow(columnPlaced) + 1;

        //start at 5 cuz colPlaced is 0-5
        switch (colFromR) {
            case 5: //means all the way on the left //x - - - - -
                return allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced + 1],
                        board[currRow][columnPlaced + 2], board[currRow][columnPlaced + 3]);
            case 4: // - x - - - - => x x x x - - || - x x x x - -
                return allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced + 1],
                        board[currRow][columnPlaced + 2], board[currRow][columnPlaced + 3]) ||
                        allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 1],
                                board[currRow][columnPlaced + 1], board[currRow][columnPlaced + 2]);
            case 3: // - - x - - - => x x x x - - || - x x x x - || - - x x x x
                return (allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 2],
                        board[currRow][columnPlaced - 1], board[currRow][columnPlaced + 1]) ||
                        allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 1],
                                board[currRow][columnPlaced + 1], board[currRow][columnPlaced + 2])) ||
                        allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced + 1],
                                board[currRow][columnPlaced + 2], board[currRow][columnPlaced + 3]);
            case 2: // - - - x - - => x x x x - - || - x x x x - || - - x x x x
                return (allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 1],
                        board[currRow][columnPlaced - 2], board[currRow][columnPlaced - 3]) ||
                        allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 2],
                                board[currRow][columnPlaced - 1], board[currRow][columnPlaced + 1])) ||
                        allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 1],
                                board[currRow][columnPlaced + 1], board[currRow][columnPlaced + 2]);
            case 1: // - - - - x - => - x x x x - || - - x x x x
                return allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 1],
                        board[currRow][columnPlaced - 2], board[currRow][columnPlaced - 3]) ||
                        allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 2],
                                board[currRow][columnPlaced - 1], board[currRow][columnPlaced + 1]);
            case 0: // - - - - - x => - - x x x x
                return allEqual(board[currRow][columnPlaced], board[currRow][columnPlaced - 1],
                        board[currRow][columnPlaced - 2], board[currRow][columnPlaced - 3]);
        }

        return false;
        */

        int ROWS = 6;
        int COLUMNS = 7;

        //last check at 3rd column
        for (int row = 0; row < ROWS; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (board[row][colm] != -1 &&
                        allEqual(board[row][colm],
                                board[row][colm + 1],
                                board[row][colm + 2],
                                board[row][colm + 3])) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Checks to see if there is a digaonal win
     *
     * @param columnPlaced  is the column where the most recent piece was placed
     *
     * @return          true if the most recent move resulted in a diagonal win
     */
    public static boolean checkDiagonalWin(int columnPlaced) {
        /*
        //only check for where the current one was place and check it's four diagonals
        int colFromR = board[0].length - 1 - columnPlaced;
        int currRow = getNextAvailableRow(columnPlaced) + 1;
        */

        int ROWS = 6;
        int COLUMNS = 7;

        //Check diagonal (bottom left to top right) /
        //start at 4th row, last check at 3rd column
        for (int row = 3; row < ROWS; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (board[row][colm] != -1 &&
                        allEqual(board[row][colm],
                                board[row - 1][colm + 1],
                                board[row - 2][colm + 2],
                                board[row - 3][colm + 3])) {
                    return true;
                }
            }
        }

        // Check diagonal (top left to bottom right) \
        //last check at 4th row, last check at 3rd column
        for (int row = 0; row < ROWS - 3; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (board[row][colm] != -1 &&
                        allEqual(board[row][colm],
                                board[row + 1][colm + 1],
                                board[row + 2][colm + 2],
                                board[row + 3][colm + 3])) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Checks to see if all the given values are the same
     *
     * @param targetVal  is the target value
     * @param otherVals  are the values that will be checked to see if same
     *
     * @return          true if all the given values are equal
     */
    public static boolean allEqual(int targetVal, int... otherVals) {
        for (int values : otherVals) {
            if (!(values == targetVal)) return false;
        }

        return true;
    }


}
