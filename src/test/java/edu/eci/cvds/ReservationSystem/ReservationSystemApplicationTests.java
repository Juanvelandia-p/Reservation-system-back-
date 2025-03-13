package edu.eci.cvds.ReservationSystem;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReservationSystemApplicationTests {
    @Test
    void contextLoads() {}

    @Test
    void mainTest() {
        // Creamos un flujo de salida en memoria para capturar los mensajes impresos en consola
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);

        // Guardamos la salida estándar original
        PrintStream originalOut = System.out;

        // Redirigimos la salida estándar a nuestro flujo de salida
        System.setOut(printStream);

        try {
            // Ejecutamos el método main de la aplicación
            ReservationSystemApplication.main(new String[]{});

            // Verificamos que no haya errores en la salida
            String output = outputStream.toString();

            // Puedes agregar verificaciones sobre lo que esperas que se imprima en la consola
            // Por ejemplo, si esperas algún mensaje específico que indique que la aplicación se inició correctamente
            assertTrue(output.contains("Started ReservationSystemApplication"), "El mensaje esperado no apareció en la salida.");
        } finally {
            System.setOut(originalOut);
        }
    }
}