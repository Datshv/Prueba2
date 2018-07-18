package baseDatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import propiedades.LecturaDeArchivoConfiguracion;

public class BaseDeDatos extends LecturaDeArchivoConfiguracion{

	public Connection conexion = null;
	
	public BaseDeDatos() {
		super();
	}
	
	@SuppressWarnings("finally")
	public Connection crearConexion() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
			 
			String valor = "jdbc:oracle:thin:@"
					+ mapaDeConfiguracion.get("Host_BaseDatos")+":"
					+ mapaDeConfiguracion.get("Puerto_BaseDatos")+"/"
					+ mapaDeConfiguracion.get("Service_Name_BaseDatos");
			System.out.println("conexion" + valor);
			conexion = DriverManager.getConnection("jdbc:oracle:thin:@"
					+ mapaDeConfiguracion.get("Host_BaseDatos")+":"
					+ mapaDeConfiguracion.get("Puerto_BaseDatos")+"/"
					+ mapaDeConfiguracion.get("Service_Name_BaseDatos")
					, mapaDeConfiguracion.get("Usuario_BaseDatos"),
					mapaDeConfiguracion.get("Clave_BaseDatos"));
//				+mapaDeConfiguracion.get("ConexionBaseDatosFinacle"));

			if (conexion != null) {
				System.out.println("conexion exitosa");
			}
		} catch (SQLException e) {
			System.out.println("Se ha generado un error en la conexion a Base de datos");
			e.printStackTrace();
		} finally {
			return conexion;
		}
	}
	
	public void cerrarConexion() {
		try {
			conexion.close();
		} catch (SQLException e) {
		 System.out.print("No se logro cerrar la conexion");
		 e.printStackTrace();
		}
	}
	

	public String consultarEnBaseDatos(String queryBaseDeDatos) {
	//	crearConexion();
		
		String sql = "";
		String valorDeBaseDeDatos = "";
		PreparedStatement sentencia = null;
		ResultSet resultado = null;

		try {
			sql = queryBaseDeDatos;

			sentencia = crearConexion().prepareStatement(sql);

			resultado = sentencia.executeQuery();
		
			while (resultado.next()) {
				valorDeBaseDeDatos = resultado.getString(1);
			}
		} catch (SQLException e) {
			System.out.println("No se logro hacer la consulta en Base de datos");
			e.printStackTrace();
		}
		cerrarConexion();
		return valorDeBaseDeDatos;
	//	return queryBaseDeDatos;
	}
	
	
	
}
