package org.stock.app.model;

import org.stock.app.StockClient;

import java.util.Objects;

public class ClientStockInfo {
    String company;
    long count;

    public ClientStockInfo(String company, long count) {
        this.company = company;
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientStockInfo that = (ClientStockInfo) o;
        return count == that.count && company.equals(that.company);
    }

    @Override
    public int hashCode() {
        return Objects.hash(company, count);
    }
}
