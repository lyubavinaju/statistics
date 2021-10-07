package socialNetwork.vk;

import http.UriReader;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import socialNetwork.AbstractSocialNetwork;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;


public class Vk extends AbstractSocialNetwork {
    private final String API;
    private final String VERSION;
    private final String COUNT;
    private final String ACCESS_TOKEN;

    Vk(Properties properties,
       Properties secretProperties, UriReader uriReader) {
        super(uriReader);
        ACCESS_TOKEN = secretProperties.getProperty("accessToken");
        API = properties.getProperty("vk.api");
        VERSION = properties.getProperty("vk.version");
        COUNT = properties.getProperty("vk.limit");
    }

    @Override
    public URI getUriGetDates(String query, long startTime, long endTime) {
        Assertions.assertTrue(startTime >= 0 && endTime >= startTime);
        try {
            URIBuilder uriBuilder = new URIBuilder(API);
            uriBuilder.addParameter("q", query);
            uriBuilder.addParameter("access_token", ACCESS_TOKEN);
            uriBuilder.addParameter("v", VERSION);
            uriBuilder.addParameter("start_time", String.valueOf(startTime));
            uriBuilder.addParameter("end_time", String.valueOf(endTime));
            uriBuilder.addParameter("count", COUNT);
            return uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Deque<Long> extractDatesFromResponse(String response) {
        JSONObject jsonGetResponse = new JSONObject(response)
                .getJSONObject("response");
        JSONArray items = jsonGetResponse
                .getJSONArray("items");
        if (jsonGetResponse.has("next_from"))
            throw new RuntimeException("Too many items in time range. Available maximum is " + COUNT +
                    "items.");

        Deque<Long> result = new ArrayDeque<>();
        for (int i = 0; i < items.length(); i++) {
            result.add(items.getJSONObject(i).getLong("date"));
        }
        return result;
    }
}
