package socialNetwork.vk;

import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reader.PropertiesReader;
import reader.PropertiesReaderImpl;
import reader.UriReader;
import reader.UriReaderImpl;
import socialNetwork.SocialNetwork;
import socialNetwork.SocialNetworkPropertiesGetter;
import withStubServer.WithStubServerTest;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.*;

public class VkWithStubServerTest extends WithStubServerTest {
    private static final int HOUR_IN_SECONDS = 3600;
    private SocialNetwork socialNetwork;

    @BeforeEach
    void init() {
        PropertiesReader propertiesReader = new PropertiesReaderImpl();
        UriReader uriReader = new UriReaderImpl();
        SocialNetworkPropertiesGetter propertiesGetter =
                new VkPropertiesGetter(propertiesReader);
        socialNetwork = new Vk(uriReader,
                propertiesGetter.getProperties(),
                propertiesGetter.getSecretProperties(),
                "http://localhost:" + PORT + "/search"
        );

    }


    @Test
    void testPositive() {
        int hours = 3;
        String query = "#лето";
        long timeStampInSeconds = System.currentTimeMillis() / 1000;

        long endTime = timeStampInSeconds - 1;
        long startTime =
                timeStampInSeconds - (long) hours * HOUR_IN_SECONDS;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        long value1 = random.nextLong(startTime, endTime + 1);
        long value2 = random.nextLong(startTime, endTime + 1);
        long value3 = random.nextLong(startTime, endTime + 1);
        long value4 = random.nextLong(startTime, endTime + 1);

        String response = """
                {
                "response" : {
                  "items" : [
                  {"date" : %d},
                  {"date" : %d},
                  {"date" : %d},
                  {"date" : %d}
                  ]
                 }
                }
                                """;
        withStubServer(PORT, s -> {
            whenHttp(s)
                    .match(method(Method.GET), startsWithUri("/search"))
                    .then(stringContent(String.format(response, value1, value2, value3, value4)));
            Deque<Long> dates = socialNetwork.getDates(query, startTime, endTime);
            Long[] result = dates.toArray(new Long[0]);

            Assertions.assertArrayEquals(result,
                    List.of(value1, value2, value3, value4).toArray());

        });

    }

    @Test
    void testIncorrectDataInApiAnswer() {
        int hours = 3;
        String query = "#лето";
        long timeStampInSeconds = System.currentTimeMillis() / 1000;

        long endTime = timeStampInSeconds - 1;
        long startTime =
                timeStampInSeconds - (long) hours * HOUR_IN_SECONDS;

        String response = """
                {
                "error" : "error"
                }
                                """;
        withStubServer(PORT, s -> {
            whenHttp(s)
                    .match(method(Method.GET), startsWithUri("/search"))
                    .then(stringContent(response));
            Assertions.assertThrows(Exception.class, () -> socialNetwork.getDates(query,
                    startTime, endTime));

        });
    }

    @Test
    void testBadCode() {
        int hours = 3;
        String query = "#лето";
        long timeStampInSeconds = System.currentTimeMillis() / 1000;

        long endTime = timeStampInSeconds - 1;
        long startTime =
                timeStampInSeconds - (long) hours * HOUR_IN_SECONDS;

        withStubServer(PORT, s -> {
            whenHttp(s)
                    .match(method(Method.GET), startsWithUri("/search"))
                    .then(status(HttpStatus.NOT_FOUND_404));
            Assertions.assertThrows(Exception.class, () -> socialNetwork.getDates(query,
                    startTime, endTime));

        });
    }

}
