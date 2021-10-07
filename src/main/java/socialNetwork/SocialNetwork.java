package socialNetwork;

import java.util.Deque;

public interface SocialNetwork {
    Deque<Long> getDates(String query, long startTime, long endTime);
}
