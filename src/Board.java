/**
 * INFO ABOUT CLASS
 *
 * Date Created: 06/11/2024
 * Date Last Updated: 06/24/2024
 * */

public class Board {

    static final int ROWS = 6;
    static final int COLUMNS = 7;
    private Square[][] board;


    public Board() {
        board = new Square[ROWS][COLUMNS];
        emptyBoard();
    }


    /**
     * Prints the board to the console
     * remove printBoard(int[][] board to printBoard()
     */
    public  void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            System.out.print("| ");
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j].getUser() == 0) System.out.print("o | ");
                else if (board[i][j].getUser() == 1) System.out.print("x | ");
                else System.out.print("- | ");
            }
            System.out.println();
        }
        //System.out.println("\n  1   2   3   4   5   6   7 "); //wrongly printing
    }

    /**
     * Empties the board by setting all items to -1.
     * Alejandro's Notes: removed emptyBoard(int[][] board) to emptyBoard()
     */
    public void emptyBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = new Square(-1);
            }
        }
    }

    /**
     * Places the user's move
     *
     * @param column    is the column where the most recent piece was placed
     * @param user          is the user who played the move to see if their piece won
     */
    public void placeMove(int column, int user) {
        //because user's input will be off by 1
        //  if (user == 1) column -= 1; Alejandro's Notes: Commented this line out

        int nextRow = getNextAvailableRow(column);
        board[nextRow][column].setUser(user);

    }

    /**
     * Gets the Square object for a specific area on the board
     *
     * @param row   Row for the object
     * @param col   Col for the obj
     *
     * @return Returns the Square object
     * */
    public Square getSquare(int row, int col) {
        return board[row][col];
    }

    /**
     * Gets the value for a specific area on the board
     *
     * @param row   Row for the object
     * @param col   Col for the obj
     *
     * @return Returns the Square object's value
     * */
    public int getSquareValue(int row, int col) {
        return board[row][col].getUser();
    }

    /**
     * Check if board is full by checking for any -1(empty) in board
     * Did not make any changes - AC
     */
    public boolean fullBoard(){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(board[i][j].getUser() == -1){ //ALEJANDRO'S NOTES CHANGED FROM == TO !=
                    return false;   //end check at first empty space
                }
            }
        }

        return true;    //no empty space found
    }

    /**
     * Converts the table to a viewable form
     @ -14,7 +108,249 @@ public class Board {
     * @return Table in console
     * */
    public String toString() {
        return "This is the board. User printBoard() to see board.";
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
            if (getSquareValue(i, column) == -1) {
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
                    && newCol < COLUMNS && getSquareValue(newRow, newCol) == player) {
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

        //last check at 4th row
        for (int colm = 0; colm < COLUMNS; colm++) {
            for (int row = 0; row < ROWS - 3; row++) {
                if(getSquareValue(row,colm) != -1 &&
                        allEqual(getSquareValue(row,colm),
                                getSquareValue(row+1,colm),
                                getSquareValue(row+2,colm),
                                getSquareValue(row+3,colm))){
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

        //last check at 3rd column
        for (int row = 0; row < ROWS; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (getSquareValue(row,colm) != -1 &&
                        allEqual(getSquareValue(row,colm),
                                getSquareValue(row,colm+1),
                                getSquareValue(row,colm+2),
                                getSquareValue(row,colm+3))) {
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

        for (int row = 3; row < ROWS; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (getSquareValue(row,colm) != -1 &&
                        allEqual(getSquareValue(row,colm),
                                getSquareValue(row - 1,colm + 1),
                                getSquareValue(row - 2,colm + 2),
                                getSquareValue(row - 3,colm + 3))) {
                    return true;
                }
            }
        }

        // Check diagonal (top left to bottom right) \
        //last check at 4th row, last check at 3rd column
        for (int row = 0; row < ROWS - 3; row++) {
            for (int colm = 0; colm < COLUMNS - 3; colm++) {
                if (getSquareValue(row,colm) != -1 &&
                        allEqual(getSquareValue(row,colm),
                                getSquareValue(row+1,colm+1),
                                getSquareValue(row+2,colm+2),
                                getSquareValue(row+3,colm+3))) {
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

    boolean checkForWinState() {
        for (int colm = 0; colm < COLUMNS; colm++) {
            int row = getNextAvailableRow(colm);
            if (row != -1){
                board[row][colm].setUser(0);
                boolean aiWin = checkForWin(colm, 0);
                board[row][colm].setUser(-1);
                boolean playerWin = checkForWin(colm, 1);
                board[row][colm].setUser(-1);
                if (aiWin || playerWin) return true;
            }
        }
        return false;
    }
}