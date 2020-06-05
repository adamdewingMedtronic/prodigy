package com.mdt.prodigy.service;

import com.mdt.prodigy.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class IntersystemsDBSchemaService extends SchemaService {

    private Connection connection;
    private boolean isDropTables = false;
    private StringBuilder status = null;

    public void createSchema(){
        try{
            status = new StringBuilder();
            status.append("Creating Intersystems schema." + System.lineSeparator());
            this.isDropTables = true;
            connection = createConnection();
            creaetCodeTable();

        } catch (SQLException e) {
            // SQLExceptions should already be logged before it gets here, but let's log it just in case.  Hate to eat the exception and it never gets logged.
            log.error("Exception during IntersystemsDBSchemaService::createSchema.", e);
        } finally {
            log.info(status.toString());
            try {
                if(connection != null && !connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                // At this point just log the error and eat the exception as we are only getting an error closing the connection.
                log.error("Exception trying to close connection while creating Intersystems DB schema for Prodigy.", e);
            }
        }
    }

    private Connection createConnection() throws SQLException {
        status.append("Creating connecting..." + System.lineSeparator());
        Configuration configuration = new Configuration().configure(HibernateUtil.getHibernateCfgXmlName());
        String dbUrl = configuration.getProperty("hibernate.connection.url");
        String username = configuration.getProperty("hibernate.connection.username");
        String password = configuration.getProperty("hibernate.connection.password");
        status.append("Creating connecting to url: " + dbUrl);

        log.debug("Getting connection...");
        try {
            this.connection = DriverManager.getConnection(dbUrl, username, password);
        } catch (SQLException e) {
            log.error("Error trying to create the connecting using dburl:" + dbUrl + ", username:" + username, e);
            throw e;
        }
        log.debug("We got a connection: " + this.connection);
        status.append("Creating connection complete." + System.lineSeparator());
        return this.connection;
    }

    private boolean doesTableExist(String table) throws SQLException {
        if (table == null) {
            return false;
        }
        boolean tableExists = false;
        DatabaseMetaData dbm = null;
        try {
            dbm = connection.getMetaData();
        } catch (SQLException e) {
            log.debug("Error trying to get connection metadata.  Connection:" + connection, e);
            throw e;
        }
        try (ResultSet tables = dbm.getTables(null, null, table, null)) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (table.equalsIgnoreCase(tableName)) {
                    tableExists = true;
                    break;
                }
            }
        } catch (SQLException e) {
            log.debug("Error trying to search database metadata.  DatabaseMetadata:" + dbm, e);
            throw e;
        }
        log.debug("doesTableExist for table " + table + ": " + tableExists);
        return tableExists;
    }

    private void  creaetCodeTable() throws SQLException {
        String table = "code";
        String ddl = "CREATE TABLE code(type VARCHAR(255), code VARCHAR(255), CONSTRAINT type_code_pk PRIMARY KEY(type, code))";
        if(doesTableExist(table)){
            if(isDropTables){
                dropTable(table);
                createTable(ddl);
            }
        }else{
            createTable(ddl);
        }
    }

    private void dropTable(String table){
        log.debug("Trying to drop table: " + table);
        status.append("Trying to drop table: " + table + System.lineSeparator());
        Statement statement = null;
        int statementStatus = -999;
        String ddl = "DROP TABLE " + table;
        try {
            statement = connection.createStatement();
            statementStatus = statement.executeUpdate(ddl);
            if (statementStatus == 0) {
                log.debug("table was dropped successfully.");
            } else {
                log.debug("table was NOT dropped.");
            }
        } catch (SQLException e) {
            log.error("Error while trying to drop table", e);
            status.append("Error while trying to drop table " + table + System.lineSeparator());
        }
        status.append("Drop table complete with status: " + statementStatus + System.lineSeparator());
    }

    private boolean createTable(String ddl) {
        status.append("Trying to create table using ddl: " + ddl + System.lineSeparator());
        boolean isSuccessful = false;
        Statement statement = null;
        int statementStatus = -999;
        //CONSTRAINT EMPLOYEEPK PRIMARY KEY (EmpNum)
        log.debug("Trying to create table:" + ddl);
        try {
            statement = connection.createStatement();
            statementStatus = statement.executeUpdate(ddl);
            if (statementStatus == 0) {
                log.debug("table was created successfully.");
                isSuccessful = true;
            } else {
                log.debug("table was NOT created.");
            }
        } catch (SQLException e) {
            log.error("Error while trying to create table", e);
            status.append("Error while trying to create table." + System.lineSeparator());
            isSuccessful = false;
        }
        status.append("Create Table complete with status: " + statementStatus + System.lineSeparator());
        return isSuccessful;
    }
}
