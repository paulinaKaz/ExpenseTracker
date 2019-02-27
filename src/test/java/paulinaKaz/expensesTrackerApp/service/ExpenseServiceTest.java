package paulinaKaz.expensesTrackerApp.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import paulinaKaz.expensesTrackerApp.dao.ExpenseDao;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService service;

    @Mock
    private ExpenseDao expenseDaoMock;

    

}