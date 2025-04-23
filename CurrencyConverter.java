import org.json.JSONObject;

public class CurrencyConverter {
    private final ApiClient apiClient;

    public CurrencyConverter() {
        // Inicializamos el ApiClient que leerá la clave API desde el archivo .env
        this.apiClient = new ApiClient(null);
    }

    /**
     * Obtiene la información de cuota de la API
     *
     * @return Un objeto JSONObject con la información de cuota
     * @throws Exception Si ocurre un error al obtener la información de cuota
     */
    public JSONObject getQuotaInfo() throws Exception {
        return apiClient.getQuotaInfo();
    }

    /**
     * Convierte una cantidad de una moneda a otra
     *
     * @param amount Cantidad a convertir
     * @param fromCurrency Código de la moneda de origen (ej. USD)
     * @param toCurrency Código de la moneda de destino (ej. ARS)
     * @return La cantidad convertida
     * @throws Exception Si ocurre un error durante la conversión
     */
    public double convert(double amount, String fromCurrency, String toCurrency) throws Exception {
        // Si las monedas son iguales, no es necesario convertir
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        // Obtenemos la tasa de cambio
        double exchangeRate = apiClient.getExchangeRate(fromCurrency, toCurrency);

        // Realizamos la conversión
        return amount * exchangeRate;
    }
}
