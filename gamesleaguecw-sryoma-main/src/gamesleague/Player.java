package gamesleague;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*; 

public class Player extends PotentialPlayer{
    private static int idCounter=0;
    private String email;
    private String displayName;
    private String name;
    private String phone;
    private LocalDate date;
    private int id;
    private ArrayList<Integer> invites;

    public Player(String email,String displayName, String name,String phone){
        this(email,displayName,name,phone,new ArrayList<Integer>());
    }

    public Player(String email,String displayName, String name,String phone,ArrayList<Integer> invites){
        super(email, invites);
        this.displayName=displayName;
        this.name=name;
        this.phone=phone;
        this.date = LocalDate.now();
        this.id = ++idCounter;
    }

    public int getID(){
        return id;
    }

    public String getDisplayName(){
        return displayName;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setDisplayName(String displayName){
        this.displayName=displayName;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public void setPhone(String phone){
        this.phone=phone;
    }
}