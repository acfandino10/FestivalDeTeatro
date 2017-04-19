package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import vos.Espectaculo;
import vos.Funcion;
import vos.Sitio;


public class DAOTablaSitios {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOSilla
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaSitios() {
		recursos = new ArrayList<Object>();
	}

	/**
	 * Método que cierra todos los recursos que estan enel arreglo de recursos
	 * <b>post: </b> Todos los recurso del arreglo de recursos han sido cerrados
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Método que inicializa la connection del DAO a la base de datos con la conexión que entra como parámetro.
	 * @param con  - connection a la base de datos
	 */
	public void setConn(Connection con){
		this.conn = con;
	}


	/**
	 * Método que, usando la conexión a la base de datos, saca todas los sitios de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM SITIOS;
	 * @return Arraylist con los sitios de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Sitio> darSitios() throws SQLException, Exception {
		ArrayList<Sitio> sitios = new ArrayList<Sitio>();

		String sql = "SELECT * FROM ISIS2304B071710.SITIOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = Integer.parseInt(rs.getString("ID"));
			String direccion = rs.getString("DIRECCION");
			String ciudad = rs.getString("CIUDAD");
			String apto = rs.getString("APTO");
			int capacidad = Integer.parseInt(rs.getString("CAPACIDAD"));
			
			sitios.add(new Sitio(id,direccion,ciudad,apto,capacidad));
		}
			
		return sitios;
	}


	/**
	 * Método que busca los sitios en la ciudad que entra como parámetro.
	 * @param ciudad - ciudad a buscar
	 * @return ArrayList con los sitios
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Sitio> buscarSitiosPorCiudad(String ciudad) throws SQLException, Exception {
		ArrayList<Sitio> sitios = new ArrayList<Sitio>();

		String sql = "SELECT * FROM ISIS2304B071710.SITIOS WHERE CIUDAD ='" + ciudad + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = Integer.parseInt(rs.getString("ID"));
			String direccion = rs.getString("DIRECCION");
			String ciudad2 = rs.getString("CIUDAD");
			String apto = rs.getString("APTO");
			int capacidad = Integer.parseInt(rs.getString("CAPACIDAD"));
			
			sitios.add(new Sitio(id,direccion,ciudad2,apto,capacidad));
		}

		return sitios;
	}

	/**
	 * Método que agrega el sitio que entra como parámetro a la base de datos.
	 * @param sitio
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addSitio(Sitio sitio) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.SITIOS VALUES (";
		sql += sitio.getId() + ",'";
		sql += sitio.getDireccion() + "','";
		sql += sitio.getCiudad() + "','";
		sql += sitio.getApto() + "')";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza el sitio que entra como parámetro en la base de datos.
	 * @param sitio
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateSitio(Sitio sitio) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.SITIOS SET ";
		
		sql += "direccion" + sitio.getDireccion();
		sql += "ciudad" + sitio.getCiudad();
		sql += "apto" + sitio.getApto();
		sql += " WHERE id = " + sitio.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina el sitio que entra como parámetro en la base de datos.
	 * @param sitio
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteSitio(Sitio sitio) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.SITIOS";
		sql += " WHERE id = " + sitio.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	
	public Sitio buscarSitioPorId(int id) throws SQLException, Exception {
		Sitio sitio = null;

		String sql = "SELECT * FROM ISIS2304B071710.SITIOS WHERE ID ='" + id + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		System.out.println("-------------------------------");
		recursos.add(prepStmt);
		System.out.println("-------------------------------");
		ResultSet rs = prepStmt.executeQuery();
		System.out.println("-------------------------------");

		while (rs.next()) {
			System.out.println("-------------------------------");
			int id2 = Integer.parseInt(rs.getString("ID"));
			System.out.println("-------------------------------");
			String direccion = rs.getString("DIRECCION");
			System.out.println("-------------------------------");
			String ciudad = rs.getString("CIUDAD");
			System.out.println("-------------------------------");
			String apto = rs.getString("APTO");
			System.out.println("-------------------------------");
			int capacidad = Integer.parseInt(rs.getString("CAPACIDAD"));
			
			sitio = new Sitio(id2,direccion,ciudad,apto,capacidad);
		}

		return sitio;
	}
	
	public ArrayList<Funcion> darFuncionesSitio(int id_sitio) throws SQLException, Exception{
		System.out.print("++++++++++++++++++++++++++++++++");
		
	
		
				ArrayList<Funcion> sitios = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304B071710.EVENTOS WHERE ID_SITIO ='" + id_sitio + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = Integer.parseInt(rs.getString("ID"));
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Date fecha = rs.getDate("FECHA");
			Timestamp hora = rs.getTimestamp("HORA");
			int id_sitio2 = Integer.parseInt(rs.getString("ID_SITIO"));
			String estado = rs.getString("ESTADO");
			Funcion eventoNuevo = new Funcion(id, fecha, id_espectaculo, hora, id_sitio2,estado);
			eventoNuevo.setDisponibilidad(Boolean.parseBoolean(rs.getString("DISPONIBILIDAD")));
			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
			sitios.add(eventoNuevo);
		}

		return sitios;		
				
				
				
				
		/**
		ArrayList<Funcion> eventos = new ArrayList<Funcion>();
		System.out.print("COMIENZA A DAR FUNCIONES DEL SITIO CON ID_SITIO"+id_sitio);
		
		DAOTablaFunciones daoEventos = new DAOTablaFunciones();
		
		
		for(Funcion evento : daoEventos.darEventos())
		{
			System.out.println("Resultaodo de recorridos: "+ evento.getId_sitio() + "es igual a" + id_sitio);
			if(evento.getId_sitio() == id_sitio)
			{
				eventos.add(evento);
			}
			
		}
		
		return eventos;
		**/
		
	}
	
	
	public String consultarSitio(int id)throws SQLException, Exception {
		System.out.println("COMIENZA CONSULTAR SITIO EN DAOTABLASITIOS");
		Sitio sitio = buscarSitioPorId(id);
		System.out.println("sitiooooo: "+sitio);
	    int cont=0;
	    System.out.println("asdasvd"+darFuncionesSitio(id).toString());
	    
	    
	/**    
		for(Funcion e: darFuncionesSitio(id)){
			System.out.println("COMIENZA A CORRER, EVENTO #"+cont);
			DAOTablaEspectaculo daoEsp = new DAOTablaEspectaculo(); 
			Espectaculo esp = daoEsp.buscarEspectaculoPorId(e.getId_espectaculo());
			System.out.println("ENCONTRO EL ESPECTACULO "+ esp);
			System.out.println("NOMMBRE "+esp.getNombre());
			System.out.println("FECHA "+e.getFecha());
			System.out.println("HORA "+e.getHora());
			//funcionesInfo.add(esp.getNombre().toString() + ", " + e.getFecha().toString() + ", " + e.getHora().toString() + "\n");
			cont++;
		}
		**/
		
		return "La consulta del sitio obtuvo los siguiente datos: " +
		"Id: " + sitio.getId() + ", " + "\n" + 
		"Direccion: " + sitio.getDireccion().toString() + ", " + "\n" +
		"Ciudad: " + sitio.getCiudad().toString() +  ", " + "\n" +
		"Este sitio es apto para las personas con las siguientes caracteristicas" + sitio.getApto() + "\n" +
		"Las funciones son: " +	"\n" +
	    darFuncionesSitio(id).toString()
		;
		
		
	}

	public ArrayList<Sitio> darSitiosPorCompania(int id) throws SQLException {
		
		ArrayList<Sitio> sitios = new ArrayList<Sitio>();

		String sql = "SELECT * FROM ISIS2304B071710.ESPECTACULOS esp "
					+	"INNER JOIN ISIS2304B071710.FUNCIONES fun ON esp.ID=fun.ID_ESPECTACULO "
					+	"INNER JOIN ISIS2304B071710.SITIOS sit ON fun.ID_SITIO=sit.ID "
					+	"WHERE esp.ID_REPRESENTANTE='"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id2 = Integer.parseInt(rs.getString("ID_SITIO"));
			String direccion = rs.getString("DIRECCION");
			String ciudad = rs.getString("CIUDAD");
			String apto = rs.getString("APTO");
			int capacidad = Integer.parseInt(rs.getString("CAPACIDAD"));
			
			sitios.add(new Sitio(id2,direccion,ciudad,apto,capacidad));
		}
			
		return sitios;
	}
}
