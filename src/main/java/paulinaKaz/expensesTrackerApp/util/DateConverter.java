package paulinaKaz.expensesTrackerApp.util;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {


    public static String convertDateToString(Date date) {
        Format format = new SimpleDateFormat("MM/dd/yyyy");
        return format.format(date);
    }

}

