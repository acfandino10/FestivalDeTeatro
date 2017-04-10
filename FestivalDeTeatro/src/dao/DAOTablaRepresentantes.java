package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Compania;
import vos.Representante;



public class DAOTablaRepresentantes {
	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAORepresentante
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaRepresentantes() {
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
	 * Método que, usando la conexión a la base de datos, saca todos los representantes de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM REPRESENTANTES;
	 * @return Arraylist con los representantes de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Representante> darRepresentantes() throws SQLException, Exception {
		ArrayList<Representante> representantes = new ArrayList<Representante>();

		String sql = "SELECT * FROM ISIS2304B071710.REPRESENTANTES";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			int id_compania = Integer.parseInt(rs.getString("ID_COMPANIA"));
			
			representantes.add(new Representante(id, nombre, correo, password, id_compania));
		}
			
		return representantes;
	}


	/**
	 * Método que busca al representante con el nombre que entra como parámetro.
	 * @param nombre - Nombre a buscar
	 * @return ArrayList con los representantes
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Representante> buscarRepresentantesPorNombre(String nombre) throws SQLException, Exception {
		ArrayList<Representante> representantes = new ArrayList<Representante>();

		String sql = "SELECT * FROM ISIS2304B071710.REPRESENTANTES WHERE NOMBRE ='" + nombre + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name2 = rs.getString("NAME");
			int id = Integer.parseInt(rs.getString("ID"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			int id_compania = Integer.parseInt(rs.getString("ID_COMPANIA"));
			
			representantes.add(new Representante(id, name2, correo, password, id_compania));
		}

		return representantes;
	}

	/**
	 * Método que agrega el representante que entra como parámetro a la base de datos.
	 * @param representante
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addRepresentante(Representante representante) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.REPRESENTANTES VALUES (";
		sql += representante.getId() + ",'";
		sql += representante.getNombre() + "','";
		sql += representante.getCorreo() + "','";
		sql += representante.getPassword() + "',";
		sql += representante.getCompania() + ")";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	//TODO Copiar y pegar el usuario a todas los metodos DDAO ISIS2304B071710
	/**
	 * Método que agrega el representante que entra como parámetro a la base de datos.
	 * @param representante
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCompania(Compania compania) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.COMPANIAS VALUES (";
		sql += compania.getId() + ",'";
		sql += compania.getNombre() + ",'";
		sql += compania.getDiaLlegada() + ",'";
		sql += compania.getDiaSalida() + ",'";
		sql += compania.getCiudad() + ")";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	
	
	/**
	 * Método que actualiza el representante que entra como parámetro en la base de datos.
	 * @param representante
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateRepresentante(Representante representante) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.REPRESENTANTES SET ";
		
		sql += "nombre='" + representante.getNombre();
		sql += "',correo='" + representante.getCorreo();
		sql += "',password='" + representante.getPassword();
		sql += "',id_compania =" + representante.getCompania();
		sql += " WHERE id = " + representante.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina el representante que entra como parámetro en la base de datos.
	 * @param representante
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteRepresentante(Representante representante) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.REPRESENTANTES";
		sql += " WHERE id = " + representante.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public Representante buscarRepresentantePorId(int id) throws SQLException {
		Representante representante = null;

		String sql = "SELECT * FROM ISIS2304B071710.REPRESENTANTES WHERE ID ='" + id + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			int id2 = Integer.parseInt(rs.getString("ID"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			int id_compania = Integer.parseInt(rs.getString("ID_COMPANIA"));
			
			representante = new Representante(id2, name, correo, password, id_compania);
		}

		return representante;
	}
	
	public Representante buscarRepresentantePorCompania(int id_compania) throws SQLException {
		Representante representante = null;

		String sql = "SELECT * FROM ISIS2304B071710.REPRESENTANTES WHERE ID_COMPANIA ='" + id_compania + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String correo = rs.getString("CORREO");
			String password = rs.getString("PASSWORD");
			int id_compania2 = Integer.parseInt(rs.getString("ID_COMPANIA"));
			
			representante = new Representante(id, name, correo, password, id_compania2);
		}

		return representante;
	}
}
