package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import vos.Compania;
import vos.Espectaculo;
import vos.Espectador;
import vos.Funcion;
import vos.Representante;
import vos.Reserva;
import vos.Silla;
import vos.Sitio;


public class DAOTablaFunciones {

	/**
	 * Arraylits de recursos que se usan para la ejecución de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexión a la base de datos
	 */
	private Connection conn;

	/**
	 * Método constructor que crea DAOEvento
	 * <b>post: </b> Crea la instancia del DAO e inicializa el Arraylist de recursos
	 */
	public DAOTablaFunciones() {
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
	 * Método que, usando la conexión a la base de datos, saca todos los eventos de la base de datos
	 * <b>SQL Statement:</b> SELECT * FROM EVENTOS;
	 * @return Arraylist con los eventos de la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Funcion> darFunciones() throws SQLException, Exception {
		ArrayList<Funcion> funcions = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304B071710.FUNCIONES";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			boolean disp = false;
			if(Integer.parseInt(rs.getString("DISPONIBILIDAD"))==1) disp=true;
			int id = Integer.parseInt(rs.getString("ID"));
			Date fecha = rs.getDate("FECHA");
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Timestamp hora = rs.getTimestamp("HORA");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			String estado = rs.getString("ESTADO");
			Funcion eventoNuevo = new Funcion(id, fecha, id_espectaculo, hora, id_sitio,estado);
			eventoNuevo.setDisponibilidad(disp);
			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
			funcions.add(eventoNuevo);
		}
		return funcions;
	}


	/**
	 * Método que busca al evento con el nombre que entra como parámetro.
	 * @param nombre - Nombre a buscar
	 * @return ArrayList con los eventos
	 * @throws SQLException - Cualquier error que la base de datos arroje.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public ArrayList<Funcion> buscarEventoPorEspectaculo(int espectaculo) throws SQLException, Exception {
		ArrayList<Funcion> funcions = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304B071710.FUNCIONES WHERE ID_ESPECTACULO ='" + espectaculo + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = Integer.parseInt(rs.getString("ID"));
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Date fecha = rs.getDate("FECHA");
			Timestamp hora = rs.getTimestamp("HORA");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			String estado = rs.getString("ESTADO");
			Funcion eventoNuevo = new Funcion(id, fecha, id_espectaculo, hora, id_sitio,estado);
			eventoNuevo.setDisponibilidad(Boolean.parseBoolean(rs.getString("DISPONIBILIDAD")));
			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
			funcions.add(eventoNuevo);
		}

		return funcions;
	}

	/**
	 * Método que agrega el evento que entra como parámetro a la base de datos.
	 * @param funcion
	 * <b> post: </b> se ha agregado a la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que baje  a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo agregar el video a la base de datos
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void addEvento(Funcion funcion) throws SQLException, Exception {

		int resp = 0;
		if(funcion.isDisponibilidad()) resp=1;
		String sql = "INSERT INTO ISIS2304B071710.FUNCIONES VALUES (";
		sql += funcion.getId() + ",";
		sql += "(TO_DATE('"+funcion.getFecha()+"', 'YYYY-MM-DD'))" + ",";
		sql += funcion.getId_espectaculo() + ",";
		sql += funcion.getId_sitio() + ",";
		sql += resp + ",";
		sql += funcion.getGanancias() + ",'";
		sql += funcion.getHora() + "','";
		sql += funcion.getEstado() + "')";

		System.out.println("SQL stmt:" + sql);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}
	
	/**
	 * Método que actualiza el evento que entra como parámetro en la base de datos.
	 * @param funcion
	 * <b> post: </b> se ha actualizado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void updateEvento(Funcion funcion) throws SQLException, Exception {


		String hr = funcion.getHora().toString().substring(0, 18);
		
		String sql = "UPDATE ISIS2304B071710.FUNCIONES SET ";
		
		int disp = 0;
		if(funcion.isDisponibilidad()) disp=1;
		sql += "fecha = (TO_DATE('"+funcion.getFecha()+"', 'YYYY-MM-DD'))" + ",";
		sql += "id_espectaculo =" + funcion.getId_espectaculo();
		sql += ",disponibilidad =" + disp;
		sql += ",ganancias =" + funcion.getGanancias();
		sql += ",hora ='" + hr;
		sql += "',id_sitio =" + funcion.getId_sitio();
		sql += " WHERE id = " + funcion.getId();

		
		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Método que elimina el evento que entra como parámetro en la base de datos.
	 * @param funcion
	 * <b> post: </b> se ha borrado en la base de datos en la transaction actual. pendiente que el video master
	 * haga commit para que los cambios bajen a la base de datos.
	 * @throws SQLException - Cualquier error que la base de datos arroje. No pudo actualizar el video.
	 * @throws Exception - Cualquier error que no corresponda a la base de datos
	 */
	public void deleteFuncion(int id) throws SQLException, Exception {

		String sql = "DELETE FROM ISIS2304B071710.FUNCIONES";
		sql += " WHERE id = " + id;

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public Funcion buscarFuncionPorId(int id) throws SQLException {
	
		Funcion funcion = null;

		String sql = "SELECT * FROM ISIS2304B071710.FUNCIONES WHERE ID ='" + id + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			boolean disp = true;
			if(rs.getString("ESTADO")==Funcion.ESTADO_REALIZADA) disp=false;
			int id2 = Integer.parseInt(rs.getString("ID"));
			Date fecha = rs.getDate("FECHA");
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			Timestamp hora = rs.getTimestamp("HORA");
			String estado = rs.getString("ESTADO");	
			funcion = new Funcion(id2, fecha, id_espectaculo, hora, id_sitio,estado);
			funcion.setDisponibilidad(disp);
			funcion.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
		}

		return funcion;
	}
	
	
	public ArrayList<Funcion> buscarEventoPorFecha(Date fecha) throws SQLException, Exception {
		ArrayList<Funcion> funcions = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304MO11620.EVENTOS WHERE FECHA ='" + fecha + "'";

		System.out.println("SQL stmt:" + sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			int id = Integer.parseInt(rs.getString("ID"));
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Date fecha2 = rs.getDate("FECHA");
			Date hora = rs.getDate("HORA");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			
			//Funcion eventoNuevo = new Funcion(id, fecha2, id_espectaculo, hora, id_sitio);
//			eventoNuevo.setDisponibilidad(Boolean.parseBoolean(rs.getString("DISPONIBILIDAD")));
//			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
//			eventos.add(eventoNuevo);
		}

		return funcions;
	}
	
