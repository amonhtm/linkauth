package dev.amonhtm.linkauth.database;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.databases.MariaDb;
import de.chojo.sadu.datasource.DataSourceCreator;
import org.mariadb.jdbc.Driver;

public class Database {

    private HikariDataSource hikariDataSource;

    public void connect() {
        this.hikariDataSource = DataSourceCreator.create(MariaDb.get())
                .configure(config -> config
                        .host("localhost")
                        .port(3306)
                        .user("devlogin")
                        .password("devpw")
                        .database("devdb")
                )
                .create()
                .withMaximumPoolSize(10)
                .withMinimumIdle(2)
                .build();

        new DatabaseUpdater(hikariDataSource);
    }

    public HikariDataSource hikariDataSource() {
        return hikariDataSource;
    }
}
