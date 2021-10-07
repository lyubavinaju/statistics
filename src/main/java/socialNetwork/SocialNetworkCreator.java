package socialNetwork;

public interface SocialNetworkCreator<T extends SocialNetwork> {
    T get();
}
