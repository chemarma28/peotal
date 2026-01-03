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

import finance.forms.rules.impl.DecimalValue;
import finance.forms.rules.impl.LocalDateValue;
import jakarta.servlet.http.HttpServletRequest;

public class FormTest {
    @Test
    public void when_all_parameters_are_provided_and_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("2025-10-12");

        final SimpleForm simpleForm = new SimpleForm(req);

        assertTrue(simpleForm.isValid());
        assertTrue(simpleForm.get("decimal").validationResult().isValid());
        assertTrue(simpleForm.get("date").validationResult().isValid());

        assertEquals(BigDecimal.valueOf(10.5), simpleForm.decimal());
        assertEquals(LocalDate.of(2025, 10, 12), simpleForm.date());
    }

    @Test
    public void when_all_parameters_are_provided_and_one_not_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("not a date");

        final SimpleForm simpleForm = new SimpleForm(req);

        assertFalse(simpleForm.isValid());
        assertTrue(simpleForm.get("decimal").validationResult().isValid());
        assertFalse(simpleForm.get("date").validationResult().isValid());

        assertEquals(BigDecimal.valueOf(10.5), simpleForm.decimal());
        assertNull(simpleForm.date());
    }

    @Test
    public void when_one_of_the_required_parameters_is_not_provided_then_form_is_not_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("date")).thenReturn("2025-10-12");

        final SimpleForm simpleForm = new SimpleForm(req);

        assertFalse(simpleForm.isValid());
        assertFalse(simpleForm.get("decimal").validationResult().isValid());
        assertTrue(simpleForm.get("date").validationResult().isValid());

        assertNull(simpleForm.decimal());
        assertEquals(LocalDate.of(2025, 10, 12), simpleForm.date());
    }

    @Test
    public void when_no_req_then_form_is_valid() {
        final SimpleForm simpleForm = new SimpleForm();

        assertTrue(simpleForm.isValid());
        assertTrue(simpleForm.get("date").validationResult().isValid());
        assertTrue(simpleForm.get("decimal").validationResult().isValid());
    }

    @Test
    public void when_optional_is_not_provided_then_form_is_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("2025-10-12");

        final SimpleForm simpleForm = new SimpleForm(req);

        assertTrue(simpleForm.isValid());
        assertTrue(simpleForm.get("decimal").validationResult().isValid());
        assertTrue(simpleForm.get("date").validationResult().isValid());
        assertTrue(simpleForm.get("optional").validationResult().isValid());

        assertEquals(BigDecimal.valueOf(10.5), simpleForm.decimal());
        assertEquals(LocalDate.of(2025, 10, 12), simpleForm.date());
        assertEquals(BigDecimal.TEN, simpleForm.optional());
    }

    @Test
    public void when_optional_is_provided_as_empty_string_then_form_is_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("2025-10-12");
        when(req.getParameter("optional")).thenReturn("");

        final SimpleForm simpleForm = new SimpleForm(req);

        assertTrue(simpleForm.isValid());
        assertTrue(simpleForm.get("decimal").validationResult().isValid());
        assertTrue(simpleForm.get("date").validationResult().isValid());
        assertTrue(simpleForm.get("optional").validationResult().isValid());

        assertEquals(BigDecimal.valueOf(10.5), simpleForm.decimal());
        assertEquals(LocalDate.of(2025, 10, 12), simpleForm.date());
        assertEquals(BigDecimal.TEN, simpleForm.optional());
    }

    @Test
    public void when_invalid_optional_is_provied_then_form_is_not_valid() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("2025-10-12");
        when(req.getParameter("optional")).thenReturn("not a decimal");

        final SimpleForm simpleForm = new SimpleForm(req);

        assertFalse(simpleForm.isValid());
        assertTrue(simpleForm.get("decimal").validationResult().isValid());
        assertTrue(simpleForm.get("date").validationResult().isValid());
        assertFalse(simpleForm.get("optional").validationResult().isValid());

        assertNull(simpleForm.optional());
        assertEquals(BigDecimal.valueOf(10.5), simpleForm.decimal());
        assertEquals(LocalDate.of(2025, 10, 12), simpleForm.date());
    }

    @Test
    public void when_valid_optional_is_provied_then_form_is_valid_and_optional_has_provided_value() {
        final HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter("decimal")).thenReturn("10.5");
        when(req.getParameter("date")).thenReturn("2025-10-12");
        when(req.getParameter("optional")).thenReturn("20");

        final SimpleForm simpleForm = new SimpleForm(req);

        assertTrue(simpleForm.isValid());
        assertTrue(simpleForm.get("decimal").validationResult().isValid());
        assertTrue(simpleForm.get("date").validationResult().isValid());
        assertTrue(simpleForm.get("optional").validationResult().isValid());

        assertEquals(BigDecimal.valueOf(20), simpleForm.optional());
        assertEquals(BigDecimal.valueOf(10.5), simpleForm.decimal());
        assertEquals(LocalDate.of(2025, 10, 12), simpleForm.date());
    }

    private final class SimpleForm extends BaseForm {
        private FormField<BigDecimal> decimal = new FormField<>("decimal", new DecimalValue());
        private FormField<LocalDate> date = new FormField<>("date", new LocalDateValue());
        private FormField<BigDecimal> optional = new FormField<>("optional", new DecimalValue())
                .optional(BigDecimal.TEN);

        public SimpleForm() {
            super.register(decimal, date, optional);
        }

        public SimpleForm(final HttpServletRequest req) {
            this();
            super.match(req);
        }

        BigDecimal decimal() {
            return decimal.validationResult().value();
        }

        LocalDate date() {
            return date.validationResult().value();
        }

        BigDecimal optional() {
            return optional.validationResult().value();
        }
    }
}
