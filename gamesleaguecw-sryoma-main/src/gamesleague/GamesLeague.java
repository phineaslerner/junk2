package gamesleague;
import java.util.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.io.*;

import java.util.ArrayList;
/**
 * GamesLeague Class Template
 * You can use this template and add the correct method code.
 * At present it is a skeleton template with placeholder methods 
 * so that it can compile and implements all methods required by GamesLeagueInterface
 *
 * @author Philip Lewis
 * @version 0.3.6 
 */

public class GamesLeague implements GamesLeagueInterface {
    //playerList stores players in the league
    private ArrayList<Player> playerList;
    private ArrayList<League> leagueList;
    private ArrayList<PotentialPlayer> potentialList;

    //no arg constructor
    public GamesLeague(){
       this.playerList = new ArrayList<>();
       this.leagueList = new ArrayList<>();
       this.potentialList = new ArrayList<>();
    }


    // Players

    /**
     * Get the players currently created in the platform.
     *
     * @return An array of player IDs in the system or an empty array if none exists.
     */
    public int[] getPlayerIds(){       //FINISHED
        //return empty array if no players exist
        if (playerList.size()==0){
            return new int[0];
        }

        //convert player list into an array of ints
        int[] idArray = new int[playerList.size()];
        for (int i=0;i<playerList.size();i++){
            idArray[i] = playerList.get(i).getID();
        }
        return idArray;
    };


    /**
     * Creates a player entry.
     *
     * @param email The email of the player (unique).
     * @param displayName The name of the player.
     * @param name The name of the player.
     * @param phone The contact phone number of the player or empty string.
     * @return The ID of the rider in the system.
     * @throws InvalidNameException If the displayName/name is null or starts/ends with whitespace, 
     *                              or if the name is less than 5 char / more than 50 char.
     *                              or if displayName is less than 1 char/more than 20 char.
     * @throws InvalidEmailException If the email is null, empty, or does not contain an '@' character,
     * @throws IllegalEmailException if it duplicates an existing email of a player
     */
    public int createPlayer(String email, String displayName, String name, String phone)  //FINISHED
        throws  InvalidEmailException,   
                IllegalEmailException,
                InvalidNameException {
                    //invalid name checks
                    if (displayName == null | name == null){
                        throw new InvalidNameException("names cannot be null");
                    }
                    if (name.length() < 5 | name.length() > 50| displayName.length()>20| displayName.length()<1){
                        throw new InvalidNameException("wrong name sizes");
                    }
                    if (displayName.charAt(0) == ' ' | displayName.charAt(displayName.length()-1) == ' '){
                        throw new InvalidNameException("names cannot start or end in whitespace");
                    }

                    //invalid email checks
                    if (email.equals(null)){
                        throw new InvalidEmailException("email cannot be null");
                    }
                    if (email.length() ==0){
                        throw new InvalidEmailException("email cannot be empty");
                    }
                    if (email.contains("@") == false){
                        throw new InvalidEmailException("email must contain @");
                    }

                    //duplicate email checks
                    boolean valid = true;
                    for (int i=0;i<playerList.size();i++){
                        if (playerList.get(i).getEmail().equals(email)){
                            valid = false;
                        }
                    }
                    if (valid == false){
                        throw new IllegalEmailException("duplicate emails not allowed");
                    }

                    //check if there is a matching potential player
                    boolean found = false;
                    for(int i = 0;i<potentialList.size();i++){
                        if (potentialList.get(i).getEmail().equals(email)){
                            found = true;
                            playerList.add(new Player(email,displayName,name,phone,potentialList.get(i).getInvites()));
                        }
                    }
                   
                    if (found == false){
                        playerList.add(new Player(email,displayName,name,phone));
                    }
                    return playerList.getLast().getID();
                }
 

    /**
     * Permenantly deactivates player account.
     * <p>
     * Note to preserve the integrity of the league tables the removal process should follow:
     * i) all personal player anonymised as below:
     *     - name & displayName to "anonymousplayerX" where X is playerId
     *     - email is set to "" and phone is set to ""
     * ii) all player gameplay reports are set to empty strings
     * iii) player is set as inactive in all their league memberships
     *
     * @param playerId The ID of the player to be deactivated.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     * @throws IllegalOperationException If the player is the sole owner of a league.
     */
    public void deactivatePlayer(int playerId) // FINISHED 
        throws IDInvalidException, IllegalOperationException {
            //loop to search for player
            boolean found = false;
            for (int i=0;i<playerList.size();i++){
                if (playerId == playerList.get(i).getID()){
                    found = true;

                    //check if league would be left with no owner
                    boolean valid = true;
                    for (int y=0;y<leagueList.size();y++){
                        if (leagueList.get(y).getOwners().size()==1  & leagueList.get(y).getOwners().contains(playerId)){
                            valid = false;
                        }
                    }
                    if (valid == false){
                        throw new IllegalOperationException("league would be left without an owner");
                    }

                    playerList.get(i).setName(String.format("anonymousplayer%d", playerList.get(i).getID()));
                    playerList.get(i).setDisplayName(String.format("anonymousplayer%d", playerList.get(i).getID()));
                    playerList.get(i).setEmail("");
                    playerList.get(i).setPhone("");
                    for (int y=0;y<leagueList.size();y++){
                        for (int z=0;z<leagueList.get(y).getPlayers().size();z++){
                            if (leagueList.get(y).getPlayers().get(z)==playerId){
                                leagueList.get(y).setActivity(z, false);
                                leagueList.get(y).anonReport(playerId);
                            }
                        }
                    }
                }
            }

            if (found == false){
                throw new IDInvalidException("no such player");
            }
        return; // placeholder so class compiles
    };
    // Reminder to ask about player ID

