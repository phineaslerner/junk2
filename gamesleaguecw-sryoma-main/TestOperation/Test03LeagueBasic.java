import gamesleague.*;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalDate.*; 


public class Test03LeagueBasic {
    public static void main(String[] args) {
        System.out.println("\n#### Running Test03LeagueBasic...\n");
        GamesLeague gl = new GamesLeague();

        int p1 = gl.createPlayer("wardlejj@wardle.com",  "BigJW", "Josh Wardle", "123-456-7890");
        System.out.println("Created Player: " + TestHelpers.getPlayerInfo(gl, p1));
        
        int p2 = gl.createPlayer("annakw@wardle.com", "AKAnna", "Anna Karl-Witham",  "");
        System.out.println("Created Player: " + TestHelpers.getPlayerInfo(gl, p2));

        int league1ID = gl.createLeague(p1,"DR League", GameType.DICEROLL);
        System.out.println("\nCreated League: " + gl.getLeagueName(league1ID));

        int league2ID = gl.createLeague(p2,"WM League", GameType.WORDMASTER);
        System.out.println("Created League: " + gl.getLeagueName(league2ID));

        System.out.println("\nChecking result of getLeagueStartDate / setLeagueStartDate");
        if (gl.getLeagueStartDate(league2ID) == -1) {
            System.out.println("PASS: gl.getLeagueStartDate("+league2ID+") == -1");
        } else {
            System.out.println("FAIL: gl.getLeagueStartDate("+league2ID+") should return -1)");
        }

        int todayEpochDay = (int)LocalDate.now().toEpochDay();

        System.out.println("Setting start date of league "+league2ID+ " to today: " +todayEpochDay);
        gl.setLeagueStartDate(league2ID, (int)LocalDate.now().toEpochDay());
        if (gl.getLeagueStartDate(league2ID) == todayEpochDay) {
            System.out.println("PASS: gl.getLeagueStartDate("+league2ID+") == "+todayEpochDay);
        } else {
            System.out.println("FAIL: gl.getLeagueStartDate("+league2ID+") should return "+todayEpochDay);
        }
        System.out.println("\nSetting end date of league "+league2ID+ " to one weeks time: " + LocalDate.now().plusWeeks(1));
        gl.setLeagueEndDate(league2ID, (int)LocalDate.now().plusWeeks(1).toEpochDay());


        int[] leagueIds = gl.getLeagueIds();
        for (int id : leagueIds) {
            TestHelpers.printLeagueInfo(gl, id);
        }

        System.out.println("Removing league: " + gl.getLeagueName(league1ID));
        gl.removeLeague(league1ID);

        String newName = "RENAMED LEAGUE";
        System.out.println("Renaming League: " + gl.getLeagueName(league2ID) + " to " + newName);
        gl.updateLeagueName(league2ID, newName); 

        int league3ID = gl.createLeague(p2,"PENDING LEAGUE", GameType.WORDMASTER);
        System.out.println("\nCreated League with pending status: " + gl.getLeagueName(league3ID));


        leagueIds = gl.getLeagueIds();
        for (int id : leagueIds) {
            TestHelpers.printLeagueInfo(gl, id);
        }
        

        int[] playerLeagues = gl.getPlayerLeagues(p2);

        System.out.println("\n" + gl.getPlayerDisplayName(p2) + " is Player (only includes in progress leagues):");
        for (int id : playerLeagues) {
            System.out.println("League "+gl.getLeagueName(id));
        }

        int[] playerOwnedLeagues = gl.getPlayerOwnedLeagues(p2);
        System.out.println("\n" + gl.getPlayerDisplayName(p2) + " is Owner (all leagues):");
        for (int id : playerOwnedLeagues) {
            System.out.println("League "+gl.getLeagueName(id));
        }

    }
}