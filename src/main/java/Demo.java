import reader.PropertiesReader;
import reader.PropertiesReaderImpl;
import reader.UriReader;
import reader.UriReaderImpl;
import socialNetwork.SocialNetwork;
import socialNetwork.SocialNetworkPropertiesGetter;
import socialNetwork.vk.Vk;
import socialNetwork.vk.VkPropertiesGetter;
import statistic.SocialNetworkStat;

public class Demo {
    public static void main(String[] args) {
        PropertiesReader propertiesReader = new PropertiesReaderImpl();
        UriReader uriReader = new UriReaderImpl();
        SocialNetworkPropertiesGetter propertiesGetter =
                new VkPropertiesGetter(propertiesReader);
        SocialNetwork socialNetwork = new Vk(uriReader,
                propertiesGetter.getProperties(),
                propertiesGetter.getSecretProperties(),
                propertiesGetter.getApi()
        );
        SocialNetworkStat stat = new SocialNetworkStat(socialNetwork);
        int[] diagram = stat.getPostsDiagramByHours("#лето", 3, System.currentTimeMillis() / 1000);
        for (int count : diagram) {
            System.out.println(count);
        }
    }
}
