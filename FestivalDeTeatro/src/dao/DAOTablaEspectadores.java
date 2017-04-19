package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Espectador;
import vos.Reserva;



public class DAOTablaEspectadores {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOEspectador
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaEspectadores() {
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
	 * Método que, usando la conexión a la base de datos, saca todos los espectadores de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM ESPECTADORES;
	 * @return Arraylist con los espectadores de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Espectador> darEspectadores() throws SQLException, Exception {
		ArrayList<Espectador> espectadores = new ArrayList<Espectador>();

		String sql = "SELECT * FROM ISIS2304B071710.ESPECTADORES";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			String tarjeta = rs.getString("TARJETA");
			int estaRegistrado2 = Integer.parseInt(rs.getString("ESTAREGISTRADO"));
			double saldo = Double.parseDouble(rs.getString("SALDO"));
			boolean estaRegistrado;
			if(estaRegistrado2==1) estaRegistrado=true;
			else estaRegistrado=false;
			espectadores.add(new Espectador(id, nombre, correo, password, tarjeta, estaRegistrado, saldo));
		}
		return espectadores;
	}


	/**
	 * Método que busca al espectador con el nombre que entra como parámetro.
	 * @param nombre - Nombre a buscar
	 * @return ArrayList con los espectadores
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Espectador> buscarEspectadorPorNombre(String nombre) throws SQLException, Exception {
		ArrayList<Espectador> espectadores = new ArrayList<Espectador>();

		String sql = "SELECT * FROM ISIS2304B071710.ESPECTADORES WHERE NOMBRE ='" + nombre + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			String tarjeta = rs.getString("TARJETA");
			Boolean estaRegistrado = rs.getBoolean("ESTAREGISTRADO");
			double saldo = Double.parseDouble(rs.getString("SALDO"));
			
			espectadores.add(new Espectador(id, name2, correo, password, tarjeta, estaRegistrado, saldo));
		}

		return espectadores;
	}

	/**
	 * Método que agrega el espectador que entra como parámetro a la base de datos.
	 * @param espectador 
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que el video baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addEspectador(Espectador espectador) throws SQLException, Exception {
		int resp=0;
		if(espectador.isEstaRegistrado())resp=1;

		int numero;
		if(espectador.isEstaRegistrado()) numero=1; else numero=0;
		String sql = "INSERT INTO ISIS2304B071710.ESPECTADORES VALUES (";
		sql += espectador.getId() + ",'";
		sql += espectador.getNombre() + "','";
		sql += espectador.getCorreo() + "','";
		sql += espectador.getPassword() + "','";
		sql += espectador.getTarjeta() + "',";
		sql += resp + ",";
		sql += numero + ",";
		sql += espectador.getSaldo() + ")";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza el espectador que entra como parámetro en la base de datos.
	 * @param espectador
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateEspectador(Espectador espectador) throws SQLException, Exception {
		
		System.out.println("COMIENZA EL UPDATE");
		
		String sql = "UPDATE ISIS2304B071710.ESPECTADORES SET ";
		
		int res = 0;
		if(espectador.isEstaRegistrado()) res=1;
		sql += "nombre = '"+espectador.getNombre()+"'";
		sql += ",correo = '" + espectador.getCorreo()+"'";
		sql += ",password = '" + espectador.getPassword()+"'";
		sql += ",tarjeta = '" + espectador.getTarjeta()+"'";
		sql += ",estaregistrado = " + res;
		sql += ",saldo = " + espectador.getSaldo();
		sql += " WHERE id = " + espectador.getId();
		
		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
		
		System.out.println("COLAPSA EN EL UPDATE");
	}

	/**
	 * Método que elimina el espectador que entra como parámetro en la base de datos.
	 * @param espectador
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteEspectador(Espectador espectador) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.ESPECTADORES";
		sql += " WHERE id = " + espectador.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public Espectador buscarEspectadorPorId(int id) throws SQLException, Exception {
		Espectador espectador = null;

		String sql = "SELECT * FROM ISIS2304B071710.ESPECTADORES WHERE ID ='" + id + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			int id2 = Integer.parseInt(rs.getString("ID"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			String tarjeta = rs.getString("TARJETA");
			boolean result=false;
			if(Integer.parseInt(rs.getString("ESTAREGISTRADO"))==1) result=true;
			Boolean estaRegistrado = result;
			double saldo = Double.parseDouble(rs.getString("SALDO"));
			
			espectador = new Espectador(id2, name, correo, password, tarjeta, estaRegistrado, saldo);
		}

		return espectador;
	}

	public ArrayList<Espectador> darEspectadoresDeFuncionYDevolverCosto(int id) throws SQLException {
		
		System.out.println("COMIENZA A DAR ESPECTADORES DE FUNCION Y DEVOLVER EL COSTO-----------");
		ArrayList<Espectador> espectadores = new ArrayList<Espectador>();

		String sql = "SELECT * FROM ISIS2304B071710.SILLAS sillas "
				+ "INNER JOIN ISIS2304B071710.RESERVAS reservas ON sillas.ID_RESERVA=reservas.ID "
				+ "INNER JOIN ISIS2304B071710.ESPECTADORES espectadores ON reservas.ID_ESPECTADOR=espectadores.ID "
				+ "WHERE reservas.ID_FUNCION="+id;

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		System.out.println("Aun no ha colapsado");
		

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			int idNuevo = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			String tarjeta = rs.getString("TARJETA");
			int estaRegistrado2 = Integer.parseInt(rs.getString("ESTAREGISTRADO"));
			double saldo = Double.parseDouble(rs.getString("SALDO"));
			boolean estaRegistrado;
			if(estaRegistrado2==1) estaRegistrado=true;
			else estaRegistrado=false;
			double costoSilla= Double.parseDouble(rs.getString("COSTO"));
			System.out.println("ESPECTADOR CON ID: "+idNuevo);
			System.out.println("CUYO SALDO ACTUAL ES: "+saldo);
			saldo+=costoSilla;
			System.out.println("SE LE RETORNA LA BOLETA Y QUEDA CON UN SALDO ACTUAL DE : "+saldo);
			espectadores.add(new Espectador(idNuevo, nombre, correo, password, tarjeta, estaRegistrado, saldo));
		}
		return espectadores;
	}
	
	public Espectador darReservasDeEspectadorYDarAbono(int id, int idReserva) throws SQLException, Exception{
		System.out.println("-------COMIENZA A DAR RESERVAS DE ESPECTADOR Y DAR ABONO------");
		Espectador espectador = null;

		String sql = "SELECT * FROM ISIS2304B071710.SILLAS sillas "
				+ "INNER JOIN ISIS2304B071710.RESERVAS reservas ON sillas.ID_RESERVA=reservas.ID "
				+ "INNER JOIN ISIS2304B071710.ESPECTADORES espectadores ON reservas.ID_ESPECTADOR=espectadores.ID "
				+ "WHERE reservas.ID="+idReserva+" AND espectadores.ID="+id;

		System.out.println(sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();
		

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			int idNuevo = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			String tarjeta = rs.getString("TARJETA");
			int estaRegistrado2 = Integer.parseInt(rs.getString("ESTAREGISTRADO"));
			double saldo = Double.parseDouble(rs.getString("SALDO"));
			boolean estaRegistrado;
			if(estaRegistrado2==1) estaRegistrado=true;
			else estaRegistrado=false;
			double costoSilla= Double.parseDouble(rs.getString("COSTO"));
			System.out.println("ESPECTADOR CON ID: "+idNuevo);
			System.out.println("CUYO SALDO ACTUAL ES: "+saldo);
			saldo=0.0;
			System.out.println("SE LE RETORNA LA BOLETA Y QUEDA CON UN SALDO ACTUAL DE : "+saldo);
			espectador = new Espectador(idNuevo, nombre, correo, password, tarjeta, estaRegistrado, saldo);
		}
		System.out.println("-------TERMINA DE DAR RESERVAS DE ESPECTADOR Y DAR ABONO--------");
		return espectador;
	}
}
