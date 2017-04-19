package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.net.aso.s;
import vos.Espectador;
import vos.Funcion;
import vos.Reserva;
import vos.Silla;

public class DAOTablaReservas {
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
	public DAOTablaReservas() {
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
			int id_evento = Integer.parseInt(rs.getString("ID_FUNCION"));
			String estado = rs.getString("ESTADOACTIVE");
			int id = Integer.parseInt(rs.getString("ID"));
			r.add(new Reserva(id_espectador, id_evento,estado,id));
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
	public ArrayList<Reserva> buscarReservaPorFuncion(int idFuncion) throws SQLException, Exception {
		ArrayList<Reserva> lista = new ArrayList<Reserva>();

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS WHERE ID_FUNCION ='" + idFuncion + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id_espectador = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			int id_evento2 = Integer.parseInt(rs.getString("ID_FUNCION"));
			String estado = rs.getString("ESTADOACTIVE");
			int id = Integer.parseInt("ID");
			lista.add(new Reserva(id_espectador, id_evento2,estado,id));
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
		sql += objeto.getIdFuncion() + ",";
		sql += objeto.getEstado() + ",";
		sql += objeto.getId() + ")";

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

		String sql = "UPDATE ISIS2304B071710.RESERVAS SET ";
		sql += "estadoactive='" + objeto.getEstado();
		sql += "' WHERE ID=" + objeto.getId();
		

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
		"AND id_funcion = " + objeto.getIdFuncion();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	

	public ArrayList<Reserva> buscarReservaPorEspectador(int id_espectador) throws SQLException, Exception {
		ArrayList<Reserva> lista = new ArrayList<Reserva>();

		String sql = "SELECT * FROM ISIS2304MO11620.RESERVAS WHERE ID_ESPECTADOR ='" + id_espectador + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id_espectador2 = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			int id_evento = Integer.parseInt(rs.getString("ID_FUNCION"));
			String estado = rs.getString("ESTADOACTIVE");
			int id = Integer.parseInt("ID");
			lista.add(new Reserva(id_espectador2, id_evento,estado,id));
		}

		return lista;
	}
	
	
	

	public ArrayList<Reserva> darReservasFuncionYCancelarlas(int idFuncion) throws NumberFormatException, SQLException {
		System.out.println("-------COMIENZA A DAR RESERVAS POR FUNCION Y CANCELARLAS------");
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS reserva "
				+ "INNER JOIN ISIS2304B071710.ESPECTADORES espectador "
				+ "ON espectador.ID=reserva.ID_ESPECTADOR "
				+ "WHERE reserva.ESTADOACTIVE='ESTADO_ACTIVA' AND reserva.ID_FUNCION="+idFuncion;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int idEspectador = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			int idFuncionNueva = Integer.parseInt(rs.getString("ID_FUNCION"));
			String estado = Reserva.ESTADO_CANCELADA;
			int id=Integer.parseInt(rs.getString("ID"));
			reservas.add(new Reserva(idEspectador, idFuncionNueva,estado,id));
		}
		System.out.println("-------TERMINA DE DAR RESERVAS POR FUNCION Y CANCELARLAS--------");
		return reservas;
	}

	public Reserva darReservasPorId(int idReserva) throws SQLException {
		Reserva reserva = null;

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS WHERE ID =" + idReserva;

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id_espectador = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			int id_evento2 = Integer.parseInt(rs.getString("ID_FUNCION"));
			String estado = rs.getString("ESTADOACTIVE");
			int id = Integer.parseInt(rs.getString("ID"));
			reserva=new Reserva(id_espectador, id_evento2,estado,id);
		}

		return reserva;
	}

	public Date darFechaReserva(int idReserva) throws SQLException {
		Date fechaReserva = null;

		String sql = "SELECT * FROM ISIS2304B071710.RESERVAS reserva "
				+ "INNER JOIN ISIS2304B071710.ESPECTADORES espectador "
				+ "ON espectador.ID=reserva.ID_ESPECTADOR "
				+ "INNER JOIN ISIS2304B071710.FUNCIONES funcion "
				+ "ON funcion.ID=reserva.ID_FUNCION "
				+ "WHERE reserva.ID='" + idReserva + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			fechaReserva=rs.getDate("FECHA");
		}

		return fechaReserva;
	}

	
	
	
	
	
}