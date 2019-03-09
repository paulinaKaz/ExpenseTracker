package paulinaKaz.expensesTrackerApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import paulinaKaz.expensesTrackerApp.dao.ExpenseDao;
import paulinaKaz.expensesTrackerApp.model.Expense;
import paulinaKaz.expensesTrackerApp.util.Category;
import paulinaKaz.expensesTrackerApp.util.DateConverter;
import paulinaKaz.expensesTrackerApp.util.Month;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

class ExpenseServiceTest {
  
    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseDao expenseDaoMock;

    private List<Expense> expenseListStub;
    private Expense expense1;
    private Expense expense2;
    private Expense expense3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        expenseListStub = prepareExpenseListStub();

    }

    @Test
    void testSaveOrUpdateExpense() {
        Expense testExpense = new Expense();
        expenseService.saveOrUpdateExpense(testExpense);
        verify(expenseDaoMock, times(1)).saveOrUpdateExpense(testExpense);

    }

    @Test
    void testDeleteExpense(){
        Expense testExpense = new Expense();
        when(expenseService.findById(2)).thenReturn(testExpense);
        expenseService.deleteExpense(2);
        verify(expenseDaoMock, times(1)).deleteExpense(testExpense);
    }

    @Test
    void testGetExpensesForSpecificMonthOrYear_whenMonthAndYearAreSend(){
        //List<Expense> resultList = Arrays.asList(expense2,expense2);
        when(expenseService.getExpensesForSpecificMonthOrYear(Month.JANUARY.toString(), 2019))
                .thenReturn(expenseListStub);
        List<Expense> testList = expenseService.getExpensesForSpecificMonthOrYear(Month.JANUARY.toString(), 2019);

        verify(expenseDaoMock, times(1)).
                getExpensesFromSpecificMonth(Month.JANUARY.getMonthNumber(), 2019);

        assertEquals(expenseListStub, testList );
        assertEquals(3, testList.size());
    }

    @Test
    void testGetExpensesForSpecificMonthOrYear_whenOnlyYearIsSend(){

        when(expenseService.getExpensesForSpecificMonthOrYear(null,2019))
                .thenReturn(expenseListStub);
        List<Expense> testList = expenseService.getExpensesForSpecificMonthOrYear(null, 2019);

        verify(expenseDaoMock, times(1)).
                getExpensesForYear(2019);

        assertEquals(expenseListStub, testList );
        assertEquals(3, testList.size());
    }

    @Test
    void testGetExpensesFromLast30Days(){

    }

    private List<Expense> prepareExpenseListStub() {
        expense1 = new Expense();
        expense2 = new Expense();
        expense3 = new Expense();
        expenseListStub = Arrays.asList(expense1, expense2, expense3);
        expense2.setDate(DateConverter.convertStringToDate("02/20/2019"));
        expense3.setDate(DateConverter.convertStringToDate("01/20/2019"));
        expense1.setDate(DateConverter.convertStringToDate("12/20/2018"));
        return expenseListStub;
    }

}