import javafx.scene.control.Button;
/**
 *
 * @author Taher
 */
public class ConnectButton extends javafx.scene.control.Button{
    private int row;
    private int column;
    
    public ConnectButton(String label, int row, int column) {
        super(label);
        this.row = row;
        this.column = column;
    }
    
    public int getRow() { return this.row; }
    public int getColumn() { return this.column; }
    public String toString() { return "" + this.row + "," + this.column; }
    
}
