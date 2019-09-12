import java.util.Observable;

/**
 *
 * @author Taher
 */
public class ConnectFourGame extends Observable {
    
    private int nRows, nColumns;    // Dimensions of grid
    private int numToWin;           // Number of adjacent checkers to be a winner (Must be less than dimensions)
    private ConnectFourEnum grid[][];           // EMPTY, RED or BLACK
    private ConnectFourEnum turn;               // RED or BLACK
    private ConnectFourEnum gameState;  
    private int nCheckers;          // Number of checkers played.  Used to detect full grid (i.e. a draw)
    
    public ConnectFourGame(ConnectFourEnum initialTurn) {
        this(8,8, 4, initialTurn);
    }
    public ConnectFourGame(int nRows, int nColumns, int numToWin, ConnectFourEnum initialTurn) {
        if (nRows < 0 || nColumns < 0) 
            throw new IllegalArgumentException("Grid must be a positive size");
        if (numToWin > nRows || numToWin > nColumns) 
            throw new IllegalArgumentException("sizeToWin must be less than dimensions");
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.numToWin = numToWin;
        this.grid = new ConnectFourEnum[nRows][nColumns];
        reset(initialTurn);
    }
    
    public void reset(ConnectFourEnum initialTurn) {
        for (int r=0;r<nRows; r++) {
            for (int c=0; c<nColumns; c++) {
                this.grid[r][c] = ConnectFourEnum.EMPTY;
            }
        }
        this.turn = initialTurn;        
        this.nCheckers = 0;
        this.gameState = ConnectFourEnum.IN_PROGRESS;
        setChanged();
        notifyObservers();
    }
    
    /**
     * @param    row      Where checker is to be added
     * @param    column   Where checker is to be added
     * @return   gateState = {IN_PROGRESS, RED, BLACK, DRAW} 
     * @throws   IllegalArgumentException is the <row,column> is out of range,
     *           or if the location is not adjacent to a filled lower spot
     */
    public ConnectFourEnum takeTurn(int row, int column)  {
        if (this.gameState != ConnectFourEnum.IN_PROGRESS) 
            throw new IllegalArgumentException("Game Over. No more plays");

        if (row < 0 || row > this.nRows) 
            throw new IllegalArgumentException("Grid is " + this.nRows + " by " + this.nColumns);
        if (column < 0 || column > this.nColumns) 
            throw new IllegalArgumentException("Grid is " + this.nRows + " by " + this.nColumns);        
        if (this.grid[row][column] != ConnectFourEnum.EMPTY) 
            throw new IllegalArgumentException("Location is already full");
        if (row != 0 && grid[row-1][column] == ConnectFourEnum.EMPTY)
            throw new IllegalArgumentException("Can only drop a checker on the lowest row or on top of another checker");
        this.grid[row][column] = this.turn;
        this.nCheckers++;
    
        this.gameState = findWinner(row, column);
        setChanged();    
        ConnectFourEnum oldTurn = this.turn;
        this.turn = (this.turn == ConnectFourEnum.RED) ? ConnectFourEnum.BLACK : ConnectFourEnum.RED;
        notifyObservers( new ConnectMove(row,column, oldTurn) );

        return this.gameState;
        
    }
 
    /** An internal (private) method that looks for the winner, with 
     * the assumption that the search begins from the latest checker
     * Hence the method being made private. The method is used for readability.
     * @param    row      Where last checker WAS added
     * @param    column   Where last checker WAS added
     * @return   IN_PROGRESS, RED, BLACK, DRAW 
     */
    private ConnectFourEnum findWinner(int row, int column) {
       
        // Look horizontally - left than right
        
        int count;
        
        count = 1;
        for (int c = column-1; c > 0; c--) {
            if (this.grid[row][column] == this.grid[row][c]) {
                count++;
                if (count == this.numToWin) {
                    return grid[row][column];
                }
            } // else, look in another direction
        }
        
        count = 1;
        for (int c = column+1; c < this.nColumns; c++) {
            if (this.grid[row][column] == this.grid[row][c]) {
                count++;
                if (count == this.numToWin) {
                    return grid[row][column];
                }
            } // else, look in another direction
        }
    
        // Look vertically
        count = 1;
        for (int r = row-1; r > 0; r--) {
            if (this.grid[r][column] == this.grid[row][column]) {
                count++;
                if (count == this.numToWin) {
                     return grid[row][column];
                }
            } // else, look in another direction
        }
        
        count = 1;
        for (int r = row+1; r < this.nRows; r++) {
            if (this.grid[row][column] == this.grid[r][column]) {
                count++;
                if (count == this.numToWin) {
                    return grid[row][column];
                }
            } // else, look in another direction
        }        
        
        // Look diagonally - Left-down - TBD
        
        // Look diagonally - Right-down - TBD

        if (nCheckers == this.nRows * this.nColumns) {
            return ConnectFourEnum.DRAW;
        }        
        return ConnectFourEnum.IN_PROGRESS;
    }
    
    public ConnectFourEnum getTurn() {
        return this.turn;
    }
    public ConnectFourEnum getGameState() {
        return this.gameState;
    }
    
   
    public String toString() {
        String s = "";
        for (int r=0;r < nRows; r++ ) {
            for (int c=0; c < nColumns; c++) {
                s += grid[r][c] + " | ";
            }
            s += "\n";
        }
        return s;
    }
    
}
