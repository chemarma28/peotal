package finance.guice;

import javax.sql.DataSource;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import finance.guice.modules.GuiceServletDiscoveryModule;
import finance.guice.modules.Thymeleaf;
import finance.web.filter.DataSourceProvidingFilter;

public class GuiceWebApplicationModule extends GuiceServletContextListener {
    private DataSource dataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:~/Workspace/dev");
        hikariConfig.setDriverClassName("org.h2.Driver");
        hikariConfig.setUsername("dbuser");
        hikariConfig.setPassword("letmein");

        return new HikariDataSource(hikariConfig);
    }

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new GuiceServletDiscoveryModule() {
            @Override
            public void configureServlets() {
                bind(DataSource.class).toInstance(dataSource());
                install(new Thymeleaf(getServletContext()));

                filter("/*").through(DataSourceProvidingFilter.class);
                setupServletsPaths("finance.web");
            }
        });
    }
}
