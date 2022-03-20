package etl.demo.models;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

@DefaultCoder(AvroCoder.class)
public class TypeDetail {
    public String type;
    public Integer coefficient;
}
