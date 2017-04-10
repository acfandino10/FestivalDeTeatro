package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.proxy.annotation.Pre;
import vos.Preferencia;

public class DAOTablaPreferencias {
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
	public DAOTablaPreferencias() {
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



	public ArrayList<Preferencia> darPreferencias() throws SQLException, Exception {
		ArrayList<Preferencia> p = new ArrayList<Preferencia>();

		String sql = "SELECT * FROM ISIS2304B071710.PREFERENCIAS";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String nombre = rs.getString("PREFERENCIA");
			int id = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			
			p.add(new Preferencia(id, nombre));
		}
		return p;
	}


	
	public ArrayList<Preferencia> buscarPreferenciaPorIdEspectador(int id) throws SQLException, Exception {
		ArrayList<Preferencia> p = new ArrayList<Preferencia>();

		String sql = "SELECT * FROM ISIS2304B071710.PREFERENCIA WHERE ID_ESPECTADOR ='" + id + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			String name = rs.getString("PREFERENCIA");
			int id2 = Integer.parseInt(rs.getString("ID_ESPECTADOR"));
			
			
			p.add(new Preferencia(id, name));
		}

		return p;
	}

	
	public void addPreferencia(Preferencia p) throws SQLException, Exception {

		String sql = "INSERT INTO ISIS2304B071710.PREFERENCIAS VALUES (";
		sql += p.getId_espectador() + ",'";
		sql += p.getPreferencia() + "')";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	
	public void updatePreferencia(Preferencia p) throws SQLException, Exception {

		String sql = "UPDATE ISIS2304B071710.PREFERENCIAS SET ";
		sql += "PREFERENCIA" + p.getPreferencia();
		sql += " WHERE id_espectador = " + p.getId_espectador();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	
	public void deletePreferencia(Preferencia p) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.PREFERENCIAS";
		sql += " WHERE id_espectador = " + p.getId_espectador();

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
}
