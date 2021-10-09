package reader;

import reader.UriReaderImpl;
import org.glassfish.grizzly.http.Method;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import withStubServer.WithStubServerTest;

import java.net.URI;
import java.net.URISyntaxException;

import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.semantics.Action.status;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.method;
import static com.xebialabs.restito.semantics.Condition.startsWithUri;

public class UriReaderImplTest extends WithStubServerTest {

    private static final int PORT = 32453;

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
