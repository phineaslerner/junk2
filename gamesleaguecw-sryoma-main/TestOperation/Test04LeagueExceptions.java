import gamesleague.*;

import java.util.Arrays;

public class Test04LeagueExceptions {
    public static void main(String[] args) {
        System.out.println("\n#### Running Test04LeagueExceptions...\n");
        GamesLeague gl = new GamesLeague();

        try {
            System.out.println("EXCEPTION TEST 0: Try to find non-existent league ");
            String pEmail = gl.getLeagueName(-99999);
            System.out.println("ERROR 0: IDInvalidException not thrown");
        } catch (IDInvalidException e) { // if error edit IDInvalidException to be public
            System.out.println("PASS 0: Caught IDInvalidException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 1: Try to find status of non-existent league ");
            Status lStatus = gl.getLeagueStatus(-99999);
            System.out.println("ERROR 1: IDInvalidException not thrown");
        } catch (IDInvalidException e) { // if error edit IDInvalidException to be public
            System.out.println("PASS 1: Caught IDInvalidException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 2: Try to create league with non-existing owner");
            int league2ID = gl.createLeague(-99999,"#1 League", GameType.WORDMASTER);
            System.out.println("ERROR 2: IDInvalidException not thrown");
        } catch (IDInvalidException e) { // if error edit IDInvalidException to be public
            System.out.println("PASS 2: Caught IDInvalidException: " + e.getMessage());
        }

        int p1 = gl.createPlayer("wardlejj@wardle.com",  "BigJW", "Josh Wardle", "123-456-7890");
        System.out.println("Created Player with id: " + p1);

        int p2 = gl.createPlayer("annakw@wardle.com", "AKAnna", "Anna Karl-Witham",  "");
        System.out.println("Created Player with id: " + p2);

        int league2ID = gl.createLeague(p2,"WM League", GameType.WORDMASTER);
        System.out.println("Created League with id: " + league2ID);

        try {
            System.out.println("EXCEPTION TEST 3: Try to create league with empty name");
            int league1ID = gl.createLeague(p1,"", GameType.DICEROLL);
            System.out.println("ERROR 3: InvalidNameException not thrown");
        } catch (InvalidNameException e) {
            System.out.println("PASS 3: Caught InvalidNameException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 3b: Try to create league with too long name");
            int league1ID = gl.createLeague(p1,"01234567890123456789X", GameType.DICEROLL);
            System.out.println("ERROR 3b: InvalidNameException not thrown");
        } catch (InvalidNameException e) {
            System.out.println("PASS 3b: Caught InvalidNameException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 4: Try to create leagues with duplicate names");
            gl.createLeague(p1,"ABCDEFG", GameType.DICEROLL);
            gl.createLeague(p1,"ABCDEFG", GameType.DICEROLL);
            System.out.println("ERROR 4: IllegalNameException not thrown");
        } catch (IllegalNameException e) {
            System.out.println("PASS 4: Caught IllegalNameException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 5: Try to check removeLeague worked");
            int id1 = gl.createLeague(p1,"XYZ", GameType.DICEROLL);
            gl.removeLeague(id1);
            int id2 = gl.createLeague(p1,"XYZ", GameType.DICEROLL);
            System.out.println("PASS 5: was able to reuse name of deleted league without exception...");
        } catch (IllegalNameException e) {
            System.out.println("FAIL 5: Should not have caused this IllegalNameException: " + e.getMessage());
        }
    }
}