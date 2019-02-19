package paulinaKaz.expensesTrackerApp.dao;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import paulinaKaz.expensesTrackerApp.model.Expense;

import java.util.Date;
import java.util.List;

@Transactional
@Repository
@SuppressWarnings("unchecked")
public class ExpenseDao {

    @Autowired
    private final SessionFactory sessionFactory;

    public ExpenseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveOrUpdateExpense(Expense expense) {
        sessionFactory.getCurrentSession().saveOrUpdate(expense);
    }

    public void deleteExpense(Expense expense) {
        sessionFactory.getCurrentSession().delete(expense);
    }

    public List<Expense> getExpensesFromLast30Days(Date date) {
        return sessionFactory.getCurrentSession().createQuery("from Expense where date > :param")
                .setParameter("param", date).getResultList();
    }

    public List<Expense> getExpensesFromSpecificMonth(int month, int year) {
        return sessionFactory.getCurrentSession().createQuery("from Expense where month(date) = :month " +
                "and year(date) = :year").setParameter("month", month)
                .setParameter("year", year).getResultList();

    }

    public List<Expense> getExpensesForYear(int year) {
        return sessionFactory.getCurrentSession().createQuery("from Expense where year(date) = :year")
                .setParameter("year", year).getResultList();
    }

    public Expense findById(int id) {
        return sessionFactory.getCurrentSession().get(Expense.class, id);
    }
}
