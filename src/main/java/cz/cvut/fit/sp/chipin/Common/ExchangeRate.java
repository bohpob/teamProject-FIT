package cz.cvut.fit.sp.chipin.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.http.HttpClient;
import java.util.Currency;

public class ExchangeRate {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static float getExchangeRate(Currency baseCurrency, Currency targetCurrency, String date) {
        double targetRate = 1;
        String API_KEY = "4caa0e5fb1d4e72f2256c80ccc0eefe8";
        try {
            URL url = new URL("https://api.exchangeratesapi.io/v1/"+ date +"?access_key=" + API_KEY + "&base=" + baseCurrency.toString() + "&symbols=" + targetCurrency.toString());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();
            CurrencyData currencyData = objectMapper.readValue(response.toString(), CurrencyData.class);
            targetRate = currencyData.getRates().get(targetCurrency.toString());
        } catch (IOException e) {
            System.out.println("IO Exception while opening connection");
        }

        return (float) targetRate;
    }
}