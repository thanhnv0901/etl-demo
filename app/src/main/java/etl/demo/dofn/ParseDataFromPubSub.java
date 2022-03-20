package etl.demo.dofn;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import etl.demo.models.UserRecord;

public class ParseDataFromPubSub extends DoFn<String, KV<String, UserRecord>> {
    private static final Logger LOG = LoggerFactory.getLogger(ParseDataFromPubSub.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    @ProcessElement
    public void ProcessElement(ProcessContext c) {
        String messages = c.element();

        try {

            UserRecord tmp = objectMapper.readValue(messages, new TypeReference<UserRecord>() {
            });

            c.output(KV.of(tmp.getId(), tmp));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
