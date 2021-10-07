package statistic;

import org.junit.jupiter.api.Assertions;
import socialNetwork.SocialNetwork;

import java.util.Deque;

public class SocialNetworkStat {
    private final SocialNetwork socialNetwork;
    public static final int HOUR_IN_SECONDS = 3600;

    public SocialNetworkStat(SocialNetwork socialNetwork) {
        this.socialNetwork = socialNetwork;
    }

    public int[] getPostsDiagramByHours(String hashtag, int hours, long timeStampInSeconds) {
        Assertions.assertTrue(hashtag.startsWith("#") && hashtag.length() > 1);
        Assertions.assertTrue(timeStampInSeconds <= System.currentTimeMillis() / 1000);
        long endTimeInSeconds = timeStampInSeconds - 1;
        long startTimeInSeconds =
                timeStampInSeconds - (long) hours * HOUR_IN_SECONDS;
        Assertions.assertTrue(startTimeInSeconds >= 0);

        Deque<Long> dates = socialNetwork
                .getDates(hashtag, startTimeInSeconds, endTimeInSeconds);

        int[] diagram = new int[hours];

        for (long date : dates
        ) {
            Assertions.assertFalse(date < startTimeInSeconds || date > endTimeInSeconds);
            int hour = (int) ((date - startTimeInSeconds) / HOUR_IN_SECONDS);
            diagram[hour]++;
        }
        return diagram;
    }
}
