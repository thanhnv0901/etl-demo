package etl.demo.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import etl.demo.configures.ConfigProperties;
import etl.demo.models.TypeDetail;

public class InfoDatabase {
    private static InfoDatabase singleInstance = null;

    private String ipAddress;
    private String port;
    private String databaseName;
    private String username;
    private String password;
    private String url;
    private Connection dbConnect;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(InfoDatabase.class);

    private InfoDatabase() {

        this.ipAddress = ConfigProperties.getProperty(ConfigProperties.POSTGRESQL_HOST_AUDIENCE);
        this.port = ConfigProperties.getProperty(ConfigProperties.POSTGRESQL_PORT_AUDIENCE);
        this.databaseName = ConfigProperties.getProperty(ConfigProperties.POSTGRESQL_DATABASE_AUDIENCE);
        this.username = ConfigProperties.getProperty(ConfigProperties.POSTGRESQL_USER_AUDIENCE);
        this.password = ConfigProperties.getProperty(ConfigProperties.POSTGRESQL_PASSWORD_AUDIENCE);

        this.url = String.format("jdbc:postgresql://%s:%s/%s", this.ipAddress, this.port, this.databaseName);
        this.dbConnect = this.connect();
    }

    private Connection connect() {

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(this.url, this.username, this.password);
            LOG.info("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            LOG.error(e.getMessage());
        }

        return conn;
    }

    public static InfoDatabase getInstance() {

        if (singleInstance == null) {
            synchronized (InfoDatabase.class) {
                if (singleInstance == null) {
                    singleInstance = new InfoDatabase();
                }
            }
        }
        return singleInstance;
    }

    public Map<String, TypeDetail> getTypeDetail() {
        Map<String, TypeDetail> typeDetails = new HashMap<String, TypeDetail>();

        // notice
        // Do something
        // notice

        return typeDetails;
    }

    public String getRegion(String ID) {

        // notice
        // Do something
        // notice

        return "asia";
    }

}
