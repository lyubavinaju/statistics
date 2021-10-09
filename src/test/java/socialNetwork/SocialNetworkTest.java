package socialNetwork;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reader.PropertiesReader;
import reader.PropertiesReaderImpl;
import reader.UriReader;
import socialNetwork.vk.Vk;
import socialNetwork.vk.VkPropertiesGetter;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;

public class SocialNetworkTest {

    private PropertiesReader propertiesReader;
    @Mock
    private UriReader uriReader;

    private SocialNetwork socialNetwork;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        propertiesReader = new PropertiesReaderImpl();
    }

    @Test
    void testVkGoodAnswer() {
        SocialNetworkPropertiesGetter propertiesGetter =
                new VkPropertiesGetter(propertiesReader);
        socialNetwork = new Vk(uriReader,
                propertiesGetter.getProperties(),
                propertiesGetter.getSecretProperties(),
                propertiesGetter.getApi()
        );

        String query = "query";
        long startTime = 100000;
        long endTime = 200000;
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
        when(uriReader
                .getResponseToString(
                        socialNetwork.getUriGetDates(query, startTime, endTime)))
                .thenReturn(String.format(response, value1, value2, value3, value4));

        Deque<Long> dates = socialNetwork.getDates(query, startTime, endTime);
        Long[] result = dates.toArray(new Long[0]);
        Assertions.assertArrayEquals(result,
                List.of(value1, value2, value3, value4).toArray());

    }

    @Test
    void testVkBadAnswer() {
        SocialNetworkPropertiesGetter propertiesGetter =
                new VkPropertiesGetter(propertiesReader);
        socialNetwork = new Vk(uriReader,
                propertiesGetter.getProperties(),
                propertiesGetter.getSecretProperties(),
                propertiesGetter.getApi()
        );

        String query = "query";
        long startTime = 100000;
        long endTime = 200000;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long value1 = random.nextLong(startTime, endTime + 1);
        long value2 = random.nextLong(startTime, endTime + 1);
        long value3 = random.nextLong(startTime, endTime + 1);
        long value4 = random.nextLong(startTime, endTime + 1);
        String response = """
                {
                "error" : "error"
                 }
                }
                                """;

        when(uriReader
                .getResponseToString(
                        socialNetwork.getUriGetDates(query, startTime, endTime)))
                .thenReturn(String.format(response, value1, value2, value3, value4));
        Assertions.assertThrows(Exception.class, () -> socialNetwork.getDates(query, startTime,
                endTime));


    }

}
