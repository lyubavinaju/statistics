package http;

import com.xebialabs.restito.server.StubServer;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;
import static com.xebialabs.restito.semantics.Condition.startsWithUri;

public class UriReaderImplTest {
    private static final int PORT = 32453;

    private void withStubServer(int port, Consumer<StubServer> callback) {
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

    @Test
    public void getResponseToString() {
        withStubServer(PORT, s -> {
            whenHttp(s)
                    .match(method(Method.GET), startsWithUri("/ping"))
                    .then(stringContent("pong"));
            try {
                String result = new UriReaderImpl().getResponseToString(
                        new URI("http://localhost:" + PORT +
                                "/ping"));
                Assertions.assertEquals("pong", result);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        });

    }


    @Test
    public void readAsTextWithNotFoundError() {
        withStubServer(PORT, s -> {
            whenHttp(s)
                    .match(method(Method.GET), startsWithUri("/ping"))
                    .then(status(HttpStatus.NOT_FOUND_404));
            try {
                URI uri = new URI("http://localhost:" + PORT + "/ping");
                UriReaderImpl uriReaderImpl = new UriReaderImpl();
                Assertions.assertThrows(Exception.class, () ->
                        uriReaderImpl.getResponseToString(uri
                        ));
            } catch (URISyntaxException e) {
                throw new RuntimeException();
            }
        });
    }
}
