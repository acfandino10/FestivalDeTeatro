package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.EspectaculoCategoria;

public class DAOTablaCategoriaEspectaculo {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOCategoriaEvento
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCategoriaEspectaculo() {
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
	 * Método que, usando la conexión a la base de datos, saca todas las categorias de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM CATEGORIAS;
	 * @return Arraylist con las cattegorias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<EspectaculoCategoria> darCategoriaEspectaculo() throws SQLException, Exception {
		ArrayList<EspectaculoCategoria> categoriaEspectaculo = new ArrayList<EspectaculoCategoria>();

		String sql = "SELECT * FROM ISIS2304B071710.CATEGORIAESPECTACULO";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreCategoria = rs.getString("NOMBRECATEGORIA");
			int idEspectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			categoriaEspectaculo.add(new EspectaculoCategoria(idEspectaculo, nombreCategoria));
		}
		return categoriaEspectaculo;
	}


	/**
	 * Método que busca la categoriaevento con el nombre que entra como parámetro.
	 * @param nombre - Nombre de la categoria a buscar
	 * @return ArrayList con la categoriaEvento
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<EspectaculoCategoria> buscarEspectaculoPorNombreCategoria(String nombre) throws SQLException, Exception {
		ArrayList<EspectaculoCategoria> categoriasEspectaculo = new ArrayList<EspectaculoCategoria>();

		String sql = "SELECT * FROM ISIS2304B071710.CATEGORIAESPECTACULO WHERE NOMBRECATEGORIA ='" + nombre + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombreCategoria = rs.getString("NOMBRECATEGORIA");
			int idEspectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			categoriasEspectaculo.add(new EspectaculoCategoria(idEspectaculo, nombreCategoria));
		}

		return categoriasEspectaculo;
	}

	/**
	 * Método que agrega la categoriaEvento que entra como parámetro a la base de datos.
	 * @param categoria - categoria a agregar. 
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que el video baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCategoriaEspectaculo(EspectaculoCategoria categoriaEspectaculo) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.CATEGORIAESPECTACULO VALUES (";
		sql += categoriaEspectaculo.getIdEspectaculo() + ",'";
		sql += categoriaEspectaculo.getNombreCategoria() + ")";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza la categoria que entra como parámetro en la base de datos.
	 * @param categoria
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateCategoriaEvento(EspectaculoCategoria categoriaEspectaculo) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.CATEGORIAESPECTACULO SET ";
		sql += "edad=" + categoriaEspectaculo.getIdEspectaculo();
		sql += " WHERE NOMBRECATEGORIA = " + categoriaEspectaculo.getNombreCategoria();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina la categoria que entra como parámetro en la base de datos.
	 * @param categoria
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteEventoCategoria(EspectaculoCategoria categoriaEspectaculo) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.CATEGORIAESPECTACULO";
		sql += " WHERE NOMBRECATEGORIA = " + categoriaEspectaculo.getNombreCategoria() +
		"AND ID_ESPECTACULO = " + categoriaEspectaculo.getIdEspectaculo();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
}
