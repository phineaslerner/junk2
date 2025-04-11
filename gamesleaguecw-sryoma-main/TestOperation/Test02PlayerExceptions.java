import gamesleague.*;

import java.util.Arrays;

public class Test02PlayerExceptions {
    public static void main(String[] args) {
        System.out.println("\n#### Running Test02PlayerExceptions...\n");

        GamesLeague gl = new GamesLeague();

        try {
            System.out.println("EXCEPTION TEST 0: Try to find non-existant ");
            String pEmail = gl.getPlayerEmail(-99999);
            System.out.println("ERROR 0: IDInvalidException not thrown");
        } catch (IDInvalidException e) { // if error edit IDInvalidException to be public
            System.out.println("PASS 0: Caught IDInvalidException: " + e.getMessage());
        }


        try {
            System.out.println("EXCEPTION TEST 1: Try to create player with empty email");
            gl.createPlayer("",  "BigJW", "Josh Wardle", "123-456-7890");
            System.out.println("ERROR 1: InvalidEmailException not thrown");
        } catch (InvalidEmailException e) {
            System.out.println("PASS 1: Caught InvalidEmailException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 2: Try to create player with email without @");
            gl.createPlayer("nothinghere-at-gmail.com",  "BigJW", "Josh Wardle", "123-456-7890");
            System.out.println("ERROR 2: InvalidEmailException not thrown");
        } catch (InvalidEmailException e) {
            System.out.println("PASS 2: Caught InvalidEmailException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 3: Try to create player with null displayName");
            gl.createPlayer("wardlejj@wardle.com",  null, "Josh Wardle", "123-456-7890");
            System.out.println("ERROR 3: InvalidNameException not thrown");
        } catch (InvalidNameException e) {
            System.out.println("PASS 3: Caught InvalidNameException: " + e.getMessage());
        }

        try {
            System.out.println("EXCEPTION TEST 4: Try to create player with existing email");
            gl.createPlayer("wardlejj@wardle.com",  "BigJW", "Josh Wardle", "123-456-7890");
            gl.createPlayer("wardlejj@wardle.com",  "LittleJW", "Josh Wardle", "123-456-7899");
            System.out.println("ERROR 4: IllegalEmailException not thrown");
        } catch (IllegalEmailException e) { // if error edit IllegalEmailException.java to be public
            System.out.println("PASS 4: Caught IllegalEmailException: " + e.getMessage());
        }



    }
}