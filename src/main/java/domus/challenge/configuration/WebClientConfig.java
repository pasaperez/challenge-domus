package domus.challenge.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {
    @Value("${data-source.base-url}")
    private String baseUrl;

    @Bean
    public WebClient customWebClient(WebClient.Builder webClientBuilder) {
        HttpClient httpClient = HttpClient.create()
                .tcpConfiguration(tcpClient ->
                        tcpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                                .doOnConnected(connection ->
                                        connection.addHandlerLast(new ReadTimeoutHandler(10000, TimeUnit.MILLISECONDS))
                                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))));

        return webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(this.baseUrl)
                .build();
    }
}
