package statistic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import socialNetwork.SocialNetwork;

import java.util.ArrayDeque;
import java.util.Deque;

import static org.mockito.Mockito.when;

public class SocialNetworkStatTest {
    private static final int HOUR_IN_SECONDS = 3600;
    private SocialNetworkStat stat;
    @Mock
    private SocialNetwork socialNetwork;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
        stat = new SocialNetworkStat(socialNetwork);
    }

    @Test
    void testGetPostsDiagramByHoursOneHour() {
        int hours = 1;
        String query = "#лето";
        long timeStampInSeconds = 163302571;

        long endTimeInSeconds = timeStampInSeconds - 1;
        long startTimeInSeconds =
                timeStampInSeconds - (long) hours * HOUR_IN_SECONDS;
        when(socialNetwork
                .getDates(query,
                        startTimeInSeconds,
                        endTimeInSeconds))
                .thenReturn(createAnswers(startTimeInSeconds, endTimeInSeconds));

        int[] postsDiagramByHours = stat.getPostsDiagramByHours(query, hours, timeStampInSeconds);
        Assertions.assertArrayEquals(new int[]{4}, postsDiagramByHours);
    }

    @Test
    void testGetPostsDiagramByHoursThreeHours() {
        int hours = 3;
        String query = "#лето";
        long timeStampInSeconds = System.currentTimeMillis() / 1000;

        long endTimeInSeconds = timeStampInSeconds - 1;
        long startTimeInSeconds =
                timeStampInSeconds - (long) hours * HOUR_IN_SECONDS;
        when(socialNetwork
                .getDates(query,
                        startTimeInSeconds,
                        endTimeInSeconds))
                .thenReturn(createAnswers(startTimeInSeconds, endTimeInSeconds));

        int[] postsDiagramByHours = stat.getPostsDiagramByHours(query, hours, timeStampInSeconds);
        Assertions.assertArrayEquals(new int[]{2, 0, 2}, postsDiagramByHours);
    }


    @Test
    void testIncorrectContentInApiAnswer() {
        int hours = 1;
        String query = "#лето";
        long timeStampInSeconds = 163302571;

        long endTimeInSeconds = timeStampInSeconds - 1;
        long startTimeInSeconds =
                timeStampInSeconds - (long) hours * HOUR_IN_SECONDS;
        Deque<Long> apiAnswer = new ArrayDeque<>();
        apiAnswer.add(100L);


        when(socialNetwork
                .getDates(query,
                        startTimeInSeconds,
                        endTimeInSeconds))
                .thenReturn(apiAnswer);
        Assertions.assertThrows(AssertionError.class,
                () -> stat.getPostsDiagramByHours(query, hours, timeStampInSeconds));

    }

    private Deque<Long> createAnswers(long startTimeInSeconds, long endTimeInSeconds) {
        Deque<Long> deque = new ArrayDeque<>();
        deque.add(startTimeInSeconds);
        deque.add(startTimeInSeconds);
        deque.add(endTimeInSeconds);
        deque.add(endTimeInSeconds);
        return deque;
    }

}