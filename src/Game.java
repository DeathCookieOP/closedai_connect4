import java.util.concurrent.TimeUnit;

/**
 * INFO ABOUT CLASS
 *
 * Date Created: 06/11/2024
 * Date Last Updated: 06/24/2024
 * */

public class Game {

    static final int ROWS = 6;
    static final int COLUMNS = 7;
    Board board;
    Player realPlayer;
    AIPlayer aiPlayer;

    public Game() {
        board = new Board();
        realPlayer = new Player(board);
        aiPlayer = new AIPlayer(board);
    }

    public void startGame() {
        try {
            board.printBoard();
            boolean playerWin = false;
            boolean aiWin = false;

            while (true) {
                realPlayer = new Player(board);
                aiPlayer = new AIPlayer(board);
                playerWin = realPlayer.playerMove();
                board.printBoard();
                if (playerWin) {
                    System.out.println("Player is winner!");
                    break;
                }

                TimeUnit.SECONDS.sleep(1);
                aiWin = aiPlayer.aiMove();
                board.printBoard();
                if (aiWin) {
                    System.out.println("AI is winner!");
                    break;
                }

                if (board.fullBoard()) {
                    System.out.println("Full Board, No Winner!");
                    break;
                }
            }
        } catch (InterruptedException e) {
            System.out.println("The game was interrupted!");
        }

    }

    public Board getBoard() {
        return board;
    }

    public int nextPlayer(int currPlayer) {
        return currPlayer == 0 ? 1 : 0;
    }

    /**
     * Gets the next available row
     *
     * @param column    is the column where the most recent piece was placed
     *
     * @return          integer of the next available row
     */
    public  int getNextAvailableRow(int column) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board.getSquareValue(i, column) == -1) {
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
     * Alejandro's Notes: removed if function and added int row to get where the move
     * was placed for vertical, horizontal and diagonal.
     */
    public  boolean checkForWin(int columnPlaced, int user) {
        int row = getNextAvailableRow(columnPlaced) + 1;
        return line(row, columnPlaced, user, 1, 0) ||
                line(row, columnPlaced, user, 0, 1) ||
                line(row, columnPlaced, user, 1, 1) ||
                line(row, columnPlaced, user, 1, -1);
    }

    /**
     * This method helps check the line of the direction for a win
     * @param row - in the name, checks the row.
     * @param colm - same as above
     * @param player  identifies player or AI
     * @param rowDelta - same as above
     * @param colmDelta - same as above
     * @return - return a true if a win is detected in the line
     */
    private  boolean line(int row, int colm, int player, int rowDelta, int colmDelta) {
        int count = 0;
        for (int i = -3; i <= 3; i++) {
            int newRow = row + i * rowDelta;
            int newCol = colm + i * colmDelta;
            if (newRow >= 0 && newRow < ROWS && newCol >= 0
                    && newCol < COLUMNS && board.getSquareValue(newRow, newCol) == player) {
                count++;
                if (count == 4) return true;
            } else {
                count = 0;
            }
        }
        return false;
    }

    /**
     * Checks to see if there is a vertical win
     *
     * @param columnPlaced  is the column where the most recent piece was placed
     *
     * @return          true if the most recent move resulted in a vertical win
     */
    public boolean efficientVerticalWin(int columnPlaced) {
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

        //last check at 4th row
        for (int colm = 0; colm < COLUMNS; colm++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if(board.getSquareValue(row,colm) != -1 &&
                        allEqual(board.getSquareValue(row,colm),
                                board.getSquareValue(row+1,colm),
                                board.getSquareValue(row+2,colm),
                                board.getSquareValue(row+3,colm))){
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
    public  boolean efficientHorizontalWin(int columnPlaced) {
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

        //last check at 3rd column
        for (int row = 0; row < ROWS; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (board.getSquareValue(row,colm) != -1 &&
                        allEqual(board.getSquareValue(row,colm),
                                board.getSquareValue(row,colm+1),
                                board.getSquareValue(row,colm+2),
                                board.getSquareValue(row,colm+3))) {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Checks to see if there is a diagonal win
     *
     * @param columnPlaced  is the column where the most recent piece was placed
     *
     * @return          true if the most recent move resulted in a diagonal win
     */
    public boolean checkDiagonalWin(int columnPlaced) {
        /*
        //only check for where the current one was place and check it's four diagonals
        int colFromR = board[0].length - 1 - columnPlaced;
        int currRow = getNextAvailableRow(columnPlaced) + 1;
        */

        //Check diagonal (bottom left to top right) /
        //start at 4th row, last check at 3rd column
        for (int row = 3; row < ROWS; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (board.getSquareValue(row,colm) != -1 &&
                        allEqual(board.getSquareValue(row,colm),
                                board.getSquareValue(row - 1,colm + 1),
                                board.getSquareValue(row - 2,colm + 2),
                                board.getSquareValue(row - 3,colm + 3))) {
                    return true;
                }
            }
        }

        // Check diagonal (top left to bottom right) \
        //last check at 4th row, last check at 3rd column
        for (int row = 0; row < ROWS - 3; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (board.getSquareValue(row,colm) != -1 &&
                        allEqual(board.getSquareValue(row,colm),
                                board.getSquareValue(row+1,colm+1),
                                board.getSquareValue(row+2,colm+2),
                                board.getSquareValue(row+3,colm+3))) {
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
    public boolean allEqual(int targetVal, int... otherVals) {
        for (int values : otherVals) {
            if (!(values == targetVal)) return false;
        }

        return true;
    }
}
