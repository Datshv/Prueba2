package propiedades;

import java.io.IOException;
import java.util.Iterator;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import manejador.EjecutarArchivoPrueba;

public class Iniciador {
	

	public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {
		
		EjecutarArchivoPrueba prueba = new EjecutarArchivoPrueba("D:\\Mateo\\Data\\Nequi_Cashback_y_Bonos.xls", "1",null);
		//prueba = new EjecutarArchivoPrueba("D:\\Nequi\\Data\\Nequi_Cashback_y_Bonos.xls", "2",prueba.mapaDeValoresQueRemplazanElRequest);
		//prueba.leerArchivoXML("D:\\Nequi\\Response_Files\\IdCaso-5-Reversar CashBack-XMLResponse.xml", "TrnIdentifier", "TrnId");
	    String resultado = prueba.leerArchivoXML(prueba.xmlResponse, "HostTransaction", "Status");
	    String resultadoEsperado = prueba.mapaDeValoresDelDataDriven.get(6);
	    if(resultado.equals(resultadoEsperado)) {
	    	System.out.println("Funciono la prueba");
	    }else {
	    	System.out.println("No funciono la prueba ");
	    }
		
	    prueba.consultarEnBaseDatos("");
		
//		Iterator<String> it = prueba.mapaDeConfiguracion.keySet().iterator();
//		while(it.hasNext()){
//		  String key = it.next();
//		  System.out.println("Clave: " + key + " -> Valor: " + prueba.mapaDeConfiguracion.get(key));
//		}
		
	

	}

}
