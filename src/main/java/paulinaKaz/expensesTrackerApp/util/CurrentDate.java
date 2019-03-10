package paulinaKaz.expensesTrackerApp.util;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CurrentDate {

    public Date getCurrentDate(){
        return new Date();
    }
}
