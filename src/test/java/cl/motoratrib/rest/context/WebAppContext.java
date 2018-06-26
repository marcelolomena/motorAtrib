package cl.motoratrib.rest.context;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;


@Configuration
@EnableWebMvc
@PropertySource("classpath:system.properties")
@ComponentScan(basePackages = {
        "cl.motoratrib.rest.controller",
        "cl.motoratrib.rest.jsrules"
})
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebAppContext extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public Properties jwtSecurityProperties() {
        return new Properties();
    }


    @Bean
    public DataSource dataSource() {
        return new DataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                return null;
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return null;
            }

            @Override
            public PrintWriter getLogWriter() throws SQLException {
                return null;
            }

            @Override
            public void setLogWriter(PrintWriter out) throws SQLException {

            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {

            }

            @Override
            public int getLoginTimeout() throws SQLException {
                return 0;
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }

            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }
        };
    }

    @Bean
    public SimpleMappingExceptionResolver exceptionResolver() {
        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();

        Properties exceptionMappings = new Properties();

        exceptionMappings.put("java.lang.Exception", "error/error");
        exceptionMappings.put("java.lang.RuntimeException", "error/error");

        exceptionResolver.setExceptionMappings(exceptionMappings);

        Properties statusCodes = new Properties();

        statusCodes.put("error/404", "404");
        statusCodes.put("error/error", "500");

        exceptionResolver.setStatusCodes(statusCodes);

        return exceptionResolver;
    }



    @Bean
    public static PropertyPlaceholderConfigurer properties() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        Resource[] resources = new ClassPathResource[]{
                new ClassPathResource("system.properties")
        };
        ppc.setLocations(resources);
        ppc.setIgnoreUnresolvablePlaceholders(true);
        return ppc;
    }


}
