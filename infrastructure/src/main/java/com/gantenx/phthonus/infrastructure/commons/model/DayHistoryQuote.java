package com.gantenx.phthonus.infrastructure.commons.model;


import com.gantenx.phthonus.infrastructure.commons.enums.Market;
import com.gantenx.phthonus.infrastructure.commons.enums.Symbol;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
@AllArgsConstructor
public class DayHistoryQuote {
    private Symbol symbol;
    private long timestamp;
    @NotNull
    private Market market;
    @NotNull
    private String preClose;
}
