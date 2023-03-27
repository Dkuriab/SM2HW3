package org.stock.app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StockRequests {
    public static String BASE_URL = "http://localhost:8080";
    public  static HttpClient httpClient = HttpClient.newHttpClient();

    // Добавлять новые компании и их акции
    public static boolean add(String company, double price, double count) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(buildRequestString("add", company, price, count)))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String result = response.body();
            return Boolean.parseBoolean(result);
        } catch (Exception exception) {
            return false;
        }
    }

    // Узнавать текущую цену акций и их количество на бирже
    public static String get(String company) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/get?company=" + company))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception exception) {
            System.err.println(exception);
            return "";
        }
    }

    // Покупать акции компаний по текущей цене
    public static boolean buy(String company, double price, double count) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(buildRequestString("buy", company, price, count)))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String result = response.body();
            return Boolean.parseBoolean(result);
        } catch (Exception exception) {
            return false;
        }
    }

    // Динамически менять курс акций
    public static boolean update(String company, double diff) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/update?company=" + company + "&diff=" + diff))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String result = response.body();
            return Boolean.parseBoolean(result);
        } catch (Exception exception) {
            return false;
        }
    }

    public static void clear() {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(BASE_URL + "/clear"))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            response.body();
        } catch (Exception ignored) {
        }
    }

    public static boolean sell(String company, double price, double count) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(buildRequestString("sell", company, price, count)))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String result = response.body();
            return Boolean.parseBoolean(result);
        } catch (Exception exception) {
            return false;
        }
    }

    private static String buildRequestString(String req, String company, double price, double count) {
        return BASE_URL + "/" + req + "?company=" + company + "&price=" + price + "&count=" + count;
    }
}
