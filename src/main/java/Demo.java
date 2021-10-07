import http.UriReaderImpl;
import socialNetwork.SocialNetworkCreator;
import socialNetwork.vk.Vk;
import socialNetwork.vk.VkCreator;
import statistic.SocialNetworkStat;

public class Demo {
    public static void main(String[] args) {
        SocialNetworkCreator<Vk> creator = new VkCreator(new UriReaderImpl());
        SocialNetworkStat stat =
                new SocialNetworkStat(creator.get());
        int[] diagram = stat.getPostsDiagramByHours("#лето", 3, System.currentTimeMillis() / 1000);
        for (int count : diagram) {
            System.out.println(count);
        }
    }
}
