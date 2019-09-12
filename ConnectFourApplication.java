
import java.awt.Point;
import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Taher
 */
public class ConnectFourApplication extends Application implements Observer {
    
    public static final int NUM_ROWS = 8;
    public static final int NUM_COLUMNS = 8;
    public static final int NUM_TO_WIN = 4;
    
    public static final int BUTTON_SIZE = 8;
    
    private ConnectFourGame gameEngine;
    private ConnectButton[][] buttons;
    private Point location;   // Newest location clicked by user (of current turn)
    private TextField whoseTurnField;

    
    @Override
    public void start(Stage stage) {
        
        gameEngine = new ConnectFourGame(NUM_ROWS, NUM_COLUMNS, NUM_TO_WIN, ConnectFourEnum.BLACK );
        gameEngine.addObserver(this);
        location = null;

        BorderPane root = new BorderPane();
        GridPane centre = new GridPane();
    
        buttons = new ConnectButton[NUM_ROWS][NUM_COLUMNS];
        ButtonHandler buttonHandler = new ButtonHandler();
        for (int r=0; r<NUM_ROWS; r++) {
            for (int c=0; c<NUM_COLUMNS; c++) {
                buttons[r][c] = new ConnectButton( ConnectFourEnum.EMPTY.toString(), r, c);
                buttons[r][c].setMaxWidth(Double.MAX_VALUE);
                buttons[r][c].setMinHeight(40);
                buttons[r][c].setOnAction( buttonHandler );
                centre.add( buttons[r][c], c, NUM_ROWS-r); 
            }
        }

        whoseTurnField = new TextField( gameEngine.getTurn() + " begins.");        

        Button btn = new Button();
        btn.setText("Take My Turn");
        btn.setOnAction(new EventHandler<ActionEvent>() {
       
        @Override
        public void handle(ActionEvent event) {
            System.out.println("Drop the checker");
            if (location != null) {
                try {
                    ConnectFourEnum newGameState = gameEngine.takeTurn(location.x, location.y);
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Information Alert");
                    alert.setHeaderText("ERROR IN PLAY");
                    alert.setContentText( e.getMessage() );
                    alert.showAndWait();
                }
                location = null;
            }
        }});
            
        root.setCenter( centre );
        root.setTop( whoseTurnField );
        root.setBottom( btn );
                
        Scene scene = new Scene(root, 510, 380);
        stage.setTitle("ConnectFour");
        stage.setScene(scene);
        stage.show();
               
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) { // First time notification after reset. Re-draw entire board
            
            for (int r=0; r<NUM_ROWS; r++) {
                for (int c=0; c<NUM_COLUMNS; c++) {
                    buttons[r][c].setText(ConnectFourEnum.EMPTY.toString());
                    buttons[r][c].setDisable(false);
                }
            }
            
        } else if (arg != null && arg instanceof ConnectMove) {
            ConnectMove move = (ConnectMove)arg;
            buttons[move.getRow()][move.getColumn()].setText( move.getColour().toString());
            buttons[move.getRow()][move.getColumn()].setDisable(true);
            
            ConnectFourEnum gameState = gameEngine.getGameState();
            if (gameState != ConnectFourEnum.IN_PROGRESS) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information Alert");
                alert.setHeaderText("Game Over");
                if (gameState == ConnectFourEnum.DRAW) {
                    alert.setContentText( "It's a draw!" );                    
                } else {
                    alert.setContentText( gameState.toString() + " wins!" );
                }
                alert.showAndWait();
                if (gameState == ConnectFourEnum.BLACK) {
                    gameEngine.reset(ConnectFourEnum.RED);
                } else {
                    gameEngine.reset(ConnectFourEnum.BLACK);
                }
                
            }
        } 
        ConnectFourEnum whoseTurn = gameEngine.getTurn();
        whoseTurnField.setText( "It's " + whoseTurn + "'s turn");
        
    }
   
    class ButtonHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            ConnectButton b = (ConnectButton)event.getSource();
            System.out.println("Button " + b.getRow() + " " + b.getColumn());
            location = new Point(b.getRow(), b.getColumn());
        }
   
   
    }
    
}
