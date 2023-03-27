package org.stock.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class StockController {

    private static final StockEmulator stockEmulator = new StockEmulator();

    @RequestMapping("/add")
    public String add(String company, Double price, Double count) {
        return String.valueOf(stockEmulator.add(company, price, count));
    }

    @RequestMapping("/update")
    public String update(String company, Double diff) {
        return String.valueOf(stockEmulator.update(company, diff));
    }

    @RequestMapping("/clear")
    public String update() {
        stockEmulator.clear();
        return "cleared";
    }

    @RequestMapping("/get")
    public String get(String company) {
        return Objects.toString(stockEmulator.get(company), "not found");
    }

    @RequestMapping("/buy")
    public String buy(String company, Double price, Double count) {
        return String.valueOf(stockEmulator.buy(company, count, price));
    }

    @RequestMapping("/sell")
    public String sell(String company, Double price, Double count) {
        return String.valueOf(stockEmulator.sell(company, count, price));
    }
}
