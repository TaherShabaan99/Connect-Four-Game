/**
 *
 * @author Taher
 */
public class ConnectMove {
    
    private int row, column;
    private ConnectFourEnum colour;
    
    public ConnectMove(int row, int column, ConnectFourEnum colour) {
        this.row = row;
        this.column = column;
        this.colour = colour;
    }
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
    public ConnectFourEnum getColour() { return this.colour; }
    
}
