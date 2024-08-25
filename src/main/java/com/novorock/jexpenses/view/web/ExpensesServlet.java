package com.novorock.jexpenses.view.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.novorock.jexpenses.domain.MonthlyExpense;
import com.novorock.jexpenses.view.to.ExpenseCardsGroupedByDate;


@WebServlet("/expenses.jsp")
public class ExpensesServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        fillContext(req, resp);
        
        req.getRequestDispatcher("WEB-INF/templates/expenses.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        fillContext(req, resp);
    
        req.getRequestDispatcher("WEB-INF/templates/expenses.jsp").forward(req, resp);
    }

    private void fillContext(HttpServletRequest req, HttpServletResponse resp) {
        Month currentMonth = LocalDate.now().getMonth();

        req.setAttribute("currentMonth", currentMonth);

        List<MonthlyExpense> monthlyExpenses = MonthlyExpense.getMonthlyExpenseSamples();

        req.setAttribute("monthlyExpenses", monthlyExpenses);

        for (MonthlyExpense expense : monthlyExpenses) {
            if (expense.monthDate.getMonth().equals(currentMonth)) {
                req.setAttribute("currentMonthlyExpense", expense);
                req.setAttribute("expenseCardGroups",
                        new ExpenseCardsGroupedByDate(expense.getExpenseCards()).getGroups());

                break;
            }
        }

        req.setAttribute("ZERO", BigDecimal.ZERO);
    }
}
