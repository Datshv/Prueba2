package propiedades;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LecturaDeArchivoConfiguracion {

	public Map<String, String> mapaDeConfiguracion = new HashMap<String, String>();
	public Map<String, String> mapaDeQuerys = new HashMap<String, String>();

	private Properties configuracion =null;
	private InputStream archivoDeLectura = null;

	public LecturaDeArchivoConfiguracion() {
		leerArchivDePropiedades();
		leerArchivDeQuerys();
	}
	
	
	
	public void leerArchivDePropiedades() {
		configuracion =  new Properties();
		try {

			// Directorio donde se encuentra el archivo de propiedades
			archivoDeLectura = new FileInputStream("src/main/resources/Nequi.properties");

			// carga el archivo de propiedades
			configuracion.load(archivoDeLectura);

			// aqui se toma el archivo de propiedades y se carga en un mapa de valores
			// estos valores se pueden usar en otras clases
			Enumeration<?> e = configuracion.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = configuracion.getProperty(key);
				// aqui se agregan los elementos en el mapa
				mapaDeConfiguracion.put(key, value);
			}
        
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (archivoDeLectura != null) {
				try {
					archivoDeLectura.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}
	
	
	public void leerArchivDeQuerys() {
		configuracion =  new Properties();
		try {

			// Directorio donde se encuentra el archivo de propiedades
			archivoDeLectura = new FileInputStream("src\\main\\resources\\Querys.properties");

			// carga el archivo de propiedades
			configuracion.load(archivoDeLectura);

			// aqui se toma el archivo de propiedades y se carga en un mapa de valores
			// estos valores se pueden usar en otras clases
			Enumeration<?> e = configuracion.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = configuracion.getProperty(key);
				// aqui se agregan los elementos en el mapa
				mapaDeQuerys.put(key, value);
			}
        
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (archivoDeLectura != null) {
				try {
					archivoDeLectura.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

}
