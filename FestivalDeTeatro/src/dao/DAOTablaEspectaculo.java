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
import vos.Reserva;
import vos.Silla;
import vos.Sitio;


public class DAOTablaEspectaculo {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOEvento
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaEspectaculo() {
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
	 * Método que, usando la conexión a la base de datos, saca todos los eventos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM EVENTOS;
	 * @return Arraylist con los eventos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Espectaculo> darEspectaculos() throws SQLException, Exception {
		ArrayList<Espectaculo> e = new ArrayList<Espectaculo>();

		String sql = "SELECT * FROM ISIS2304B071710.ESPECTACULOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String descripcion = rs.getString("DESCRIPCION");
			double duracion = Double.parseDouble(rs.getString("DURACION"));
			String idioma = rs.getString("IDIOMA");
			boolean traductor = rs.getBoolean("TRADUCCION");
			int id_representante = Integer.parseInt(rs.getString("ID_REPRESENTANTE"));
			
			
			e.add(new Espectaculo(id, nombre, descripcion, duracion, idioma, traductor, id_representante));
		}
		return e;
	}


	/**
	 * Método que busca al evento con el nombre que entra como parámetro.
	 * @param nombre - Nombre a buscar
	 * @return ArrayList con los eventos
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Espectaculo> buscarEspectaculoPorNombre(String nombre) throws SQLException, Exception {
		ArrayList<Espectaculo> e = new ArrayList<Espectaculo>();

		String sql = "SELECT * FROM ISIS2304B071710.ESPECTACULOS WHERE NOMBRE ='" + nombre + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String descripcion = rs.getString("DESCRIPCION");
			double duracion = Double.parseDouble(rs.getString("DURACION"));
			String idioma = rs.getString("IDIOMA");
			boolean traductor = rs.getBoolean("TRADUCCION");
			int id_representante = Integer.parseInt(rs.getString("ID_REPRESENTANTE"));
			
			e.add(new Espectaculo(id, name2, descripcion, duracion,idioma, traductor, id_representante ));
		}

		return e;
	}

	/**
	 * Método que agrega el evento que entra como parámetro a la base de datos.
	 * @param evento
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addEspectaculo(Espectaculo evento) throws SQLException, Exception {

		int resp = 0;
		if(evento.isNuevaFuncion()) resp=1;
		int resp2=0;
		if(evento.isTraduccionEspanol()) resp=1;
		String sql = "INSERT INTO ISIS2304B071710.ESPECTACULOS VALUES (";
		sql += evento.getId() + ",'";
		sql += evento.getNombre() + "',";
		sql += evento.getRentabilidad() + ",";
		sql += resp + ",'";
		sql += evento.getDescripcion() + "',";
		sql += evento.getDuracion() + ",'";
		sql += evento.getIdioma() + "',";
		sql += resp2 + ",";
		sql += evento.getId_representante() + ")";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza el evento que entra como parámetro en la base de datos.
	 * @param evento
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateEspectaculo(Espectaculo evento) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.ESPECTACULOS SET ";
		
		sql += "nombre" + evento.getNombre();
		sql += "rentabilidad" + evento.getRentabilidad();
		sql += "nuevaFuncion" + evento.isNuevaFuncion();
		sql += "descripcion" + evento.getDescripcion();
		sql += "duracion" + evento.getDuracion();
		sql += "idioma" + evento.getIdioma();
		sql += "traduccion" + evento.isTraduccionEspanol();
		sql += "id_representante" + evento.getId_representante();
		sql += " WHERE id = " + evento.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina el evento que entra como parámetro en la base de datos.
	 * @param evento
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteEspectaculo(Espectaculo evento) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.ESPECTACULOS";
		sql += " WHERE id = " + evento.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public Espectaculo buscarEspectaculoPorId(int id) throws SQLException {
	
		Espectaculo e = null;

		String sql = "SELECT * FROM ISIS2304B071710.EVENTOS WHERE ID ='" + id + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NAME");
			int id2 = Integer.parseInt(rs.getString("ID"));
			String descripcion = rs.getString("DESCRIPCION");
			double duracion = Double.parseDouble(rs.getString("DURACION"));
			String idioma = rs.getString("IDIOMA");
			boolean traductor = rs.getBoolean("TRADUCCION");
			int id_representante = Integer.parseInt(rs.getString("ID_REPRESENTANTE"));		
	
			
			e = new Espectaculo(id2, nombre, descripcion, duracion, idioma, traductor, id_representante );
		}

		return e;
	}
	
	
	public ArrayList<Espectaculo> buscarEspectaculoPorRepresentante(int id_representante) throws SQLException, Exception {
		ArrayList<Espectaculo> e = new ArrayList<Espectaculo>();

		String sql = "SELECT * FROM ISIS2304B071710.ESPECTACULOS WHERE ID_REPRESENTANTE ='" + id_representante + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String descripcion = rs.getString("DESCRIPCION");
			double duracion = Double.parseDouble(rs.getString("DURACION"));
			String idioma = rs.getString("IDIOMA");
			boolean traductor = rs.getBoolean("TRADUCCION");
			int id_representante2 = Integer.parseInt(rs.getString("ID_REPRESENTANTE"));
			
			e.add(new Espectaculo(id, name, descripcion, duracion,idioma, traductor, id_representante2 ));
		}

		return e;
	}
	
	
	public double reporteEspectaculoTotal(int id) throws SQLException
	{
		Espectaculo es = buscarEspectaculoPorId(id);
		return es.getRentabilidad();
	}
	
	
	public double reporteEspectaculoSitio(int id_espectaculo, int id_sitio) throws SQLException, Exception
	{
		
		double ganancias = 0.0;
		DAOTablaSitios daoSitio = new DAOTablaSitios();
		
		DAOTablaFunciones daoEv = new DAOTablaFunciones();

		Sitio sitio = daoSitio.buscarSitioPorId(id_sitio);
		
		
		
	
	for(Funcion funcion : daoEv.darFunciones()){
			
			if(funcion.getId_espectaculo() == id_espectaculo && funcion.getId_sitio() == id_sitio)
			{
				ganancias += funcion.getGanancias();
			}
						
		}
		
	return ganancias;	
	}
	
	
	public double reporteEspectaculoEspectador(int id_espectaculo, int id_espectador) throws SQLException, Exception
	{
		double ganancias = 0.0;
		DAOTablaReservas daoEspectador = new DAOTablaReservas();
		DAOTablaFunciones daoEv = new DAOTablaFunciones();
		
		ArrayList<Reserva> cliente = daoEspectador.buscarReservaPorEspectador(id_espectador);
		
    	  for(Reserva s : cliente){
    		  
    		  for(Funcion ev : daoEv.darFunciones()){
    				
    				if(ev.getId_espectaculo() == id_espectaculo && s.getId_espectador()==id_espectador)
    				{
    					ganancias += ev.getGanancias();
    				}
    							
    			}
    		  
    		  
    	  }
			return ganancias;	
	}
	
	
	//TODO 
		public ArrayList<Funcion> darFuncionesEspectaculo(int id_espectaculo) throws SQLException, Exception{
			System.out.print("++++++++++++++++++++++++++++++++");
			
		
			
					ArrayList<Funcion> sitios = new ArrayList<Funcion>();

			String sql = "SELECT * FROM ISIS2304B071710.EVENTOS WHERE ID_ESPECTACULO ='" + id_espectaculo + "'";

			System.out.println("SQL stmt:" + sql);

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				int id = Integer.parseInt(rs.getString("ID"));
				int id_espectaculo2 = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
				Date fecha = rs.getDate("FECHA");
				Timestamp hora = rs.getTimestamp("HORA");
				int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
				String estado=rs.getString("ESTADO");
				Funcion eventoNuevo = new Funcion(id, fecha, id_espectaculo2, hora, id_sitio,estado);
				sitios.add(eventoNuevo);
			}

			return sitios;
		}
}
