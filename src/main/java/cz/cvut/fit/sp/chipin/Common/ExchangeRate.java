package cz.cvut.fit.sp.chipin.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.net.http.HttpClient;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class ExchangeRate {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static float getExchangeRate(Currency baseCurrency, Currency targetCurrency, String date) {
        double targetRate = 1;
        String API_KEY = "4caa0e5fb1d4e72f2256c80ccc0eefe8";

        try {
            if (baseCurrency.toString() == "EUR") {
                URL url = new URL("http://api.exchangeratesapi.io/v1/"+ date +"?access_key=" + API_KEY);
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
            } else {
                URL url = new URL("https://api.freecurrencyapi.com/v1/latest?apikey=fca_live_MRyf6nGUeIu8KU3pGE62KVturFlocYCzaDXzukZL&currencies=" + targetCurrency.toString() + "&base_currency=" + baseCurrency.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();
                LatestCurrencyData currencyData = objectMapper.readValue(response.toString(), LatestCurrencyData.class);
                targetRate = currencyData.getData().get(targetCurrency.toString());
            }

        } catch (IOException e) {
            System.out.println("IO Exception while opening connection");
        }

        return (float) targetRate;
    }

}