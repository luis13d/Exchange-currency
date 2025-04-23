
## Conversor de Monedas

Un conversor de monedas en Java que utiliza la API de ExchangeRate para obtener tasas de cambio en tiempo real. Esta aplicación de consola permite convertir entre diferentes monedas y consultar la cuota de uso de la API.

## Características

- Interfaz de consola con menú interactivo  
- Conversiones entre múltiples monedas:
  - Dólar a Peso argentino  
  - Peso argentino a Dólar  
  - Dólar a Real brasileño  
  - Real brasileño a Dólar  
  - Dólar a Peso colombiano  
  - Peso colombiano a Dólar  
- Tasas de cambio en tiempo real mediante la API de ExchangeRate  
- Sistema de caché para reducir el número de llamadas a la API  
- Consulta de cuota de uso de la API  
- Configuración de API key mediante archivo `.env`  

## Requisitos

- Java JDK 8 o superior  
- Conexión a Internet para acceder a la API de ExchangeRate  
- Una clave API de ExchangeRate-API  

## Estructura del Proyecto

El proyecto está compuesto por tres clases principales:

- `ConversorApp.java`: Clase principal que contiene el método `main` y maneja la interfaz de usuario.  
- `CurrencyConverter.java`: Clase que maneja la lógica de conversión de monedas.  
- `ApiClient.java`: Clase que se encarga de comunicarse con la API de ExchangeRate.  

Además, se incluye:

- `json-20231013.jar`: Biblioteca para procesar respuestas JSON de la API.  
- `.env`: Archivo para almacenar la clave API de ExchangeRate.  

## Instalación

1. Clona este repositorio:
   ```bash
   git clone https://github.com/tu-usuario/conversor-de-monedas.git
   cd conversor-de-monedas
   ```

2. Configura tu clave API:
   - Obtén una clave API gratuita en ExchangeRate-API  
   - Edita el archivo `.env` y reemplaza `YOUR-API-KEY` con tu clave API:
     ```env
     EXCHANGERATE_API_KEY=tu-clave-api-aquí
     ```

## Compilación

Para compilar el proyecto, ejecuta el siguiente comando:

En Windows:
```bash
javac -cp ".;./json-20231013.jar" ApiClient.java CurrencyConverter.java ConversorApp.java
```

En sistemas Unix (Linux/macOS):
```bash
javac -cp ".:./json-20231013.jar" ApiClient.java CurrencyConverter.java ConversorApp.java
```

## Ejecución

En Windows:
```bash
java -cp ".;./json-20231013.jar" ConversorApp
```

En sistemas Unix (Linux/macOS):
```bash
java -cp ".:./json-20231013.jar" ConversorApp
```

## Uso

Al ejecutar la aplicación, se mostrará un menú con las siguientes opciones:

```
*************************************************
Sea bienvenido/a al Conversor de Moneda =]
*************************************************

1) Dólar >>> Peso argentino  
2) Peso argentino >>> Dólar  
3) Dólar >>> Real brasileño  
4) Real brasileño >>> Dólar  
5) Dólar >>> Peso colombiano  
6) Peso colombiano >>> Dólar  
7) Consultar cuota de API  
8) Salir  
*************************************************
Elija una opción válida:
```

1. Selecciona una opción ingresando el número correspondiente.  
2. Si eliges una opción de conversión (1-6), se te pedirá ingresar la cantidad a convertir.  
3. La aplicación mostrará el resultado de la conversión utilizando la tasa de cambio actual.  
4. Si eliges la opción 7, se mostrará información sobre tu cuota de uso de la API.  
5. Para salir de la aplicación, selecciona la opción 8.  

## Ejemplo de Conversión

```
Elija una opción válida: 1  
Ingrese la cantidad a convertir: 100  
Consultando tasas de cambio para USD desde la API...  
Tasas de cambio actualizadas correctamente.  
100,00 USD = 107738,00 ARS
```

## Ejemplo de Consulta de Cuota

```
Elija una opción válida: 7  
Consultando información de cuota de la API...

*************************************************
INFORMACIÓN DE CUOTA DE LA API
*************************************************
Límite de solicitudes mensuales: 1500  
Solicitudes restantes: 1492  
Solicitudes utilizadas: 8  
Porcentaje utilizado: 0,53%  
Día de renovación: 11 de cada mes  
*************************************************
```

## Limitaciones

- La versión gratuita de ExchangeRate-API tiene un límite de 1,500 solicitudes por mes.  
- La aplicación almacena en caché las tasas de cambio durante una hora para reducir el número de llamadas a la API.
