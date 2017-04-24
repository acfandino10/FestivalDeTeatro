package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.ws.rs.Path;

import vos.Espectador;
import vos.Funcion;
import vos.Silla;


public class DAOTablaSillas {
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
	public DAOTablaSillas() {
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
	 * Método que, usando la conexión a la base de datos, saca todas las sillas de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM SILLAS;
	 * @return Arraylist con los representantes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Silla> darSillas() throws SQLException, Exception {
		ArrayList<Silla> sillas = new ArrayList<Silla>();

		String sql = "SELECT * FROM ISIS2304B071710.SILLAS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int numero = Integer.parseInt(rs.getString("NUMERO"));
			double costo = Double.parseDouble(rs.getString("COSTO"));
			String localidad = rs.getString("LOCALIDAD");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			Silla toAdd = new Silla(numero, costo, localidad, id_sitio);
			toAdd.setEstaReservada(Boolean.parseBoolean(rs.getString("ESTARESERVADA")));
			sillas.add(toAdd);
		}
			
		return sillas;
	}


	/**
	 * Método que busca las sillas en la localidad que entra como parámetro.
	 * @param name - localidad a buscar
	 * @return ArrayList con las sillas
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Silla> buscarSillasPorLocalidad(String localidad) throws SQLException, Exception {
		ArrayList<Silla> sillas = new ArrayList<Silla>();

		String sql = "SELECT * FROM ISIS2304B071710.SILLAS WHERE LOCALIDAD ='" + localidad + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int numero = Integer.parseInt(rs.getString("NUMERO"));
			double costo = Double.parseDouble(rs.getString("COSTO"));
			String localidad2 = rs.getString("LOCALIDAD");
			int id_sitio = Integer.parseInt(rs.getString("SITIO"));
			
			Silla toAdd = new Silla(numero, costo, localidad2, id_sitio);
			toAdd.setEstaReservada(Boolean.parseBoolean(rs.getString("ESTARESERVADA")));
			sillas.add(toAdd);
		}

		return sillas;
	}

	/**
	 * Método que agrega la silla que entra como parámetro a la base de datos.
	 * @param silla
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addSilla(Silla silla) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.SILLAS VALUES (";
		sql += silla.getNumero() + ",'";
		sql += silla.isEstaReservada() + ",'";
		sql += silla.getCosto() + ",'";
		sql += silla.getLocalidad() + ",'";
		sql += silla.getId_sitio() + ")";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza la silla que entra como parámetro en la base de datos.
	 * @param silla
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateSilla(Silla silla) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.SILLAS SET ";
		
		int resp=0;
		if(silla.isEstaReservada()) resp=1;
		sql += "estaReservada=" + resp;
		sql += ",costo=" + silla.getCosto();
		sql += ",localidad='" + silla.getLocalidad();
		sql += "',id_sitio=" + silla.getId_sitio();
		sql += ",id_reserva="+silla.getIdReserva();
		sql += " WHERE numero = " + silla.getNumero();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina la silla que entra como parámetro en la base de datos.
	 * @param silla
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteSilla(Silla silla) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.SILLAS";
		sql += " WHERE numero = " + silla.getNumero();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	
	public Silla buscarSillasPorNumero(int numero) throws SQLException, Exception {
		Silla silla = null;

		String sql = "SELECT * FROM ISIS2304B071710.SILLAS WHERE NUMERO ='" + numero + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int numero2 = Integer.parseInt(rs.getString("NUMERO"));
			double costo = Double.parseDouble(rs.getString("COSTO"));
			String localidad = rs.getString("LOCALIDAD");
			int id_sitio = Integer.parseInt(rs.getString("SITIO"));
			
			Silla toAdd = new Silla(numero2, costo, localidad, id_sitio);
			toAdd.setEstaReservada(Boolean.parseBoolean(rs.getString("ESTARESERVADA")));
			silla = toAdd;
		}

		return silla;
	}
	
	
	
	public ArrayList<Silla> buscarSillasPorSitio(int id_sitio) throws SQLException, Exception {
		ArrayList<Silla> sillas = new ArrayList<Silla>();

		String sql = "SELECT * FROM ISIS2304MO11620.SILLAS WHERE ID_SITIO ='" + id_sitio + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int numero = Integer.parseInt(rs.getString("NUMERO"));
			double costo = Double.parseDouble(rs.getString("COSTO"));
			String localidad = rs.getString("LOCALIDAD");
			int id_sitio2 = Integer.parseInt(rs.getString("SITIO"));
			
			sillas.add(new Silla(numero, costo, localidad, id_sitio2));
		}

		return sillas;
	}
	
	public ArrayList<Silla> darSillasCanceladas(int idEspectador, int idFuncion) throws SQLException{
		ArrayList<Silla> sillas = new ArrayList<Silla>();

		String sql = "SELECT silla.* FROM ISIS2304B071710.FUNCIONES fun "
				+"INNER JOIN ISIS2304B071710.RESERVAS res ON fun.ID=res.ID_FUNCION "
				+"INNER JOIN ISIS2304B071710.SILLAS silla ON res.NUMEROSILLA=silla.NUMERO "
				+"INNER JOIN ISIS2304B071710.ESPECTADORES esp ON res.ID_ESPECTADOR=esp.ID "
				+"WHERE esp.ESTAREGISTRADO=1 AND res.ESTADOACTIVE='ESTADO_CANCELADA' AND esp.ID='"+idEspectador+"' AND fun.ID='"+idFuncion+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int numero = Integer.parseInt(rs.getString("NUMERO"));
			double costo = Double.parseDouble(rs.getString("COSTO"));
			String localidad = rs.getString("LOCALIDAD");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			Silla toAdd = new Silla(numero, costo, localidad, id_sitio);
			toAdd.setEstaReservada(Boolean.parseBoolean(rs.getString("ESTARESERVADA")));
			sillas.add(toAdd);
		}
			
		return sillas;
	}

	public Silla cancelarFuncionSillaPorReserva(int idReserva) throws SQLException {
		Silla silla = null;

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS reserva "
				+"INNER JOIN ISIS2304B071710.SILLAS silla "
				+"ON silla.ID_RESERVA=reserva.ID WHERE reserva.ID="+idReserva;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int numero = Integer.parseInt(rs.getString("NUMERO"));
			double costo = Double.parseDouble(rs.getString("COSTO"));
			String localidad = rs.getString("LOCALIDAD");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			silla = new Silla(numero, costo, localidad, id_sitio);
			silla.setEstaReservada(false);
		}
			
		return silla;
	}
	
	/**
	 * Método que busca las sillas en la localidad y sitio que entra como parámetro.
	 * @param name - localidad a buscar
	 * @return ArrayList con las sillas
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Silla> buscarSillasPorSitioYLocalidad(int sitio, String localidad) throws SQLException, Exception {
		ArrayList<Silla> sillas = new ArrayList<Silla>();
		
		//localidad=localidad.substring(1, localidad.length()-1);
		
		System.out.println("-------La localidad es " + localidad);
		String sql = "SELECT * FROM ISIS2304B071710.SILLAS WHERE LOCALIDAD ='" + localidad + "' AND ID_SITIO ='" + sitio + "'" ;		
		System.out.println("SQL stmt:" + sql);
		System.out.println("-----SIRVIO LA BUSQUEDA SQL");

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		System.out.println("al menos entro a esta porqueria");
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
        int i = 1;
		while (rs.next()) {
			System.out.println("VENGA, SIRVA POR favorcitoooo.---" + i++);
			int numero = Integer.parseInt(rs.getString("NUMERO"));
			double costo = Double.parseDouble(rs.getString("COSTO"));
			Silla toAdd = new Silla(numero, costo, localidad, sitio);
			int estaReservada = Integer.parseInt(rs.getString("ESTARESERVADA"));
			boolean siOno = false;
			if(estaReservada == 1) siOno = true; 
			toAdd.setEstaReservada(siOno);
			if(siOno) toAdd.setId_reserva(Integer.parseInt(rs.getString("ID_RESERVA")));
			sillas.add(toAdd);
		}
		System.out.println("-----Tengo" + sillas);
		return sillas;
	}
	
}
