package reader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class UriReaderImpl implements UriReader {
    private static final int RESPONSE_OK_CODE = 200;

    @Override
    public String getResponseToString(URI uri) {
        try {
            HttpGet request = new HttpGet(uri);

            HttpClient client = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setCookieSpec(CookieSpecs.IGNORE_COOKIES).build()).build();
            HttpResponse response = client.execute(request);

            if (response.getStatusLine().getStatusCode() != RESPONSE_OK_CODE) {
                throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                        response.getStatusLine().getReasonPhrase());
            }

            String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return json;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
