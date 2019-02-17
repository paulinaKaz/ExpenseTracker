package paulinaKaz.expensesTrackerApp.util;


public enum Category {
    GROCERIES("Groceries", "fas fa-shopping-basket fa-2x m-2"),
    RESTAURANT("Restaurant", "fas fa-utensils fa-2x m-2"),
    TRANSPORT("Transport", "fas fa-bus-alt fa-2x m-2"),
    SHOPPING("Shopping", "fas fa-shopping-bag fa-2x m-2"),
    LEISURE("Leisure", "fas fa-glass-martini-alt fa-2x m-2"),
    GIFTS("Gifts", "fas fa-gift fa-2x m-2"),
    HEALTH("Health", "fas fa-briefcase-medical fa-2x m-2"),
    EDUCATION("Education", "fas fa-book fa-2x m-2");

    private String categoryName;
    private String categoryIcon;

    Category(String categoryName, String categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryIcon() {
        return categoryIcon;
    }
}
