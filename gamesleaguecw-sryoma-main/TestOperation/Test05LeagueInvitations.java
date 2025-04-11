import gamesleague.*;
import java.util.Arrays;
import java.time.LocalDate;
import java.time.LocalDate.*; 


public class Test05LeagueInvitations {

    
 
    public static void main(String[] args) {
        GamesLeague gl = new GamesLeague();

        System.out.println("\n#### Running Test05LeagueInvitations...\n");
        
        int todayEpochDay = (int)LocalDate.now().toEpochDay();

        int p1 = gl.createPlayer("wardlejj@wardle.com",  "BigJW", "Josh Wardle", "123-456-7890");
        System.out.println("Created Player: " + TestHelpers.getPlayerInfo(gl, p1));

        int p2 = gl.createPlayer("annakw@wardle.com", "AKAnna", "Anna Karl-Witham",  "");
        System.out.println("Created Player: " + TestHelpers.getPlayerInfo(gl, p2));

        int league1ID = gl.createLeague(p1,"DR League", GameType.DICEROLL);
        System.out.println("\nCreated League: " + gl.getLeagueName(league1ID));

        gl.invitePlayerToLeague(league1ID, gl.getPlayerEmail(p2));
        System.out.println("Invited Player: " + gl.getPlayerDisplayName(p2) + " to league: " + gl.getLeagueName(league1ID));
        String nonPlayerEmail = "p.lewis@nowhere.com";
        gl.invitePlayerToLeague(league1ID, nonPlayerEmail);
        System.out.println("Invited Non-player: " + nonPlayerEmail + " to league: " + gl.getLeagueName(league1ID));


        int[] leagueIds = gl.getLeagueIds();
        for (int id : leagueIds) {
            TestHelpers.printLeagueInfo(gl, id);
        }

        System.out.println("Accepting invitations: ");
        
        leagueIds = gl.getLeagueIds();
        int p3 = gl.createPlayer("p.lewis@nowhere.com", "PWL", "Philip L",  "");
        System.out.println("Created Player: " + TestHelpers.getPlayerInfo(gl, p3));

        System.out.println("Accepts invite: " + gl.getPlayerDisplayName(p3));
        gl.acceptInviteToLeague(league1ID, p3);

        System.out.println("Accepts invite: " + gl.getPlayerDisplayName(p2));    
        gl.acceptInviteToLeague(league1ID, p2);

        System.out.println("Switch to inactive: " + gl.getPlayerDisplayName(p3));
        gl.setLeaguePlayerInactive(league1ID,p3);

        leagueIds = gl.getLeagueIds();
        for (int id : leagueIds) {
            TestHelpers.printLeagueInfo(gl, id);
        }

        System.out.println("Add owner: " + gl.getPlayerDisplayName(p2));
        System.out.println("Removed owner: " + gl.getPlayerDisplayName(p1));        

        gl.addOwner(league1ID, p2);
        gl.removeOwner(league1ID, p1);

        System.out.println("Switch to active: " + gl.getPlayerDisplayName(p3));
        gl.setLeaguePlayerActive(league1ID,p3);


        for (int id : leagueIds) {
            TestHelpers.printLeagueInfo(gl, id);
        }


        System.out.println("Cloning " + gl.getLeagueName(league1ID) + " to Cloned league");
        int league3ID =  gl.cloneLeague(league1ID, "Cloned league");

        leagueIds = gl.getLeagueIds();
        for (int id : leagueIds) {
            TestHelpers.printLeagueInfo(gl, id);
        }
        System.out.println("Removing " + gl.getLeagueName(league3ID));
        gl.removeLeague(league3ID);

        System.out.println("Resetting " + gl.getLeagueName(league1ID));
        gl.resetLeague(league1ID);

        leagueIds = gl.getLeagueIds();
        for (int id : leagueIds) {
            TestHelpers.printLeagueInfo(gl, id);
        }



    }
}