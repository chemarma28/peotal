package finance.web;

import java.io.IOException;
import java.time.LocalDate;

import com.google.inject.Singleton;

import finance.guice.routing.Route;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/html/expenses.jte")
public class ExpensesHtmlResource extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final String yearParam = req.getParameter("year");
        final String monthParam = req.getParameter("month");

        final int year = yearParam == null ? LocalDate.now().getYear(): Integer.parseInt(yearParam);
        int month = monthParam == null ? LocalDate.now().getMonthValue() : Integer.parseInt(monthParam);

        month = month < 1 || 12 < month ? LocalDate.now().getMonthValue() : month;

        if (yearParam == null || monthParam == null)
                resp.sendRedirect(String.format("/html/expenses.jte?year=%s&month=%s&set=true", year, month));

        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
    }
}
