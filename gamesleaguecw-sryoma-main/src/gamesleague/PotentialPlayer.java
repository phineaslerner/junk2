package gamesleague;
import java.util.*; 

public class PotentialPlayer{
    protected String email;
    protected ArrayList<Integer> invites;

    public PotentialPlayer(String email){
        this(email, new ArrayList<Integer>());
    }
    public PotentialPlayer(String email, ArrayList<Integer> invites){
        this.email=email;
        this.invites = invites;
    }

    public String getEmail(){
        return email;
    }

    public ArrayList<Integer> getInvites(){
        return invites;
    }

    public void addInvite(int inviteid) {
        invites.add(inviteid);
    }

    public void removeInvite(int inviteid) {
        invites.remove(Integer.valueOf(inviteid));
    }
}