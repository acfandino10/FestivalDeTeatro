package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Espectador;



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
			int id_bancoTarjeta = Integer.parseInt(rs.getString("ID_BANCOTARJETA"));
			boolean estaRegistrado;
			if(estaRegistrado2==1) estaRegistrado=true;
			else estaRegistrado=false;
			espectadores.add(new Espectador(id, nombre, correo, password, tarjeta, estaRegistrado, id_bancoTarjeta));
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
			int id_bancoTarjeta = Integer.parseInt(rs.getString("ID_BANCOTARJETA"));
			
			espectadores.add(new Espectador(id, name2, correo, password, tarjeta, estaRegistrado, id_bancoTarjeta));
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
		sql += espectador.getBancoTarjeta() + ")";

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

		int res = 0;
		if(espectador.isEstaRegistrado()) res=1;
		String sql = "UPDATE ISIS2304B071710.ESPECTADORES SET ";
		sql += "NOMBRE = '" + espectador.getNombre();
		sql += "',CORREO = '" + espectador.getCorreo();
		sql += "',PASSWORD = '" + espectador.getPassword();
		sql += "',TARJETA = '" + espectador.getTarjeta();
		sql += "',ESTAREGISTRADO = " + res;
		sql += ",ID_BANCOTARJETA = " + espectador.getBancoTarjeta();
		sql += " WHERE id = " + espectador.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
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
			int id_bancoTarjeta = Integer.parseInt(rs.getString("ID_BANCOTARJETA"));
			
			espectador = new Espectador(id2, name, correo, password, tarjeta, estaRegistrado, id_bancoTarjeta);
		}

		return espectador;
	}
}
