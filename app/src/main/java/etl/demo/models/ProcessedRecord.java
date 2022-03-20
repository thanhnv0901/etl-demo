package etl.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

import lombok.Getter;
import lombok.Setter;

@DefaultCoder(AvroCoder.class)
public class ProcessedRecord {

    @Getter
    @Setter
    @JsonProperty("id")
    private String id;

    @Getter
    @Setter
    @JsonProperty("toltal_mark")
    private Integer toltalMark;

    @Getter
    @Setter
    @JsonProperty("region")
    private String region;

    @Getter
    @Setter
    @JsonProperty("timestamp")
    private Long timeStamp;
}
