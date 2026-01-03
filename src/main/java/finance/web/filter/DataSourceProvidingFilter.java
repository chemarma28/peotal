package finance.web.filter;

import java.io.IOException;

import javax.sql.DataSource;

import org.javalite.activejdbc.Base;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

@Singleton
public class DataSourceProvidingFilter implements Filter {
    @Inject
    private DataSource ds;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Base.open(ds);

        chain.doFilter(request, response);

        Base.close();
    }

}
