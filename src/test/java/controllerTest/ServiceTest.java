package controllerTest;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import paulinaKaz.expensesTrackerApp.dao.ExpenseDao;
import paulinaKaz.expensesTrackerApp.service.ExpenseService;

public class ServiceTest {

    @InjectMocks
    private ExpenseService service;

    @Mock
    private ExpenseDao expenseDaoMock;

}
