package finance.guice;

import javax.sql.DataSource;

import com.google.common.collect.ImmutableList;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import finance.guice.modules.Thymeleaf;
import finance.guice.routing.GuiceServletRoutingModule;
import finance.web.ConnectionProvided;
import finance.web.html.DashboardResource;
import finance.web.hx.forms.ExpenseCardFormResource;
import finance.web.hx.forms.IncomeFormResource;

public class GuiceWebApplicationModule extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new GuiceServletRoutingModule() {
            @Override
            public void configureServlets() {
                filter("/*").through(ConnectionProvided.class);
                install(new Thymeleaf(getServletContext()));
                dispatch(ImmutableList.of(
                        DashboardResource.class,
                        ExpenseCardFormResource.class,
                        IncomeFormResource.class));

                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setJdbcUrl("jdbc:h2:~/Workspace/dev");
                hikariConfig.setDriverClassName("org.h2.Driver");
                hikariConfig.setUsername("dbuser");
                hikariConfig.setPassword("letmein");

                DataSource ds = new HikariDataSource(hikariConfig);

                bind(DataSource.class).toInstance(ds);
            }
        });
    }
}
