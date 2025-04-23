import java.util.Scanner;
import org.json.JSONObject;

public class ConversorApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final CurrencyConverter converter = new CurrencyConverter();

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            displayMenu();
            int option = getOption();

            switch (option) {
                case 1:
                    convertCurrency("USD", "ARS"); // Dólar a Peso argentino
                    break;
                case 2:
                    convertCurrency("ARS", "USD"); // Peso argentino a Dólar
                    break;
                case 3:
                    convertCurrency("USD", "BRL"); // Dólar a Real brasileño
                    break;
                case 4:
                    convertCurrency("BRL", "USD"); // Real brasileño a Dólar
                    break;
                case 5:
                    convertCurrency("USD", "COP"); // Dólar a Peso colombiano
                    break;
                case 6:
                    convertCurrency("COP", "USD"); // Peso colombiano a Dólar
                    break;
                case 7:
                    showApiQuota();
                    break;
                case 8:
                    running = false;
                    System.out.println("¡Gracias por usar el Conversor de Moneda!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println();
        System.out.println("*************************************************");
        System.out.println("Sea bienvenido/a al Conversor de Moneda =]");
        System.out.println("*************************************************");
        System.out.println();
        System.out.println("1) Dólar >>> Peso argentino");
        System.out.println();
        System.out.println("2) Peso argentino >>> Dólar");
        System.out.println();
        System.out.println("3) Dólar >>> Real brasileño");
        System.out.println();
        System.out.println("4) Real brasileño >>> Dólar");
        System.out.println();
        System.out.println("5) Dólar >>> Peso colombiano");
        System.out.println();
        System.out.println("6) Peso colombiano >>> Dólar");
        System.out.println();
        System.out.println("7) Consultar cuota de API");
        System.out.println();
        System.out.println("8) Salir");
        System.out.println("*************************************************");
        System.out.print("Elija una opción válida: ");
    }

    private static int getOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Opción inválida
        }
    }

    private static void convertCurrency(String fromCurrency, String toCurrency) {
        System.out.print("Ingrese la cantidad a convertir: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine());
            double result = converter.convert(amount, fromCurrency, toCurrency);

            String fromSymbol = getCurrencySymbol(fromCurrency);
            String toSymbol = getCurrencySymbol(toCurrency);

            System.out.printf("%.2f %s = %.2f %s%n%n", amount, fromSymbol, result, toSymbol);
        } catch (NumberFormatException e) {
            System.out.println("Cantidad inválida. Intente nuevamente.");
        } catch (Exception e) {
            System.out.println("Error al realizar la conversión: " + e.getMessage());
        }
    }

    private static String getCurrencySymbol(String currencyCode) {
        switch (currencyCode) {
            case "USD": return "USD";
            case "ARS": return "ARS";
            case "BRL": return "BRL";
            case "COP": return "COP";
            default: return currencyCode;
        }
    }



    /**
     * Muestra la información de cuota de la API
     */
    private static void showApiQuota() {
        try {
            JSONObject quotaInfo = converter.getQuotaInfo();

            System.out.println("\n*************************************************");
            System.out.println("INFORMACIÓN DE CUOTA DE LA API");
            System.out.println("*************************************************");

            // Extraemos la información relevante
            int requestsRemaining = quotaInfo.getInt("requests_remaining");
            int requestsLimit = quotaInfo.getInt("plan_quota");
            int refreshDay = quotaInfo.getInt("refresh_day_of_month");

            // Calculamos el porcentaje de uso
            int requestsUsed = requestsLimit - requestsRemaining;
            double percentageUsed = (double) requestsUsed / requestsLimit * 100;

            // Mostramos la información
            System.out.println("Límite de solicitudes mensuales: " + requestsLimit);
            System.out.println("Solicitudes restantes: " + requestsRemaining);
            System.out.println("Solicitudes utilizadas: " + requestsUsed);
            System.out.println("Porcentaje utilizado: " + String.format("%.2f", percentageUsed) + "%");
            System.out.println("Día de renovación: " + refreshDay + " de cada mes");
            System.out.println("*************************************************\n");

        } catch (Exception e) {
            System.out.println("Error al obtener información de cuota: " + e.getMessage());
        }
    }
}
