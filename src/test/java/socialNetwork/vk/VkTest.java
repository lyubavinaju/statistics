package socialNetwork.vk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reader.PropertiesReader;
import reader.PropertiesReaderImpl;
import reader.UriReader;
import reader.UriReaderImpl;
import socialNetwork.SocialNetworkPropertiesGetter;

import java.util.Deque;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class VkTest {
    Vk vk;

    @BeforeEach
    void init() {
        PropertiesReader propertiesReader = new PropertiesReaderImpl();
        UriReader uriReader = new UriReaderImpl();
        SocialNetworkPropertiesGetter propertiesGetter =
                new VkPropertiesGetter(propertiesReader);
        vk = new Vk(uriReader,
                propertiesGetter.getProperties(),
                propertiesGetter.getSecretProperties(),
                propertiesGetter.getApi()
        );
    }

    @Test
    void testGetUriWithNegativeStartTime() {
        Assertions.assertThrows(AssertionError.class,
                () -> vk.getUriGetDates("query", -1, 1000));

    }

    @Test
    void testGetUriWithIncorrectRange() {
        Assertions.assertThrows(AssertionError.class,
                () -> vk.getUriGetDates("query", 1000, 100));

    }

    @Test
    void testExtractDatesFromResponse() {
        long startTime = 100000;
        long endTime = 200000;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long value1 = random.nextLong(startTime, endTime + 1);
        long value2 = random.nextLong(startTime, endTime + 1);
        long value3 = random.nextLong(startTime, endTime + 1);
        long value4 = random.nextLong(startTime, endTime + 1);
        String responsePattern = """
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
        String response = String.format(responsePattern, value1, value2, value3, value4);
        Deque<Long> dates = vk.extractDatesFromResponse(response);
        Long[] result = dates.toArray(new Long[0]);
        Assertions.assertArrayEquals(result,
                List.of(value1, value2, value3, value4).toArray());
    }

    @Test
    void testTooLongResponse() {
        long startTime = 100000;
        long endTime = 200000;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        long value1 = random.nextLong(startTime, endTime + 1);
        long value2 = random.nextLong(startTime, endTime + 1);
        long value3 = random.nextLong(startTime, endTime + 1);
        long value4 = random.nextLong(startTime, endTime + 1);
        String responsePattern = """
                {
                "response" : {
                  "items" : [
                  {"date" : %d},
                  {"date" : %d},
                  {"date" : %d},
                  {"date" : %d}
                  ],
                  "next_from": "next"
                 }
                }
                                """;

        Assertions.assertThrows(
                Exception.class,
                () -> vk.extractDatesFromResponse(String.format(responsePattern, value1, value2, value3, value4))
        );

    }
}
