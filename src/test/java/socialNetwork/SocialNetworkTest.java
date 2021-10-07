package socialNetwork;

import http.UriReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import socialNetwork.vk.VkCreator;

import java.util.Deque;
import java.util.List;

import static org.mockito.Mockito.when;

public class SocialNetworkTest {
    @Mock
    private UriReader uriReader;
    private AbstractSocialNetwork socialNetwork;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        socialNetwork = new VkCreator(uriReader).get();
    }

    @Test
    void test() {
        String query = "query";
        long startTime = 100000;
        long endTime = 200000;
        when(uriReader
                .getResponseToString(
                        socialNetwork.getUriGetDates(query, startTime, endTime)))
                .thenReturn("""
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
                                        """);

        Deque<Long> dates = socialNetwork.getDates(query, startTime, endTime);
        Long[] result = dates.toArray(new Long[0]);
        Assertions.assertArrayEquals(result,
                List.of(100000L, 100001L, 100002L, 100003L).toArray());

    }

}
