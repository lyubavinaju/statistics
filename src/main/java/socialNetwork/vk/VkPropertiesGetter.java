package socialNetwork.vk;

import reader.PropertiesReader;
import socialNetwork.SocialNetworkPropertiesGetter;

import java.util.Properties;

public class VkPropertiesGetter implements SocialNetworkPropertiesGetter {

    PropertiesReader propertiesReader;

    public VkPropertiesGetter(PropertiesReader propertiesReader) {
        this.propertiesReader = propertiesReader;
    }

    @Override
    public Properties getProperties() {
        return propertiesReader.readProperties("vk.properties");
    }

    @Override
    public Properties getSecretProperties() {
        return propertiesReader.readProperties("vk_secret.properties");
    }

    @Override
    public String getApi() {
        return propertiesReader
                .readProperties("vk_api.properties").getProperty("api");
    }
}
