package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.net.aso.s;
import vos.Funcion;
import vos.Reserva;
import vos.Silla;

public class DAOTablaReserva {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOCreacion
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaReserva() {
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
	 * Método que, usando la conexión a la base de datos, saca todas las creaciones de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CREACIONES;
	 * @return Arraylist con las cattegorias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Reserva> darReservas() throws SQLException, Exception {
		ArrayList<Reserva> r = new ArrayList<Reserva>();

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id_espectador = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			int id_evento = Integer.parseInt(rs.getString("ID_EVENTO"));
			int numero_silla = Integer.parseInt(rs.getString("NUMEROSILLA"));
			r.add(new Reserva(id_espectador, id_evento, numero_silla));
		}
		return r;
	}


	/**
	 * Método que busca la reserva con el id_evento que entra como parámetro.
	 * @param id_evento - evento de la reserva a buscar
	 * @return ArrayList 
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Reserva> buscarReservaPorEvento(int id_evento) throws SQLException, Exception {
		ArrayList<Reserva> lista = new ArrayList<Reserva>();

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS WHERE ID_EVENTO ='" + id_evento + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id_espectador = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			int id_evento2 = Integer.parseInt(rs.getString("ID_EVENTO"));
			int numero_silla = Integer.parseInt(rs.getString("NUMEROSILLA"));
			lista.add(new Reserva(id_espectador, id_evento2, numero_silla));
		}

		return lista;
	}

	/**
	 * Método que agrega la reserva que entra como parámetro a la base de datos.
	 * @param reserva a agregar. 
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addReserva(Reserva objeto) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304MO11620.RESERVAS VALUES (";
		sql += objeto.getId_espectador() + ",";
		sql += objeto.getId_evento() + ",";
		sql += objeto.getNumero_silla() + ",";
		sql += objeto.getAbono() + ")";

		System.out.println("SQL stmt:" + sql);
 
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza la reserva que entra como parámetro en la base de datos.
	 * @param id_evento
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateReserva(Reserva objeto) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304MO11620.RESERVAS SET ";
		sql += "id_espectador" + objeto.getId_espectador();
		sql += "id_evento = " + objeto.getId_evento();
		sql += "abono = " + objeto.getAbono();
		sql += " WHERE numerosilla = " + objeto.getNumero_silla();
		

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina la reserva que entra como parámetro en la base de datos.
	 * @param 
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteReserva(Reserva objeto) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.RESERVAS";
		sql += " WHERE id_espectador = " + objeto.getId_espectador() +
		"AND id_evento = " + objeto.getId_evento() +
		"AND numerosilla = " + objeto.getNumero_silla();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public Reserva buscarReservaPorSilla(int silla) throws SQLException, Exception {
		Reserva l = null;

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS WHERE NUMEROSILLA ='" + silla + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id_espectador = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			int id_evento = Integer.parseInt(rs.getString("ID_EVENTO"));
			int numero_silla = Integer.parseInt(rs.getString("NUMEROSILLA"));
			l = new Reserva(id_espectador, id_evento, numero_silla);
		}

		return l;
	}

	public ArrayList<Reserva> buscarReservaPorEspectador(int id_espectador) throws SQLException, Exception {
		ArrayList<Reserva> lista = new ArrayList<Reserva>();

		String sql = "SELECT * FROM ISIS2304MO11620.RESERVAS WHERE ID_ESPECTADOR ='" + id_espectador + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id_espectador2 = Integer.parseInt(rs.getString("ESPECTADOR"));
			int id_evento = Integer.parseInt(rs.getString("EVENTO"));
			int numero_silla = Integer.parseInt(rs.getString("NUMERO_SILLA"));
			lista.add(new Reserva(id_espectador2, id_evento, numero_silla));
		}

		return lista;
	}
	
	//TODO se debe agregar en las tablas y en el UML un atributo en reserva que se llame abono
	public double addAbono(int numero) throws SQLException, Exception{
		DAOTablaSillas sillas = new DAOTablaSillas();
		Silla silla = sillas.buscarSillasPorNumero(numero);
		double abono = silla.getCosto()*0.2; 
		
		if(!silla.isEstaReservada()){
		String sql = "UPDATE ISIS2304MO11620.RESERVAS SET ";
		sql += "abono" + abono;
		sql += " WHERE numero_silla = " + numero;
		

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		return abono;		
	 }
		else{
			throw new Exception("No hay disponibilidad"); 
		}
		
	}
	
	
	public void deleteAbono(int numero) throws SQLException, Exception{
		double abono = 0.0; 
		
		String sql = "UPDATE ISIS2304MO11620.RESERVAS SET ";
		sql += "abono" + abono;
		sql += " WHERE numero_silla = " + numero;
		

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();		
	 
	}
	
	
	
	
}