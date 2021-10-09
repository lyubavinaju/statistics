package reader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReaderImpl implements PropertiesReader {
    @Override
    public Properties readProperties(final String propertiesFileName) {
        try (InputStream inputStream = PropertiesReaderImpl.class.getClassLoader()
                .getResourceAsStream(propertiesFileName)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
