package gamesleague;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String m) {
        super(m);
    }
}