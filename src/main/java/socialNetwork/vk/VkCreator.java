package socialNetwork.vk;

import http.UriReader;
import http.UriReaderImpl;
import socialNetwork.SocialNetworkCreator;
import utils.PropertiesReader;

import java.util.Properties;

public class VkCreator implements SocialNetworkCreator<Vk> {
    UriReader uriReader;

    public VkCreator(UriReader uriReader) {
        this.uriReader = uriReader;
    }

    private Properties getProperties() {
        return PropertiesReader.readProperties("vk.properties");
    }


    private Properties getSecretProperties() {
        return PropertiesReader.readProperties("vk_secret.properties");
    }

    @Override
    public Vk get() {
        return new Vk(getProperties(), getSecretProperties(), uriReader);
    }
}
