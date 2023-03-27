package org.stock.app.model;

import org.stock.app.StockClient;

import java.util.Objects;

public class StockInfo {
    public String company;
    public double price;
    public double count;

    public StockInfo(String company, double price, double count) {
        this.company = company;
        this.price = price;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockInfo stockInfo = (StockInfo) o;
        return Double.compare(stockInfo.price, price) == 0 && count == stockInfo.count && company.equals(stockInfo.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, price, count);
    }
}
