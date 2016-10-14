package io.sphere.sdk.client;

import io.sphere.sdk.http.HttpClient;
import io.sphere.sdk.http.HttpHeaders;
import io.sphere.sdk.http.HttpRequest;
import io.sphere.sdk.http.HttpResponse;
import io.sphere.sdk.meta.BuildInfo;
import io.sphere.sdk.projects.queries.ProjectGet;
import io.sphere.sdk.test.IntegrationTest;
import org.junit.Test;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAgentIntegrationTest extends IntegrationTest {
    @Test
    public void userAgent() throws Exception {
        final FakeHttpClient httpClient = new FakeHttpClient();
        final SphereClient client = SphereClientFactory.of(() -> httpClient).createClient(getSphereClientConfig());
        client.execute(ProjectGet.of()).toCompletableFuture().join();
        assertThat(httpClient.getLastUserAgent())
                .startsWith("commercetools-jvm-sdk")
                .contains("JVM-SDK-integration-tests")
                .contains(BuildInfo.version());
    }

    private static class FakeHttpClient implements HttpClient {
        private final HttpClient delegate = SphereClientFactory.of().createHttpClient();
        private String lastUserAgent;

        @Override
        public CompletionStage<HttpResponse> execute(final HttpRequest httpRequest) {
            lastUserAgent = httpRequest.getHeaders().getHeader(HttpHeaders.USER_AGENT).stream().findFirst().orElse(null);
            return delegate.execute(httpRequest);
        }

        @Override
        public void close() {
            delegate.close();
        }

        @Nullable
        @Override
        public String getUserAgent() {
            return delegate.getUserAgent();
        }

        public String getLastUserAgent() {
            return lastUserAgent;
        }
    }
}
