package request;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import baseDatos.BaseDeDatos;

public class CreadorDelRequest extends BaseDeDatos{

	private String nombreArchivoExcel;
	private String filaConDatosDePrueba;
	public Map<Integer, String> mapaDeValoresDelDataDriven;
	public Map<String, String> mapaDeValoresQueRemplazanElRequest;
	public String xmlRequest;
	public String mqFile;
	public String xmlResponse;
	
	public CreadorDelRequest(String nombreArchivoExcel, String filaConDatosDePrueba,Map<String,String> mapaDeValoresQueRemplazanElRequest) {
		super();	
		this.nombreArchivoExcel= nombreArchivoExcel;
	    this.filaConDatosDePrueba= filaConDatosDePrueba;
		lecturaArchivoExcel();
		crearArchivoRequest();
		remplazosEnArchivoRequest(mapaDeValoresQueRemplazanElRequest);
		remplazadorDeArchivoRequest();
		archivoMQfile();
		ejecutarMQjar();
		System.out.println("salio");
	}
	


	
	
	/**
	 * Este metodo permite leer el archivo excel el cual contiene los datos para el File Reference
	 */
	private void lecturaArchivoExcel() {
		// directorio donde se encuentra el archivo excel
		String directorioDelArchivoExcel = nombreArchivoExcel;
		// Crear un archivo excel (.xls or .xlsx)
		Workbook workbook;
		try {

			workbook = WorkbookFactory.create(new File(directorioDelArchivoExcel));

			// Aqui se toma el valor de la primera hoja del libro de excel
			Sheet sheet = workbook.getSheetAt(0);

			// Este data formater toma el valor de la celda y lo convierte en String
			DataFormatter dataFormatter = new DataFormatter();

			// se dan vueltas por cada fila en la hoja de datos de excel y se valida si es la fila buscada
			// cuando se encuentra la fila se recorren todas las celdas de la fila encontrada
			for (Row row : sheet) {
				if (dataFormatter.formatCellValue(row.getCell(0)).equals(filaConDatosDePrueba)) {
					int key = 1;
					mapaDeValoresDelDataDriven = new HashMap<Integer, String>();
					for (Cell cell : row) {
						String cellValue = dataFormatter.formatCellValue(cell);
						System.out.print(cellValue + "\t");
						
						mapaDeValoresDelDataDriven.put(key, cellValue);
						key=key+1;
					}
					break;
				}
			}
			// Cierre del libro
			workbook.close();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	/**
	 * Copia el archivo de Reference file y lo pega en el directorio del request files 
	 */
	private void crearArchivoRequest() {
		Calendar date = Calendar.getInstance();
	    SimpleDateFormat dateformatter = new SimpleDateFormat("ddMMyyyy hh.mm.sss");
		String sourceFile = mapaDeConfiguracion.get("PathXMLReference") + mapaDeValoresDelDataDriven.get(4);
		this.xmlRequest = mapaDeConfiguracion.get("PathXMLRequest")+ mapaDeValoresDelDataDriven.get(3)+"  "+dateformatter.format(date.getTime())+ "REQUEST"+".xml";
		this.xmlResponse = mapaDeConfiguracion.get("PathXMLResponse")+mapaDeValoresDelDataDriven.get(3)+ " "+dateformatter.format(date.getTime())+ "RESPONSE"+".xml";
		try {
			File inFile = new File(sourceFile);
			File outFile = new File(xmlRequest);

			FileInputStream in = new FileInputStream(inFile);
			FileOutputStream out = new FileOutputStream(outFile);

			int c;
			while ((c = in.read()) != -1)
				out.write(c);

			in.close();
			out.close();
			System.out.println("finalizo");
		} catch (IOException e) {
			System.err.println("Hubo un error de entrada/salida!!!");
		}
	}
	
	
	
	
/**
 * Metodo encargado de crear un mapa el cual contiene los valores que seran remplazados en 
 * el Request
 * @param mapaDeValoresQueRemplazanElRequest
 */
	private void remplazosEnArchivoRequest(Map<String, String> mapaDeValoresQueRemplazanElRequest) {
		
		if(mapaDeValoresQueRemplazanElRequest== null) {
			this.mapaDeValoresQueRemplazanElRequest= new HashMap<String,String>();
		}else {
			this.mapaDeValoresQueRemplazanElRequest = mapaDeValoresQueRemplazanElRequest;
		}
		
		
		for (Map.Entry<Integer, String> entry : mapaDeValoresDelDataDriven.entrySet())
		{
			String remplazo = "";
			if (entry.getValue().contains("**")) {
				String[] array = entry.getValue().split(",");

				switch (array.length) {
				case 2:
					if (!array[1].toLowerCase().trim().equals("utilizarvalorguardado")) {
						remplazo = array[1];
					}else {
						remplazo = mapaDeValoresQueRemplazanElRequest.get(array[0]);
					}

					break;
				case 3:
					if (array[1].toLowerCase().trim().equals("random")) {

						switch (array[2].toLowerCase().trim()) {
						case "requestid":
							Calendar date = Calendar.getInstance();
							SimpleDateFormat dateformatter = new SimpleDateFormat("ddMMyyyyhh.mm.sss");
							Random random = new Random();
							int rand = random.nextInt(9 - 0 + 1) + 0;
							remplazo = remplazo + rand;
							remplazo = dateformatter.format(date.getTime()) + remplazo;
							break;
						default:
							System.out.println("No se encontro una combinacion correcta en el DataDriven");
							break;
						}

					}
					break;

				case 4:
					if (array[1].trim().equals("Random")) {

						switch (array[2].toLowerCase().trim()) {
						case "number":

							for (int numero = 1; numero <= Integer.parseInt(array[3]); numero++) {
								Random random = new Random();
								int rand = random.nextInt(9 - 0 + 1) + 0;
								remplazo = remplazo + rand;
							}
							break;

						case "email":

							for (int numero = 1; numero <= Integer.parseInt(array[3]); numero++) {
								Random random = new Random();
								int rand = random.nextInt(9 - 0 + 1) + 0;
								remplazo = remplazo + rand;
							}
							remplazo = "correoPrueba" + remplazo + "@nequi.com";
							break;

						default:
							System.out.println("No se encontro una correcta combinacion de valores en el DataDriven");
							break;
						}

					}else if(array[1].trim().equals("SQL")){
						 
					remplazo = consultarEnBaseDatos(mapaDeQuerys.get(array[3].trim().toString()));
					}

					break;
				default:
					break;
				}
				this.mapaDeValoresQueRemplazanElRequest.put(array[0], remplazo);

			}
		}
	}

	

/**
 * Metodo encargado de remplazar todo los valores ** en el request
 */
	private void remplazadorDeArchivoRequest() {
		
		Path path = Paths.get(xmlRequest);
		Charset charset = StandardCharsets.UTF_8;

		String content;
		try {
			content = new String(Files.readAllBytes(path), charset);
		
	
		
		Iterator<String> llave = mapaDeValoresQueRemplazanElRequest.keySet().iterator();
		while(llave.hasNext()){
		  String key = llave.next();
		  key = key.substring(2, key.length()-2);
		  
		  content = content.replaceAll(Pattern.quote("**"+key+"**"), mapaDeValoresQueRemplazanElRequest.get("**"+key+"**"));
			
		  
		  System.out.println("Clave: " + key + " -> Valor: " + mapaDeValoresQueRemplazanElRequest.get(key));
		}
		Files.write(path, content.getBytes(charset));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * Este metodo nos permite leer el archivo xml que necesitemos y buscar un 
	 * tag interno con su nodo padre e hijo
	 * @param XML
	 * @param nodoPadre
	 * @param nodoHijo
	 * @return
	 */
	public String leerArchivoXML(String XML, String nodoPadre, String nodoHijo) {

				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				String valorEncontradoEnXML = null;
				try {

					
					DocumentBuilder db = dbf.newDocumentBuilder();
					Document doc = db.parse(XML);
					NodeList nList  = doc.getElementsByTagName(nodoPadre);
					  
					 	for (int i = 0; i < nList.getLength(); i++) 
				        {
				            NodeList nList2 = nList.item(i).getChildNodes();
				            for(int j = 0; j < nList2.getLength(); j++) {
				            	
				            	if(nList2.item(j).getNodeName().trim().equals(nodoHijo)) {
				            		valorEncontradoEnXML = nList2.item(j).getTextContent().trim();
				            	}
				            }
				        }
					
					
				}catch(ParserConfigurationException pce) {
					pce.printStackTrace();
				}catch(SAXException se) {
					se.printStackTrace();
				}catch(IOException ioe) {
					ioe.printStackTrace();
				}
		
				if(valorEncontradoEnXML == null) {
					valorEncontradoEnXML = "No se encontro el valor esperado en el XML";
				}
				
		return valorEncontradoEnXML;
	}
	
	
	/**
	 * La creacion de este archivo es necesario para la ejecucion del
	 * jar MQ el cual se conecta a las colas MQ
	 */
	private void archivoMQfile() {
		
		PrintWriter writer;
		mqFile = mapaDeConfiguracion.get("RutaTrabajo") + mapaDeConfiguracion.get("ExcecutionFile");
		try {
			writer = new PrintWriter(mqFile, "UTF-8");
			writer.println("ArchivoXML_Request = "+ xmlRequest.substring(xmlRequest.lastIndexOf("/")+1,xmlRequest.length()));
			writer.println("ArchivoXML_Response = "+xmlResponse.substring(xmlResponse.lastIndexOf("/")+1,xmlResponse.length()));
			writer.println("MQ_Message_Id = "+mapaDeValoresQueRemplazanElRequest.get("**Request**"));
		    writer.println("Comunicador = Inicio");
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
	/**
	 * Ejecucion de archivoMQJar
	 */
	private void ejecutarMQjar() {
		try {
			if (new File(mapaDeConfiguracion.get("RutaTrabajo") + mapaDeConfiguracion.get("JavaMQFile")).exists()) {

				Process proc = Runtime.getRuntime().exec(
						"java -jar " + mapaDeConfiguracion.get("RutaTrabajo") + mapaDeConfiguracion.get("JavaMQFile"));
				proc.waitFor();
				proc.destroy();
			} else {
				throw new java.lang.RuntimeException(
						"No existe el archivo MQconnection.jar en la ruta " + mapaDeConfiguracion.get("RutaTrabajo"));
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

}