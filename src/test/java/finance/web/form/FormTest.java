package finance.web.form;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import finance.web.forms.ChainQ;
import finance.web.forms.Form;
import finance.web.forms.Form.Result;
import finance.web.forms.FormField;
import finance.web.forms.NumberBtQ;
import finance.web.forms.NumberQ;
import jakarta.servlet.http.HttpServletRequest;

public class FormTest {
    @Test
    public void default_flow() {
        final HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getParameter("number1")).thenReturn("120.5");
        when(req.getParameter("number2")).thenReturn("notanumber");

        Form f = new Form(req,
                new FormField<>("number1", new NumberQ()),
                new FormField<>("number2", new NumberQ()));

        assertTrue(f.failedQ());
        assertEquals(f.validation("number1"), Result.SUCCESS);
        assertEquals(f.validation("number2"), Result.FAIL);

        assertEquals(f.value("number1", BigDecimal.class), new BigDecimal("120.5"));
        assertThrows(IllegalArgumentException.class, () -> f.value("number2", BigDecimal.class));
    }

    @Test
    public void number_in_range() {
        final HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        when(req.getParameter("number1")).thenReturn("0.7");
        when(req.getParameter("number2")).thenReturn("1.1");

        Form f1 = new Form(req,
                new FormField<>("number1",
                        new ChainQ<>(new NumberQ(), new NumberBtQ(new BigDecimal(0), new BigDecimal(1)))));

        assertFalse(f1.failedQ());
        assertEquals(f1.validation("number1"), Result.SUCCESS);
        assertEquals(f1.value("number1", BigDecimal.class), new BigDecimal("0.7"));

        Form f2 = new Form(req,
                new FormField<>("number2",
                        new ChainQ<>(new NumberQ(), new NumberBtQ(new BigDecimal(0), new BigDecimal(1)))));

        assertTrue(f2.failedQ());
        assertEquals(f2.validation("number2"), Result.FAIL);
        assertThrows(IllegalArgumentException.class, () -> f2.value("number2", BigDecimal.class));
    }
}
