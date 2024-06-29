import java.util.PriorityQueue;

/**
 * INFO ABOUT CLASS
 *
 * Date Created: 06/24/2024
 * Date Last Updated: 06/24/2024
 * */

public class AIPlayer {

    Board board;
    static final int ROWS = 6;
    static final int COLUMNS = 7;

    public AIPlayer(Board board) {
        this.board = board;
    }

    public boolean aiMove() {
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;

        for (int colm = 0; colm < COLUMNS; colm++) {
            if (board.getSquareValue(0, colm) == -1) {
                int currentRow = getNextAvailableRow(colm);
                board.getSquare(currentRow, colm).setUser(0);
                int score = minimax(0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
                board.getSquare(currentRow, colm).setUser(-1);

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

        System.out.println("\nAI chooses: " + (bestMove + 1));
        board.placeMove(bestMove, 0);
        return board.checkForWin(bestMove, 0);
    }

    /**
     * finds the first column for placing a move
     * @return - the index of the first available column
     */
    private int firstColumn() {
        for (int colm = 0; colm < COLUMNS; colm++) {
            if (board.getSquareValue(0,colm) == -1) {
                return colm;
            }
        }
        return -1;
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
     * Minimax algorithm with alpha-beta pruning to evaluate board.
     * @param depth - depth of game tree
     * @param maximizingPlayer - is the current move maximizing?
     * @param alpha - alpha value
     * @param beta - beta value
     * @return the best score for the current player
     */
    public int minimax(int depth, boolean maximizingPlayer, int alpha, int beta) {
        if (board.checkForWinState()) {
            return evaluateBoard();
        }

        if (depth == 8) {
            return evaluateBoard();
        }

        int bestScore = maximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int col = 0; col < COLUMNS; col++) {
            int row = getNextAvailableRow(col);
            if (row != -1) {
                board.getSquare(row, col).setUser(maximizingPlayer ? 0 : 1);
                int score = minimax(depth + 1, !maximizingPlayer, alpha, beta);
                board.getSquare(row, col).setUser(-1);

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

    public int evaluateBoard() {
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
    private int evalPosition(int row, int colm, int rowDelta, int colDelta) {
        int aiCount = 0;
        int playerCount = 0;
        int emptyCount = 0;

        for (int i = 0; i < 4; i++) {
            int cell = board.getSquareValue(row + i * rowDelta,colm + i * colDelta);
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

    /*---------------------------------------------------------------*/

    public boolean bestFirstAI() {

        PriorityQueue<node> bestFirst = new PriorityQueue<>((a, b) -> b.score - a.score); // Max-heap, largest to smallest

        for (int colm = 0; colm < COLUMNS; colm++) {    //potential moves
            if (board.getSquareValue(0, colm) == -1) {      //check if colm is open
                int currentRow = getNextAvailableRow(colm);
                board.getSquare(currentRow, colm).setUser(0);   //set move to user 0 (AI)
                int score = evaluateBoard();

                node tempNode = new node(score, colm);          //create node, save score and move
                bestFirst.add(tempNode);                        //add node to queue

                board.getSquare(currentRow, colm).setUser(-1); //reset move
            }
        }

        node bestNode = bestFirst.poll();

        int bestMove = bestNode.move;

        // Return the best score found
        if(bestMove != -1){
            System.out.println("\nAI chooses: " + (bestMove + 1));
            board.placeMove(bestMove, 0);
            return board.checkForWin(bestMove, 0);
        }else{
            System.out.println("No move found");
            return false;
        }
    }

    public static class node {
        private int score = Integer.MIN_VALUE;
        private int move = -1;

        // Constructor
        public node(int score, int move) {
            this.score = score;
            this.move = move;
        }
    }

}