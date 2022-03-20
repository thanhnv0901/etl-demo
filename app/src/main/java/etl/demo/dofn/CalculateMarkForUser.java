package etl.demo.dofn;

import java.util.Calendar;
import java.util.Map;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;

import etl.demo.database.InfoDatabase;
import etl.demo.models.ProcessedRecord;
import etl.demo.models.TypeDetail;
import etl.demo.models.UserRecord;

@SuppressWarnings("serial")
public class CalculateMarkForUser extends DoFn<KV<String, Iterable<UserRecord>>, ProcessedRecord> {

    // private static final Logger LOG = LoggerFactory.getLogger(CalculateMarkForUser.class);
    public InfoDatabase inforDB = null;
    public Map<String, TypeDetail> typeDetails;

    @Setup
    public void setup() {
        this.inforDB = InfoDatabase.getInstance();
    }

    @StartBundle
    public void startBundle() {
        this.typeDetails = this.inforDB.getTypeDetail();
    }

    @ProcessElement
    public void ProcessElement(ProcessContext c) {

        ProcessedRecord processedRecord = new ProcessedRecord();
        KV<String, Iterable<UserRecord>> userRecords = c.element();

        Integer toltalRecord = 0;

        for (UserRecord ur : userRecords.getValue()) {

            TypeDetail typeDetail = this.typeDetails.get(ur.getTypeGame());

            // If bonous for type supported, add it
            if (typeDetail != null) {
                toltalRecord += typeDetail.coefficient * ur.getMark();
            }
        }

        processedRecord.setId(userRecords.getKey());
        processedRecord.setToltalMark(toltalRecord);
        processedRecord.setRegion(this.inforDB.getRegion(userRecords.getKey()));
        processedRecord.setTimeStamp(Calendar.getInstance().getTimeInMillis());

        c.output(processedRecord);

    }

}
