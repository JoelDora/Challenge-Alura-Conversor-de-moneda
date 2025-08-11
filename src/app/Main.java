package app;

import app.http.HttpClientHelper;
import app.model.ApiResponse;
import app.service.CurrencyConvertService;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.*;

public class Main {

    private static final String API_KEY = "Tu API Key Aquí";
    private static final String BASE = "USD";
    private static final List<String> MONEDAS_INTERES =
            List.of("USD", "EUR", "MXN", "CLP", "BRL");

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        Map<String, BigDecimal> rates = cargarDesdeApi(BASE);

        boolean running = true;
        while (running) {
            System.out.println("\n=== Conversor de Monedas ===");
            System.out.println("Base: " + BASE);
            System.out.println("Monedas disponibles: " + MONEDAS_INTERES);
            System.out.println("-----------------------------");
            System.out.println("1) Convertir");
            System.out.println("2) Listar tasas");
            System.out.println("3) Refrescar tasas desde la API");
            System.out.println("0) Salir");
            System.out.print("Elige una opción: ");

            String choice = in.nextLine().trim();

            switch (choice) {
                case "1":
                    convertir(in, rates);
                    break;
                case "2":
                    listarTasas(rates);
                    break;
                case "3":
                    rates = cargarDesdeApi(BASE);
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        System.out.println("¡Hasta luego!");
    }



    private static void convertir(Scanner in, Map<String, BigDecimal> rates) {
        if (rates.isEmpty()) {
            System.out.println("No hay tasas cargadas. Usa '3) Refrescar tasas'.");
            return;
        }

        System.out.println("Monedas: " + MONEDAS_INTERES);

        System.out.print("Moneda de ORIGEN: ");
        String from = in.nextLine().trim().toUpperCase();

        System.out.print("Moneda de DESTINO: ");
        String to = in.nextLine().trim().toUpperCase();

        if (!MONEDAS_INTERES.contains(from) || !MONEDAS_INTERES.contains(to)) {
            System.out.println("Moneda no válida. Usa alguna de: " + MONEDAS_INTERES);
            return;
        }
        if (!from.equals(BASE) && rates.get(from) == null) {
            System.out.println("No hay tasa para " + from + " respecto a " + BASE);
            return;
        }
        if (!to.equals(BASE) && rates.get(to) == null) {
            System.out.println("No hay tasa para " + to + " respecto a " + BASE);
            return;
        }

        BigDecimal amount = leerMontoSeguro(in);
        if (amount == null) return;

        CurrencyConvertService converter = new CurrencyConvertService();
        BigDecimal result = converter.convert(amount, from, to, rates, BASE);

        System.out.println(amount + " " + from + " = " + result + " " + to);
    }

    private static void listarTasas(Map<String, BigDecimal> rates) {
        if (rates.isEmpty()) {
            System.out.println("No hay tasas cargadas. Usa '3) Refrescar tasas'.");
            return;
        }
        System.out.println("Tasas contra " + BASE + ":");
        for (String code : MONEDAS_INTERES) {
            BigDecimal v = code.equals(BASE) ? BigDecimal.ONE : rates.get(code);
            if (v != null) {
                System.out.println(code + " = " + v);
            }
        }
    }



    private static Map<String, BigDecimal> cargarDesdeApi(String base) {
        try {
            String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + base;
            HttpClientHelper http = new HttpClientHelper();
            String json = http.get(url);

            ApiResponse resp = new Gson().fromJson(json, ApiResponse.class);

            if (!"success".equalsIgnoreCase(resp.getResult())) {
                System.out.println("La API respondió: " + resp.getResult());
                return Collections.emptyMap();
            }
            Map<String, BigDecimal> all = resp.getConversion_rates();

            Map<String, BigDecimal> filtered = new HashMap<>();
            for (String code : MONEDAS_INTERES) {
                if (!code.equals(base)) {
                    BigDecimal v = all.get(code);
                    if (v != null) filtered.put(code, v);
                }
            }
            System.out.println("Tasas actualizadas (" + base + "): " + filtered.size() + " monedas.");
            return filtered;

        } catch (Exception e) {
            System.out.println("No se pudo cargar desde la API: " + e.getMessage());
            return Collections.emptyMap();
        }
    }

    private static BigDecimal leerMontoSeguro(Scanner in) {
        System.out.print("Monto a convertir: ");
        String texto = in.nextLine().trim().replace(",", ".");
        try {
            BigDecimal amount = new BigDecimal(texto);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("El monto debe ser positivo.");
                return null;
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Monto inválido.");
            return null;
        }
    }
}
