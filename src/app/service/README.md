# Conversor de Monedas (Java)

Proyecto del Challenge ONE – Java Back End.

## Requisitos
- Java 11+ (probado con Java 21)
- (Opcional) IntelliJ IDEA

## Configuración
1. Coloca tu API Key de https://www.exchangerate-api.com en `Main.API_KEY`.
2. Ejecuta `Main`.

## Uso
Menú:
1) Convertir
2) Listar tasas
3) Refrescar tasas
0) Salir

Monedas soportadas (ejemplo): USD, EUR, MXN, CLP, BRL.

## Diseño
- `HttpClientHelper`: llamadas HTTP con `HttpClient`.
- `ApiResponse`: mapea JSON (`base_code`, `conversion_rates`) con Gson.
- `RateService`: carga y filtra tasas (solo monedas de interés).
- `CurrencyConverterService`: convierte con `BigDecimal` y redondeo HALF_EVEN.

## Ejemplo
USD -> EUR, monto 100
Resultado: 90.00 EUR

## Notas
- Si la API falla, usa “3) Refrescar tasas”.
- Redondeo bancario a 2 decimales.
