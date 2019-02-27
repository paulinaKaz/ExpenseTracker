package paulinaKaz.expensesTrackerApp.util;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {


    public static String convertDateToString(Date date) {
        Format format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }

    public static Date convertStringToDate(String date) {
        try {
            return new SimpleDateFormat("MM/dd/yyyy").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

}

