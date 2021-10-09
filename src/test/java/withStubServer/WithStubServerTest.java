package withStubServer;

import com.xebialabs.restito.server.StubServer;

import java.util.function.Consumer;

public class WithStubServerTest {
    protected static final int PORT = 32453;

    protected void withStubServer(int port, Consumer<StubServer> callback) {
        StubServer stubServer = null;
        try {
            stubServer = new StubServer(port).run();
            callback.accept(stubServer);
        } finally {
            if (stubServer != null) {
                stubServer.stop();
            }
        }
    }
}
