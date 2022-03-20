package etl.demo.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

import lombok.Getter;
import lombok.Setter;

@DefaultCoder(AvroCoder.class)
public class UserRecord {

    @Getter
    @Setter
    @JsonProperty("id")
    private String id;

    @Getter
    @Setter
    @JsonProperty("mark")
    private Integer Mark;

    @Getter
    @Setter
    @JsonProperty("type_game")
    private String typeGame;

    @Getter
    @Setter
    @JsonProperty("timestamp")
    private Long timeStamp;

}
