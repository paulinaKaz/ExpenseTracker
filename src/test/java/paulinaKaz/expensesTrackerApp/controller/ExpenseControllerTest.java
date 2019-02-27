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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver =
                new InternalResourceViewResolver("/WEB-INF/views", ".jsp");
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setViewResolvers(viewResolver).build();

    }

    @Test
    void testShowExpenseForm() throws Exception {
        Pattern datePattern = Pattern.compile("^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$");
        mockMvc.perform(get("/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_FORM))
                .andExpect(model().attribute("defaultDate", matchesPattern(datePattern)))
                .andExpect(model().attribute("expense", Matchers.any(Expense.class)))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("month", Month.values()));

    }

    @Test
    void testProcessExpenseForm_WithoutErrors() throws Exception {
        Date expenseDate = DateConverter.convertStringToDate("02/20/2019");
        mockMvc.perform(post("/add")
                .param("date", "02/20/2019")
                .param("value", "60")
                .param("category", Category.EDUCATION.toString())
                .param("description", "zakupy w lidlu"))
                //.sessionAttr("expense", new Expense()))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_VIEW))
                .andExpect(model().attribute("expense", Matchers.any(Expense.class)))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().attribute("expense", allOf(
                        hasProperty("date", is(expenseDate)),
                        hasProperty("value", is(60d)),
                        hasProperty("category", is(Category.EDUCATION)),
                        hasProperty("description", is("zakupy w lidlu")))));

        ArgumentCaptor<Expense> argument = ArgumentCaptor.forClass(Expense.class);

        verify(expenseServiceMock, times(1)).saveOrUpdateExpense(argument.capture());
    }

    @Test
    void testProcessExpenseForm_WithErrors() throws Exception {
        Date expenseDate = DateConverter.convertStringToDate("02/20/2020");
        mockMvc.perform(post("/add")
                .param("date", "02/20/2020")
                .param("value", "-5")
                .param("category", Category.EDUCATION.toString())
                .param("description", " "))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_FORM))
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
    void testShowSpecificMonthOrYear() throws Exception {
        List<Expense> testList = Arrays.asList(new Expense(), new Expense());
        when(expenseServiceMock.getExpensesForSpecificMonthOrYear(Month.JANUARY.toString(), 2019))
                .thenReturn(testList);
        when(expenseServiceMock.getMessage(Month.JANUARY.toString(), 2019))
                .thenReturn(MONTHLY_EXPENSES_LIST_MESSAGE);

        mockMvc.perform(get("/showSpecificMoth")
                .param("month", Month.JANUARY.toString())
                .param("year", "2019"))
                .andExpect(status().isOk())
                .andExpect(view().name(LIST_VIEW))
                .andExpect(model().attribute("monthlyExpenses", testList))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("message", MONTHLY_EXPENSES_LIST_MESSAGE));

        verify(expenseServiceMock, times(1))
                .getExpensesForSpecificMonthOrYear(Month.JANUARY.toString(), 2019);
        verify(expenseServiceMock, times(1)).getMessage(Month.JANUARY.toString(), 2019);
    }

    @Test
    void testShowLast30Days() throws Exception {
        List<Expense> testList = Arrays.asList(new Expense(), new Expense());
        when(expenseServiceMock.getExpensesFromLast30Days()).thenReturn(testList);
        when(expenseServiceMock.getMessage(null, 0)).thenReturn(LAST_30_DAYS_LIST_MESSAGE);

        mockMvc.perform(get("/showLast30Days", "/home")
                .param("month", "null")
                .param("year", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name(LIST_VIEW))
                .andExpect(model().attribute("monthlyExpenses", testList))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("selectedCategory", "all"))
                .andExpect(model().attribute("message", LAST_30_DAYS_LIST_MESSAGE));

        verify(expenseServiceMock, times(1)).getExpensesFromLast30Days();
        verify(expenseServiceMock, times(1)).getMessage(null, 0);
    }

    @Test
    void testShowExpensesForSpecificCategory_WhenSessionExists() throws Exception {
        List<Expense> testList = Arrays.asList(new Expense(), new Expense());
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("monthlyExpenses", testList);

        when(expenseServiceMock.getExpensesForSpecificCategory(testList, Category.EDUCATION.toString()))
                .thenReturn(testList);

        mockMvc.perform(get("/expensesList")
                .param("selectedCategory", Category.EDUCATION.toString())
                .param("message", LAST_30_DAYS_LIST_MESSAGE)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(LIST_VIEW))
                .andExpect(model().attribute("filteredMonthlyExpenses", testList))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("selectedCategory", Category.EDUCATION.toString()))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("message", LAST_30_DAYS_LIST_MESSAGE));

        verify(expenseServiceMock, times(1))
                .getExpensesForSpecificCategory(testList, Category.EDUCATION.toString());

    }

    @Test
    void testShoeExpenseListForSpecificCategory_WhenSessionExpired() throws Exception {

        MockHttpSession session = new MockHttpSession();
        session.setAttribute("monthlyExpenses", null);
        when(expenseServiceMock.getExpensesForSpecificCategory(null, Category.EDUCATION.toString()))
                .thenThrow(NullPointerException.class);

        mockMvc.perform(get("/expensesList")
                .param("selectedCategory", Category.EDUCATION.toString())
                .param("message", SESSION)
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name(MESSAGE))
                .andExpect(model().attribute("message", SESSION));

        verify(expenseServiceMock, times(1))
                .getExpensesForSpecificCategory(null, Category.EDUCATION.toString());
    }

    @Test
    void testShowExpense() throws Exception {

        Expense testExpense = new Expense();
        when(expenseServiceMock.findById(2)).thenReturn(testExpense);

        mockMvc.perform(get("/showExpense")
                .param("id", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_VIEW))
                .andExpect(model().attribute("expense", testExpense))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attributeHasNoErrors());

        verify(expenseServiceMock, times(1)).findById(2);
    }

    @Test
    void testShowExpenseEditForm() throws Exception {

        Expense testEditedExpense = new Expense();

        when(expenseServiceMock.findById(1)).thenReturn(testEditedExpense);

        mockMvc.perform(get("/edit")
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_FORM))
                .andExpect(model().attribute("expense", testEditedExpense))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attributeHasNoErrors());

        verify(expenseServiceMock, times(1)).findById(1);
    }

    @Test
    void testProcessExpenseEditForm_WithoutErrors() throws Exception {

        Date expenseDate = DateConverter.convertStringToDate("02/26/2019");
        mockMvc.perform(post("/edit")
                .param("date", "02/26/2019")
                .param("value", "20")
                .param("category", Category.TRANSPORT.toString())
                .param("description", "obiad"))
                //.sessionAttr("expense", new Expense()))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_VIEW))
                .andExpect(model().attribute("expense", Matchers.any(Expense.class)))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attributeHasNoErrors())
                .andExpect(model().attribute("expense", allOf(
                        hasProperty("date", is(expenseDate)),
                        hasProperty("value", is(20d)),
                        hasProperty("category", is(Category.TRANSPORT)),
                        hasProperty("description", is("obiad")))));

        ArgumentCaptor<Expense> argument = ArgumentCaptor.forClass(Expense.class);

        verify(expenseServiceMock, times(1)).saveOrUpdateExpense(argument.capture());

    }

    @Test
    void TestProcessExpenseEditForm_WithErrors() throws Exception {

        Date expenseDate = DateConverter.convertStringToDate("02/28/2020");
        mockMvc.perform(post("/edit")
                .param("date", "02/28/2020")
                .param("value", "-20")
                .param("category", Category.TRANSPORT.toString())
                .param("description", " "))
                .andExpect(status().isOk())
                .andExpect(view().name(EDIT_FORM))
                .andExpect(model().attributeHasFieldErrors("expense", "date"))
                .andExpect(model().attributeHasFieldErrors("expense", "value"))
                .andExpect(model().attributeHasFieldErrors("expense", "description"))
                .andExpect(model().attribute("month", Month.values()))
                .andExpect(model().attribute("categories", Category.values()))
                .andExpect(model().attribute("expense", allOf(
                        hasProperty("date", is(expenseDate)),
                        hasProperty("value", is(-20d)),
                        hasProperty("description", is(" ")),
                        hasProperty("category", is(Category.TRANSPORT)))));

        verifyZeroInteractions(expenseServiceMock);
    }

    @Test
    void testDeleteExpense() throws Exception {

        int validId = 2;
        mockMvc.perform(post("/delete/{id}", validId))
                .andExpect(status().isOk())
                .andExpect(view().name(MESSAGE))
                .andExpect(model().attribute("message", DELETED))
                .andExpect(model().attribute("month", Month.values()));

        verify(expenseServiceMock, times(1)).deleteExpense(validId);
    }

}
