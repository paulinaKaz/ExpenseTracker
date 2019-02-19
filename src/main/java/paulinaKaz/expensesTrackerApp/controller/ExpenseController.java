package paulinaKaz.expensesTrackerApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import paulinaKaz.expensesTrackerApp.model.Expense;
import paulinaKaz.expensesTrackerApp.service.ExpenseService;
import paulinaKaz.expensesTrackerApp.util.Category;
import paulinaKaz.expensesTrackerApp.util.DateConverter;
import paulinaKaz.expensesTrackerApp.util.Month;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static paulinaKaz.expensesTrackerApp.util.Messages.DELETED;
import static paulinaKaz.expensesTrackerApp.util.Messages.SESSION;
import static paulinaKaz.expensesTrackerApp.util.ViewsAndRedirections.*;

@Controller
@SessionAttributes("monthlyExpenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


    @GetMapping("/add")
    public String showExpenseForm(Model model) {
        Expense expense = new Expense();
        model.addAttribute(expense);
        model.addAttribute("defaultDate", DateConverter.convertDateToString(new Date()));
        model.addAttribute("categories", Category.values());
        model.addAttribute("month", Month.values());
        return EXPENSE_FORM;
    }

    @PostMapping("/add")
    public String processExpenseForm(@Valid Expense expense, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return EXPENSE_FORM;
        }
        model.addAttribute(expense);
        model.addAttribute("month", Month.values());
        expenseService.saveOrUpdateExpense(expense);
        return EXPENSE_VIEW;
    }

    @GetMapping("/showSpecificMoth")
    @SuppressWarnings("unchecked")
    public String showSpecificMonthOrYear(String month, Integer year, Model model) {
        List<Expense> expenseList = expenseService.getExpensesForSpecificMonthOrYear(month, year);
        model.addAttribute("monthlyExpenses", expenseList);
        model.addAttribute("month", Month.values());
        model.addAttribute("categories", Category.values());
        model.addAttribute("message", expenseService.getMessage(month, year));
        return LIST_VIEW;
    }

    @GetMapping({"/showLast30Days", "/home"})
    public String showLast30Days(Model model) {
        List<Expense> expenseList = expenseService.getExpensesFromLast30Days();
        model.addAttribute("monthlyExpenses", expenseList);
        model.addAttribute("month", Month.values());
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", "all");
        model.addAttribute("message", expenseService.getMessage(null, 0));
        return LIST_VIEW;
    }

    @GetMapping("/expensesList")
    public String showExpensesForSpecificCategory(Model model, String selectedCategory, HttpSession session, String message) {
        try {
            List<Expense> expenseList = (List<Expense>) session.getAttribute("monthlyExpenses");
            model.addAttribute("filteredMonthlyExpenses",
                    expenseService.getExpensesForSpecificCategory(expenseList, selectedCategory));
        } catch (NullPointerException ex) {
            model.addAttribute("message", SESSION);
            return MESSAGE;
        }
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", selectedCategory);
        model.addAttribute("month", Month.values());
        model.addAttribute("message", message);
        return LIST_VIEW;
    }

    @GetMapping("/showExpense")
    public String showExpense(int id, Model model) {
        Expense chosenExpense = expenseService.findById(id);
        model.addAttribute("expense", chosenExpense);
        model.addAttribute("month", Month.values());
        return EXPENSE_VIEW;
    }

    @GetMapping("/edit")
    public String showExpenseEditForm(int id, Model model) {
        Expense editedExpense = expenseService.findById(id);
        model.addAttribute(editedExpense);
        model.addAttribute("categories", Category.values());
        model.addAttribute("month", Month.values());
        return EDIT_FORM;
    }

    @PostMapping("/edit")
    public String processExpenseEditForm(@Valid Expense expense, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Category.values());
            model.addAttribute("month", Month.values());
            return EDIT_FORM;
        }
        model.addAttribute(expense);
        model.addAttribute("month", Month.values());
        expenseService.saveOrUpdateExpense(expense);
        return EXPENSE_VIEW;
    }

    @PostMapping("/delete/{id}")
    public String deleteExpense(@PathVariable("id") int id, Model model) {
        expenseService.deleteExpense(id);
        model.addAttribute("message", DELETED);
        model.addAttribute("month", Month.values());
        return MESSAGE;
    }

}
