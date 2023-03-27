import org.stock.app.StockRequests;
import org.stock.app.StockClient;
import org.stock.app.model.ClientStockInfo;
import org.junit.*;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {
    public static StockClient client;
    @ClassRule
    public static GenericContainer simpleWebServer = new FixedHostPortGenericContainer("stock:1.0-SNAPSHOT").withFixedExposedPort(8080, 8080).withExposedPorts(8080);


    @Before
    public void before() {
        StockRequests.add("Company-A", 123.31, 213);
        StockRequests.add("Company-B", 4123.42, 124124);
        StockRequests.add("Company-C", 123, 35);
        client = new StockClient();
    }

    @After
    public void after() {
        StockRequests.clear();
    }

    @Test
    public void userSuccessfullyBuyStock() {
        long userId = client.addUser();
        long buyingCount = 10;
        double userBalance = 1000000.0;
        client.addMoney(userId, userBalance);

        String company = "Company-A";
        double companyPrice = client.getPrice(company);


        assertTrue(client.buy(userId,company, buyingCount, companyPrice));
        assertEquals(userBalance - buyingCount * companyPrice, client.getFreeMoney(userId));
        ClientStockInfo expectedStock = new ClientStockInfo(company, buyingCount);
        assertEquals(expectedStock, client.getStock(userId, company));
    }

    @Test
    public void failBuyStock() {
        long userId = client.addUser();
        long buyingCount = 10;
        double userBalance = 1000.0;
        client.addMoney(userId, userBalance);
        String company = "Company-A";
        double priceOfCompany = client.getPrice(company);
        StockRequests.update(company, 10000);

        assertFalse(client.buy(userId,company, buyingCount, priceOfCompany));
        assertEquals(userBalance, client.getFreeMoney(userId));
        assertNull(client.getStock(userId, company));
    }

    @Test
    public void failNotEnoughMoney() {
        long userId = client.addUser();
        long buyingCount = 1000;
        double userBalance = 100.0;
        client.addMoney(userId, userBalance);
        String company = "Company-A";
        double priceOfCompany = client.getPrice(company);

        assertFalse(client.buy(userId,company, buyingCount, priceOfCompany));
        assertEquals(userBalance, client.getFreeMoney(userId));
        assertNull(client.getStock(userId, company));
    }

    @Test
    public void failBuyStockNotEnoughFreeStocks() {
        String company = "Company-C";
        long buyingCount = 45;

        long userId = client.addUser();
        double userBalance = 1000000.0;
        client.addMoney(userId, userBalance);
        double priceOfCompany = client.getPrice(company);

        assertFalse(client.buy(userId,company, buyingCount, priceOfCompany));
        assertEquals(userBalance, client.getFreeMoney(userId));
        assertNull(client.getStock(userId, company));
    }

    @Test
    public void successSellStock() {
        String company = "Company-C";
        long buyingCount = 30;

        long userId = client.addUser();
        double userBalance = 1000000.0;
        client.addMoney(userId, userBalance);
        double priceOfCompany = client.getPrice(company);

        assertTrue(client.buy(userId, company, buyingCount, priceOfCompany));

        assertTrue(client.sell(userId, company, buyingCount / 2, priceOfCompany));

        assertEquals(userBalance - priceOfCompany * ((double) buyingCount / 2), client.getFreeMoney(userId));
        ClientStockInfo expectedStock = new ClientStockInfo(company, buyingCount / 2);
        assertEquals(expectedStock, client.getStock(userId, company));
    }

    @Test
    public void failSellStockNotEnoughStocks() {
        long userId = client.addUser();
        long buyingCount = 10;
        double userBalance = 10000000.0;

        String company = "Company-A";
        client.addMoney(userId, userBalance);
        double priceOfCompany = client.getPrice(company);
        client.buy(userId,company, buyingCount, priceOfCompany);

        StockRequests.update(company, 10);
        assertFalse(client.sell(userId, company, buyingCount * 2, priceOfCompany));
        assertEquals(userBalance - priceOfCompany * buyingCount, client.getFreeMoney(userId));
        ClientStockInfo expectedStock = new ClientStockInfo(company, buyingCount);
        assertEquals(expectedStock, client.getStock(userId, company));
    }

}
