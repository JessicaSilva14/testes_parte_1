package br.com.testes.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@EnableJpaRepositories(
        basePackages = "br.com.testes.repository.tertiary",
        entityManagerFactoryRef = "tertiaryEntityManagerFactory",
        transactionManagerRef   = "tertiaryTransactionManager"
)
public class TertiaryDataSourceConfig {

    @Bean("tertiaryDataSource")
    @ConfigurationProperties("datasource.tertiary")
    public DataSource tertiaryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean("tertiaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean tertiaryEntityManagerFactory(
            @Qualifier("tertiaryDataSource") DataSource ds) {

        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(ds);
        em.setPackagesToScan("br.com.testes.entity");
        em.setPersistenceUnitName("tertiary");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> p = new HashMap<>();
        p.put("hibernate.hbm2ddl.auto", "create-drop");
        p.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        p.put("hibernate.show_sql", "true");
        em.setJpaPropertyMap(p);
        return em;
    }

    @Bean("tertiaryTransactionManager")
    public PlatformTransactionManager tertiaryTransactionManager(
            @Qualifier("tertiaryEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
