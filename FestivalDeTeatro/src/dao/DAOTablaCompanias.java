package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Compania;


public class DAOTablaCompanias {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOCompania
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaCompanias() {
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
	 * Método que, usando la conexión a la base de datos, saca todas las companias de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM COMPANIAS;
	 * @return Arraylist con las companias de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Compania> darCompanias() throws SQLException, Exception {
		ArrayList<Compania> companias = new ArrayList<Compania>();

		String sql = "SELECT * FROM ISIS2304B071710.COMPANIAS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String ciudad = rs.getString("CIUDAD");
			Date diaLlegada = rs.getDate("DIALLEGADA");
			Date diaSalida = rs.getDate("DIASALIDA");
			companias.add(new Compania(id, nombre, diaLlegada, diaSalida, ciudad));
		}
		return companias;
	}


	/**
	 * Método que busca la compania con el nombre que entra como parámetro.
	 * @param nombre - Nombre a buscar
	 * @return ArrayList con las companias
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Compania> buscarCompaniaPorNombre(String nombre) throws SQLException, Exception {
		ArrayList<Compania> companias = new ArrayList<Compania>();

		String sql = "SELECT * FROM ISIS2304B071710.COMPANIAS WHERE NOMBRE ='" + nombre + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name2 = rs.getString("NOMBRE");
			int id = Integer.parseInt(rs.getString("ID"));
			String ciudad = rs.getString("CIUDAD");
			Date diaLlegada = rs.getDate("DIALLEGADA");
			Date diaSalida = rs.getDate("DIASALIDA");
			
			companias.add(new Compania(id, name2, diaLlegada,diaSalida, ciudad));
		}

		return companias;
	}

	/**
	 * Método que agrega la compania que entra como parámetro a la base de datos.
	 * @param compania 
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que el video baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addCompania(Compania compania) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.COMPANIAS VALUES (";
		sql += compania.getId() + ",'";
		sql += compania.getNombre() + "',";
		sql += "(TO_DATE('"+compania.getDiaLlegada()+"', 'YYYY/MM/DD'))" + ",";
		sql += "(TO_DATE('"+compania.getDiaSalida()+"', 'YYYY/MM/DD'))" + ",'";
		sql += compania.getCiudad() + "')";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza la compania que entra como parámetro en la base de datos.
	 * @param compania
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateCompania(Compania compania) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.COMPANIAS SET ";
		sql += "nombre" + compania.getNombre();
		sql += "diaLlegada" + compania.getDiaLlegada();
		sql += "diaSalida" + compania.getDiaSalida();
		sql += "ciudad" + compania.getCiudad();
		sql += " WHERE id = " + compania.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina la compania que entra como parámetro en la base de datos.
	 * @param compania
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteCompania(Compania compania) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.COMPANIAS";
		sql += " WHERE id = " + compania.getId();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public Compania buscarCompaniaPorId(int id) throws SQLException, Exception {
		Compania companias = null;

		String sql = "SELECT * FROM ISIS2304B071710.COMPANIAS WHERE ID ='" + id + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("NAME");
			int id2 = Integer.parseInt(rs.getString("ID"));
			String ciudad = rs.getString("CIUDAD");
			Date diaLlegada = rs.getDate("DIALLEGADA");
			Date diaSalida = rs.getDate("DIASALIDA");
			
			companias = new Compania(id2, name, diaLlegada,diaSalida, ciudad);
		}

		return companias;
	}

	public int darAsistenciaTotalPorCompania(int id) throws SQLException {
		int ans=0;
		
		String sql = "SELECT COUNT(*) FROM ISIS2304B071710.ESPECTACULOS esp "
				+ "INNER JOIN ISIS2304B071710.FUNCIONES fun ON esp.ID=fun.ID_ESPECTACULO "
				+ "INNER JOIN ISIS2304B071710.RESERVAS res ON res.ID_FUNCION=fun.ID "
				+ "WHERE esp.ID_REPRESENTANTE='"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			ans = Integer.parseInt(rs.getString("COUNT(*)"));
		}
		return ans;
	}
	
	public int darAsistenciaRegistradosPorCompania(int id) throws SQLException{
		
		int ans=0;
		
		String sql = "SELECT COUNT(*) FROM ISIS2304B071710.ESPECTACULOS esp "
				+ "INNER JOIN ISIS2304B071710.FUNCIONES fun ON esp.ID=fun.ID_ESPECTACULO "
				+ "INNER JOIN ISIS2304B071710.RESERVAS res ON res.ID_FUNCION=fun.ID "
				+ "INNER JOIN ISIS2304B071710.ESPECTADORES esp ON res.ID_ESPECTADOR=esp.ID "
				+ "WHERE esp.ID_REPRESENTANTE='"+id+"' AND esp.ESTAREGISTRADO=1";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			ans = Integer.parseInt(rs.getString("COUNT(*)"));
		}
		return ans;
	}
	
	public double darGananciaPorCompania(int id) throws SQLException{
	double ans=-1.0;
		
		String sql = "SELECT * FROM ISIS2304B071710.ESPECTACULOS esp "
					+"INNER JOIN ISIS2304B071710.FUNCIONES fun ON esp.ID=fun.ID_ESPECTACULO "
				+ "WHERE esp.ID_REPRESENTANTE='"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			ans = Double.parseDouble(rs.getString("GANANCIAS"));
		}
		return ans;
	
	}
}
