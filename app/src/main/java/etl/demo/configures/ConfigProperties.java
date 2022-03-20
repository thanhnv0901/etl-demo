package etl.demo.configures;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
    private static Properties properties;
    private static String enviroment = "";

    public static final String PROJECT_ID = "project_id";
    public static final String TEMP_LOCATION = "temp_location";
    public static final String WINDOW_HOUR_DURATION = "window_hour_duration";

    public static final String POSTGRESQL_HOST_AUDIENCE = "postgresql_host_audience";
    public static final String POSTGRESQL_PORT_AUDIENCE = "postgresql_port_audience";
    public static final String POSTGRESQL_DATABASE_AUDIENCE = "postgresql_database_audience";
    public static final String POSTGRESQL_USER_AUDIENCE = "postgresql_user_audience";
    public static final String POSTGRESQL_PASSWORD_AUDIENCE = "postgresql_password_audience";

    public static final String PUBSUB_TOPIC_INPUT = "pubsub_topic_input";
    public static final String PUBSUB_SUBSCRIPTION_INPUT = "pubsub_subscription_input";

    public static final String PUBSUB_TOPIC_ASIA_OUTPUT = "pubsub_topic_asia_output";
    public static final String PUBSUB_SUBSCRIPTION_ASIA_OUTPUT = "pubsub_subscription_asia_output";
    public static final String PUBSUB_TOPIC_OTHER_OUTPUT = "pubsub_topic_other_output";
    public static final String PUBSUB_SUBSCRIPTION_OTHER_OUTPUT = "pubsub_subscription_other_output";

    public static void setEnvironment(String env) {
        ConfigProperties.enviroment = env;
    }

    private static String getPropertyFile() {
        String propertyFile;
        String defaultPropertyFile = "config.production.properties";

        String jobEnv = ConfigProperties.enviroment;
        switch (jobEnv) {
            case "production":
                propertyFile = "config.production.properties";
                break;
            case "staging":
                propertyFile = "config.staging.properties";
                break;
            case "test":
                propertyFile = "config.test.properties";
                break;
            default:
                propertyFile = defaultPropertyFile;
        }

        return propertyFile;
    }

    public static String getProperty(String name) {
        if (properties == null) {
            properties = new Properties();
            String propertyFile = getPropertyFile();
            try {
                InputStream inputStream = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(propertyFile);

                properties.load(inputStream);
            } catch (IOException e) {
                System.err.println(e);
            } catch (Exception e) {
                System.err.println(e);
            }
        }

        String value = properties.getProperty(name, "").trim();

        if (value.equals(""))
            System.err.print("Configuration is missing: " + name);

        return value;
    }

}