package paulinaKaz.expensesTrackerApp.service;

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paulinaKaz.expensesTrackerApp.dao.ExpenseDao;
import paulinaKaz.expensesTrackerApp.model.Expense;
import paulinaKaz.expensesTrackerApp.util.Category;
import paulinaKaz.expensesTrackerApp.util.Month;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static paulinaKaz.expensesTrackerApp.util.Messages.LAST_30_DAYS_LIST_MESSAGE;
import static paulinaKaz.expensesTrackerApp.util.Messages.MONTHLY_EXPENSES_LIST_MESSAGE;


@Service
public class ExpenseService {

    @Autowired
    private ExpenseDao expenseDao;


    public void saveOrUpdateExpense(Expense expense) {
        expenseDao.saveOrUpdateExpense(expense);
    }

    public void deleteExpense(int id) {
        Expense expense = findById(id);
        expenseDao.deleteExpense(expense);
    }

    public List<Expense> getExpensesForSpecificMonthOrYear(String month, Integer year) {
        if (EnumUtils.isValidEnum(Month.class, month)) {
            Month monthAsEnum = Enum.valueOf(Month.class, month);
            return sortList(expenseDao.getExpensesFromSpecificMonth(monthAsEnum.getMonthNumber(), year));
        } else return sortList(expenseDao.getExpensesForYear(year));
    }

    public List<Expense> getExpensesFromLast30Days() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date d = calendar.getTime();
        return sortList(expenseDao.getExpensesFromLast30Days(d));
    }

    public Expense findById(int id) {
        return expenseDao.findById(id);
    }

    public List<Expense> getExpensesForSpecificCategory(List<Expense> expenseList, String category) {
        if (EnumUtils.isValidEnum(Category.class, category)) {
            Category categoryAsEnum = Enum.valueOf(Category.class, category);
            return expenseList.stream().filter(expense -> expense.getCategory() == categoryAsEnum).sorted()
                    .collect(Collectors.toList());
        }
        return null;
    }

    public String getMessage(String month, int year) { //nazwa metody createListHeader
        if (EnumUtils.isValidEnum(Month.class, month)) {
            Month monthAsEnum = Enum.valueOf(Month.class, month);
            return MONTHLY_EXPENSES_LIST_MESSAGE + monthAsEnum.getMonthName() + " " + year; // tu mozna uzyc stringbuilder
        } else return LAST_30_DAYS_LIST_MESSAGE;
    }

    private List<Expense> sortList(List<Expense> expenseList) {
        Collections.sort(expenseList);
        return expenseList;
    }


}