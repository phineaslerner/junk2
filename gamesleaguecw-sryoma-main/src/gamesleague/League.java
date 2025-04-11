package gamesleague;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*; 

class League {
    // static counter
    private static int idCounter = 1;
    private int leagueId;
    private String name;
    private GameType gameType;
    private ArrayList<Integer> owners;
    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;
    private ArrayList<Integer> players;
    private ArrayList<Boolean> activity;
    private ArrayList<ArrayList<String>> playerReports; //list of lists for player reports
    private ArrayList<ArrayList<Integer>> playerScores;
    private ArrayList<ArrayList<Integer>> playerPoints;

    // league construction (I think i needed to do this?)
    public League(int ownerId, String name, GameType gameType) {
        //I saw something on stack about syncronizing this but I couldnt figure it out
        this.leagueId = idCounter++;
        this.name = name;
        this.gameType = gameType;
        this.owners = new ArrayList<>();
        this.players = new ArrayList<>();
        this.activity = new ArrayList<>();
        this.playerReports = new ArrayList<>(); //initialise playerScores list
        this.playerScores = new ArrayList<>();
        this.playerPoints = new ArrayList<>();

        this.owners.add(ownerId);
        this.players.add(ownerId);
        this.activity.add(true);
        initializePlayer(ownerId); //add ownners score list
        this.status = Status.PENDING;
        this.startDate=LocalDate.MAX;
        this.endDate=LocalDate.MAX;
        
    }
// get league id
    public int getLeagueId() {
        return leagueId;
    }
// ger league name
    public String getName() {
        return name;
    }
// update league name
    public void setName(String newName) {
        this.name = newName;
    }
// get owners
    public List<Integer> getOwners() {
        return owners;
    }
// add owners
    public void addOwner(int playerId) {
        owners.add(playerId);
    }
// remove owners
    public void removeOwner(int playerId) {
        owners.remove(Integer.valueOf(playerId));
    }

    public List<Integer> getPlayers() {
        return players;
    }

    public void addPlayer(int playerId) {
        players.add(playerId);
        activity.add(true);
        initializePlayer(playerId); //add new player score list
    }

    public void removePlayer(int playerId) {
        int index = players.indexOf(Integer.valueOf(playerId)); //find index
        if (index != -1) { // ensure player exists before removal
        
            activity.remove(players.indexOf(Integer.valueOf(playerId)));
            players.remove(Integer.valueOf(playerId));

            playerReports.removeIf(scoreList -> scoreList.get(0).equals(Integer.toString(playerId)));
        }
    }

    public void updateStatus(){
        if (LocalDate.now().isBefore(startDate)){
            status = Status.PENDING;
        }
        if (LocalDate.now().isAfter(endDate)){
            status = Status.CLOSED;
        }
        if (LocalDate.now().isAfter(startDate) && LocalDate.now().isBefore(endDate)){
            status = Status.IN_PROGRESS;
        }

    }

// get status
    public Status getStatus() {
        return status;
    }

    public GameType getGameType(){
        return gameType;
    }

// set start date
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

// set end date
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate(){
        return endDate;
    }

    public ArrayList<Boolean> getActivity(){
        return activity;
    }

    public void setActivity(int index, Boolean newActivity){
        activity.set(index, newActivity);
    }

    public void initializePlayer(int playerId) { //initialize player score list
        ArrayList<String> reportList = new ArrayList<>();
        reportList.add(Integer.toString(playerId)); //first entry is playerid
        playerReports.add(reportList);

        ArrayList<Integer> scoreList = new ArrayList<>();
        scoreList.add(playerId);
        playerScores.add(scoreList);
        playerPoints.add(scoreList);
    }

    public String getReport(int day, int playerId){
        for (int i=0;i<playerReports.size();i++){
            if (Integer.toString(playerId).equals(playerReports.get(i).get(0))){
                if (playerReports.get(i).size()<day-(int)startDate.toEpochDay()+2){
                    return"";
                }
                return playerReports.get(i).get(day-(int)startDate.toEpochDay()+1);
            }
        }
        return "";
    }

    public ArrayList<String> getPlayerReportList(int playerId){
        for (int i=0;i<playerReports.size();i++){
            if (Integer.toString(playerId).equals(playerReports.get(i).get(0))){
                return playerReports.get(i);
            }
        }
        return new ArrayList<>();
    }

    public void anonReport(int playerId){
        for (int i=0;i<playerReports.size();i++){
            if (Integer.toString(playerId).equals(playerReports.get(i).get(0))){
                for (int y=1;y<playerReports.get(i).size();y++){
                    playerReports.get(i).set(y,"");
                }
            }
        }
    }

