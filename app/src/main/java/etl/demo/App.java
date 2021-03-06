/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package etl.demo;

import java.io.File;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.StreamingOptions;
import org.apache.beam.sdk.transforms.GroupByKey;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.transforms.windowing.FixedWindows;
import org.apache.beam.sdk.transforms.windowing.Window;
import org.apache.beam.sdk.values.KV;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.joda.time.Duration;

import etl.demo.configures.ConfigProperties;
import etl.demo.dofn.CalculateMarkForUser;
import etl.demo.dofn.ParseDataFromPubSub;
import etl.demo.dofn.PrepareDataToPubSub;
import etl.demo.dofn.RegionClassifier;
import etl.demo.models.ProcessedRecord;
import etl.demo.models.UserRecord;

public class App {

        public interface MyOptions extends StreamingOptions {

                @Description("Environment")
                @Default.String("test")
                String getEnvironment();

                void setEnvironment(String value);

        }

        public static void main(String[] args) {

                // Setup option
                MyOptions myOptions = PipelineOptionsFactory.fromArgs(args).withValidation().as(MyOptions.class);

                // Setup environment
                ConfigProperties.setEnvironment(myOptions.getEnvironment());

                // Set options for pipeline
                myOptions.setStreaming(true);
                myOptions.setTempLocation(ConfigProperties.getProperty(ConfigProperties.TEMP_LOCATION));

                Pipeline myPipeline = Pipeline.create(myOptions);

                PCollection<ProcessedRecord> userRecordColl = myPipeline
                                .apply("Read messages from Pubsub", PubsubIO.readStrings()
                                                .fromSubscription(ConfigProperties.getProperty(
                                                                ConfigProperties.PUBSUB_SUBSCRIPTION_INPUT)))
                                .apply("Parse messages to Object", ParDo.of(new ParseDataFromPubSub()))
                                .apply("Apply Window", Window
                                                .<KV<String, UserRecord>>into(FixedWindows.of(Duration.standardHours(
                                                                Integer.parseInt(ConfigProperties.getProperty(
                                                                                ConfigProperties.WINDOW_HOUR_DURATION))))))
                                .apply("Group record user by key", GroupByKey.create())
                                .apply("Total mark for per user", ParDo.of(new CalculateMarkForUser()));

                // classify by region
                TupleTag<ProcessedRecord> asiaRegionTag = new TupleTag<ProcessedRecord>() {
                };
                TupleTag<ProcessedRecord> otherRegionTag = new TupleTag<ProcessedRecord>() {
                };

                PCollectionTuple pTuple = userRecordColl.apply("Classify record by region",
                                ParDo.of(new RegionClassifier(otherRegionTag)).withOutputTags(asiaRegionTag,
                                                TupleTagList.of(otherRegionTag)));

                PCollection<ProcessedRecord> asiaUsers = pTuple.get(asiaRegionTag);
                PCollection<ProcessedRecord> otherUsers = pTuple.get(otherRegionTag);

                // Send to Pubsub
                asiaUsers.apply("Parse object to string", ParDo.of(new PrepareDataToPubSub())).apply(
                                "Send to Asia Pubsub",
                                PubsubIO.writeStrings().to(ConfigProperties
                                                .getProperty(ConfigProperties.PUBSUB_TOPIC_ASIA_OUTPUT)));

                otherUsers.apply("Parse object to string", ParDo.of(new PrepareDataToPubSub())).apply(
                                "Send to Other Pubsub",
                                PubsubIO.writeStrings().to(ConfigProperties
                                                .getProperty(ConfigProperties.PUBSUB_TOPIC_OTHER_OUTPUT)));

                myPipeline.run();
        }
}
