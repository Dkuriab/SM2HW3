package org.stock.app;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            StockClient client = new StockClient();
            while (scanner.hasNext()) {
                String s = scanner.next();
                switch (s) {
                    case "get":
                        System.out.println(StockRequests.get(scanner.next()));
                        break;
                    case "add":
                        System.out.println(StockRequests.add(scanner.next(), scanner.nextDouble(), scanner.nextLong()));
                        break;
                    case "sell":
                        System.out.println(StockRequests.sell(scanner.next(), scanner.nextDouble(), scanner.nextLong()));
                        break;
                    case "buy":
                        System.out.println(StockRequests.buy(scanner.next(), scanner.nextDouble(), scanner.nextLong()));
                        break;
                    case "update":
                        System.out.println(StockRequests.update(scanner.next(), scanner.nextDouble()));
                        break;
                    case "addUser":
                        System.out.println(client.addUser());
                        break;
                    case "addMoney":
                        System.out.println(client.addMoney(scanner.nextLong(), scanner.nextDouble()));
                        break;
                    case "getAllMoney":
                        System.out.println(client.getAllMoney(scanner.nextLong()));
                        break;
                    case "getStocks":
                        System.out.println(client.getStocksFullInfo(scanner.nextLong()));
                        break;
                    case "userBuy":
                        System.out.println(client.buy(scanner.nextLong(), scanner.next(), scanner.nextLong(), scanner.nextDouble()));
                        break;
                    case "userSell":
                        System.out.println(client.sell(scanner.nextLong(), scanner.next(), scanner.nextLong(), scanner.nextDouble()));
                        break;
                    default:
                        break;
                }
            }
        }
    }

}