	public ArrayList<Funcion> buscarEventoPorCompania(int compania) throws SQLException, Exception {
		ArrayList<Funcion> funcions = new ArrayList<Funcion>();

		DAOTablaFunciones lista = new DAOTablaFunciones();
		
		DAOTablaRepresentantes daoRep = new DAOTablaRepresentantes();
		DAOTablaEspectaculo daoEsp = new DAOTablaEspectaculo();
		Representante r = daoRep.buscarRepresentantePorCompania(compania);
		ArrayList<Espectaculo> es = daoEsp.buscarEspectaculoPorRepresentante(r.getId());
		
		
		for(Espectaculo e : es)
		{
			for(Funcion funcion : lista.darFunciones())
			if(e.getId()== funcion.getId_espectaculo() ){
			 funcions.add(funcion);	
			}
		}
		

		return funcions;
		
	}
	
	public double reporteFuncionTotal(int id) throws SQLException
	{
		Funcion funcion = buscarFuncionPorId(id);
		return funcion.getGanancias();
	}
	
	public double reporteFuncionLocalidad(int id_evento, String localidad) throws SQLException, Exception
	{
		
		double ganancias = 0.0;
		DAOTablaSitios daoSitio = new DAOTablaSitios();
		DAOTablaSillas daoSilla = new DAOTablaSillas();
		
		Funcion funcion = buscarFuncionPorId(id_evento);
		Sitio sitio = daoSitio.buscarSitioPorId(funcion.getId_sitio());
		
		
		ArrayList<Silla> sillas = daoSilla.buscarSillasPorLocalidad(localidad);

    	  for(Silla s : sillas){
    		  if(s.getId_sitio() == sitio.getId())
    		  {
    			 if(s.isEstaReservada())
    			 {
    				 ganancias = ganancias + s.getCosto();
    			 }
    		  }	
    	  }
			return ganancias;	
	}
	
	public double reporteFuncionEspectador(int id_evento, int id_espectador) throws SQLException, Exception
	{
		double ganancias = 0.0;
		DAOTablaReservas daoEspectador = new DAOTablaReservas();
		DAOTablaSillas daoSilla = new DAOTablaSillas();
		
		ArrayList<Reserva> cliente = daoEspectador.buscarReservaPorEspectador(id_espectador);
		
    	  for(Reserva s : cliente){
    		  
//    		  if(s.getId_evento()== id_evento){
//    		  Silla silla = daoSilla.buscarSillasPorNumero(s.getNumero_silla());
//    		  ganancias = ganancias + silla.getCosto();
//    		 }
    		  
    	  }
			return ganancias;	
	}
	
