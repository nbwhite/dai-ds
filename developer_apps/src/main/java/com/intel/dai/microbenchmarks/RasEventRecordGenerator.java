package com.intel.dai.microbenchmarks;

import java.time.Instant;

public class RasEventRecordGenerator {
    public RasEventRecordGenerator(long nextId) {
        this.nextId = nextId;
    }

    public RasEventRecord getNextRecord() {
        RasEventRecord rec = RECORDS[nextRecord];
        rec.id = nextId++;
        rec.dbUpdatedTimestamp = Instant.now();
        rec.lastChtTimestamp = Instant.now();

        //nextRecord++;
        //nextRecord %= NUM_RECORDS;
	nextRecord();

        return rec;
    }

    private static RasEventRecord createRecord(String eventType, String instanceData) {
        RasEventRecord record = new RasEventRecord();
        // Use static data for most of the record.  The DescriptiveName is the main difference between records (to exercise
        // the RAS event table properly as it's partitioned on DescriptiveName in the case of the VoltDB DB schema).
        record.eventType = eventType;
        record.lctn = LOCATION;
        record.serNum = SERIAL_NUMBER;
        record.jobId = JOB_ID;
        record.numberRepeats = NUMBER_REPEATS;
        record.controlOperation = CONTROL_OPERATION;
        record.controlOperationDone = CONTROL_OPERATION_DONE;
        record.instanceData = instanceData;
        record.lastChgAdapterType = LAST_CHG_ADAPTER_TYPE;
        record.lastChgWorkItemId = LAST_CHG_WORK_ITEM_ID;

        // Leave the timestamps and ID blank, as these should be assigned at the time of record generation.

        return record;
    }

    private static void nextRecord() {
	nextRecord++;
	nextRecord %= NUM_RECORDS;
    }

    private static final String LOCATION = "R0-0-CH0-CB0-CN1";
    private static final String SERIAL_NUMBER = "QSGR2120115724";
    private static final String JOB_ID = "0";
    private static final int NUMBER_REPEATS = 0;
    private static final String CONTROL_OPERATION = null;
    private static final String CONTROL_OPERATION_DONE = "N";
    private static final String LAST_CHG_ADAPTER_TYPE = "MICROBENCHMARK";
    private static final long LAST_CHG_WORK_ITEM_ID = 1L;

    private static final int NUM_RECORDS = 16;
    private static RasEventRecord RECORDS[];

    private static int nextRecord = 0;

    static {
        RECORDS = new RasEventRecord[NUM_RECORDS];
        RECORDS[0] = createRecord("1000000012", "Cray HSS ALPS application status:");
        RECORDS[1] = createRecord("1000000013", "Cray HSS attribute request:");
        RECORDS[2] = createRecord("1000000014", "Cray HSS attribute data:");
        RECORDS[3] = createRecord("1000000015", "Cray HSS boot response:");
        RECORDS[4] = createRecord("1000000016", "Cray HSS set debug level:");
        RECORDS[5] = createRecord("1000000017", "Cray HSS set debug level response:");
        RECORDS[6] = createRecord("1000000018", "Cray HSS dump cabinet data request:");
        RECORDS[7] = createRecord("1000000019", "Cray HSS cabinet data dump:");
        RECORDS[8] = createRecord("1000000020", "Cray HSS global node id list request:");
        RECORDS[9] = createRecord("1000000021", "Cray HSS global node id list response:");
        RECORDS[10] = createRecord("1000000022", "Cray HSS HSN handshake:");
        RECORDS[11] = createRecord("1000000023", "Cray HSS HSN link configuration:");
        RECORDS[12] = createRecord("1000000024", "Cray HSS HSN link configuration response:");
        RECORDS[13] = createRecord("1000000025", "Cray HSS HSN link data request:");
        RECORDS[14] = createRecord("1000000026", "Cray HSS HSN load request:");
        RECORDS[15] = createRecord("1000000027", "Cray HSS HSN load response:");
    }

    private long nextId;
}
