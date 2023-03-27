package org.stock.app.model;

public class Stock {
    public final double count;
    public final double price;

    public Stock(double count, double price) {
        this.count = count;
        this.price = price;
    }

    /**
     * @return Json string
     */
    @Override
    public String toString() {
        return "{" + "\"count\":" + count + "," + "\"price\":" + price + '}';
    }
}
