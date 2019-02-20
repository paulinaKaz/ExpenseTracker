package controllerTest;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import paulinaKaz.expensesTrackerApp.controller.ExpenseController;
import paulinaKaz.expensesTrackerApp.model.Expense;
import paulinaKaz.expensesTrackerApp.service.ExpenseService;
import paulinaKaz.expensesTrackerApp.util.Category;
import paulinaKaz.expensesTrackerApp.util.Month;

import java.util.regex.Pattern;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static paulinaKaz.expensesTrackerApp.util.ViewsAndRedirections.EXPENSE_FORM;


class ControllerTest {

    @InjectMocks
    private ExpenseController controller;
    @Mock
    private ExpenseService expenseService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver("/WEB-INF/views", ".jsp");
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
    void testProcessExpenseFormWithoutErrors() throws Exception {
        mockMvc.perform(post("/add"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param()

    }

}
