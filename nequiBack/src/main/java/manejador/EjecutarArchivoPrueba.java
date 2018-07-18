package manejador;

import java.util.Map;

import request.CreadorDelRequest;

public class EjecutarArchivoPrueba extends CreadorDelRequest{

  	
	public EjecutarArchivoPrueba(String nombreArchivoExcel, String filaConDatosDePrueba, Map<String,String> mapaDeValoresQueRemplazanElRequest) {
		super(nombreArchivoExcel, filaConDatosDePrueba, mapaDeValoresQueRemplazanElRequest);
	}
	
	
}
