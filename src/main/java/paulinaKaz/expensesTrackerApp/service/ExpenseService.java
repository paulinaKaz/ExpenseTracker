package paulinaKaz.expensesTrackerApp.service;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paulinaKaz.expensesTrackerApp.dao.ExpenseDao;
import paulinaKaz.expensesTrackerApp.model.Expense;
import paulinaKaz.expensesTrackerApp.util.Category;
import paulinaKaz.expensesTrackerApp.util.CurrentDate;
import paulinaKaz.expensesTrackerApp.util.Month;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static paulinaKaz.expensesTrackerApp.util.Messages.LAST_30_DAYS_LIST_HEADER;
import static paulinaKaz.expensesTrackerApp.util.Messages.MONTHLY_EXPENSES_LIST_HEADER;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseDao expenseDao;
    @Autowired
    private CurrentDate currentDate;

    public Expense findById(int id) {
        return expenseDao.findById(id);
    }

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
        Date dateBefore30Days = DateUtils.addDays(currentDate.getCurrentDate(), -30);
        return sortList(expenseDao.getExpensesSinceDate(dateBefore30Days));
    }


    public List<Expense> filterExpenseListByCategory(List<Expense> expenseList, String category) {
        if (EnumUtils.isValidEnum(Category.class, category)) {
            Category categoryAsEnum = Enum.valueOf(Category.class, category);
            return expenseList.stream().filter(expense -> expense.getCategory() == categoryAsEnum).sorted()
                    .collect(Collectors.toList());
        }
        return null;
    }

    public String createListHeader(String month, int year) {
        if (EnumUtils.isValidEnum(Month.class, month)) {
            Month monthAsEnum = Enum.valueOf(Month.class, month);
            StringBuilder builderHeader = new StringBuilder();
            builderHeader.append(MONTHLY_EXPENSES_LIST_HEADER);
            builderHeader.append(monthAsEnum.getMonthName());
            builderHeader.append(" ");
            builderHeader.append(year);
            return builderHeader.toString();
        } else if (year != 0) {
            StringBuilder builderHeader = new StringBuilder();
            builderHeader.append(MONTHLY_EXPENSES_LIST_HEADER);
            builderHeader.append(year);
            return builderHeader.toString();
        } else return LAST_30_DAYS_LIST_HEADER;
    }

    private List<Expense> sortList(List<Expense> expenseList) {
        Collections.sort(expenseList);
        return expenseList;
    }


}