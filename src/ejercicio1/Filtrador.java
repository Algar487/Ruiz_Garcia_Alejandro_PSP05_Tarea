package ejercicio1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
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

            //Obtenemos por consola el nombre del archivo que el programa debe intentar leer
            nombreArchivo = args[0];

            //creamos la estructura donde se irán guardando los registros válidos
            HashMap<String, String> usuarios = new HashMap<String, String>();

            //intentamos abrir el archivo con el nombre pasado por consola
            try (FileReader fr = new FileReader(nombreArchivo);
                    BufferedReader entrada = new BufferedReader(fr);) {

                //definimos el patrón que debe cumplir cada registro
                Pattern patron = Pattern.compile("[a-z0-9]{6,10}:[a-zA-Z0-9]{8,12}");

                String linea = entrada.readLine();

                //vamos leyendo linea a linea el texto encontrado en el archivo indicado
                while (linea != null) {
                    Matcher m = patron.matcher(linea);
                    if (m.matches()) { // si la línea cumple las condiciones del patron, introducimos los datos de usuario en nuestro HashMap
                        String[] parts = linea.split(":");

                        //comprobamos que la contraseña no sea igual al nombre de usuario. En tal caso, descartamos este registro
                        if (parts[0] != parts[1]) {
                            usuarios.put(parts[0], parts[1]);
                        }
                    }
                    linea = entrada.readLine(); //leemos la siguiente línea del archivo   
                }

                //convertimos nuestro HashMap en TreeMap para poder ordenar los registros por orden alfabético de la clave
                Map<String, String> treeMap = new TreeMap<String, String>(usuarios);

                //imprimimos por consola los usuarios correctos
                System.out.println("Total de usuarios correctos: " + treeMap.size());
                int i = 1;
                for (Map.Entry<String, String> entry : treeMap.entrySet()) {
                    System.out.print(i + ": usuario= " + entry.getKey() + " passwd= " + entry.getValue() + "\n");
                    i++;
                }

            } catch (FileNotFoundException e) {
                System.out.println("Error: archivo " + nombreArchivo + "·no encontrado.");
            } catch (IOException e) {
                System.out.println("Error: fallo en el acceso al archivo: " + e.getMessage());
            }

        } else if (args.length == 0) {
            System.out.println("Error. No ha introducido ningún argumento por consola");
        } else {
            System.out.println("Error. Debe introducir un único argumento por consola");
        }
    }
}
