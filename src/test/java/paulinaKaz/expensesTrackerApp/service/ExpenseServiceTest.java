package paulinaKaz.expensesTrackerApp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import paulinaKaz.expensesTrackerApp.dao.ExpenseDao;
import paulinaKaz.expensesTrackerApp.model.Expense;
import paulinaKaz.expensesTrackerApp.util.Category;
import paulinaKaz.expensesTrackerApp.util.CurrentDate;
import paulinaKaz.expensesTrackerApp.util.DateConverter;
import paulinaKaz.expensesTrackerApp.util.Month;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static paulinaKaz.expensesTrackerApp.util.Messages.LAST_30_DAYS_LIST_HEADER;
import static paulinaKaz.expensesTrackerApp.util.Messages.MONTHLY_EXPENSES_LIST_HEADER;

class ExpenseServiceTest {

    @InjectMocks
    private ExpenseService expenseService;

    @Mock
    private ExpenseDao expenseDaoMock;
    @Mock
    private CurrentDate currentDateMock;

    private Expense expense1;
    private Expense expense2;
    private Expense expense3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        createTestExpenses();

    }

    @Test
    void testFindById() {
        when(expenseDaoMock.findById(1)).thenReturn(expense1);

        Expense testExpense = expenseService.findById(1);

        assertTrue(testExpense == expense1);
        verify(expenseDaoMock, times(1)).findById(1);
    }

    @Test
    void testSaveOrUpdateExpense() {
        expenseService.saveOrUpdateExpense(expense1);

        verify(expenseDaoMock, times(1)).saveOrUpdateExpense(expense1);

    }

    @Test
    void testDeleteExpense() {
        when(expenseDaoMock.findById(1)).thenReturn(expense1);

        expenseService.deleteExpense(1);

        verify(expenseDaoMock, times(1)).findById(1);
        verify(expenseDaoMock, times(1)).deleteExpense(expense1);
    }

    @Test
    void testGetExpensesForSpecificMonthOrYear_whenBothMonthAndYearArePassed() {
        when(expenseDaoMock.getExpensesFromSpecificMonth(1, 2019))
                .thenReturn(Arrays.asList(expense1, expense2, expense3));

        List<Expense> testList = expenseService.getExpensesForSpecificMonthOrYear(Month.JANUARY.toString(), 2019);


        assertEquals(expense2, testList.get(0));
        assertEquals(expense1, testList.get(1));
        assertEquals(expense3, testList.get(2));
        verify(expenseDaoMock, times(1)).
                getExpensesFromSpecificMonth(Month.JANUARY.getMonthNumber(), 2019);

    }

    @Test
    void testGetExpensesForSpecificMonthOrYear_whenOnlyYearIsPassed() {
        when(expenseDaoMock.getExpensesForYear(2019)).thenReturn(Arrays.asList(expense1, expense2, expense3));

        List<Expense> testList = expenseService.getExpensesForSpecificMonthOrYear(null, 2019);

        assertEquals(expense2, testList.get(0));
        assertEquals(expense1, testList.get(1));
        assertEquals(expense3, testList.get(2));
        verify(expenseDaoMock, times(1)).getExpensesForYear(2019);

    }

    @Test
    void testGetExpensesFromLast30Days() {
        Date currentDate = DateConverter.convertStringToDate("01/31/2019");
        Date pastDate = DateConverter.convertStringToDate("01/01/2019");
        when(currentDateMock.getCurrentDate()).thenReturn(currentDate);
        when(expenseDaoMock.getExpensesSinceDate(pastDate)).thenReturn(Arrays.asList(expense1, expense2, expense3));

        List<Expense> testList = expenseService.getExpensesFromLast30Days();

        assertEquals(expense2, testList.get(0));
        assertEquals(expense1, testList.get(1));
        assertEquals(expense3, testList.get(2));
        verify(currentDateMock, times(1)).getCurrentDate();
        verify(expenseDaoMock, times(1)).getExpensesSinceDate(pastDate);

    }

    @Test
    void testFilterExpenseListByCategory_whenCategorySpecified_andListContainsExpensesWithThisCategory() {
        List<Expense> listToFilter = Arrays.asList(expense1, expense2, expense3);

        List<Expense> testList = expenseService.filterExpenseListByCategory(listToFilter, Category.EDUCATION.toString());

        assertTrue(testList.size() == 2);
        assertEquals(expense2, testList.get(0));
        assertEquals(expense1, testList.get(1));
        assertEquals(Category.EDUCATION, testList.get(0).getCategory());
        assertEquals(Category.EDUCATION, testList.get(1).getCategory());
    }

    @Test
    void testFilterExpenseListByCategory_whenCategorySpecified_butNoExpensesWithThisCategoryInList() {
        List<Expense> listToFilter = Arrays.asList(expense1, expense2, expense3);

        List<Expense> testList = expenseService.filterExpenseListByCategory(listToFilter, Category.GROCERIES.toString());

        assertTrue(testList.size() == 0);
    }

    @Test
    void testFilterExpenseListByCategory_whenNoCategorySpecified() {
        List<Expense> listToFilter = Arrays.asList(expense1, expense2, expense3);

        List<Expense> testList = expenseService.filterExpenseListByCategory(listToFilter, "ALL");

        assertNull(testList);
    }

    @Test
    void testCreateListHeader_whenBothMonthAndYearSpecified() {
        String testHeader = expenseService.createListHeader(Month.JANUARY.toString(), 2019);

        assertEquals(testHeader, MONTHLY_EXPENSES_LIST_HEADER + "January " + 2019);
    }

    @Test
    void testCreateListHeader_whenOnlyYearSpecified() {
        String testHeader = expenseService.createListHeader(null, 2019);

        assertEquals(testHeader, MONTHLY_EXPENSES_LIST_HEADER + 2019);
    }

    @Test
    void testCreateListHeader_whenNeitherMonthNorYearSpecified() {
        String testHeader = expenseService.createListHeader(null, 0);

        assertEquals(testHeader, LAST_30_DAYS_LIST_HEADER);
    }

    private void createTestExpenses() {
        expense1 = new Expense();
        expense2 = new Expense();
        expense3 = new Expense();
        expense1.setDate(DateConverter.convertStringToDate("01/02/2019"));
        expense2.setDate(DateConverter.convertStringToDate("01/03/2019"));
        expense3.setDate(DateConverter.convertStringToDate("01/01/2019"));
        expense1.setCategory(Category.EDUCATION);
        expense2.setCategory(Category.EDUCATION);
        expense3.setCategory(Category.TRANSPORT);
    }

}