    /**
     * Check if a player has been deactivated.
     * 
     * @param playerId The ID of the player.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     * 
     * @return true if player has been deactivated or false otherwise 
     */
    public boolean isDeactivatedPlayer(int playerId) 
        throws IDInvalidException{             //FINISHED
            //loop to search for player
            for (int i=0;i<playerList.size();i++){
                if (playerId == playerList.get(i).getID()){

                    //determine if player is deactivated based on their email being empty
                    if (playerList.get(i).getEmail().equals("")){
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
            throw new IDInvalidException("no such player");
        };


    /**
     * Updates the player's display name.
     * 
     * @param playerId The ID of the player to be updated.
     * @param displayName The new display name of the player.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     * @throws InvalidNameException If the name is null, starts/ends with whitespace, 
     *                              is less than 1 characters or more than 20 characters.
     */
    public void updatePlayerDisplayName(int playerId, String displayName)  // FINISHED
        throws  IDInvalidException, InvalidNameException {
            //checks for an invalid name
            if (displayName.equals(null)){
                throw new InvalidNameException("new display name cannot be null");
            }
            if (displayName.length()<1|displayName.length()>20){
                throw new InvalidNameException("incorrect new display name length");
            }
            if (displayName.charAt(0) == ' ' | displayName.charAt(displayName.length()-1) == ' '){
                throw new InvalidNameException("new display name cannot start or end with whitespace");
            }
            
            //search for player and change their name if found
            boolean found = false;
            for (int i=0;i<playerList.size();i++){
                if (playerId == playerList.get(i).getID()){
                    found = true;
                    playerList.get(i).setDisplayName(displayName);
                }
            }
            if (found == false){
                throw new IDInvalidException("cannot find id in system");
            }
    }

    /**
     * Get the player id from the email.
     *
     * @param email The email of the player.
     * @return The ID of the player in the system or -1 if the player does not exist.
     */
    public int getPlayerId(String email){  //FINISHED
        for (int i=0;i<playerList.size();i++){
            if (email == playerList.get(i).getEmail()){
                return playerList.get(i).getID();
            }
        }
        return -1;
    }


    /**
     * Get the player's display name.
     * 
     * @param playerId The ID of the player being queried.
     * @return The display name of the player.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     * 
     */
    public String getPlayerDisplayName(int playerId) throws IDInvalidException{     //FINISHED
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){
                return playerList.get(i).getDisplayName();
            }
        }
        throw new IDInvalidException("ID doesn't match to any player");
    }


    /**
     * Get the player's email.
     * 
     * @param playerId The ID of the player being queried.
     * @return The email of the player.
     * @throws IDInvalidException If the email does not match to any player in the system.
     * 
     */
    public String getPlayerEmail(int playerId) throws IDInvalidException{ //FINISHED
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){
                return playerList.get(i).getEmail();
            }
        }
        throw new IDInvalidException("ID doesn't match to any player");
    }


    /**
     * Get the in progress leagues a player is currently a member.
     * 
     * @param playerId The ID of the player being queried.
     * @return An array of league IDs the player is in or an empty array if none exists.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     */
    public int[] getPlayerLeagues(int playerId) throws IDInvalidException{    //FINISHED
        //check if player exists
        boolean found = false;
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){
                found = true;
            }
        }

        if (found == false){
            throw new IDInvalidException("ID doesn't match to any player");
        }

        //create a list of all the leagues the player is a member of
        ArrayList<Integer> memberList = new ArrayList<>();
        for (int i=0;i<leagueList.size();i++){
            if (leagueList.get(i).getPlayers().contains(playerId)&leagueList.get(i).getStatus()==Status.IN_PROGRESS){
                memberList.add(leagueList.get(i).getLeagueId());
            }
        }

        //convert the list of leagues the player is a member of into an array
        int[] memberArray = new int[memberList.size()]; 
        for (int i=0;i<memberList.size();i++){
            memberArray[i] = memberList.get(i).intValue();
        }
        return memberArray;
    };


    /** 
     * Get the leagues a player owns.
     * 
     * @param playerId The ID of the player being queried.
     * @return An array of league IDs the player owns or an empty array if none exists.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     */
    public int[] getPlayerOwnedLeagues(int playerId) throws IDInvalidException{   //FINISHED
        //check if player exists
        boolean found = false;
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){
                found = true;
            }
        }

        if (found == false){
            throw new IDInvalidException("ID doesn't match to any player");
        }

        //create a list of all the leagues a player owns
        ArrayList<Integer> ownList = new ArrayList<>();
        for (int i=0;i<leagueList.size();i++){
            if (leagueList.get(i).getOwners().contains(playerId)){
                ownList.add(leagueList.get(i).getLeagueId());
            }
        }

        //convert the list into an array of ints
        int[] ownArray = new int[ownList.size()]; 
        for (int i=0;i<ownList.size();i++){
            ownArray[i] = ownList.get(i).intValue();
        }
        return ownArray;
    };

    /**
     * Get the user's invites
     * 
     * @param playerId The ID of the player being queried.
     * @return An array of league IDs the player has invites to or an empty array if none exists.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     */
    public int[] getPlayerInvites(int playerId) throws IDInvalidException{     //FINISHED
        //loop to search for player
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){

                //converts the list of invites a player has into an array and returns it
                int[] inviteArray = new int[playerList.get(i).getInvites().size()];
                for (int y=0;y<playerList.get(i).getInvites().size();y++){
                    inviteArray[y] = playerList.get(i).getInvites().get(y).intValue();
                }
                return inviteArray;
            }
        }
        throw new IDInvalidException("no such player");
    };


    /**
     * Get the user's rounds played game stat across all leagues
     * (includes closed/removed leagues)
     * 
     * @param playerId The ID of the player being queried.
     * @return number of rounds played by the player.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     */
    public int getPlayerRoundsPlayed(int playerId) throws IDInvalidException{    //FINISHED
        int roundsPlayed =0;
        //search for player
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){
                //search for leagues
                for (int y=0;y<leagueList.size();y++){
                    if (leagueList.get(y).getPlayers().contains(playerId)){
                        //count rounds
                        for(int z =1;z<leagueList.get(y).getPlayerReportList(playerId).size();z++){
                            if (!leagueList.get(y).getPlayerReportList(playerId).get(z).equals("")){
                                roundsPlayed++;
                            }
                        }
                    }
                }
                return roundsPlayed;
            }
        }
        throw new IDInvalidException("no such player");
    };


    /**
     * Get the user's round participation percentage stat
     *  i.e. if they play all games in all their leagues every day this will be 100
     *  (includes closed/removed leagues)
     * 
     * @param playerId The ID of the player being queried.
     * @return percentage of rounds (0-100) played by the player across all leagues.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     */
    public double getPlayerRoundsPercentage(int playerId) throws IDInvalidException{
        double totalRounds =0;
        double roundsPlayed =0;
        //search for player
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){
                //search for leagues
                for (int y=0;y<leagueList.size();y++){
                    if (leagueList.get(y).getPlayers().contains(playerId)){
                        //count rounds and participation
                        for(int z =1;z<leagueList.get(y).getPlayerReportList(playerId).size();z++){
                            totalRounds++;
                            if (!leagueList.get(y).getPlayerReportList(playerId).get(z).equals("")){
                                roundsPlayed++;
                            }
                        }
                    }
                }

                //calculate percentage
                return (roundsPlayed/totalRounds)*100;
            }
        }
        throw new IDInvalidException("no such player");
    };

    /**
     * Get the date that the user joined the site
     * 
     * @param playerId The ID of the player being queried.
     * @return LocalDate that stores the date the player created their account.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     */
    public LocalDate getPlayerJoinDate(int playerId) throws IDInvalidException{    //FINISHED
        for (int i=0;i<playerList.size();i++){
            if (playerId == playerList.get(i).getID()){
                return playerList.get(i).getDate();
            }
        }
        throw new IDInvalidException("ID doesn't match to any player");
    };

    // Leagues

    /**
     * Get the leagues currently created in the platform.
     *
     * @return An array of leagues IDs in the system or an empty array if none exists.
     */
    public int[] getLeagueIds(){   //FINISHED
        int[] leagueIDArray = new int[leagueList.size()];
        for (int i=0;i<leagueList.size();i++){
            leagueIDArray[i] = leagueList.get(i).getLeagueId();
            }
        return leagueIDArray;
    };

    /**
     * Creates a league.
     * <p>
     * 
     *
     * @param owner PlayerId of the league owner.
     * @param name The name of the league.
     * @param gameType The game for which the league is set up.
     * @return The ID of the league in the system.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     * @throws InvalidNameException If the name is null, starts/ends with whitespace, 
     *                              is less than 1 characters or more than 20 characters.
     * @throws IllegalNameException if it duplicates an existing league name
     */
    public int createLeague(int owner, String name, GameType gameType ) 
        throws  IDInvalidException, 
                InvalidNameException, 
                IllegalNameException{           //FINISHED

        //checks to see if owner player exists
        boolean found = false;
        for (int i=0;i<playerList.size();i++){
            if (owner == playerList.get(i).getID()){
                found = true;
            }
        }
        if (found == false){
            throw new IDInvalidException("ID doesn't match to any player");
        }

        //checks to see if the name is valid
        if (name.equals(null)){
            throw new InvalidNameException("name cannot be null");
        }

        if (name.length()<1|name.length()>20){
            throw new InvalidNameException("invalid name size");
        }

        if (name.charAt(0) == ' ' | name.charAt(name.length()-1) == ' '){
            throw new InvalidNameException("new display name cannot start or end with whitespace");
        }

        boolean valid = true;
            for (int i=0;i<leagueList.size();i++){
                if (leagueList.get(i).getName().equals(name)){
                    valid = false;
                }
            }
        if (valid == false){
            throw new IllegalNameException("duplicate league names not allowed");
        }

        //create a new league, add to the list and return its id
        League temp = new League(owner,name,gameType);
        leagueList.add(temp);
        return temp.getLeagueId();  
    };

    /**
     * Removes a league and all associated game data from the system.
     * <p>
     * 
     *
     * @param leagueId The ID of the league to be removed.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public void removeLeague(int leagueId) throws IDInvalidException{     //FINISHED
        boolean found = false;
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                found = true;
                leagueList.remove(i);
            }
        }
        if (found == false){
            throw new IDInvalidException("ID doesn't match to any league");
        }
    };

    /**
     * Get name of league
     * 
     * @param leagueId The ID of the league being queried.
     * @return The name of the league.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public String getLeagueName(int leagueId) throws IDInvalidException{  //FINISHED
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                return leagueList.get(i).getName();
            }
        }
        throw new IDInvalidException("ID doesn't match to any league");
    };

    /**
     * Update the name of a league
     * 
     * @param leagueId The ID of the league to be updated.
     * @param newName The new name of the league.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidNameException If the name is null, starts/ends with whitespace, 
     *                              is less than 1 characters or more than 20 characters.
     * @throws IllegalNameException if it duplicates an existing league name.
     * 
     */
    public void updateLeagueName(int leagueId, String newName) 
        throws IDInvalidException, 
                InvalidNameException, 
                IllegalNameException{         //FINISHED

            //checks to see if the new name is valid
            if (newName.equals(null)){
                throw new InvalidNameException("name cannot be null");
            }

            if (newName.charAt(0) == ' ' | newName.charAt(newName.length()-1) == ' '){
                throw new InvalidNameException("name cannot start or end with whitespace");
            }

            if (newName.length()<1 |newName.length()>20){
                throw new InvalidNameException("incorrect name length");
            }

            //search for league and change its name
            boolean found = false;
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    leagueList.get(i).setName(newName);;
                    found = true;
                }
            }

            if (found==false){
                throw new IDInvalidException("ID doesn't match to any league");
            }
    };


    /**
     * Invites a potential player (may not yet be site member) to a league.
     * <p>
     * 
     * @param leagueId The ID of the league to invite the player to.
     * @param email The email of the player to be invited.
     * @throws IDInvalidException If the ID does not match to any league or player in the system.
     * @throws InvalidEmailException If the email is null, empty, or does not contain an '@' character.
     */
    public void invitePlayerToLeague(int leagueId, String email) 
        throws IDInvalidException, InvalidEmailException{           //FINISHED
            //checks to see if email is valid
            if (email.equals(null)){
                throw new InvalidEmailException("email cannot be null");
            }

            if (email.equals("")){
                throw new InvalidEmailException("email cannot be empty");
            }

            if (!email.contains("@")){
                throw new InvalidEmailException("email must contain @");
            }

            //checking league exists
            boolean found = false;
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;
                }
            }
            if (found==false){
                throw new IDInvalidException("no such league");
            }

            //search for player and add the invite to them
            found = false;
            for (int i=0;i<playerList.size();i++){
                if (email == playerList.get(i).getEmail()){
                    found = true;
                    playerList.get(i).addInvite(leagueId);
                }
            }

            //if player isn't found, check if they are a potential player
            if (found == false){
                boolean potentialFound = false;
                for (int i=0;i<potentialList.size();i++){
                    if (email == potentialList.get(i).getEmail()){
                        potentialFound = true;
                        potentialList.get(i).addInvite(leagueId);
                    }
                }
                //if the Potential player isn't found, create a new potential player
                if (potentialFound ==false){
                    PotentialPlayer temp = new PotentialPlayer(email);
                    temp.addInvite(leagueId);
                    potentialList.add(temp);
                }
            }
    };

    /**
     * Accepts an invite to a league.
     *
     * @param leagueId The ID of the league to accept the invite to.
     * @param playerId The ID of the player.
     * 
     * @throws IDInvalidException If the ID does not match to any league or player in the system.
     * @throws IllegalOperationException If the player email does not have an active invitation.
     */
    public void acceptInviteToLeague(int leagueId, int playerId) 
        throws IDInvalidException, IllegalOperationException{     //FINISHED
            //search for player
            boolean found =false;
            for (int i=0;i<playerList.size();i++){
                if (playerId == playerList.get(i).getID()){
                    found = true;

                    //check for matching invite
                    boolean invitefound = false;
                    for (int y=0;y<playerList.get(i).getInvites().size();y++){
                        if (leagueId == playerList.get(i).getInvites().get(y)){
                            playerList.get(i).removeInvite(leagueId);
                            invitefound=true;

                            //search for league and add player to it
                            for (int z=0;z<leagueList.size();z++){
                                if (leagueId == leagueList.get(z).getLeagueId()){
                                    leagueList.get(z).addPlayer(playerId);
                                }
                            }
                        }
                    }
                    if (invitefound==false){
                        throw new IllegalOperationException("no such invite");
                    }
                }
            }
            if (found ==false){
                throw new IDInvalidException("no such player");
            }
    };

    /**
     * Removes invite from league
     * 
     * @param leagueId The ID of the league to remove the invite from.
     * @param email The email of the player to remove the invite from.
     * @throws IDInvalidException If the ID does not match to any league or player in the system.
     * @throws IllegalEmailException If the email does not have an active invitation.
     */
    public void removeInviteFromLeague(int leagueId, String email) 
        throws IDInvalidException, IllegalEmailException{       //FINISHED
            //checking league exists
            boolean found = false;
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;
                }
            }
            if (found==false){
                throw new IDInvalidException("no such league");
            }

            //checking player exists
            found = false;
            for (int i=0;i<playerList.size();i++){
                if (email == playerList.get(i).getEmail()){
                    found = true;

                    //checking if the player has this invite
                    boolean invitefound = false;
                    for (int y=0;y<playerList.get(i).getInvites().size();y++){
                        if (leagueId == playerList.get(i).getInvites().get(y)){
                            invitefound = true;
                            playerList.get(i).removeInvite(leagueId);
                        }
                    }
                    if (invitefound == false){
                        throw new IllegalEmailException("no such invite to this email");
                    }

                }
            }
            //if a player with the email isn't found, check potential players for a match
            if (found==false){
                for (int i=0;i<potentialList.size();i++){
                    if (email == potentialList.get(i).getEmail()){
                        found = true;
    
                        //checking if the player has this invite
                        boolean invitefound = false;
                        for (int y=0;y<potentialList.get(i).getInvites().size();y++){
                            if (leagueId == potentialList.get(i).getInvites().get(y)){
                                invitefound = true;
                                potentialList.get(i).removeInvite(leagueId);
                            }
                        }
                        if (invitefound == false){
                            throw new IllegalEmailException("no such invite to this email");
                        }
    
                    }
                }
                if (found == false){
                    throw new IDInvalidException("no such player");
                }
            }
    };


    /**
     * Get league invitations for non-existing players (email addresses)
     * 
     * @param leagueId The ID of the league being queried.
     * @return An array of email addresses of players with pending invites or an empty array if none exists.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public String[] getLeagueEmailInvites(int leagueId) throws IDInvalidException{       //FINISHED
        ArrayList<String> inviteList = new ArrayList<>();
        for (int i=0;i<potentialList.size();i++){
            boolean found = false;
            //if the potential player has a matching invite, add them to the list
            for (int y=0;y<potentialList.get(i).getInvites().size();y++){
                if (leagueId == potentialList.get(i).getInvites().get(y)){
                    found = true;
                }
            }
            if (found == true){
                inviteList.add(potentialList.get(i).getEmail());
            }
        }

        //convert the list into an array
        String[] inviteArray = new String[inviteList.size()];
        for (int i=0;i<inviteList.size();i++){
            inviteArray[i] = inviteList.get(i);
        }
        return inviteArray;
    };


    /**
     * Get league invitations made to existing players (player IDs)
     * 
     * @param leagueId The ID of the league being queried.
     * @return An array of player IDs who have pending invites or an empty array if none exists.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public int[] getLeaguePlayerInvites(int leagueId) throws IDInvalidException{        //FINISHED
        ArrayList<Integer> inviteList = new ArrayList<>();
        for (int i=0;i<playerList.size();i++){
            boolean found = false;
            //if the player has a matching invite, add them to the list
            for (int y=0;y<playerList.get(i).getInvites().size();y++){
                if (leagueId == playerList.get(i).getInvites().get(y)){
                    found = true;
                }
            }
            if (found == true){
                inviteList.add(playerList.get(i).getID());
            }
        }

        //convert the list into an array
        int[] inviteArray = new int[inviteList.size()];
        for (int i=0;i<inviteList.size();i++){
            inviteArray[i] = inviteList.get(i).intValue();
        }
        return inviteArray;
    };


    /**
     * Get the players in a league. 
     * The order of players should be the user that created the league,
     * (i.e. original owner), followed by other players in the order they accepted 
     * the league invitations.
     * 
     * @param leagueId The ID of the league being queried.
     * @return An array of player IDs in the league or an empty array if none exists.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public int[] getLeaguePlayers(int leagueId) throws IDInvalidException{         //FINISHED
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                int[] playerArray = new int[leagueList.get(i).getPlayers().size()];
                for (int y=0;y<leagueList.get(i).getPlayers().size();y++){
                    playerArray[y] = leagueList.get(i).getPlayers().get(y).intValue();
                }
                return playerArray;
            }
        }
        throw new IDInvalidException("no such league");
    };


    /**
     * Get the owners of a league.
     * 
     * @param leagueId The ID of the league being queried.
     * @return An array of player IDs that are owners of the league or empty array if none exists.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public int[] getLeagueOwners(int leagueId) throws IDInvalidException{    //FINISHED
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                int[] ownersArray = new int[leagueList.get(i).getOwners().size()];
                for (int y=0;y<leagueList.get(i).getOwners().size();y++){
                    ownersArray[y] = leagueList.get(i).getOwners().get(y).intValue();
                }
                return ownersArray;
            }
        }
        throw new IDInvalidException("ID doesn't match any existing league");
    };

    /**
     * Get the status of a league. Your code should look at the current local date
     * as epoch day and compare it with any start and end dates that have been set for the league
     * Note that leagues are created without a specified start/end date
     * 
     *  - If the league has start date is in the future (or no start date specified)
     *    the status should be PENDING
     * 
     *  - If the league has a specified start date in the past and 
     *    a specified end date in the future (or no specified end date) then 
     *    the status should be IN_PROGRESS
     * 
     *  - If the league has a specified end date in the past then
     *    the status should be CLOSED
     * 
     * @param leagueId The ID of the league being queried.
     * 
     * @return The status of the league as enum as above
     *          PENDING       not yet started
     *          IN_PROGRESS   league is active
     *          CLOSED        current date is after specified league end date 
     *  
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public Status getLeagueStatus(int leagueId ) 
        throws IDInvalidException{          //FINISHED
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    leagueList.get(i).updateStatus();
                    return leagueList.get(i).getStatus();
                }
            }
        
        throw new IDInvalidException("no such league");
    };


    /**
     * Start league
     *
     * @param leagueId The ID of the league to start.
     * @param day Should be set to the epoch day when league will be made active.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws IllegalOperationException If the league is already started.
     */
    public void setLeagueStartDate(int leagueId, int  day) 
        throws IDInvalidException, IllegalOperationException{    //FINISHED
            boolean found = false;
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;
                    leagueList.get(i).updateStatus();
                    if (leagueList.get(i).getStatus().equals(Status.PENDING)){
                        leagueList.get(i).setStartDate(LocalDate.ofEpochDay(Long.valueOf(day)));
                    }
                    else{
                        throw new IllegalOperationException("league is already started");
                    }
                }
            }
            if (found == false){
                throw new IDInvalidException("league not found");
            }
        
        };


    /** 
     * Close league, day specified may be any date after the league start day
     * 
     * @param leagueId The ID of the league to close.
     * @param day Should be set to the epoch day when league will be closed.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws IllegalOperationException If the league is already closed or invalid day.
     */
    public void setLeagueEndDate(int leagueId, int day) 
        throws IDInvalidException, IllegalOperationException{     //FINISHED
            boolean found = false;
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;
                    leagueList.get(i).updateStatus();
                    //check if status is closed
                    if (leagueList.get(i).getStatus().equals(Status.CLOSED)){
                        throw new IllegalOperationException("league is already closed");
                    }
                    else{
                        //check if date is valid
                        if (leagueList.get(i).getStartDate().isAfter(LocalDate.ofEpochDay(Long.valueOf(day)))){
                            throw new IllegalOperationException("invalid date");
                        }
                        else{
                            leagueList.get(i).setEndDate(LocalDate.ofEpochDay(Long.valueOf(day)));
                        }
                    }
                }
            }
            if (found == false){
                throw new IDInvalidException("league not found");
            }
    };


    /**
     * Get the league start date (as epoch day).
     * 
     * @param leagueId The ID of the league being queried.
     * @return The start date of the league as epoch day.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public int getLeagueStartDate(int leagueId) throws IDInvalidException{   //FINISHED
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                return (int)leagueList.get(i).getStartDate().toEpochDay();
            }
        }
        
        throw new IDInvalidException("no such league");
    };


    /**
     * Get the date a closed league closed date (as epoch day).
     * 
     * @param leagueId The ID of the league being queried.
     * @return The closure date of the league as epoch day or -1 if not closed.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public int getLeagueCloseDate(int leagueId) throws IDInvalidException{    //FINISHED
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                return (int)leagueList.get(i).getEndDate().toEpochDay();
            }
        }
        
        throw new IDInvalidException("no such league");
    };


    /**
     * Reset league by removing all gameplay history i.e. no scores, and gives it pending status. 
     * status of active/inactive players is unchanged.
     * 
     * @param leagueId The ID of the league to reset.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     */
    public void resetLeague(int leagueId) throws IDInvalidException{
        boolean found = false;
        //search for league
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                found = true;
                leagueList.get(i).resetLeague();
            }
        }

        if (found == false){
            throw new IDInvalidException("no such league");
        }
    };


    /**
     * Clone a league to make a new league. 
     * Owners of the original league are also owners of the new league.
     * Invitations are created for all players in the original league.
     * League status is set to pending.
     * 
     * @param leagueId The ID of the league to clone.
     * @param newName The name of the new league.
     * @return The ID of the new league.
     * @throws IDInvalidException If the ID does not match to any player in the system.
     * @throws InvalidNameException If the name is null, starts/ends with whitespace, 
     *                              is less than 1 characters or more than 20 characters.
     * @throws IllegalNameException if it duplicates an existing league name
     */
    public int cloneLeague(int leagueId, String newName) 
        throws IDInvalidException, IllegalNameException, IllegalNameException{
        //checks to see if the name is valid
        if (newName.equals(null)){
            throw new InvalidNameException("name cannot be null");
        }

        if (newName.length()<1|newName.length()>20){
            throw new InvalidNameException("invalid name size");
        }

        if (newName.charAt(0) == ' ' | newName.charAt(newName.length()-1) == ' '){
            throw new InvalidNameException("new display name cannot start or end with whitespace");
        }
        
        boolean valid = true;
            for (int i=0;i<leagueList.size();i++){
                if (leagueList.get(i).getName().equals(newName)){
                    valid = false;
                }
            }
        if (valid == false){
            throw new IllegalNameException("duplicate league names not allowed");
        }

        //search for league
        for (int i=0;i<leagueList.size();i++){
            if (leagueId == leagueList.get(i).getLeagueId()){
                //create league
                League temp = new League(leagueList.get(i).getOwners().get(0),newName,leagueList.get(i).getGameType());
                leagueList.add(temp);

                //add rest of owners
                for (int y=1;y<leagueList.get(i).getOwners().size();y++){
                    temp.addOwner(leagueList.get(i).getOwners().get(y));
                }

                //send invites to rest of players
                for (int y= leagueList.get(i).getOwners().size();y<leagueList.get(i).getPlayers().size();y++){
                    invitePlayerToLeague(temp.getLeagueId(),getPlayerEmail(leagueList.get(i).getPlayers().get(y)));
                }

                return temp.getLeagueId();
            }
        }

        throw new IDInvalidException("no such league");
    };


    /**
     * Checks if player is active in the league.
     * 
     * @param leagueId The ID of the league.
     * @param playerId The ID of the player.
     * 
     * @throws IDInvalidException If the ID does not match to any league or player in the league.
     */
    public boolean isLeaguePlayerActive(int leagueId, int playerId) 
        throws IDInvalidException{          // FINISHED
            //search for league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){

                    //search for player
                    for (int y=0;y<leagueList.get(i).getPlayers().size();y++){
                        if (playerId == leagueList.get(i).getPlayers().get(y)){
                            return leagueList.get(i).getActivity().get(y);
                        }
                    }
                    throw new IDInvalidException("no such player in the league");
                }
            }
            throw new IDInvalidException("no such league");
        
    };

    /** 
     * Sets a player to be registered as inactive in the league.
     * 
     * @param leagueId The ID of the league.
     * @param playerId The ID of the player.
     * 
     * @throws IDInvalidException If the ID does not match to any league or player in the system.
     * @throws IllegalOperationException If the ID does not match to any player in the league.
     */
    public void setLeaguePlayerInactive(int leagueId, int playerId) 
        throws IDInvalidException, IllegalOperationException {          //FINISHED
            //search for player in system
            boolean playerfound =false;
            for (int i=0;i<playerList.size();i++){
                if (playerId == playerList.get(i).getID()){
                    playerfound = true;
                }
            }
            if (playerfound==false){
                throw new IDInvalidException("no such player in system");
            }

            boolean found = false;
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;

                    //search for player in league
                    playerfound = false;
                    for (int y=0;y<leagueList.get(i).getPlayers().size();y++){
                        if (playerId == leagueList.get(i).getPlayers().get(y)){
                            playerfound=true;
                            leagueList.get(i).setActivity(y,false);
                        }
                    }
                    if (playerfound == false){
                        throw new IllegalOperationException("no such player");
                    }
                }
            }
            if (found == false){
                throw new IDInvalidException("no such league");
            }
    };

    /** 
     * Sets a player to be registered as active in the league.
     * 
     * @param leagueId The ID of the league.
     * @param playerId The ID of the player.
     * 
     * @throws IDInvalidException If the ID does not match to any league or player in the system.
     * @throws IllegalOperationException If the ID does not match to any player in the league.
     */
    public void setLeaguePlayerActive(int leagueId, int playerId) 
        throws IDInvalidException, IllegalOperationException{          //FINISHED
            //search for player in system
            boolean playerfound =false;
            for (int i=0;i<playerList.size();i++){
                if (playerId == playerList.get(i).getID()){
                    playerfound = true;
                }
            }
            if (playerfound==false){
                throw new IDInvalidException("no such player in system");
            }

            boolean found = false;
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;

                    //search for player
                    playerfound = false;
                    for (int y=0;y<leagueList.get(i).getPlayers().size();y++){
                        if (playerId == leagueList.get(i).getPlayers().get(y)){
                            playerfound=true;
                            leagueList.get(i).setActivity(y,true);
                        }
                    }
                    if (playerfound == false){
                        throw new IllegalOperationException("no such player");
                    }
                }
            }
            if (found == false){
                throw new IDInvalidException("no such league");
            }
    };


    /** 
     * Add owner   
     * 
     * @param leagueId The ID of the league to add the owner to.
     * @param playerId The ID of the player to add as an owner.
     * 
     * @throws IDInvalidException If the ID does not match to any league or player in the system.
     * @throws IllegalOperationException If the player is not a member of the league.
     * 
     */
    public void addOwner(int leagueId, int playerId) 
        throws IDInvalidException, IllegalOperationException{   //FINISHED
            boolean found = false;
            //search for league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;
                    boolean playerfound = false;
                    //search for player
                    for (int y=0;y<playerList.size();y++){
                        if (playerId == playerList.get(y).getID()){
                            playerfound = true;
                            //check is player is in league
                            if (leagueList.get(i).getPlayers().contains(playerId)){
                                leagueList.get(i).addOwner(playerId);
                            }
                            else{
                                throw new IllegalOperationException("player is not a member of the league");
                            }
                        }
                    }
                    if (playerfound == false){
                        throw new IDInvalidException("no such player");
                    }
                }
            }
            if (found == false){
                throw new IDInvalidException("no such league");
            }
    };

    /** 
     * Remove owner
     * 
     * @param leagueId The ID of the league to remove the owner from.
     * @param playerId The ID of the player to remove as an owner.
     * 
     * @throws IDInvalidException If the ID does not match to any league or player in the system.
     * @throws IllegalOperationException If the process would leave the league without an owner.
     * 
     */
    public void removeOwner(int leagueId, int playerId) 
        throws IDInvalidException, IllegalOperationException{     //FINISHED
            boolean found = false;
            //search for league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;
                    boolean playerfound = false;
                    //search for player
                    for (int y=0;y<playerList.size();y++){
                        if (playerId == playerList.get(y).getID()){
                            playerfound = true;
                            //check if league would be left without owner
                            if (leagueList.get(i).getOwners().size()==1  & leagueList.get(i).getOwners().contains(playerId)){
                                throw new IllegalOperationException("league would be left without an owner");
                            }
                            else{
                                leagueList.get(i).removeOwner(playerId);
                            }
                        }
                    }
                    if (playerfound == false){
                        throw new IDInvalidException("no such player");
                    }
                }
            }
            if (found == false){
                throw new IDInvalidException("no such league");
            }
    };


    // Results

    /**
     * Register gameplay by a player in a league. 
     * 
     * 
     * @param leagueId The ID of the league being queried.
     * @param playerId The ID of the player being queried.
     * @param day The epoch day the game was played.
     * @param gameReport A report detailing the gameplay, may be empty if no report made,
     *                   this may be updated e.g. after other players take actions that affect result
     *
     * @throws IDInvalidException If ID do not match to any league & player in the system.
     * @throws IllegalOperationException If the day is not a valid day for the league.
     */
    public void registerGameReport(int day, int leagueId,  int playerId, String gameReport ) 
        throws IDInvalidException, IllegalOperationException{
            boolean found =false;
            //search for league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //check if date is valid
                    if (day<(int)leagueList.get(i).getStartDate().toEpochDay()|day>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new IllegalOperationException("invalid date");
                    }

                    //search for player
                    boolean playerfound = false;
                    for (int y=0;y<leagueList.get(i).getPlayers().size();y++){
                        if (playerId == leagueList.get(i).getPlayers().get(y)){
                            found = true;
                            leagueList.get(i).registerReport(day, playerId, gameReport);
                        }
                    }
                    if (playerfound ==false){
                        throw new IDInvalidException("No such player in league");
                    }
                }
            }
            if (found==false){
                throw new IDInvalidException("no such league");
            }
    };


    /** 
     * Get the game report for a player in a league.
     * 
     * @param leagueId The ID of the league being queried.
     * @param playerId The ID of the player being queried.
     * @param day The epoch day the game was played.
     * 
     * @return The game report for the player in the league on the day, or empty string if no report entry.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not a valid day for the league.
     */
    public String getGameReport(int day, int leagueId,  int playerId) 
        throws IDInvalidException, InvalidDateException{
            //search for league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //check date is valid
                    if (day<(int)leagueList.get(i).getStartDate().toEpochDay()|day>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //search for player
                    for (int y=0;y<playerList.size();y++){
                        if (playerId == playerList.get(y).getID()){
                            return leagueList.get(i).getReport(day, playerId);
                        }
                    }
                    
                    throw new IDInvalidException("no such player");
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Register day game scores. Will be called when all play in a round is complete.
     * with scores ordered to match player array returned by getLeaguePlayers().
     * 
     * @param day The epoch day the game was played.
     * @param leagueId The ID of the league being queried.
     * @param scores The game scores with order to match the array returned by getLeaguePlayers().
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws IllegalArgumentException If the day specified has already been closed,
     *                                  or if current date is 2 days or more after the day being registered.
     */
    public void registerDayScores(int day, int leagueId, int[] scores) 
        throws IDInvalidException, IllegalArgumentException{
            //search for league
            boolean found = false;
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;

                    //check if date is valid
                    if ((int)LocalDate.now().toEpochDay()>=day+2|leagueList.get(i).getDayStatus(day)==Status.CLOSED){
                        throw new IllegalArgumentException("invalid date");
                    }

                    leagueList.get(i).registerScore(day, scores);
                }
            }
            
            if (found == false){
                throw new IDInvalidException("no such league");
            }
    };


    /**
     * Register a void day for a league - all points set to zero
     *
     * @param leagueId The ID of the league being queried.
     * @param playerId The ID of the league being queried.
     * @param day The epoch day being queried.
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws IllegalArgumentException If the day is not a valid day for the league,
     *                 or the current day is 2 days or more after the day being voided.
     */
    public void voidDayPoints(int day, int leagueId) 
        throws IDInvalidException, IllegalArgumentException{
            //search for league
            boolean found = false;
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    found = true;

                    //check date is valid
                    if (day<(int)leagueList.get(i).getStartDate().toEpochDay()|day>(int)leagueList.get(i).getEndDate().toEpochDay()|(int)LocalDate.now().toEpochDay()>=day+2){
                        throw new InvalidDateException("invalid date");
                    }
                    
                    leagueList.get(i).voidPoints(day);
                }
            }
            if (found == false){
                throw new IDInvalidException("no such league");
            }
    };  


    /**
     * Get the status of league games for a given day.
     * with results ordered to match player array returned by getLeaguePlayers().
     * @param leagueId The ID of the league being queried.
     * @param day The epoch day being queried.
     * 
     * @return The status of the day as enum
     *          PENDING       no gameplay yet
     *          IN_PROGRESS   active and still games to play
     *          CLOSED        when all players have played or day has ended   
     *  
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not a valid day for the league.
     */
    public Status getDayStatus(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){

                    //check date is valid
                    if (day<(int)leagueList.get(i).getStartDate().toEpochDay()|day>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    return leagueList.get(i).getDayStatus(day);
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the scores of a round for a given day.
     * with results ordered to match player array returned by getLeaguePlayers().
     *
     * @param leagueId The ID of the league being queried.
     * @param day The epoch day being queried.
     * 
     * @return An array of registered score results for each player from the day 
     *         or empty array if no gameplay from any players yet.
     *         (where gameplay is in progress the returned scores will be 0)
     *          
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not a valid day for the league.
     */
    public int[] getDayScores(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //check date is valid
                    if (day<(int)leagueList.get(i).getStartDate().toEpochDay()|day>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //put all the scores into an array
                    int[] dayScores = new int[leagueList.get(i).getPlayers().size()];
                    for (int y =0;y<leagueList.get(i).getPlayers().size();y++){
                        dayScores[y]=leagueList.get(i).getScore(day, leagueList.get(i).getPlayers().get(y));
                    }

                    //check if there is no gameplay from players yet
                    boolean play = false;
                    for (int y =0;y<dayScores.length;y++){
                        if (dayScores[y] != 0){
                            play = true;
                        }
                    }

                    if (play == false){
                        return new int[] {};
                    }

                    return dayScores;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the league points of a league for a given day.
     * with results ordered to match player array returned by getLeaguePlayers().
     *
     * @param leagueId The ID of the league being queried.
     * @param day The epoch day being queried.
     * 
     * @return An array of the league points for each player from the day 
     *         or empty array if league points have not been finalised yet. 
     *          
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not a valid day for the league.
     */
    public int[] getDayPoints(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //check date is valid
                    if (day<(int)leagueList.get(i).getStartDate().toEpochDay()|day>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //put all the points into an array
                    int[] dayPoints = new int[leagueList.get(i).getPlayers().size()];
                    for (int y =0;y<leagueList.get(i).getPlayers().size();y++){
                        dayPoints[y]=leagueList.get(i).getPoints(day, leagueList.get(i).getPlayers().get(y));
                    }

                    //check if there is no gameplay from players yet
                    boolean play = false;
                    for (int y =0;y<dayPoints.length;y++){
                        if (dayPoints[y] != 0){
                            play = true;
                        }
                    }

                    if (play == false){
                        return new int[] {};
                    }

                    return dayPoints;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the player rankings of a league for a given day, 
     * with results ordered to match player array returned by getLeaguePlayers().
     * 
     * @param leagueId The ID of the league being queried.
     * @param day The epoch day being queried.
     * 
     * @return an array containing the rankings of the players
     *         or empty array if rankings not yet available.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not a valid day for the league.
     */
    public int[] getDayRanking(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //check date is valid
                    if (day<(int)leagueList.get(i).getStartDate().toEpochDay()|day>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //rank player points
                    int[] rankings = new int[leagueList.get(i).getPlayers().size()];
                    int[] points = getDayPoints(leagueId, day);

                    int rank = 1;
                    int max = -1;
                    for (int z =0;z<points.length;){
                        for (int y =0;y<=points.length;y++){
                            if (points[y]>max){
                                max = points[y];
                            }
                        }
                        for (int y =0;y<=points.length;y++){
                            if (points[y]==max){
                                rankings[y]=rank;
                                points[y]=-1;
                                z++;
                            }
                        }
                        rank++;
                    }
                    return rankings;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the status of a league for a given week.
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the week being queried.
     * 
     * @return The status of the day as enum
     *          PENDING       no gameplay yet
     *          IN_PROGRESS   active and still games to play
     *          CLOSED        ended all games played or week ended    
     *  
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not within a valid week for the league.
     */
    public Status getWeekStatus(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the week
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfWeek() != DayOfWeek.MONDAY){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+7>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //determine status
                    boolean pending = true;
                    boolean closed = true;
                    for(int y=0;y<7;y++){
                        if (getDayStatus(leagueId, startday+y)==Status.IN_PROGRESS){
                            closed=false;
                            pending=false;
                        }
                        else if (getDayStatus(leagueId, startday+y)==Status.CLOSED){
                            pending=false;
                        }
                        else if (getDayStatus(leagueId, startday+y)==Status.PENDING){
                            closed = false;
                        }
                    }

                    //return correct status
                    if (pending){
                        return Status.PENDING;
                    }
                    else if (closed){
                        return Status.CLOSED;
                    }
                    else{
                        return Status.IN_PROGRESS;
                    }
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the league points of a league for a given week,
     * with results ordered to match player array returned by getLeaguePlayers().
     *  
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the week being queried.
     * 
     * @return An array of the points of each players total for the week (or part week played) 
     *         or empty array if no points yet.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not within a valid week for the league.
     */
    public int[] getWeekPoints(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the week
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfWeek() != DayOfWeek.MONDAY){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+7>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //add up all the points
                    int[] totalPoints = new int[leagueList.get(i).getPlayers().size()];
                    for(int y=0;y<7;y++){
                        int[] dayPoints = getDayPoints(leagueId, startday+y);
                        if (dayPoints.length!=0){
                            for (int z=0;z<dayPoints.length;z++){
                                totalPoints[z]+=dayPoints[z];
                            }
                        }
                    }

                    return totalPoints;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the ranking of a league for a given week.
     * with results ordered to match player array returned by getLeaguePlayers().
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the week being queried.
     * 
     * @return An array of the rankings of each players for the week (or part week played) 
     *         or empty array if no rankings yet.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException     If the day is not within a valid week for the league.
     */
    public int[] getWeekRanking(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the week
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfWeek() != DayOfWeek.MONDAY){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+7>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //rank player points
                    int[] rankings = new int[leagueList.get(i).getPlayers().size()];
                    int[] points = getWeekPoints(leagueId, day);

                    int rank = 1;
                    int max = -1;
                    for (int z =0;z<points.length;){
                        for (int y =0;y<=points.length;y++){
                            if (points[y]>max){
                                max = points[y];
                            }
                        }
                        for (int y =0;y<=points.length;y++){
                            if (points[y]==max){
                                rankings[y]=rank;
                                points[y]=-1;
                                z++;
                            }
                        }
                        rank++;
                    }
                    return rankings;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the status of a league for a given month.
     * 
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the month being queried.
     * 
     * @return The status of the month as enum
     *          PENDING       no gameplay yet
     *          IN_PROGRESS   active and still games to play
     *          CLOSED        ended all games played or month ended    
     *  
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not within a valid month for the league.
     */
    public Status getMonthStatus(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the month
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfMonth() != 1){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+LocalDate.ofEpochDay(startday).lengthOfMonth()-1>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //determine status
                    boolean pending = true;
                    boolean closed = true;
                    for(int y=0;y<LocalDate.ofEpochDay(startday).lengthOfMonth();y++){
                        if (getDayStatus(leagueId, startday+y)==Status.IN_PROGRESS){
                            closed=false;
                            pending=false;
                        }
                        else if (getDayStatus(leagueId, startday+y)==Status.CLOSED){
                            pending=false;
                        }
                        else if (getDayStatus(leagueId, startday+y)==Status.PENDING){
                            closed = false;
                        }
                    }

                    //return correct status
                    if (pending){
                        return Status.PENDING;
                    }
                    else if (closed){
                        return Status.CLOSED;
                    }
                    else{
                        return Status.IN_PROGRESS;
                    }
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the league points of a league for a given month.
     * with results ordered to match player array returned by getLeaguePlayers().
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the month being queried.
     * 
     * @return An array of the points of each players total for the month (or part month played) 
     *         or empty array if no points yet.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not within a valid month for the league.
     */
    public int[] getMonthPoints(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the month
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfMonth() != 1){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+LocalDate.ofEpochDay(startday).lengthOfMonth()-1>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //add up all the points
                    int[] totalPoints = new int[leagueList.get(i).getPlayers().size()];
                    for(int y=0;y<LocalDate.ofEpochDay(startday).lengthOfMonth();y++){
                        int[] dayPoints = getDayPoints(leagueId, startday+y);
                        if (dayPoints.length!=0){
                            for (int z=0;z<dayPoints.length;z++){
                                totalPoints[z]+=dayPoints[z];
                            }
                        }
                    }

                    return totalPoints;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the ranking of a league for a given month.
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the month being queried.
     * 
     * @return An array of the rankings of each players for the month (or part month played) 
     *         or empty array if no rankings yet.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException     If the day is not within a valid month for the league.
     */
    public int[] getMonthRanking(int leagueId, int day ) 
            throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the week
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfMonth() != 1){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+LocalDate.ofEpochDay(startday).lengthOfMonth()-1>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //rank player points
                    int[] rankings = new int[leagueList.get(i).getPlayers().size()];
                    int[] points = getMonthPoints(leagueId, day);

                    int rank = 1;
                    int max = -1;
                    for (int z =0;z<points.length;){
                        for (int y =0;y<=points.length;y++){
                            if (points[y]>max){
                                max = points[y];
                            }
                        }
                        for (int y =0;y<=points.length;y++){
                            if (points[y]==max){
                                rankings[y]=rank;
                                points[y]=-1;
                                z++;
                            }
                        }
                        rank++;
                    }
                    return rankings;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the status of a league for a given year.
     * 
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the year being queried.
     * 
     * @return The status of the day as enum
     *          PENDING       no gameplay yet
     *          IN_PROGRESS   active and still games to play
     *          CLOSED        ended all games played or year has ended    
     *  
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not within a valid year for the league.
     */
    public Status getYearStatus(int leagueId, int day ) 
            throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the year
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfYear() != 1){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+LocalDate.ofEpochDay(startday).lengthOfYear()-1>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //determine status
                    boolean pending = true;
                    boolean closed = true;
                    for(int y=0;y<LocalDate.ofEpochDay(startday).lengthOfYear();y++){
                        if (getDayStatus(leagueId, startday+y)==Status.IN_PROGRESS){
                            closed=false;
                            pending=false;
                        }
                        else if (getDayStatus(leagueId, startday+y)==Status.CLOSED){
                            pending=false;
                        }
                        else if (getDayStatus(leagueId, startday+y)==Status.PENDING){
                            closed = false;
                        }
                    }

                    //return correct status
                    if (pending){
                        return Status.PENDING;
                    }
                    else if (closed){
                        return Status.CLOSED;
                    }
                    else{
                        return Status.IN_PROGRESS;
                    }
                }
            }
            throw new IDInvalidException("no such league");               
    };


    /**
     * Get the league points of a league for a given year.
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the year being queried.
     * 
     * @return An array of the league points of each players total for the year (or part year played) 
     *         or empty array if no points yet.
     * 
     * @throws IDInvalidException If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not within a valid month for the league.
     */
    public int[] getYearPoints(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the year
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfYear() != 1){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+LocalDate.ofEpochDay(startday).lengthOfYear()-1>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //add up all the points
                    int[] totalPoints = new int[leagueList.get(i).getPlayers().size()];
                    for(int y=0;y<LocalDate.ofEpochDay(startday).lengthOfYear();y++){
                        int[] dayPoints = getDayPoints(leagueId, startday+y);
                        if (dayPoints.length!=0){
                            for (int z=0;z<dayPoints.length;z++){
                                totalPoints[z]+=dayPoints[z];
                            }
                        }
                    }

                    return totalPoints;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Get the ranking of a league for a given year.
     * 
     * @param leagueId  The ID of the league being queried.
     * @param day       Epoch day that is within the year being queried.
     * 
     * @return An array of the rankings of each players for the year (or part year played) 
     *         or empty array if no rankings yet.
     * 
     * @throws IDInvalidException   If the ID does not match to any league in the system.
     * @throws InvalidDateException If the day is not within a valid year for the league.
     **/
    public int[] getYearRanking(int leagueId, int day ) 
        throws IDInvalidException, InvalidDateException{
            //search for matching league
            for (int i=0;i<leagueList.size();i++){
                if (leagueId == leagueList.get(i).getLeagueId()){
                    //find start of the week
                    int startday=day;
                    while (LocalDate.ofEpochDay(startday).getDayOfYear() != 1){
                        startday-=1;
                    }

                    //check if date is valid
                    if (startday<(int)leagueList.get(i).getStartDate().toEpochDay() | startday+LocalDate.ofEpochDay(startday).lengthOfYear()-1>(int)leagueList.get(i).getEndDate().toEpochDay()){
                        throw new InvalidDateException("invalid date");
                    }

                    //rank player points
                    int[] rankings = new int[leagueList.get(i).getPlayers().size()];
                    int[] points = getYearPoints(leagueId, day);

                    int rank = 1;
                    int max = -1;
                    for (int z =0;z<points.length;){
                        for (int y =0;y<=points.length;y++){
                            if (points[y]>max){
                                max = points[y];
                            }
                        }
                        for (int y =0;y<=points.length;y++){
                            if (points[y]==max){
                                rankings[y]=rank;
                                points[y]=-1;
                                z++;
                            }
                        }
                        rank++;
                    }
                    return rankings;
                }
            }
            throw new IDInvalidException("no such league");
    };


    /**
     * Method empties this GamesLeague instance of its contents and resets all
     * internal counters.
     */
    public void eraseGamesLeagueData(){
        this.playerList = new ArrayList<>();
        this.leagueList = new ArrayList<>();
        this.potentialList = new ArrayList<>();
    };


    /**
     * Method saves this GamesLeague instance contents into a serialised file,
     * with the filename given in the argument.
     * <p>
     * The state of this GamesLeague instance must be unchanged if any
     * exceptions are thrown.
     *
     * @param filename Location of the file to be saved.
     * @throws IOException If there is a problem experienced when trying to save the 
     *                     contents to the file.
     */
    public void saveGamesLeagueData(String filename) throws IOException{
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));
        out.writeObject(playerList);
        out.writeObject(leagueList);
        out.writeObject(potentialList);
        out.close();
    };


    /**
     * Method should load and replace this GamesLeague instance contents with the
     * serialised contents stored in the file given in the argument.
     *
     * @param filename Location of the file to be loaded.
     * @throws IOException If there is a problem experienced when trying
     *                     to load the store contents from the file.
     * @throws ClassNotFoundException If required class files cannot be found when loading.
     */
    public void loadGamesLeagueData(String filename) throws IOException, ClassNotFoundException{
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));
            playerList = (ArrayList<Player>) in.readObject();
            leagueList = (ArrayList<League>) in.readObject();
            potentialList = (ArrayList<PotentialPlayer>) in.readObject();
            in.close();
        }
        catch(IOException e){
            System.out.println("file not found");
        }
        catch(ClassNotFoundException e){
            System.out.println("invalid objects in file");
        }
    };
}