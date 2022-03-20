package etl.demo.dofn;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.TupleTag;

import etl.demo.constants.Constants;
import etl.demo.models.ProcessedRecord;

public class RegionClassifier extends DoFn<ProcessedRecord, ProcessedRecord> {

    TupleTag<ProcessedRecord> otherRegionTag = new TupleTag<ProcessedRecord>() {
    };

    public RegionClassifier(TupleTag<ProcessedRecord> tag) {
        this.otherRegionTag = tag;
    }

    @ProcessElement
    public void ProcessElement(ProcessContext c) {

        ProcessedRecord processedRecord = c.element();

        if (processedRecord.getRegion().equals(Constants.AsiaRegion)) {
            c.output(processedRecord);
        } else {
            c.output(this.otherRegionTag, processedRecord);
        }

    }
}
