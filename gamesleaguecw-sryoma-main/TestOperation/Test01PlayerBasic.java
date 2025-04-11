import gamesleague.*;
import java.util.Arrays;

public class Test01PlayerBasic {
    public static void main(String[] args) {
        System.out.println("\n#### Running Test01PlayerBasic...\n");

        GamesLeague gl = new GamesLeague();

        int p1 = gl.createPlayer("wardlejj@wardle.com",  "BigJW", "Josh Wardle", "123-456-7890");
        System.out.println("Created Player: " + TestHelpers.getPlayerInfo(gl, p1));
        int p2 = gl.createPlayer("annakw@wardle.com", "AKAnna", "Anna Karl-Witham",  "");
        System.out.println("Created Player: " + TestHelpers.getPlayerInfo(gl, p2));

        System.out.println("\nPlayers now stored in GamesLeague backend:");
        int[] playerIds = gl.getPlayerIds();
        for (int id : playerIds) {
            System.out.println(TestHelpers.getPlayerInfo(gl, id));
        }

        System.out.println("\nDeactivating player: " + gl.getPlayerDisplayName(p1));
        System.out.println("Player info before deactivation:");
        System.out.println(TestHelpers.getPlayerInfo(gl, p1));
        gl.deactivatePlayer(p1);
        System.out.println("Player info after deactivation:");
        System.out.println(TestHelpers.getPlayerInfo(gl, p1));

        for (int id : playerIds) {
            if ( gl.isDeactivatedPlayer(id) ) {
                System.out.println("Player with displayName " + gl.getPlayerDisplayName(id) + " is deactivated");
            } else {
                System.out.println("Player with displayName " + gl.getPlayerDisplayName(id) + " is active");
            }
        }
    }
}