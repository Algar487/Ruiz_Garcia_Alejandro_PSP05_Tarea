package ejercicio1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Alejandro
 */
public class Filtrador {

    public static void main(String[] args) {
        String nombreArchivo = "";

        // Analizamos los posibles argumentos pasados al proceso
        if (args.length == 1) {

            System.out.println("LISTA DE USUARIOS CORRECTOS");
            System.out.println("----------------------------");

            //Obtenemos el nombre del archivo por consola
            nombreArchivo = args[0];

            try (FileReader fr = new FileReader(nombreArchivo);
                    BufferedReader entrada = new BufferedReader(fr);) {

                Pattern patron = Pattern.compile("[a-z0-9]{6,10}:[a-zA-Z0-9]{8,12}");

                String linea = entrada.readLine();
                while (linea != null) {
                    System.out.println(linea);
                    Matcher m = patron.matcher(linea);
                    if (m.matches()) { // Si la línea cumple las condiciones de estructura
                            System.out.print("Esta línea cumple la estructura\n");
                    }
                    linea = entrada.readLine(); //se lee la siguiente línea del archivo   
                }

            } catch (FileNotFoundException e) {
                System.out.println("Error: archivo " + nombreArchivo + "·no encontrado.");
            } catch (IOException e) {
                System.out.println("Error: fallo en el acceso al archivo: " + e.getMessage());
            }

        } else if (args.length == 0) {
            System.out.println("Error. Debe introducir un único argumento por consola");
        } else {
            System.out.println("Error. Debe introducir un único argumento por consola");
        }
    }
}
