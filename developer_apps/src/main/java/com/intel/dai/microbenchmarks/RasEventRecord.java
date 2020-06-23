package com.intel.dai.microbenchmarks;

import java.time.Instant;

public class RasEventRecord {
    public long id;
    public String eventType;
    public String lctn;
    public String serNum;
    public String jobId;
    public int numberRepeats;
    public String controlOperation;
    public String controlOperationDone;
    public String instanceData;
    public Instant dbUpdatedTimestamp;
    public Instant lastChtTimestamp;
    public String lastChgAdapterType;
    public long lastChgWorkItemId;
}
