package controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import paulinaKaz.expensesTrackerApp.service.ExpenseService;


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


    }
}
