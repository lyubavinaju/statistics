import http.UriReaderImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import socialNetwork.SocialNetworkCreator;
import socialNetwork.vk.Vk;
import socialNetwork.vk.VkCreator;
import statistic.SocialNetworkStat;

public class IntegrationTest {

    @Test
    void testGood() {
        SocialNetworkCreator<Vk> creator = new VkCreator(new UriReaderImpl());
        SocialNetworkStat stat =
                new SocialNetworkStat(creator.get());
        int[] diagram = stat.getPostsDiagramByHours("#лето", 6, 1633025711);
        for (int count : diagram) {
//            System.out.println(count);
            Assertions.assertTrue(count >= 0);
        }
    }

    @Test
    void testTooManyItems() {
        SocialNetworkCreator<Vk> creator = new VkCreator(new UriReaderImpl());
        SocialNetworkStat stat =
                new SocialNetworkStat(creator.get());

        Assertions.assertThrows(Exception.class, () -> stat.getPostsDiagramByHours("#осень",
                10, 1633025711));
    }

}
