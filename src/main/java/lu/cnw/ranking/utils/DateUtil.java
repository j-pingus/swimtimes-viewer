package lu.cnw.ranking.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {
    public static Integer THIS_YEAR = null;
    static String[] yearsOfInterest = null;

    public static int thisYear() {
        if (THIS_YEAR == null) {
            Calendar c = new GregorianCalendar();
            c.setTime(new Date());
            THIS_YEAR = c.get(Calendar.YEAR);
        }
        return THIS_YEAR;

    }

    public static String[] getYearsOfInterest() {
        if (yearsOfInterest == null) {
            int currentYear = thisYear();
            yearsOfInterest = new String[]{
                    "" + currentYear,
                    "" + (currentYear - 1)
            };
        }
        return yearsOfInterest;
    }

    public static boolean isYearOfInteres(String date) {
        return date.contains(getYearsOfInterest()[0]) ||
                date.contains(getYearsOfInterest()[1]);
    }
}
