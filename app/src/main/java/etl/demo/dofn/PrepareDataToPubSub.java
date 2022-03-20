package etl.demo.dofn;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import etl.demo.models.ProcessedRecord;

public class PrepareDataToPubSub extends DoFn<ProcessedRecord, String> {
    private static final Logger LOG = LoggerFactory.getLogger(PrepareDataToPubSub.class);
    private static ObjectWriter ow = new ObjectMapper().writer();

    @ProcessElement
    public void processElement(ProcessContext c) {

        try {
            ProcessedRecord obj = c.element();

            String JSONFormat = "";
            try {
                JSONFormat = ow.writeValueAsString(obj);
            } catch (Exception e) {
                LOG.error("Error occurred while format to JSON", e.getMessage());

            }

            c.output(JSONFormat);
        } catch (Exception e) {
            LOG.error("Error occurred while compressing data", e.getMessage());
        }
    }
}
