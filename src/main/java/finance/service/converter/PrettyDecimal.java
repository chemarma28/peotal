package finance.service.converter;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.modelmapper.AbstractConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;

@Singleton
public class PrettyDecimal extends AbstractConverter<BigDecimal, String> {
    private final NumberFormat fnumber;

    @Inject
    public PrettyDecimal(NumberFormat fnumber) {
        this.fnumber = fnumber;
    }

    @Override
    protected String convert(BigDecimal decimal) {
        return decimal == null ? fnumber.format(0) : fnumber.format(decimal);
    }
}
