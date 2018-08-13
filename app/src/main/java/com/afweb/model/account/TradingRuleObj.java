/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.afweb.model.account;

import com.vpumlmodel.account.tradingRule;

/**
 *
 * @author eddy
 */
public class TradingRuleObj extends tradingRule {

    public static final String TR_MV = "Trading";
    public static final int INT_TR_MV = 5;

    public static final String SIM_TR_MV = "Sim_MV";
    public static final int INT_SIM_TR_MV = 10;

    public static final String SIM_TR_MACD = "Sim_MACD";
    public static final int INT_SIM_TR_MACD = 11;

    public static final String SIM_TR_RSI = "Sim_RSI";
    public static final int INT_SIM_TR_RSI = 12;

    private String symbol;

    /**
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * @param symbol the symbol to set
     */
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