	public ArrayList<Funcion> darFuncionesRealizadasEspectador(int id) throws NumberFormatException, SQLException {

		System.out.println("LLEGA Y DA LAS FUNCIONES REALIZADAS DEL ESPECTADOR");
		ArrayList<Funcion> funciones = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304B071710.FUNCIONES fun "
				+"INNER JOIN ISIS2304B071710.RESERVAS res ON fun.ID=res.ID_FUNCION "
				+"INNER JOIN ISIS2304B071710.ESPECTADORES esp ON res.ID_ESPECTADOR=esp.ID "
				+"WHERE fun.ESTADO='ESTADO_REALIZADA' AND esp.ESTAREGISTRADO=1 AND esp.ID='"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			boolean disp = false;
			if(Integer.parseInt(rs.getString("DISPONIBILIDAD"))==1) disp=true;
			int id2 = Integer.parseInt(rs.getString("ID"));
			Date fecha = rs.getDate("FECHA");
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Timestamp hora = rs.getTimestamp("HORA");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			String estado = rs.getString("ESTADO");
			Funcion eventoNuevo = new Funcion(id2, fecha, id_espectaculo, hora, id_sitio,estado);
			eventoNuevo.setDisponibilidad(disp);
			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
			funciones.add(eventoNuevo);
		}
		return funciones;
	}

	public ArrayList<Funcion> darFuncionesEnCursoEspectador(int id) throws NumberFormatException, SQLException {

		System.out.println("LLEGA Y DA LAS FUNCIONES EN CURSO DEL ESPECTADOR");
		
		ArrayList<Funcion> funciones = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304B071710.FUNCIONES fun "
				+"INNER JOIN ISIS2304B071710.RESERVAS res ON fun.ID=res.ID_FUNCION "
				+"INNER JOIN ISIS2304B071710.ESPECTADORES esp ON res.ID_ESPECTADOR=esp.ID "
				+"WHERE fun.ESTADO='ESTADO_ENCURSO' AND esp.ESTAREGISTRADO=1 AND esp.ID='"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			boolean disp = false;
			if(Integer.parseInt(rs.getString("DISPONIBILIDAD"))==1) disp=true;
			int id2 = Integer.parseInt(rs.getString("ID"));
			Date fecha = rs.getDate("FECHA");
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Timestamp hora = rs.getTimestamp("HORA");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			String estado = rs.getString("ESTADO");
			Funcion eventoNuevo = new Funcion(id2, fecha, id_espectaculo, hora, id_sitio,estado);
			eventoNuevo.setDisponibilidad(disp);
			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
			funciones.add(eventoNuevo);
		}
		return funciones;
	}
	
	public ArrayList<Funcion> darFuncionesPrevistasEspectador(int id) throws NumberFormatException, SQLException {

		System.out.println("LLEGA Y DA LAS FUNCIONES PREVISTAS DEL ESPECTADOR");
		ArrayList<Funcion> funciones = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304B071710.FUNCIONES fun "
				+"INNER JOIN ISIS2304B071710.RESERVAS res ON fun.ID=res.ID_FUNCION "
				+"INNER JOIN ISIS2304B071710.ESPECTADORES esp ON res.ID_ESPECTADOR=esp.ID "
				+"WHERE fun.ESTADO='ESTADO_PREVISTAS' AND esp.ESTAREGISTRADO=1 AND esp.ID='"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			boolean disp = false;
			if(Integer.parseInt(rs.getString("DISPONIBILIDAD"))==1) disp=true;
			int id2 = Integer.parseInt(rs.getString("ID"));
			Date fecha = rs.getDate("FECHA");
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Timestamp hora = rs.getTimestamp("HORA");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			String estado = rs.getString("ESTADO");
			Funcion eventoNuevo = new Funcion(id2, fecha, id_espectaculo, hora, id_sitio,estado);
			eventoNuevo.setDisponibilidad(disp);
			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
			funciones.add(eventoNuevo);
		}
		return funciones;
	}
	
	public ArrayList<Funcion> darFuncionesCanceladas(int id) throws SQLException{

		System.out.println("LLEGA Y DA LAS FUNCIONES CANCELADAS");
		ArrayList<Funcion> funciones = new ArrayList<Funcion>();

		String sql = "SELECT * FROM ISIS2304B071710.FUNCIONES fun "
				+"INNER JOIN ISIS2304B071710.RESERVAS res ON fun.ID=res.ID_FUNCION "
				+"INNER JOIN ISIS2304B071710.SILLAS silla ON res.NUMEROSILLA=silla.NUMERO "
				+"INNER JOIN ISIS2304B071710.ESPECTADORES esp ON res.ID_ESPECTADOR=esp.ID "
				+"WHERE esp.ESTAREGISTRADO=1 AND res.ESTADOACTIVE='ESTADO_CANCELADA' AND esp.ID='"+id+"'";

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			
			boolean disp = false;
			if(Integer.parseInt(rs.getString("DISPONIBILIDAD"))==1) disp=true;
			int id2 = Integer.parseInt(rs.getString("ID"));
			Date fecha = rs.getDate("FECHA");
			int id_espectaculo = Integer.parseInt(rs.getString("ID_ESPECTACULO"));
			Timestamp hora = rs.getTimestamp("HORA");
			int id_sitio = Integer.parseInt(rs.getString("ID_SITIO"));
			String estado = rs.getString("ESTADO");
			Funcion eventoNuevo = new Funcion(id2, fecha, id_espectaculo, hora, id_sitio,estado);
			eventoNuevo.setDisponibilidad(disp);
			eventoNuevo.setGanancias(Double.parseDouble(rs.getString("GANANCIAS")));
			funciones.add(eventoNuevo);
		}
		return funciones;
	}
}
