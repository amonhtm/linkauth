package dev.amonhtm.linkauth.database;

import com.zaxxer.hikari.HikariDataSource;
import de.chojo.sadu.base.QueryFactory;
import de.chojo.sadu.databases.MariaDb;
import de.chojo.sadu.updater.SqlUpdater;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

public class DatabaseUpdater extends QueryFactory {

    public DatabaseUpdater(HikariDataSource hikariDataSource) {
        super(hikariDataSource);

        try {
            SqlUpdater.builder(hikariDataSource, MariaDb.get()).execute();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
