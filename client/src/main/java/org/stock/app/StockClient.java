package org.stock.app;

import org.stock.app.model.ClientStockInfo;
import org.stock.app.model.UserInfo;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.stream.DoubleStream;

public class StockClient {

    String baseURL = "http://localhost:8080";
    public Map<Long, UserInfo> usersToInfo = new HashMap<>();
    Random random = new Random();

    HttpClient httpClient = HttpClient.newHttpClient();


    public long addUser() {
        long id = random.nextLong();
        while (usersToInfo.containsKey(id)) {
            id = random.nextLong();
        }
        usersToInfo.put(id, new UserInfo());
        return id;
    }

    public boolean addMoney(Long userId, double extraMoney) {
        UserInfo userInfo = usersToInfo.get(userId);
        if (userInfo == null) {
            return false;
        }
        userInfo.freeMoney += extraMoney;
        return true;
    }

    public List<org.stock.app.model.StockInfo> getStocksFullInfo(long userId) {
        UserInfo userInfo = usersToInfo.get(userId);
        if (userInfo == null) {
            return Collections.emptyList();
        }
        return getStocksFullInfo(userInfo);
    }
    public double getAllMoney(long userId) {
        UserInfo userInfo = usersToInfo.get(userId);
        if (userInfo == null) {
            return 0;
        }
        return userInfo.freeMoney + getStocksFullInfo(userInfo).stream().flatMapToDouble(u -> DoubleStream.of(u.count * u.price)).sum();
    }

    public boolean sell(long userId, String company, long count, double price) {
        UserInfo userInfo = usersToInfo.get(userId);
        if (userInfo == null) {
            return false;
        }
        Long numberOfStock = userInfo.stocks.get(company);
        if (numberOfStock == null || numberOfStock < count) {
            return false;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseURL + "/sell?company=" + company + "&price=" + price + "&count=" + count))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String result = response.body();
            boolean flag =  Boolean.parseBoolean(result);
            if (flag) {
                userInfo.freeMoney += price * count;
                Long prevStock = userInfo.stocks.get(company);
                if (prevStock == count) {
                    userInfo.stocks.remove(company);
                } else {
                    userInfo.stocks.put(company, prevStock - count);
                }
            }
            return flag;
        } catch (Exception exception) {
            return false;
        }
    }

    public boolean buy(long userId, String company, long count, double price) {
        UserInfo userInfo = usersToInfo.get(userId);
        if (userInfo == null || userInfo.freeMoney < count * price) {
            return false;
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseURL + "/buy?company=" + company + "&price=" + price + "&count=" + count))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String result = response.body();
            boolean flag = Boolean.parseBoolean(result);
            if (flag) {
                userInfo.stocks.merge(company, count, Long::sum);
                userInfo.freeMoney -= price * count;
            }
            return flag;
        } catch (Exception exception) {
            return false;
        }
    }

    private List<org.stock.app.model.StockInfo> getStocksFullInfo(UserInfo userInfo) {
        List<org.stock.app.model.StockInfo> result = new ArrayList<>();
        for (Map.Entry<String, Long> entry : userInfo.stocks.entrySet()) {
            result.add(getStockFullInfo(entry.getKey(), entry.getValue()));
        }
        result.sort(Comparator.comparing(o -> o.company));
        return result;
    }

    private org.stock.app.model.StockInfo getStockFullInfo(String company, long count) {
        double curStockPrice = getPrice(company);
        return new org.stock.app.model.StockInfo(company, curStockPrice, count);
    }

    public double getPrice(String company) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(baseURL + "/get?company=" + company))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String jsonString = response.body();
            JSONObject obj = new JSONObject(jsonString);
            return obj.getDouble("price");
        } catch (Exception exception) {
            return 0;
        }
    }

    public ClientStockInfo getStock(long userId, String company) {
        Map<String, Long> stocks = usersToInfo.get(userId).stocks;
        if (stocks.containsKey(company)) {
            return new ClientStockInfo(company, usersToInfo.get(userId).stocks.get(company));
        } else {
            return null;
        }
    }

    public double getFreeMoney(long userId) {
        return usersToInfo.get(userId).freeMoney;
    }
}
