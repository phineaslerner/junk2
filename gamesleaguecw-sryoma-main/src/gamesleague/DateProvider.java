package gamesleague;

import java.time.LocalDate;

/**
 * The DateProvider class provides a way to manipulate the current date by applying an offset.
 * This can be useful for testing purposes or simulating different dates.
 * Put  DateProvider.now()  instead of  DateProvider.now()  where it appears in your project.
 * 
 * In your test app you can then use the following code to manipulate the date for testing.
 * When testing use methods DateProvider.addDay() and DateProvider.setOffset() 
 * to manipulate the date and simulate the passage of time.
 */
public class DateProvider {

    private static int offset = 0;

    /**
     * Sets the offset in days to be applied to the current date.
     *
     * @param offset the number of days to offset the current date
     */
    public static void setOffset(int offset) {
        DateProvider.offset = offset;
    }

    /**
     * Sets the DateProvider to a particular date.
     *
     * @param date the date to set the offset to
     */
    public static void setDate(LocalDate date) {
        DateProvider.setOffset(0);
        DateProvider.setOffset((int)date.toEpochDay()-(int)DateProvider.now().toEpochDay());
    }

    /**
     * Increments the offset by one day.
     */
    public static void addDay() {
        DateProvider.offset++;
    }

    /**
     * Returns the current date with the applied offset.
     *
     * @return the current date plus the offset in days
     */
    public static LocalDate now() {
        // roundabout way to get DateProvider.now() without 
        // calling that command.
        // This avoids find and replace issues when
        // replacing DateProvider.now() with DateProvider.now()
        // in a project.
        LocalDate date = LocalDate.ofEpochDay(1); 
        return date.now().plusDays(offset);
    }
}