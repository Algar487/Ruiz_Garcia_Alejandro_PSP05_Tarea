package ejercicio2;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Alejandro
 */
public class Filtrador {

    public static void main(String[] args) {

        String nombreArchivo = "";

        Logger logger = Logger.getLogger("filtrador");
        FileHandler fh = null;
        try {
            fh = new FileHandler("./filtrador.log", true);
        } catch (IOException ex) {
            System.out.println("Error al crear el archivo log. IOException");
        } catch (SecurityException ex) {
            System.out.println("Error al crear el archivo log. SecurityException");
        }
        logger.addHandler(fh);
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);

        registrarEvento("START", logger);

        // Analizamos los posibles argumentos pasados al proceso
        if (args.length == 1) {

            System.out.println("LISTA DE USUARIOS CORRECTOS2");
            System.out.println("----------------------------");

            //Obtenemos por consola el nombre del archivo que el programa debe intentar leer
            nombreArchivo = args[0];

            //creamos la estructura donde se irán guardando los registros válidos
            HashMap<String, String> usuarios = new HashMap<String, String>();

            //intentamos abrir el archivo con el nombre pasado por consola
            try (FileReader fr = new FileReader(nombreArchivo);
                    BufferedReader entrada = new BufferedReader(fr);) {

                registrarEvento("OPEN_FILE_DONE", logger, nombreArchivo);
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
                            registrarEvento("VALID_ACCOUNT", logger, linea);
                        }
                    } else if (!m.matches()) {
                        registrarEvento("INVALID_ACCOUNT", logger, linea);

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
                registrarEvento("END", logger);

            } catch (FileNotFoundException e) {
                System.out.println("Error: archivo " + nombreArchivo + "·no encontrado.");
                registrarEvento("OPEN_FILE_FAIL", logger, nombreArchivo);
                registrarEvento("END", logger);

            } catch (IOException e) {
                System.out.println("Error: fallo en el acceso al archivo: " + e.getMessage());
                registrarEvento("OPEN_FILE_FAIL", logger, nombreArchivo);
                registrarEvento("END", logger);
            }

        } else if (args.length == 0) {
            System.out.println("Error. No ha introducido ningún argumento por consola");
            registrarEvento("ARGUMENT_NOT_FOUND", logger);
            registrarEvento("END", logger);
        } else {
            System.out.println("Error. Debe introducir un único argumento por consola");
            registrarEvento("ARGUMENT_NOT_FOUND", logger);
            registrarEvento("END", logger);
        }
    }

    /**
     * Método que gestiona los mensajes log. Recoge como argumentos el tipo de
     * evento a registrar y el objeto Logger declarado en el método main.
     *
     * @param mensaje
     * @param logger
     */
    public static void registrarEvento(String mensaje, Logger logger) {

        switch (mensaje) {

            case "START":

                logger.log(Level.INFO, "INFORMACIÓN: Aplicación iniciada\n");

                break;

            case "ARGUMENT_NOT_FOUND":

                logger.log(Level.SEVERE, "ERROR: No se ha especificado correctamente el nombre del archivo por consola\n");

                break;

            case "END":

                logger.log(Level.INFO, "INFORMACIÓN: Aplicación finalizada\n\n\n\n\n\n\n\n");

                break;
            default:
        }
    }

    /**
     * Método que gestiona los mensajes log. Recoge como argumentos el tipo de
     * evento a registrar, el objeto Logger declarado en el método main y un
     * último argumento para completar los registros log.
     *
     * @param mensaje
     * @param logger
     * @param argumento
     */
    public static void registrarEvento(String mensaje, Logger logger, String argumento) {

        switch (mensaje) {

            case "OPEN_FILE_DONE":

                logger.log(Level.INFO, "INFORMACIÓN: Archivo abierto: " + argumento + "\n");

                break;
            case "OPEN_FILE_FAIL":

                logger.log(Level.SEVERE, "ERROR: No es posible abrir el archivo: " + argumento + "\n");

                break;

            case "VALID_ACCOUNT":

                logger.log(Level.INFO, "Cuenta de usuario válida. Línea aceptada: " + argumento + "\n");

                break;

            case "INVALID_ACCOUNT":

                logger.log(Level.WARNING, "Cuenta de usuario no válida. Línea rechazada: " + argumento + "\n");

                break;

            default:
        }

    }

}
