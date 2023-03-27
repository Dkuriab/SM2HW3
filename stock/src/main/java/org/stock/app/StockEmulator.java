package org.stock.app;

import org.stock.app.model.Stock;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

public class StockEmulator {

    HashMap<String, Stock> stockHashMap = new HashMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public boolean add(String company, double startPrice, double numberOfStocks) {
        lock.lock();
        try {
            Stock curStock = stockHashMap.putIfAbsent(company, new Stock(numberOfStocks, startPrice));
            return curStock == null;
        } finally {
            lock.unlock();
        }
    }

    public boolean add(String company, double startPrice) {
        return add(company, startPrice, 0);
    }

    public Stock get(String company) {
        lock.lock();
        try {
            return stockHashMap.get(company);
        } finally {
            lock.unlock();
        }
    }

    public boolean update(String company, double diff) {
        lock.lock();
        try {
            Stock curStock = stockHashMap.get(company);
            if (curStock == null) {
                return false;
            }
            stockHashMap.put(company, new Stock(curStock.count, curStock.price + diff));
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean buy(String company, double count, double priceForOne) {
        lock.lock();
        try {
            Stock curStock = stockHashMap.get(company);
            if (curStock == null || curStock.count < count || curStock.price != priceForOne) {
                return false;
            } else {
                stockHashMap.put(company, new Stock(curStock.count - count, curStock.price));
                return true;
            }
        } finally {
            lock.unlock();
        }
    }

    public boolean sell(String company, double count, double priceForOne) {
        lock.lock();
        try {
            Stock curStock = stockHashMap.get(company);
            if (curStock == null || curStock.price != priceForOne) {
                return false;
            } else {
                stockHashMap.put(company, new Stock(curStock.count + count, curStock.price));
                return true;
            }
        } finally {
            lock.unlock();
        }
    }


    public void clear() {
        lock.lock();
        try {
            stockHashMap.clear();
        } finally {
            lock.unlock();
        }
    }
}