    public int getScore(int day, int playerId){
        for (int i=0;i<playerScores.size();i++){
            if (playerId == playerScores.get(i).get(0)){
                if (playerScores.get(i).size()<day-(int)startDate.toEpochDay()+2){
                    return 0;
                }
                return playerScores.get(i).get(day-(int)startDate.toEpochDay()+1);
            }
        }
        return 0;
    }

    public int getPoints(int day, int playerId){
        for (int i=0;i<playerPoints.size();i++){
            if (playerId == playerPoints.get(i).get(0)){
                if (playerPoints.get(i).size()<day-(int)startDate.toEpochDay()+2){
                    return 0;
                }
                return playerPoints.get(i).get(day-(int)startDate.toEpochDay()+1);
            }
        }
        return 0;
    }

    public void registerReport(int day,int playerId,String gameReport){
        for (int i=0;i<playerReports.size();i++){
            if (Integer.toString(playerId).equals(playerReports.get(i).get(0))){
                if (playerReports.get(i).size()<day-(int)startDate.toEpochDay()+2){
                    for (int y=0;y<(day-(int)startDate.toEpochDay()+2-playerReports.get(i).size());y++){
                        playerReports.get(i).add("");
                    }
                }
                playerReports.get(i).set(day-(int)startDate.toEpochDay()+1,gameReport);
            }
        }
    }

    public void registerScore(int day, int[] scores){
        //add scores to list
        for (int i=0;i<playerScores.size();i++){
            //extend the list if it is too short
            if (playerScores.get(i).size()<day-(int)startDate.toEpochDay()+2){
                for (int y=0;y<(day-(int)startDate.toEpochDay()+2-playerScores.get(i).size());y++){
                    playerScores.get(i).add(0);
                }
            }

            playerScores.get(i).set(day-(int)startDate.toEpochDay()+1,scores[i]);
        }

        // add points to list
        int winner = -1;
        int runnerup = -1;
        if (gameType == GameType.DICEROLL){
            winner = 999;
            runnerup = 999;
            for (int i=0;i<scores.length;i++){
                if (scores[i]<winner){
                    runnerup=winner;
                    winner=scores[i];
                }
                else if (scores[i]<runnerup){
                    runnerup=scores[i];
                }
            }
        }
        else{
            winner = 0;
            runnerup = 0;
            for (int i=0;i<scores.length;i++){
                if (scores[i]>winner){
                    runnerup=winner;
                    winner=scores[i];
                }
                else if (scores[i]>runnerup){
                    runnerup=scores[i];
                }
            }
        }

        for (int i=0;i<scores.length;i++){
            if (playerPoints.get(i).size()<day-(int)startDate.toEpochDay()+2){
                for (int y=0;y<(day-(int)startDate.toEpochDay()+2-playerScores.get(i).size());y++){
                    playerScores.get(i).add(0);
                }
            }
            if (scores[i]==winner){
                playerPoints.get(i).set(day-(int)startDate.toEpochDay()+1,3);
            }
            else if (scores[i]==runnerup){
                playerPoints.get(i).set(day-(int)startDate.toEpochDay()+1,1);
            }
        }
    }

    public void voidPoints(int day){
        for (int i=0;i<playerPoints.size();i++){
            if (playerPoints.get(i).size()<day-(int)startDate.toEpochDay()+2){
                for (int y=0;y<(day-(int)startDate.toEpochDay()+2-playerScores.get(i).size());y++){
                    playerScores.get(i).add(0);
                }
            }

            playerPoints.get(i).set(day-(int)startDate.toEpochDay()+1,0);
        }
    }

    public Status getDayStatus(int day){
        //if day has ended
        if((int)LocalDate.now().toEpochDay()>day){
            return Status.CLOSED;
        }

        //count the number of unplayed games
        int count = 0;
        for (int i=0;i<playerReports.size();i++){
            if (playerReports.get(i).get(day-(int)startDate.toEpochDay()+1).equals("")|playerPoints.get(i).size()<day-(int)startDate.toEpochDay()+2){
                count++;
            }
        }
        if (count == 0){
            return Status.CLOSED;
        }
        else if (count == playerReports.size()){
            return Status.PENDING;
        }
        else{
            return Status.IN_PROGRESS;
        }
    }

    public void resetLeague(){
        status = Status.PENDING;
        startDate=LocalDate.MAX;
        endDate=LocalDate.MAX;
        playerReports = new ArrayList<>();
        playerScores = new ArrayList<>();
        playerPoints = new ArrayList<>();

        for (int i = 0; i<players.size();i++){
            initializePlayer(players.get(i));
        }
    }
}