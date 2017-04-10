package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Banco;

public class DAOTablaBancos {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOBanco
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaBancos() {
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
	 * Método que, usando la conexión a la base de datos, saca todos los bancos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM Bancos;
	 * @return Arraylist con los bancos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Banco> darBancos() throws SQLException, Exception {
		ArrayList<Banco> bancos = new ArrayList<Banco>();

		String sql = "SELECT * FROM ISIS2304B071710.BANCOS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NAME");
			int id = Integer.parseInt(rs.getString("ID"));
			String dirPrincipal = rs.getString(rs.getString("DIRECCION"));
			bancos.add(new Banco(id, nombre, dirPrincipal));
		}
		return bancos;
	}


	/**
	 * Método que busca el/los bancos con el nombre que entra como parametro.
	 * @param nombre - Nombre de el/los bancos a buscar
	 * @return ArrayList con los bancos encontrados
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Banco> buscarBancosPorNombre(String nombre) throws SQLException, Exception {
		ArrayList<Banco> bancos = new ArrayList<Banco>();

		String sql = "SELECT * FROM ISIS2304B071710.BANCOS WHERE NOMBRE ='" + nombre + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre2 = rs.getString("NAME");
			int id = Integer.parseInt(rs.getString("ID"));
			String dirPrincipal = rs.getString("DIRECCION");
			bancos.add(new Banco(id, nombre2, dirPrincipal));
		}

		return bancos;
	}

	/**
	 * Método que agrega el banco que entra como parámetro a la base de datos.
	 * @param banco - el banco a agregar. banco !=  null
	 * <b> post: </b> se ha agregado el banco a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que el banco baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addBanco(Banco banco) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.CLIENTES_REMITENTES VALUES (";
		sql += banco.getId() + ",'";
		sql += banco.getNombre() + "',";
		sql += banco.getDirPrincipal() + ")";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza el banco que entra como parámetro en la base de datos.
	 * @param banco - el banco a actualizar. banco !=  null
	 * <b> post: </b> se ha actualizado el banco en la base de datos en la transaction actual. pendiente que el banco master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateBanco(Banco banco) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.BANCOS SET ";
		sql += "nombre='" + banco.getNombre() + "',";
		sql += "dirPrincipal=" + banco.getDirPrincipal();
		sql += " WHERE id = " + banco.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina el video que entra como parámetro en la base de datos.
	 * @param video - el video a borrar. video !=  null
	 * <b> post: </b> se ha borrado el video en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteBanco(Banco banco) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.BANCOS";
		sql += " WHERE id = " + banco.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	
}
