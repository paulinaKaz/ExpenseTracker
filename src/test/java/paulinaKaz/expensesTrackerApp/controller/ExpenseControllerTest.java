package paulinaKaz.expensesTrackerApp.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import paulinaKaz.expensesTrackerApp.model.Expense;
import paulinaKaz.expensesTrackerApp.service.ExpenseService;
import paulinaKaz.expensesTrackerApp.util.Category;
import paulinaKaz.expensesTrackerApp.util.DateConverter;
import paulinaKaz.expensesTrackerApp.util.Month;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static paulinaKaz.expensesTrackerApp.util.Messages.*;
import static paulinaKaz.expensesTrackerApp.util.ViewsAndRedirections.*;


class ExpenseControllerTest {

    @InjectMocks
    private ExpenseController controller;

    @Mock
    private ExpenseService expenseServiceMock;

    private MockMvc mockMvc;
    private static final String DESCRIPTION = "someDescription";
    private static final String EXAMPLE_WRONG_DATE = "02/20/2020";
    private static final String EXAMPLE_DATE = "02/20/2019";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver =
                new InternalResourceViewResolver("/WEB-INF/views", ".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();

    }

    @Test
    void testShowNewExpenseForm() throws Exception {
        Pattern datePattern = Pattern.compile("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$");
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(NEW_EXPENSE_FORM_VIEW))
                .andExpect(model().attribute("expense", Matchers.any(Expense.class)))
                .andExpect(model().attribute("defaultDate", matchesPattern(datePattern)))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("month", Month.values()));

    }

    @Test
    void testProcessNewExpenseForm_WithoutErrors() throws Exception {
        Date expenseDate = DateConverter.convertStringToDate(EXAMPLE_DATE);
        mockMvc.perform(post("/add")
                .param("date", EXAMPLE_DATE)
                .param("value", "60")
                .param("category", Category.EDUCATION.toString())
                .param("description", DESCRIPTION))
                .andExpect(status().isOk())
                .andExpect(view().name(SINGLE_EXPENSE_VIEW))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().attribute("expense", Matchers.any(Expense.class)))
                .andExpect(model().attribute("expense", allOf(
                        hasProperty("date", is(expenseDate)),
                        hasProperty("value", is(60d)),
                        hasProperty("category", is(Category.EDUCATION)),
                        hasProperty("description", is(DESCRIPTION)))))
                .andExpect(model().attribute("month", Month.values()));


        ArgumentCaptor<Expense> argument = ArgumentCaptor.forClass(Expense.class);

        verify(expenseServiceMock, times(1)).saveOrUpdateExpense(argument.capture());
    }

    @Test
    void testProcessNewExpenseForm_WithErrors() throws Exception {
        Date expenseDate = DateConverter.convertStringToDate(EXAMPLE_WRONG_DATE);
        mockMvc.perform(post("/add")
                .param("date", EXAMPLE_WRONG_DATE)
                .param("value", "-5")
                .param("category", Category.EDUCATION.toString())
                .param("description", " "))
                .andExpect(status().isOk())
                .andExpect(view().name(NEW_EXPENSE_FORM_VIEW))
                .andExpect(model().attributeHasFieldErrors("expense", "date"))
                .andExpect(model().attributeHasFieldErrors("expense", "value"))
                .andExpect(model().attributeHasFieldErrors("expense", "description"))
                .andExpect(model().attribute("expense", allOf(
                        hasProperty("date", is(expenseDate)),
                        hasProperty("value", is(-5d)),
                        hasProperty("category", is(Category.EDUCATION)),
                        hasProperty("description", is(" ")))))
                .andExpect(model().attribute("categories", Category.values()));

        verifyZeroInteractions(expenseServiceMock);

    }

    @Test
    void testShowExpensesForSpecificMonthOrYear() throws Exception {
        List<Expense> testList = Arrays.asList(new Expense(), new Expense());
        when(expenseServiceMock.getExpensesForSpecificMonthOrYear(Month.JANUARY.toString(), 2019))
                .thenReturn(testList);
        when(expenseServiceMock.createListHeader(Month.JANUARY.toString(), 2019))
                .thenReturn(MONTHLY_EXPENSES_LIST_HEADER);

        mockMvc.perform(get("/showSpecificMoth")
                .param("month", Month.JANUARY.toString())
                .param("year", "2019"))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_LIST_VIEW))
                .andExpect(model().attribute("monthlyExpenses", testList))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("message", MONTHLY_EXPENSES_LIST_HEADER));

        verify(expenseServiceMock, times(1))
                .getExpensesForSpecificMonthOrYear(Month.JANUARY.toString(), 2019);
        verify(expenseServiceMock, times(1)).createListHeader(Month.JANUARY.toString(), 2019);
    }

    @Test
    void testShowExpensesFromLast30Days() throws Exception {
        List<Expense> testList = Arrays.asList(new Expense(), new Expense());
        when(expenseServiceMock.getExpensesFromLast30Days()).thenReturn(testList);
        when(expenseServiceMock.createListHeader(null, 0)).thenReturn(LAST_30_DAYS_LIST_HEADER);

        mockMvc.perform(get("/showLast30Days", "/home")
                .param("month", "null")
                .param("year", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_LIST_VIEW))
                .andExpect(model().attribute("monthlyExpenses", testList))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("selectedCategory", "all"))
                .andExpect(model().attribute("message", LAST_30_DAYS_LIST_HEADER));

        verify(expenseServiceMock, times(1)).getExpensesFromLast30Days();
        verify(expenseServiceMock, times(1)).createListHeader(null, 0);
    }

    @Test
    void testShowExpensesForSpecificCategory_WhenMonthlyExpensesAttributeInSession() throws Exception {
        List<Expense> testList = Arrays.asList(new Expense(), new Expense());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("monthlyExpenses", testList);

        when(expenseServiceMock.filterExpenseListByCategory(testList, Category.EDUCATION.toString()))
                .thenReturn(testList);

        mockMvc.perform(get("/expensesList")
                .param("selectedCategory", Category.EDUCATION.toString())
                .param("message", LAST_30_DAYS_LIST_HEADER)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_LIST_VIEW))
                .andExpect(model().attribute("filteredMonthlyExpenses", testList))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("selectedCategory", Category.EDUCATION.toString()))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("message", LAST_30_DAYS_LIST_HEADER));

        verify(expenseServiceMock, times(1))
                .filterExpenseListByCategory(testList, Category.EDUCATION.toString());

    }

    @Test
    void testShoeExpenseListForSpecificCategory_WhenMonthlyExpensesAttributeNotInSession() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("monthlyExpenses", null);
        when(expenseServiceMock.filterExpenseListByCategory(null, Category.EDUCATION.toString()))
                .thenThrow(NullPointerException.class);

        mockMvc.perform(get("/expensesList")
                .param("selectedCategory", Category.EDUCATION.toString())
                .param("message", SESSION_EXPIRED_MESSAGE)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(MESSAGE_VIEW))
                .andExpect(model().attribute("message", SESSION_EXPIRED_MESSAGE));

        verify(expenseServiceMock, times(1))
                .filterExpenseListByCategory(null, Category.EDUCATION.toString());
    }

    @Test
    void testShowExpense() throws Exception {

        Expense testExpense = new Expense();
        when(expenseServiceMock.findById(2)).thenReturn(testExpense);

        mockMvc.perform(get("/showExpense")
                .param("id", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(SINGLE_EXPENSE_VIEW))
                .andExpect(model().attribute("expense", testExpense))
                .andExpect(model().attribute("month", Month.values()));

        verify(expenseServiceMock, times(1)).findById(2);
    }

    @Test
    void testShowExpenseEditForm() throws Exception {

        Expense testExpenseToEdit = new Expense();

        when(expenseServiceMock.findById(1)).thenReturn(testExpenseToEdit);

        mockMvc.perform(get("/edit")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_EXPENSE_FORM_VIEW))
                .andExpect(model().attribute("expense", testExpenseToEdit))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()));

        verify(expenseServiceMock, times(1)).findById(1);
    }

    @Test
    void testProcessExpenseEditForm_WithoutErrors() throws Exception {

        Date expenseDate = DateConverter.convertStringToDate(EXAMPLE_DATE);
        mockMvc.perform(post("/edit")
                .param("date", EXAMPLE_DATE)
                .param("value", "20")
                .param("category", Category.TRANSPORT.toString())
                .param("description", DESCRIPTION))
                .andExpect(status().isOk())
                .andExpect(view().name(SINGLE_EXPENSE_VIEW))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().attribute("expense", Matchers.any(Expense.class)))
                .andExpect(model().attribute("expense", allOf(
                        hasProperty("date", is(expenseDate)),
                        hasProperty("value", is(20d)),
                        hasProperty("category", is(Category.TRANSPORT)),
                        hasProperty("description", is(DESCRIPTION)))))
                .andExpect(model().attribute("month", Month.values()));

        ArgumentCaptor<Expense> argument = ArgumentCaptor.forClass(Expense.class);

        verify(expenseServiceMock, times(1)).saveOrUpdateExpense(argument.capture());

    }

    @Test
    void TestProcessExpenseEditForm_WithErrors() throws Exception {

        Date expenseDate = DateConverter.convertStringToDate(EXAMPLE_WRONG_DATE);
        mockMvc.perform(post("/edit")
                .param("date", EXAMPLE_WRONG_DATE)
                .param("value", "-20")
                .param("category", Category.TRANSPORT.toString())
                .param("description", " "))
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_EXPENSE_FORM_VIEW))
                .andExpect(model().attributeHasFieldErrors("expense", "date"))
                .andExpect(model().attributeHasFieldErrors("expense", "value"))
                .andExpect(model().attributeHasFieldErrors("expense", "description"))
                .andExpect(model().attribute("expense", allOf(
                        hasProperty("date", is(expenseDate)),
                        hasProperty("value", is(-20d)),
                        hasProperty("description", is(" ")),
                        hasProperty("category", is(Category.TRANSPORT)))))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()));

        verifyZeroInteractions(expenseServiceMock);
    }

    @Test
    void testDeleteExpense() throws Exception {

        int validId = 2;
        mockMvc.perform(post("/delete/{id}", validId))
                .andExpect(status().isOk())
                .andExpect(view().name(MESSAGE_VIEW))
                .andExpect(model().attribute("message", EXPENSE_DELETED_MESSAGE))
                .andExpect(model().attribute("month", Month.values()));

        verify(expenseServiceMock, times(1)).deleteExpense(validId);
    }

}
