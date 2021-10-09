package socialNetwork.vk;

import reader.UriReader;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import socialNetwork.AbstractSocialNetwork;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;


public class Vk extends AbstractSocialNetwork {
    public Vk(UriReader uriReader, Properties properties,
              Properties secretProperties, String api) {
        super(uriReader, properties, secretProperties, api);
    }

    @Override
    public URI getUriGetDates(String query, long startTime, long endTime) {
        Assertions.assertTrue(startTime >= 0 && endTime >= startTime);
        try {
            URIBuilder uriBuilder = new URIBuilder(api);
            secretProperties.forEach((key, value) -> uriBuilder.addParameter(key.toString(),
                    value.toString()));
            properties.forEach((key, value) -> uriBuilder.addParameter(key.toString(),
                    value.toString()));
            uriBuilder.addParameter("q", query);
            uriBuilder.addParameter("start_time", String.valueOf(startTime));
            uriBuilder.addParameter("end_time", String.valueOf(endTime));
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
            throw new RuntimeException("Too many items in time range. Available maximum is " + properties.getProperty("count") +
                    "items.");

        Deque<Long> result = new ArrayDeque<>();
        for (int i = 0; i < items.length(); i++) {
            result.add(items.getJSONObject(i).getLong("date"));
        }
        return result;
    }
}
