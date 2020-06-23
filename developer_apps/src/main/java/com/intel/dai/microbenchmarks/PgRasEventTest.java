// Copyright (C) 2017-2018 Intel Corporation
//
// SPDX-License-Identifier: Apache-2.0

package com.intel.dai.microbenchmarks;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import com.intel.config_io.ConfigIO;
import com.intel.config_io.ConfigIOFactory;
import com.intel.config_io.ConfigIOParseException;
import com.intel.properties.PropertyMap;
import com.intel.properties.PropertyNotExpectedType;
import com.intel.xdg.XdgConfigFile;

public class PgRasEventTest {
    public static void main(String[] args) {
        try {
            PgRasEventTest test = new PgRasEventTest();
            test.runTest();
            test.tearDown();
        } catch (Exception ex) {
            System.err.println("An error occurred while executing test: " + ex.getMessage());
            System.err.println("Exiting test...");
            System.exit(1);
        }
    }

    public PgRasEventTest() throws Exception {
        initParser();
        loadConfig();
        initDbConn();
        initTestParams();
        initRecordGenerator();
        initStmt();
    }

    public void runTest() throws Exception {
        System.out.println("PG RAS Event Test");
        System.out.println(String.format("Number of transactions: %d", numTransactions));
        System.out.println(String.format("Number of records per transaction: %d", recordsPerTransaction));

        System.out.println("Starting test...");
        Instant start = Instant.now();
        for (int i = 0; i < numTransactions; i++) {
            performTransaction(recordsPerTransaction);
        }
        Instant end = Instant.now();
        System.out.println("Done!");

        double testDurationSec = Duration.between(start, end).toNanos() / (1000000000.0d);
        double transPerSec = numTransactions / testDurationSec;
        double eventsPerSec = (numTransactions * recordsPerTransaction) / testDurationSec;

        System.out.println(String.format("Total time: %f s", testDurationSec));
        System.out.println(String.format("Transactions per second: %f", transPerSec));
        System.out.println(String.format("Events (event records) per second: %f", eventsPerSec));
    }

    private void performTransaction(int recordsPerTrans) throws Exception {
        for (int i = 0; i < recordsPerTrans; i++) {
            RasEventRecord rec = recGenerator.getNextRecord();

            try {
                stmt.setLong(1, rec.id);
                stmt.setString(2, rec.eventType);
                stmt.setString(3, rec.lctn);
                stmt.setString(4, rec.serNum);
                stmt.setString(5, rec.jobId);
                stmt.setInt(6, rec.numberRepeats);
                stmt.setString(7, rec.controlOperation);
                stmt.setString(8, rec.controlOperationDone);
                stmt.setString(9, rec.instanceData);
                stmt.setTimestamp(10, Timestamp.from(rec.dbUpdatedTimestamp));
                stmt.setTimestamp(11, Timestamp.from(rec.lastChtTimestamp));
                stmt.setString(12, rec.lastChgAdapterType);
                stmt.setLong(13, rec.lastChgWorkItemId);
            } catch (SQLException ex) {
                throw new Exception("Error preparing statement: " + ex.getMessage());
            }

            try {
                stmt.execute();
            } catch (SQLException ex) {
                throw new Exception("Error executing statement: " + ex.getMessage());
            }
        }
        try {
            conn.commit();
        } catch (SQLException ex) {
            throw new Exception("Error committing transaction: " + ex.getMessage());
        }
    }

    private void initParser() {
        parser = ConfigIOFactory.getInstance("json");
    }

    private void loadConfig() throws Exception {
        assert parser != null;

        XdgConfigFile configFile = new XdgConfigFile("ucs");
        try (InputStream configStream = configFile.Open(CONFIG_FILE)) {
            config = parser.readConfig(configStream).getAsMap();
        } catch (IOException | ConfigIOParseException ex) {
            throw new Exception(
                    String.format("Unable to load configuration file (%s): %s", CONFIG_FILE, ex.getMessage()));
        }
    }

    private void initDbConn() throws Exception {
        assert config != null;

        try {
            PropertyMap dbConfig = config.getMap("db");
            if (dbConfig == null) {
                throw new Exception("Configuration parameter not found: 'db'");
            }

            String url = dbConfig.getString("url");
            if (url == null) {
                throw new Exception("Database configuration parameter not found: 'url'");
            }

            String user = dbConfig.getString("username");
            if (user == null) {
                throw new Exception("Database configuration parameter not found: 'username'");
            }

            String password = dbConfig.getString("password");
            if (password == null) {
                throw new Exception("Database configuration parameter not found: 'password'");
            }

            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
        } catch (PropertyNotExpectedType ex) {
            throw new Exception("Unable to load DB configuration parameters: " + ex.getMessage());
        } catch (SQLException ex) {
            throw new Exception("Unable to establish DB connection: " + ex.getMessage());
        }
    }

    private void initTestParams() throws Exception {
        assert config != null;

        try {
            PropertyMap testConfig = config.getMap("test");
            if (testConfig == null) {
                throw new Exception("Configuration parameter not found: 'test'");
            }

            numTransactions = testConfig.getInt("num-transactions");
            recordsPerTransaction = testConfig.getInt("records-per-transaction");
            nextId = testConfig.getInt("next-id");
        } catch (PropertyNotExpectedType ex) {
            throw new Exception("Unable to load test configuration parameters: " + ex.getMessage());
        }
    }

    private void initRecordGenerator() {
        assert config != null;

        recGenerator = new RasEventRecordGenerator(nextId);
    }

    private void initStmt() throws Exception {
        assert conn != null;

        try {
            stmt = conn.prepareCall(SQL_STMT);
        } catch (SQLException ex) {
            throw new Exception("Unable to prepare SQL statement: " + ex.getMessage());
        }
    }

    private void tearDown() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                // Ignore...
            }
        }
    }

    private static final String CONFIG_FILE = "PgRasEventTest.json";

    //private static final String SQL_STMT = "insert into Tier2_RasEvent(Id, EventType, Lctn, Sernum, JobId, " +
    //        "NumberRepeats, ControlOperation, ControlOperationDone, InstanceData, DbUpdatedTimestamp, LastChgTimestamp, " +
    //        "LastChgAdapterType, LastChgWorkItemId) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";

    private static final String SQL_STMT = "{call InsertOrUpdateRasEvent(?,?,?,?,?,?,?,?,?,?,?,?,?)}";

    private ConfigIO parser;
    private PropertyMap config;

    private Connection conn;
    private PreparedStatement stmt;
    private int recordsPerTransaction;
    private int numTransactions;
    private long nextId;
    private RasEventRecordGenerator recGenerator;
}
