package com.gantenx.phthonus.socket;


import com.gantenx.phthonus.enums.Market;
import com.gantenx.phthonus.enums.Symbol;
import com.gantenx.phthonus.model.websocket.BinanceEvent;
import com.gantenx.phthonus.model.websocket.BinanceRequest;
import com.gantenx.phthonus.model.websocket.BinanceTicker;
import com.gantenx.phthonus.model.websocket.RealTimeQuote;
import com.gantenx.phthonus.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.gantenx.phthonus.constants.Constant.*;

@Slf4j
public class BinanceBaseSocketClient extends BaseSocketClient {

    public BinanceBaseSocketClient() throws URISyntaxException {
        super(Market.BINANCE.getWebSocketUrl());
    }

    @Override
    protected Consumer<String> getCallback() {
        return text -> {
            try {
                BinanceEvent binanceEvent = JsonUtils.readValue(text, BinanceEvent.class);
                BinanceTicker data = binanceEvent.getData();
                String symbol = data.getSymbol();
                Symbol symbolEnum = Symbol.findBySymbolWithoutDot(symbol);
                RealTimeQuote realTimeQuote = new RealTimeQuote();
                realTimeQuote.setSymbol(symbolEnum);
                realTimeQuote.setTimestamp(System.currentTimeMillis());
                realTimeQuote.setAsk(data.getBestAskPrice());
                realTimeQuote.setLast(data.getLastTradedPrice());
                realTimeQuote.setBid(data.getBestBidPrice());
                realTimeQuote.setMarket(Market.BINANCE);
                kafkaSender.send(KAFKA_TOPIC, MQTT_TOPIC_REALTIME_QUOTE, realTimeQuote);
            } catch (Exception e) {
                log.error("error during sink.{}", text, e);
            }
        };
    }

    @Override
    protected String buildSubscription(Symbol[] symbols) {
        List<String> list = new ArrayList<>();
        for (Symbol s : symbols) {
            String symbol = s.getBase() + s.getQuote();
            String x = symbol.toUpperCase() + "@ticker";
            list.add(x.toLowerCase());
        }
        BinanceRequest request = new BinanceRequest(BINANCE_SUBSCRIBE, list.toArray(new String[0]), id++);
        return JsonUtils.toJson(request);
    }
}
