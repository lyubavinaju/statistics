package socialNetwork;

import reader.UriReader;

import java.net.URI;
import java.util.Deque;
import java.util.Properties;

public abstract class AbstractSocialNetwork implements SocialNetwork {
    UriReader uriReader;
    protected Properties properties;
    protected Properties secretProperties;
    protected String api;

    public AbstractSocialNetwork(UriReader uriReader,
                                 Properties properties,
                                 Properties secretProperties,
                                 String api) {
        this.properties = properties;
        this.secretProperties = secretProperties;
        this.uriReader = uriReader;
        this.api = api;
    }

    @Override
    public Deque<Long> getDates(String query, long startTime, long endTime) {
        URI uri = getUriGetDates(query, startTime, endTime);
        String response = uriReader.getResponseToString(uri);
        Deque<Long> dates = extractDatesFromResponse(response);
        return dates;
    }
}
