package finance.forms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServletRequest;

public class FormTest {
    @Test
    public void when_all_parameters_are_provided_and_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("2025-10-12");

        final SimpleForm form = new SimpleForm().field("decimal", new Number()).field("date", new Date());
        form.bindFromRequest(req);

        assertTrue(form.isValid());
        assertEquals(BigDecimal.valueOf(10.5), form.value("decimal", BigDecimal.class));
        assertEquals(LocalDate.of(2025, 10, 12), form.value("date", LocalDate.class));
        assertFalse(form.hasErrors("decimal"));
        assertFalse(form.hasErrors("date"));
    }

    @Test
    public void when_all_parameters_are_provided_and_one_not_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("not a date");

        final SimpleForm form = new SimpleForm().field("decimal", new Number()).field("date", new Date());
        form.bindFromRequest(req);

        assertFalse(form.isValid());
        assertEquals(BigDecimal.valueOf(10.5), form.value("decimal", BigDecimal.class));
        assertNull(form.value("date", LocalDate.class));
    }

    @Test
    public void when_one_of_the_required_parameters_is_not_provided_then_form_is_not_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("date")).thenReturn("2025-10-12");

        final SimpleForm form = new SimpleForm().field("decimal", new Required<>(new Number())).field("date", new Date());
        form.bindFromRequest(req);

        assertTrue(form.hasErrors());
        assertTrue(form.hasErrors("decimal"));
        assertFalse(form.hasErrors("date"));

        assertEquals(LocalDate.of(2025, 10, 12), form.value("date", LocalDate.class));
    }

    @Test
    public void when_no_optional_then_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");

        final SimpleForm form = new SimpleForm().field("decimal", new Required<>(new Number())).field("date", new Date());
        form.bindFromRequest(req);

        assertTrue(form.isValid());
        assertEquals(BigDecimal.valueOf(10.5), form.value("decimal", BigDecimal.class));
        assertNull(form.value("date", LocalDate.class));
    }
}
