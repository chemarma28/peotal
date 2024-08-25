package com.novorock.jexpenses.internal.filters;

import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TemplateContextStartup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        Locale locale = Locale.US;
        context.setAttribute("locale", locale);
        context.setAttribute("localeStr", locale.toString().replace('_', '-'));

        Currency currency = Currency.getInstance(locale);
        String currencyCode = currency.getCurrencyCode();
        context.setAttribute("currencyCode", currencyCode);

        DateTimeFormatter cmDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        context.setAttribute("cmDateTimeFormatter", cmDateTimeFormatter);

        NumberFormat cmNumberFormatter = NumberFormat.getNumberInstance(locale);
        cmNumberFormatter.setMinimumFractionDigits(2);
        context.setAttribute("cmNumberFormatter", cmNumberFormatter);

        NumberFormat cmCurrencyFormatter = NumberFormat.getCurrencyInstance(locale);
        context.setAttribute("cmCurrencyFormatter", cmCurrencyFormatter);

        DateFormatSymbols cmDateFormatSymbols = new DateFormatSymbols(locale);
        context.setAttribute("cmMonths", cmDateFormatSymbols.getMonths());
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }
}
