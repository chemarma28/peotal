package com.novorock.jexpenses.domain;

import java.util.List;

public class Contact {
    public Long id;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public Office office;
    public List<ExpenseCard> expenseCards;
    boolean isAdmin;

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Office getOffice() {
        return office;
    }

    public List<ExpenseCard> getExpenseCards() {
        return expenseCards;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
