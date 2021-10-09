import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reader.PropertiesReader;
import reader.PropertiesReaderImpl;
import reader.UriReader;
import reader.UriReaderImpl;
import socialNetwork.SocialNetwork;
import socialNetwork.SocialNetworkPropertiesGetter;
import socialNetwork.vk.Vk;
import socialNetwork.vk.VkPropertiesGetter;
import statistic.SocialNetworkStat;

public class IntegrationTest {
    private SocialNetworkStat stat;

    @BeforeEach
    void init() {
        PropertiesReader propertiesReader = new PropertiesReaderImpl();
        UriReader uriReader = new UriReaderImpl();
        SocialNetworkPropertiesGetter propertiesGetter =
                new VkPropertiesGetter(propertiesReader);
        SocialNetwork socialNetwork = new Vk(uriReader,
                propertiesGetter.getProperties(),
                propertiesGetter.getSecretProperties(),
                propertiesGetter.getApi()
        );
        stat = new SocialNetworkStat(socialNetwork);

    }

    @Test
    void testGood() {
        int[] diagram = stat.getPostsDiagramByHours("#лето", 6, 1633025711);
        for (int count : diagram) {
//            System.out.println(count);
            Assertions.assertTrue(count >= 0);
        }
    }

    @Test
    void testTooManyItems() {
        Assertions.assertThrows(Exception.class, () -> stat.getPostsDiagramByHours("#осень",
                10, 1633025711));
    }

}