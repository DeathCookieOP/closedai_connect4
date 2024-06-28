import org.w3c.dom.Node;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    static int ROWS = 6;
    static int COLUMNS = 7;
    static int[][] board = new int[ROWS][COLUMNS];
    static Scanner scanner = new Scanner(System.in); // Alejandro's Notes: Added this line

    public static void main(String[] args) { // removed this line: "throws InterruptedException"
        int choice = 0;

        System.out.println("Choose AI:\n1: Alpha-Beta Pruning\n2: Best First Search");
        System.out.print("Choice: ");
        choice = scanner.nextInt();

        do{
            switch (choice){
                case 1:

                    try {
                        emptyBoard();
                        printBoard();
                        boolean playerWin = false;
                        boolean aiWin = false;

                        while (true) {
                            playerWin = playerMove();
                            printBoard();
                            if (playerWin) {
                                System.out.println("Player is winner!");
                                break;
                            }

                            TimeUnit.SECONDS.sleep(1);
                            aiWin = aiMove();
                            printBoard();
                            if (aiWin) {
                                System.out.println("AI is winner!");
                                break;
                            }

                            if (fullBoard()) {
                                System.out.println("Full Board, No Winner!");
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        System.out.println("The game was interrupted!");
                    } finally {
                        scanner.close();
                    }
                    break;

                case 2:

                    System.out.println("Best-First Search");

                    try {
                        emptyBoard();
                        printBoard();
                        boolean playerWin = false;
                        boolean aiWin = false;

                        while (true) {
                            playerWin = playerMove();
                            printBoard();
                            if (playerWin) {
                                System.out.println("Player is winner!");
                                break;
                            }

                            TimeUnit.SECONDS.sleep(1);
                            aiWin = BestFirstAI();
                            printBoard();
                            if (aiWin) {
                                System.out.println("AI is winner!");
                                break;
                            }

                            if (fullBoard()) {
                                System.out.println("Full Board, No Winner!");
                                break;
                            }
                        }
                    } catch (InterruptedException e) {
                        System.out.println("The game was interrupted!");
                    } finally {
                        scanner.close();
                    }

                    break;
                default:
                    System.out.println("Invalid Choice.\nChoose AI:\n1: Alpha-Beta Pruning\n2: Best First Search");
                    System.out.print("Choice: ");
                    choice = scanner.nextInt();
            }

        }while(choice != 1 && choice != 2);

    }

    /**
     * The player's move
     * Updated this with a while loop to check to see if user input is valid
     * and if the column is full
     */
    public static boolean playerMove() {
        // Scanner scanner = new Scanner(System.in); Commented this line out
        System.out.print("Choose a column: ");
        int userMove = scanner.nextInt();

        while (userMove > COLUMNS || userMove < 1 || board[0][userMove - 1] != -1) {
            if (userMove <= COLUMNS && userMove >= 1) {
                System.out.println("Column is full, choose a different column");
            } else {
                System.out.println("Invalid column");
            }
            System.out.print("Choose a column: ");
            userMove = scanner.nextInt();
        }

        placeMove(userMove - 1, 1);
        return checkForWin(userMove - 1, 1);
    }
     /** The AI's move (currently just picks a random int) <--Previously
      * UPDATE: This handles the AI's move using the minimax algorithm
      * and places the move.
      * @return true if the AI wins after the move.
      * */

    public static boolean aiMove() {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int colm = 0; colm < COLUMNS; colm++) {
            if (board[0][colm] == -1) {
                int currentRow = getNextAvailableRow(colm);
                board[currentRow][colm] = 0;
                int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board[currentRow][colm] = -1;

//System.out.println("Evaluating Column: " + (col + 1) + " with score: " + score); Alejandro's Notes: this was added to fix error i was having

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = colm;
                }
            }
        }
        if (bestMove == -1) {
            bestMove = firstColumn();
        }

        System.out.println("AI chooses: " + (bestMove + 1));
        placeMove(bestMove, 0);
        return checkForWin(bestMove, 0);
    }

    /**
     * Minimax algorithm with alpha-beta pruning to evaluate board.
     * @param depth - depth of game tree
     * @param maximizingPlayer - is the current move maximizing?
     * @param alpha - alpha value
     * @param beta - beta value
     * @return the best score for the current player
     */
    public static int minimax(int depth, boolean maximizingPlayer, int alpha, int beta) {
        if (checkForWinState()) {
            return evaluateBoard();
        }

        if (depth == 8) {
            return evaluateBoard();
        }

        int bestScore = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int col = 0; col < COLUMNS; col++) {
            int row = getNextAvailableRow(col);
            if (row != -1) {
                board[row][col] = maximizingPlayer ? 0 : 1;
                int score = minimax(depth + 1, !maximizingPlayer, alpha, beta);
                board[row][col] = -1;

                if (maximizingPlayer) {
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, score);
                } else {
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, score);
                }

                if (beta <= alpha) {
                    break; // Alpha-beta pruning
                }
            }
        }

        return bestScore;
    }

    /**
     * This will evaluate the board and return a score
     * @return - score of board state
     */

    public static int evaluateBoard() {
        int score = 0;

        /* Aleandro's Note: four (4) for loops needed to check
        Horizontal, Vertical, Diagonal (slope) */
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                score += evalPosition(row, col, 0, 1);
            }
        }

        for (int col = 0; col < COLUMNS; col++) {
            for (int row = 0; row < ROWS - 3; row++) {
                score += evalPosition(row, col, 1, 0);
            }
        }

        for (int row = 0; row < ROWS - 3; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                score += evalPosition(row, col, 1, 1);
            }
        }

        for (int row = 3; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS - 3; col++) {
                score += evalPosition(row, col, -1, 1);
            }
        }

        return score;
    }

    /**
     *
     * @param row - Evaluates the row / column/
     * @param colm - ^^
     * @return - score for the position
     */
    private static int evalPosition(int row, int colm, int rowDelta, int colDelta) {
        int aiCount = 0;
        int playerCount = 0;
        int emptyCount = 0;

        for (int i = 0; i < 4; i++) {
            int cell = board[row + i * rowDelta][colm + i * colDelta];
            if (cell == 0) aiCount++;
            else if (cell == 1) playerCount++;
            else emptyCount++;
        }

        if (aiCount == 4) return 1000;
        if (playerCount == 4) return -1000;

        // Intermediate scores
        if (aiCount == 3 && emptyCount == 1) return 100;
        if (playerCount == 3 && emptyCount == 1) return -100;
        if (aiCount == 2 && emptyCount == 2) return 10;
        if (playerCount == 2 && emptyCount == 2) return -10;

        return aiCount - playerCount;
    }
    /**
     * Prints the board to the console
     * remove printBoard(int[][] board to printBoard()
     */
    public static void printBoard() {
        for (int i = 0; i < ROWS; i++) {
            System.out.print("| ");
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] == 0) System.out.print("o | ");
                else if (board[i][j] == 1) System.out.print("x | ");
                else System.out.print("- | ");
            }
            System.out.println();
        }
        System.out.println("\n  1   2   3   4   5   6   7 ");
    }



    /**
     * Empties the board by setting all items to -1.
     * Alejandro's Notes: removed emptyBoard(int[][] board) to emptyBoard()
     */
    public static void emptyBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = -1;
            }
        }
    }

    /**
     * Places the user's move
     *
     * @param column    is the column where the most recent piece was placed
     * @param user          is the user who played the move to see if their piece won
     */
    public static void placeMove(int column, int user) {
        //because user's input will be off by 1
      //  if (user == 1) column -= 1; Alejandro's Notes: Commented this line out

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
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][column] == -1) {
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
    public static boolean checkForWin(int columnPlaced, int user) {
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
    private static boolean line(int row, int colm, int player, int rowDelta, int colmDelta) {
        int count = 0;
        for (int i = -3; i <= 3; i++) {
            int newRow = row + i * rowDelta;
            int newCol = colm + i * colmDelta;
            if (newRow >= 0 && newRow < ROWS && newCol >= 0 && newCol < COLUMNS && board[newRow][newCol] == player) {
                count++;
                if (count == 4) return true;
            } else {
                count = 0;
            }
        }
        return false;
    }

    /**
     * finds the first column for placing a move
     * @return - the index of the first available column
     */
    private static int firstColumn() {
        for (int colm = 0; colm < COLUMNS; colm++) {
            if (board[0][colm] == -1) {
                return colm;
            }
        }
        return -1;
    }



    /**
     * Check if board is full by checking for any -1(empty) in board
     * Did not make any changes - AC
     */
    public static boolean fullBoard(){
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(board[i][j] == -1){ //ALEJANDRO'S NOTES CHANGED FROM == TO !=
                    return false;   //end check at first empty space
                }
            }
        }

        return true;    //no empty space found
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
     * Checks to see if there is a diagonal win
     *
     * @param columnPlaced  is the column where the most recent piece was placed
     *
     * @return          true if the most recent move resulted in a diagonal win
     */
    public static boolean checkDiagonalWin(int columnPlaced) {


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

    private static boolean checkForWinState() {
        for (int colm = 0; colm < COLUMNS; colm++) {
            int row = getNextAvailableRow(colm);
            if (row != -1){
                board[row][colm] = 0;
                boolean aiWin = checkForWin(colm, 0);
                board[row][colm] = -1;
                boolean playerWin = checkForWin(colm, 1);
                board[row][colm] = -1;
                if (aiWin || playerWin) return true;
            }
        }
        return false;
    }

/*----------------------------------------------------------------------*/

    public static boolean BestFirstAI() {

        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int colm = 0; colm < COLUMNS; colm++) {
            if (board[0][colm] == -1) {
                int currentRow = getNextAvailableRow(colm);
                board[currentRow][colm] = 0;    //play test move

                int score = bestFirstSearch();
                board[currentRow][colm] = -1;   //remove move

                if (score > bestScore) {    //record score if higher
                    bestScore = score;
                    bestMove = colm;
                }
            }
        }
        if (bestMove == -1) {           //if no valid move found
            bestMove = firstColumn();
        }

        System.out.println("AI chooses: " + (bestMove + 1));
        placeMove(bestMove, 0);
        return checkForWin(bestMove, 0);

    }

    public static int bestFirstSearch() {

        PriorityQueue<Integer> open = new PriorityQueue<>((a, b) -> b - a); // max-heap, largest to smallest





        return 0; // No winning move found
    }


}
