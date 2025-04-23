import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.json.JSONObject;

public class ApiClient {
    private final String apiKey;
    private final String baseUrl = "https://v6.exchangerate-api.com/v6/";
    private Map<String, Map<String, Double>> ratesCache = new HashMap<>();
    private Map<String, Long> cacheTimestamps = new HashMap<>();
    private static final long CACHE_DURATION = 3600000; // 1 hora en milisegundos

    public ApiClient(String apiKey) {
        // Si se proporciona una clave API, la usamos
        // De lo contrario, intentamos leerla del archivo .env
        if (apiKey != null && !apiKey.equals("YOUR-API-KEY")) {
            this.apiKey = apiKey;
        } else {
            this.apiKey = loadApiKeyFromEnvFile();
        }
        System.out.println("API Key configurada: " + (this.apiKey.equals("YOUR-API-KEY") ? "No configurada" : "Configurada correctamente"));
    }

    /**
     * Carga la clave API desde el archivo .env
     *
     * @return La clave API o "YOUR-API-KEY" si no se pudo cargar
     */
    private String loadApiKeyFromEnvFile() {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(".env")) {
            properties.load(reader);
            String apiKey = properties.getProperty("EXCHANGERATE_API_KEY");
            if (apiKey != null && !apiKey.isEmpty() && !apiKey.equals("YOUR-API-KEY")) {
                return apiKey;
            }
        } catch (Exception e) {
            System.out.println("Error al cargar la clave API desde .env: " + e.getMessage());
        }
        return "YOUR-API-KEY";
    }

    /**
     * Obtiene la información de cuota de la API
     *
     * @return Un objeto JSONObject con la información de cuota
     * @throws Exception Si ocurre un error al obtener la información de cuota
     */
    public JSONObject getQuotaInfo() throws Exception {
        // Verificamos que la API key esté configurada
        if (apiKey.equals("YOUR-API-KEY")) {
            throw new Exception("API key no configurada. Por favor, configure su API key en el archivo .env");
        }

        // Hacemos la llamada a la API
        String urlStr = baseUrl + apiKey + "/quota";
        System.out.println("Consultando información de cuota de la API...");

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Procesamos la respuesta JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            String result = jsonResponse.getString("result");

            if ("success".equals(result)) {
                return jsonResponse;
            } else {
                throw new Exception("Error en la respuesta de la API: " + jsonResponse.getString("error-type"));
            }
        } else {
            throw new Exception("Error al obtener información de cuota. Código de respuesta: " + responseCode);
        }
    }

    /**
     * Obtiene la tasa de cambio entre dos monedas
     *
     * @param fromCurrency Código de la moneda de origen (ej. USD)
     * @param toCurrency Código de la moneda de destino (ej. ARS)
     * @return La tasa de cambio
     * @throws Exception Si ocurre un error al obtener la tasa de cambio
     */
    public double getExchangeRate(String fromCurrency, String toCurrency) throws Exception {
        // Verificamos si tenemos tasas en caché para la moneda base y si están actualizadas
        if (!isCacheValid(fromCurrency)) {
            // Si no hay caché válido, obtenemos las tasas actualizadas
            updateRatesCache(fromCurrency);
        }

        // Obtenemos la tasa de cambio desde el caché
        Map<String, Double> rates = ratesCache.get(fromCurrency);
        if (rates == null || !rates.containsKey(toCurrency)) {
            throw new Exception("No se pudo obtener la tasa de cambio para " + fromCurrency + " a " + toCurrency);
        }

        return rates.get(toCurrency);
    }

    /**
     * Verifica si el caché para una moneda base es válido
     */
    private boolean isCacheValid(String baseCurrency) {
        Long timestamp = cacheTimestamps.get(baseCurrency);
        return timestamp != null && (System.currentTimeMillis() - timestamp) < CACHE_DURATION;
    }

    /**
     * Actualiza el caché de tasas para una moneda base
     */
    private void updateRatesCache(String baseCurrency) throws Exception {
        Map<String, Double> rates = new HashMap<>();

        // Verificamos que la API key esté configurada
        if (apiKey.equals("YOUR-API-KEY")) {
            throw new Exception("API key no configurada. Por favor, configure su API key en el archivo .env");
        }

        // Hacemos la llamada a la API
        String urlStr = baseUrl + apiKey + "/latest/" + baseCurrency;
        System.out.println("Consultando tasas de cambio para " + baseCurrency + " desde la API...");

        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Procesamos la respuesta JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            String result = jsonResponse.getString("result");

            if ("success".equals(result)) {
                JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");

                // Guardamos las tasas que nos interesan
                rates.put(baseCurrency, 1.0); // La moneda base siempre es 1.0

                // Guardamos las tasas para las monedas que nos interesan
                String[] currencies = {"USD", "ARS", "BRL", "COP"};
                for (String currency : currencies) {
                    if (conversionRates.has(currency)) {
                        rates.put(currency, conversionRates.getDouble(currency));
                    }
                }

                System.out.println("Tasas de cambio actualizadas correctamente.");
            } else {
                throw new Exception("Error en la respuesta de la API: " + jsonResponse.getString("error-type"));
            }
        } else {
            throw new Exception("Error al obtener tasas de cambio. Código de respuesta: " + responseCode);
        }

        // Guardamos las tasas en el caché
        ratesCache.put(baseCurrency, rates);
        cacheTimestamps.put(baseCurrency, System.currentTimeMillis());
    }
}
