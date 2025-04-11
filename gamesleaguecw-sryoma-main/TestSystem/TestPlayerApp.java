import gamesleague.*;

public class TestPlayerApp  {
        public static void main(String[] args) {
            System.out.println("Testing GamesLeague methods for player management");
            GamesLeague gl = new GamesLeague();
            System.out.println(gl.createPlayer("asdx@ad", "ewfvtj", "jndughn", "12345678"));
            int[] idArray = gl.getPlayerIds();
            for (int i = 0; i < idArray.length; i++) {
                System.out.println(idArray[i]);
            gl.updatePlayerDisplayName(12345678, "rohmjrtng");
}
        }
}