import java.util.Scanner;
/**
 *
 * @author Taher
 */
public class ConnectFourTestClient {
        public static void main(String args[]) {
        ConnectFourGame game = new ConnectFourGame( ConnectFourEnum.BLACK );
        Scanner scanner = new Scanner(System.in);

        do { 
            System.out.println(game.toString());
            System.out.println(game.getTurn() + ": Where do you want to mark? Enter row column");
            int row = scanner.nextInt();
            int column = scanner.nextInt();
            scanner.nextLine();
            try {
                game.takeTurn(row, column);
            } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
            
        } while (game.getGameState() == ConnectFourEnum.IN_PROGRESS);
        System.out.println( "Game Over " + game.getGameState() );
       
    }
}
