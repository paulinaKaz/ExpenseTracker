package controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import paulinaKaz.expensesTrackerApp.service.ExpenseService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static paulinaKaz.expensesTrackerApp.util.ViewsAndRedirections.EXPENSE_FORM;

 class ControllerTest {

    @InjectMocks
    private Controller controller;
    @Mock
    private ExpenseService expenseService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);

    }

    @Test
    void testShowExpenseForm() throws Exception {
        mockMvc.perform(get("/expense/add"))
                .andExpect(status().isOk())
                .andExpect(view().name(EXPENSE_FORM))
                .andExpect(model().a)

    }
}
