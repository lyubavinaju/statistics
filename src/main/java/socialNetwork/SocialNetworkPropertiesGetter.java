package socialNetwork;

import java.util.Properties;

public interface SocialNetworkPropertiesGetter {
    Properties getProperties();

    Properties getSecretProperties();

    String getApi();
}
