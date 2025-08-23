package finance.web;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

import javax.naming.OperationNotSupportedException;

import com.google.inject.Singleton;

import finance.guice.routing.Route;
import finance.records.CardFormData;
import finance.service.validation.ValidDecimal;
import finance.service.validation.ValidLocalDate;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Singleton
@Route("/hx/card")
public class ExpenseCard extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LocalDate cardDate = null;

        try {
            cardDate = new ValidLocalDate().fromString("cardDate");
        } catch (OperationNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        CardFormData formData = new CardFormData(Objects.requireNonNullElse(cardDate, LocalDate.of(1, 1, 1)),
                new ValidDecimal().fromString(req.getParameter("cardAmount")), "");

        
    }
}
