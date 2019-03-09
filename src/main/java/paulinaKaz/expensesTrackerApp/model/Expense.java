package paulinaKaz.expensesTrackerApp.model;

import org.springframework.format.annotation.DateTimeFormat;
import paulinaKaz.expensesTrackerApp.util.Category;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;


@Entity
public class Expense implements Comparable<Expense> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @DecimalMin(value = "0.1", message = "{typeMismatch.double}")
    private double value;

    @Enumerated(EnumType.STRING)
    private Category category;

    @NotBlank(message = "{field.required}")
    @Size(max = 50, message = "{description.tooLong}")
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    @PastOrPresent(message = "{date.wrongTime}")
    private Date date;

    public Expense(double value, Category category, String description, Date date) {
        this.value = value;
        this.category = category;
        this.description = description;
        this.date = date;
    }

    public Expense() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int compareTo(Expense expense) {
        return expense.getDate().compareTo(getDate());
    }
}
