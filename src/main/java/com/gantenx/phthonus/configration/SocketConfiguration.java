package com.gantenx.phthonus.configration;

import com.gantenx.phthonus.enums.Market;
import com.gantenx.phthonus.socket.SocketTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SocketConfiguration {

    @Bean
    public SocketTask binanceSocketTask() {
        return new SocketTask(Market.BINANCE);
    }

    @Bean
    public SocketTask cryptoSocketTask() {
        return new SocketTask(Market.CRYPTO_COM);
    }
}
