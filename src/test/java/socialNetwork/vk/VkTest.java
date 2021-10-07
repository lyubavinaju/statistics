package socialNetwork.vk;

import http.UriReaderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Deque;
import java.util.List;

public class VkTest {
    Vk vk;

    @BeforeEach
    void init() {
        vk = new VkCreator(new UriReaderImpl()).get();
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
        String response = """
                {
                "response" : {
                  "items" : [
                  {"date" : 100000},
                  {"date" : 100001},
                  {"date" : 100002},
                  {"date" : 100003}
                  ]
                 }
                }
                                """;
        Deque<Long> dates = vk.extractDatesFromResponse(response);
        Long[] result = dates.toArray(new Long[0]);
        Assertions.assertArrayEquals(result,
                List.of(100000L, 100001L, 100002L, 100003L).toArray());
    }

    @Test
    void testTooLongResponse() {
        String response = """
                {
                "response" : {
                  "items" : [
                  {"date" : 100000},
                  {"date" : 100001},
                  {"date" : 100002},
                  {"date" : 100003}
                  ],
                  "next_from": "next"
                 }
                }
                                """;

        Assertions.assertThrows(
                Exception.class,
                () -> vk.extractDatesFromResponse(response)
        );

    }
}
