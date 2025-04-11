import java.time.LocalDate;

import gamesleague.*;

public class TestHelpers {

    public static String getPlayerInfo(GamesLeague gl, int playerId){
        String pEmail = gl.getPlayerEmail(playerId);
        String pDisplayName = gl.getPlayerDisplayName(playerId);
        String pActive = gl.isDeactivatedPlayer(playerId) ? "true" : "false";
        String pJoinDate;
        if (gl.getPlayerJoinDate(playerId) == null) {
            pJoinDate = "NA";
        } else {
         pJoinDate = gl.getPlayerJoinDate(playerId).toString();
        }
        String playerString = String.format("Player[displayName=%s, email=%s, joinDate=%s, deactivated=%s]", pDisplayName, pEmail, pJoinDate, pActive);
        return playerString;
    }

    public static void printPlayerInfo(GamesLeague gl, int playerId){
        System.out.println(getPlayerInfo(gl, playerId));
    }

    public static void printLeagueInfo(GamesLeague gl, int LeagueID){
        int todayEpochDay = (int)DateProvider.now().toEpochDay();
        String lName = gl.getLeagueName(LeagueID);
        System.out.println("\n########## League Details for "+ lName +" ############\n");
        String startDateString = "NA";
        String endDateString = "NA";
        String end="";
        String statusString = gl.getLeagueStatus(LeagueID).toString();
        if (gl.getLeagueStartDate(LeagueID) != -1) {
            startDateString = LocalDate.ofEpochDay(gl.getLeagueStartDate(LeagueID)).toString();
        }
        if (gl.getLeagueCloseDate(LeagueID) != -1) {
            endDateString = LocalDate.ofEpochDay(gl.getLeagueCloseDate(LeagueID)).toString();
        }
        String infoString = String.format("League[name=%s, status=%s, start=%s, closed_on=%s]\n", lName, statusString, startDateString, endDateString);
        System.out.println(infoString);
        int[] ownerIds = gl.getLeagueOwners(LeagueID);
        end="<None>\n";
        System.out.println("Owners");
        for (int ownerId : ownerIds) {
            String pEmail = gl.getPlayerEmail(ownerId);
            String pDisplayName = gl.getPlayerDisplayName(ownerId);    
            System.out.println(gl.getPlayerDisplayName(ownerId));
            end="";
        }
            System.out.println(end);
        end="<None>\n";
        System.out.println("Players");
        int[] playerIds = gl.getLeaguePlayers(LeagueID);
        for (int playerId : playerIds) {
            String active = " (active)";
            if (!gl.isLeaguePlayerActive(LeagueID, playerId)) {
                active = " (inactive)";
            }   
            System.out.println(gl.getPlayerDisplayName(playerId)+active);
            end="";
        }
        System.out.println(end);


        System.out.println("\nEmail Invitations");
        end="<None>\n";
        for (String email : gl.getLeagueEmailInvites(LeagueID)) {
            System.out.println(email);
            end = "";
        }
        System.out.println(end);
        end="<None>\n";
        System.out.println("Player Invitations");
        for (int playerId : gl.getLeaguePlayerInvites(LeagueID)) {
            System.out.println(gl.getPlayerDisplayName(playerId) + playerId);
            end = "";
        }
        System.out.println(end);
        System.out.println("\n################################################\n");
    }

}
