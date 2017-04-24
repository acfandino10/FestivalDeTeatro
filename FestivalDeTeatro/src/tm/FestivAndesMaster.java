package tm;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Date;

import dao.DAOTablaBancos;
import dao.DAOTablaCategorias;
import dao.DAOTablaCompanias;
import dao.DAOTablaEspectaculo;
import dao.DAOTablaEspectadores;
import dao.DAOTablaFunciones;
import dao.DAOTablaPreferencias;
import dao.DAOTablaRepresentantes;
import dao.DAOTablaReservas;
import dao.DAOTablaSillas;
import dao.DAOTablaSitios;
import dao.DAOTablaUsuarios;
import vos.Banco;
import vos.Categoria;
import vos.Compania;
import vos.Espectaculo;
import vos.Espectador;
import vos.Funcion;
import vos.NotaDebito;
import vos.Preferencia;
import vos.Representante;
import vos.Reserva;
import vos.Silla;
import vos.Sitio;
import vos.Usuario;

public class FestivAndesMaster {

	/**
	 * Atributo estático que contiene el path relativo del archivo que tiene los datos de la conexión
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estático que contiene el path absoluto del archivo que tiene los datos de la conexión
	 */
	private  String connectionDataPath;

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;
	
	/**
	 * Conexión a la base de datos
	 */
	private Connection conn;


	/**
	 * Método constructor de la clase, esta clase modela y contiene cada una de las 
	 * transacciones y la logia de negocios que estas conllevan.
	 * <b>post: </b> Se crea el objeto Master, se inicializa el path absoluto de el archivo de conexión y se
	 * inicializa los atributos que se usan par la conexión a la base de datos.
	 * @param contextPathP - path absoluto en el servidor del contexto del deploy actual
	 */
	public FestivAndesMaster(String contextPathP) {
		connectionDataPath = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
		initConnectionData();
	}

	/*
	 * Método que  inicializa los atributos que se usan para la conexion a la base de datos.
	 * <b>post: </b> Se han inicializado los atributos que se usan par la conexión a la base de datos.
	 */
	private void initConnectionData() {
		try {
			File arch = new File(this.connectionDataPath);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(arch);
			prop.load(in);
			in.close();
			this.url = prop.getProperty("url");
			this.user = prop.getProperty("usuario");
			this.password = prop.getProperty("clave");
			this.driver = prop.getProperty("driver");
			Class.forName(driver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método que  retorna la conexión a la base de datos
	 * @return Connection - la conexión a la base de datos
	 * @throws SQLException - Cualquier error que se genere durante la conexión a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("Connecting to: " + url + " With user: " + user);
		return DriverManager.getConnection(url, user, password);
	}	

		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Banco> darBancos() throws Exception {
			ArrayList<Banco> lista;
			DAOTablaBancos dao = new DAOTablaBancos();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darBancos();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param nombre - Nombre del objeto a buscar. name != null
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Banco> buscarBancosPorNombre(String nombre) throws Exception {
			ArrayList<Banco> lista;
			DAOTablaBancos dao = new DAOTablaBancos();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarBancosPorNombre(nombre);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addBanco(Banco objeto) throws Exception {
			DAOTablaBancos dao = new DAOTablaBancos();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addBanco(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addBancos(ArrayList<Banco> objetos) throws Exception {
			DAOTablaBancos dao = new DAOTablaBancos();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Banco objeto : objetos)
					dao.addBanco(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateBanco(Banco objeto) throws Exception {
			DAOTablaBancos dao = new DAOTablaBancos();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateBanco(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteBanco(Banco objeto) throws Exception {
			DAOTablaBancos dao = new DAOTablaBancos();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteBanco(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
	

		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Categoria> darCategorias() throws Exception {
			ArrayList<Categoria> lista;
			DAOTablaCategorias dao = new DAOTablaCategorias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darCategorias();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param nombre - Nombre del objeto a buscar. name != null
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Categoria> buscarCategoriasPorNombre(String nombre) throws Exception {
			ArrayList<Categoria> lista;
			DAOTablaCategorias dao = new DAOTablaCategorias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarCategoriaPorNombre(nombre);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addCategoria(Categoria objeto) throws Exception {
			DAOTablaCategorias dao = new DAOTablaCategorias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addCategoria(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addCategorias(ArrayList<Categoria> objetos) throws Exception {
			DAOTablaCategorias dao = new DAOTablaCategorias();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Categoria objeto : objetos)
					dao.addCategoria(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateCategoria(Categoria objeto) throws Exception {
			DAOTablaCategorias dao = new DAOTablaCategorias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateCategoria(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteCategoria(Categoria objeto) throws Exception {
			DAOTablaCategorias dao = new DAOTablaCategorias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteCategoria(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Compania> darCompanias() throws Exception {
			ArrayList<Compania> lista;
			DAOTablaCompanias dao = new DAOTablaCompanias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darCompanias();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param nombre - Nombre del objeto a buscar. name != null
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Compania> buscarCompaniaPorNombre(String nombre) throws Exception {
			ArrayList<Compania> lista;
			DAOTablaCompanias dao = new DAOTablaCompanias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarCompaniaPorNombre(nombre);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addCompania(Compania objeto) throws Exception {
			DAOTablaCompanias dao = new DAOTablaCompanias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addCompania(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addCompanias(ArrayList<Compania> objetos) throws Exception {
			DAOTablaCompanias dao = new DAOTablaCompanias();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Compania objeto : objetos)
					dao.addCompania(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateCompania(Compania objeto) throws Exception {
			DAOTablaCompanias dao = new DAOTablaCompanias();
			try 
			{
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateCompania(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteCompania(Compania objeto) throws Exception {
			DAOTablaCompanias dao = new DAOTablaCompanias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteCompania(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}



		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Espectador> darEspectadores() throws Exception {
			ArrayList<Espectador> lista;
			DAOTablaEspectadores dao = new DAOTablaEspectadores();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darEspectadores();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param nombre - Nombre del objeto a buscar. name != null
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Espectador> buscarEspectadoresPorNombre(String nombre) throws Exception {
			ArrayList<Espectador> lista;
			DAOTablaEspectadores dao = new DAOTablaEspectadores();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarEspectadorPorNombre(nombre);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addEspectador(Espectador objeto) throws Exception {
			DAOTablaEspectadores dao = new DAOTablaEspectadores();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addEspectador(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addEspectadores(ArrayList<Espectador> objetos) throws Exception {
			DAOTablaEspectadores dao = new DAOTablaEspectadores();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Espectador objeto : objetos)
					dao.addEspectador(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateEspectador(Espectador objeto) throws Exception {
			DAOTablaEspectadores dao = new DAOTablaEspectadores();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateEspectador(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteEspectador(Espectador objeto) throws Exception {
			DAOTablaEspectadores dao = new DAOTablaEspectadores();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteEspectador(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		
		
		public double reporteEspectaculoTotal(int id) throws SQLException {
			
	    	 double espectaculo = 0.0;
	    	
				DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					espectaculo = dao.reporteEspectaculoTotal(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return espectaculo;
			}
	     
	     
	     public double reporteEspectaculoSitio(int id, int sitio) throws Exception {
				
	    	 double evento = 0.0;
	    	
				DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					evento = dao.reporteEspectaculoSitio(id, sitio);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return evento;
			}
	     
	     public double reporteEspectaculoEspectador(int id_espectaculo, int id_espectador) throws Exception {
				
	    	 double es = 0.0;
	    	
				DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					es = dao.reporteEspectaculoEspectador(id_espectaculo, id_espectador);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return es;
			}
		
		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Funcion> darFunciones() throws Exception {
			ArrayList<Funcion> lista;
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darFunciones();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param nombre - Nombre del objeto a buscar. name != null
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Funcion> buscarEventosPorEspectaculo(int espectaculo) throws Exception {
			ArrayList<Funcion> lista;
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarEventoPorEspectaculo(espectaculo);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		
		public ArrayList<Funcion> buscarEventosPorFecha(Date fecha) throws Exception {
			ArrayList<Funcion> lista=null;
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				//lista = dao.buscarEventoPorFecha(fecha);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		
		public ArrayList<Funcion> buscarEventosPorCompania(int id_compania) throws Exception {
			ArrayList<Funcion> lista;
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarEventoPorCompania(id_compania);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addEvento(Funcion objeto) throws Exception {
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addEvento(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addEventos(ArrayList<Funcion> objetos) throws Exception {
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Funcion objeto : objetos)
					dao.addEvento(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateEvento(Funcion objeto) throws Exception {
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateEvento(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteFuncion(int id) throws Exception {
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteFuncion(id);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		
     public Funcion buscarFuncionPorId(int id, boolean conectado) throws SQLException {
			
    	 Funcion funcion = null;
    	
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				if(!conectado){
				this.conn = darConexion();
				}
				dao.setConn(conn);
				funcion = dao.buscarFuncionPorId(id);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {

					dao.cerrarRecursos();
					if(!conectado){
					if(this.conn!=null)
						this.conn.close();
					}
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return funcion;
		}
		
     
     public double reporteFuncionTotal(int id) throws SQLException {
			
    	 double evento = 0.0;
    	
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				evento = dao.reporteFuncionTotal(id);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return evento;
		}
     
     
     public double reporteFuncionLocalidad(int id, String localidad) throws Exception {
			
    	 double evento = 0.0;
    	
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				evento = dao.reporteFuncionLocalidad(id, localidad);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return evento;
		}
     
     public double reporteFuncionEspectador(int id_evento, int id_espectador) throws Exception {
			
    	 double evento = 0.0;
    	
			DAOTablaFunciones dao = new DAOTablaFunciones();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				evento = dao.reporteFuncionEspectador(id_evento, id_espectador);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return evento;
		}
     
		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Representante> darRepresentantes() throws Exception {
			ArrayList<Representante> lista;
			DAOTablaRepresentantes dao = new DAOTablaRepresentantes();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darRepresentantes();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param nombre - Nombre del objeto a buscar. name != null
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Representante> buscarRepresentantesPorNombre(String nombre) throws Exception {
			ArrayList<Representante> lista;
			DAOTablaRepresentantes dao = new DAOTablaRepresentantes();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarRepresentantesPorNombre(nombre);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addRepresentante(Representante objeto) throws Exception {
			DAOTablaRepresentantes dao = new DAOTablaRepresentantes();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addRepresentante(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addRepresentantes(ArrayList<Representante> objetos) throws Exception {
			DAOTablaRepresentantes dao = new DAOTablaRepresentantes();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Representante objeto : objetos)
					dao.addRepresentante(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateRepresentante(Representante objeto) throws Exception {
			DAOTablaRepresentantes dao = new DAOTablaRepresentantes();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateRepresentante(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteRepresentante(Representante objeto) throws Exception {
			DAOTablaRepresentantes dao = new DAOTablaRepresentantes();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteRepresentante(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
	
		
	

		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Silla> darSillas() throws Exception {
			ArrayList<Silla> lista;
			DAOTablaSillas dao = new DAOTablaSillas();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darSillas();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param nombre - Nombre del objeto a buscar. name != null
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Silla> buscarSillasPorLocalidad(String localidad) throws Exception {
			ArrayList<Silla> lista;
			DAOTablaSillas dao = new DAOTablaSillas();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarSillasPorLocalidad(localidad);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		public Silla buscarSillasPorNumero(int numero) throws Exception {
			Silla l = null;
			DAOTablaSillas dao = new DAOTablaSillas();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				l = dao.buscarSillasPorNumero(numero);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return l;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addSilla(Silla objeto) throws Exception {
			DAOTablaSillas dao = new DAOTablaSillas();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addSilla(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addSillas(ArrayList<Silla> objetos) throws Exception {
			DAOTablaSillas dao = new DAOTablaSillas();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Silla objeto : objetos)
					dao.addSilla(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateSilla(Silla objeto) throws Exception {
			DAOTablaSillas dao = new DAOTablaSillas();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateSilla(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteSilla(Silla objeto) throws Exception {
			DAOTablaSillas dao = new DAOTablaSillas();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteSilla(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
	
	
		/**
		 * Método que modela la transacción que retorna todos los objetos de la base de datos.
		 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Sitio> darSitios() throws Exception {
			ArrayList<Sitio> lista;
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darSitios();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		/**
		 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
		 * @param ciudad -  del objeto a buscar.
		 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
		 * @throws Exception -  cualquier error que se genere durante la transacción
		 */
		public ArrayList<Sitio> buscarSitiosPorCiudad(String ciudad) throws Exception {
			ArrayList<Sitio> lista;
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarSitiosPorCiudad(ciudad);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		public Sitio buscarSitiosPorId(int id) throws Exception {
			Sitio lista = null;
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarSitioPorId(id);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		public String consultarSitios(int id_sitio) throws Exception {
			String lista = null;
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.consultarSitio(id_sitio);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		
		/**
		 * Método que modela la transacción que agrega un solo objeto a la base de datos.
		 * <b> post: </b> se ha agregado el objeto que entra como parámetro
		 * @param objeto - el objeto  a agregar. objeto != null
		 * @throws Exception - cualquier error que se genera agregando el objeto
		 */
		public void addSitio(Sitio objeto) throws Exception {
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addSitio(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
		 * <b> post: </b> se han agregado los objetos que entran como parámetro
		 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
		 * @throws Exception - cualquier error que se genera agregando los objetos
		 */
		public void addSitios(ArrayList<Sitio> objetos) throws Exception {
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Sitio objeto : objetos)
					dao.addSitio(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		/**
		 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
		 * @param objeto -  a actualizar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los objetos
		 */
		public void updateSitio(Sitio objeto) throws Exception {
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updateSitio(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

		/**
		 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
		 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
		 * @param objeto - a eliminar. objeto != null
		 * @throws Exception - cualquier error que se genera actualizando los videos
		 */
		public void deleteSitio(Sitio objeto) throws Exception {
			DAOTablaSitios dao = new DAOTablaSitios();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deleteSitio(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
	
		
		public ArrayList<Preferencia> darPreferencias() throws Exception {
			ArrayList<Preferencia> lista;
			DAOTablaPreferencias dao = new DAOTablaPreferencias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.darPreferencias();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}

		
		
		public ArrayList<Preferencia> buscarPreferenciasPorIdEspectador(int id) throws Exception {
			ArrayList<Preferencia> lista;
			DAOTablaPreferencias dao = new DAOTablaPreferencias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				lista = dao.buscarPreferenciaPorIdEspectador(id);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
			return lista;
		}
		

		
		public void addPreferencia(Preferencia objeto) throws Exception {
			DAOTablaPreferencias dao = new DAOTablaPreferencias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.addPreferencia(objeto);
				conn.commit();

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		
		
		
		
		public void addPreferencias(ArrayList<Preferencia> objetos) throws Exception {
			DAOTablaPreferencias dao = new DAOTablaPreferencias();
			try 
			{
				//////Transacción - ACID Example
				this.conn = darConexion();
				conn.setAutoCommit(false);
				dao.setConn(conn);
				for(Preferencia objeto : objetos)
					dao.addPreferencia(objeto);
				conn.commit();
			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				conn.rollback();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}
		
		
		
		
		public void updatePreferencia(Preferencia objeto) throws Exception {
			DAOTablaPreferencias dao = new DAOTablaPreferencias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.updatePreferencia(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

	
		
		public void deletePreferencia(Preferencia objeto) throws Exception {
			DAOTablaPreferencias dao = new DAOTablaPreferencias();
			try 
			{
				//////Transacción
				this.conn = darConexion();
				dao.setConn(conn);
				dao.deletePreferencia(objeto);

			} catch (SQLException e) {
				System.err.println("SQLException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.err.println("GeneralException:" + e.getMessage());
				e.printStackTrace();
				throw e;
			} finally {
				try {
					dao.cerrarRecursos();
					if(this.conn!=null)
						this.conn.close();
				} catch (SQLException exception) {
					System.err.println("SQLException closing resources:" + exception.getMessage());
					exception.printStackTrace();
					throw exception;
				}
			}
		}

			
				public ArrayList<Reserva> darReservas() throws Exception {
					ArrayList<Reserva> lista;
					DAOTablaReservas dao = new DAOTablaReservas();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						lista = dao.darReservas();

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return lista;
				}

				/**
				 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
				 * @param nombre - Nombre del objeto a buscar. name != null
				 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
				 * @throws Exception -  cualquier error que se genere durante la transacción
				 */
				public ArrayList<Reserva> buscarReservasPorFuncion(int id) throws Exception {
					ArrayList<Reserva> lista;
					DAOTablaReservas dao = new DAOTablaReservas();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						lista = dao.buscarReservaPorFuncion(id);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return lista;
				}
	//TODO			
				/**
				 * Método que modela la transacción que agrega un solo objeto a la base de datos.
				 * <b> post: </b> se ha agregado el objeto que entra como parámetro
				 * @param objeto - el objeto  a agregar. objeto != null
				 * @throws Exception - cualquier error que se genera agregando el objeto
				 */
				public Reserva addReserva(Reserva objeto) throws Exception {
					System.out.println("COMEINZA METODO ADD RESERVA EN EL MASTER");
					DAOTablaReservas dao = new DAOTablaReservas();
					Reserva reserva=objeto;
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						System.out.println("LA CONEXION FUE EXITOSA----------------");
												
						Funcion funcion = buscarFuncionPorId(objeto.getIdFuncion(),true);
						System.out.println("ENCONTRO LA FUNCION QUE BUSCABA: "+funcion.toString());
							if(funcion.isDisponibilidad())
							{
								System.out.println("LA FUNCION ES DISPONIBLE Y POR LO TANTO SE COMIENZA A AGREGAR LA RESERVA");
								dao.addReserva(objeto);
								conn.commit();
								System.out.println("No llega aca poruqe exception");
							   
							}else
							{
								System.out.println("COMIENZA A HACER ROLLBACK");
								conn.rollback();
								throw new Exception("No hay disponibilidad");								
							}
							

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return reserva;
				}
				
				/**
				 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
				 * <b> post: </b> se han agregado los objetos que entran como parámetro
				 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
				 * @throws Exception - cualquier error que se genera agregando los objetos
				 */
				public void addReservas(ArrayList<Reserva> objetos) throws Exception {
					DAOTablaReservas dao = new DAOTablaReservas();
					try 
					{
						//////Transacción - ACID Example
						this.conn = darConexion();
						conn.setAutoCommit(false);
						dao.setConn(conn);
						for(Reserva objeto : objetos)
							dao.addReserva(objeto);
						conn.commit();
					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						conn.rollback();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						conn.rollback();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}
				
				/**
				 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
				 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
				 * @param objeto -  a actualizar. objeto != null
				 * @throws Exception - cualquier error que se genera actualizando los objetos
				 */
				public void updateReserva(Reserva objeto) throws Exception {
					DAOTablaReservas dao = new DAOTablaReservas();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.updateReserva(objeto);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}

				/**
				 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
				 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
				 * @param objeto - a eliminar. objeto != null
				 * @throws Exception - cualquier error que se genera actualizando los videos
				 */
				public void deleteReserva(Reserva objeto) throws Exception {
					DAOTablaReservas dao = new DAOTablaReservas();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.deleteReserva(objeto);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}
	
				
				
				/**
				 * Método que modela la transacción que retorna todos los objetos de la base de datos.
				 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
				 * @throws Exception -  cualquier error que se genere durante la transacción
				 */
				public ArrayList<Usuario> darUsuarios() throws Exception {
					ArrayList<Usuario> lista;
					DAOTablaUsuarios dao = new DAOTablaUsuarios();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						lista = dao.darUsuarios();

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return lista;
				}

				/**
				 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
				 * @param nombre - Nombre del objeto a buscar. name != null
				 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
				 * @throws Exception -  cualquier error que se genere durante la transacción
				 */
				public ArrayList<Usuario> buscarUsuariosPorNombre(String nombre) throws Exception {
					ArrayList<Usuario> lista;
					DAOTablaUsuarios dao = new DAOTablaUsuarios();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						lista = dao.buscarUsuariosPorNombre(nombre);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return lista;
				}
				
				/**
				 * Método que modela la transacción que agrega un solo objeto a la base de datos.
				 * <b> post: </b> se ha agregado el objeto que entra como parámetro
				 * @param objeto - el objeto  a agregar. objeto != null
				 * @throws Exception - cualquier error que se genera agregando el objeto
				 */
				public void addUsuario(Usuario objeto) throws Exception {
					DAOTablaUsuarios dao = new DAOTablaUsuarios();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.addUsuario(objeto);
						conn.commit();

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}
				
				/**
				 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
				 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
				 * @param objeto - a eliminar. objeto != null
				 * @throws Exception - cualquier error que se genera actualizando los videos
				 */
				public void deleteEspectaculo(Espectaculo objeto) throws Exception {
					DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.deleteEspectaculo(objeto);
				
					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}

				/**
				 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
				 * <b> post: </b> se han agregado los objetos que entran como parámetro
				 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
				 * @throws Exception - cualquier error que se genera agregando los objetos
				 */
				public void addUsuarios(ArrayList<Usuario> objetos) throws Exception {
					DAOTablaUsuarios dao = new DAOTablaUsuarios();
					try 
					{
						//////Transacción - ACID Example
						this.conn = darConexion();
						conn.setAutoCommit(false);
						dao.setConn(conn);
						for(Usuario objeto : objetos)
							dao.addUsuario(objeto);
						conn.commit();
					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						conn.rollback();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						conn.rollback();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}
				
				/**
				 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
				 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
				 * @param objeto -  a actualizar. objeto != null
				 * @throws Exception - cualquier error que se genera actualizando los objetos
				 */
				public void updateUsuario(Usuario objeto) throws Exception {
					DAOTablaUsuarios dao = new DAOTablaUsuarios();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.updateUsuario(objeto);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}

				/**
				 * Método que modela la transacción que elimina el objeto que entra como parámetro a la base de datos.
				 * <b> post: </b> se ha eliminado el objeto que entra como parámetro
				 * @param objeto - a eliminar. objeto != null
				 * @throws Exception - cualquier error que se genera actualizando los videos
				 */
				public void deleteUsuario(Usuario objeto) throws Exception {
					DAOTablaUsuarios dao = new DAOTablaUsuarios();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.deleteUsuario(objeto);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}
				
				public Representante buscarRepresentantesPorId(int id) throws Exception {
				
					DAOTablaRepresentantes dao = new DAOTablaRepresentantes();
					Representante u = null;
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						u = dao.buscarRepresentantePorId(id);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return u;
				}

				public Usuario buscarUsuariosPorId(int id) throws Exception {
				
					DAOTablaUsuarios dao = new DAOTablaUsuarios();
					Usuario u = null;
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						u = dao.buscarUsuariosPorId(id);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return u;
				}

				
				/**
				 * Método que modela la transacción que retorna todos los objetos de la base de datos.
				 * @return Lista - objeto que modela  un arreglo de objetos. este arreglo contiene el resultado de la búsqueda
				 * @throws Exception -  cualquier error que se genere durante la transacción
				 */
				public ArrayList<Espectaculo> darEspectaculos() throws Exception {
					ArrayList<Espectaculo> lista;
					DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						lista = dao.darEspectaculos();

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return lista;
				}

				/**
				 * Método que modela la transacción que busca el/los objetos en la base de datos con el nombre entra como parámetro.
				 * @param nombre - Nombre del objeto a buscar. name != null
				 * @return Lista - objeto que modela  un arreglo. este arreglo contiene el resultado de la búsqueda
				 * @throws Exception -  cualquier error que se genere durante la transacción
				 */
				public ArrayList<Espectaculo> buscarEspectaculoPorNombre(String espectaculo) throws Exception {
					ArrayList<Espectaculo> lista;
					DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						lista = dao.buscarEspectaculoPorNombre(espectaculo);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return lista;
				}
				
				/**
				 * Método que modela la transacción que agrega un solo objeto a la base de datos.
				 * <b> post: </b> se ha agregado el objeto que entra como parámetro
				 * @param objeto - el objeto  a agregar. objeto != null
				 * @throws Exception - cualquier error que se genera agregando el objeto
				 */
				public void addEspectaculo(Espectaculo objeto) throws Exception {
					DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.addEspectaculo(objeto);
						conn.commit();

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}
				
				/**
				 * Método que modela la transacción que agrega los objetos que entran como parámetro a la base de datos.
				 * <b> post: </b> se han agregado los objetos que entran como parámetro
				 * @param objetos - objeto que modela una lista y se pretenden agregar. objetos != null
				 * @throws Exception - cualquier error que se genera agregando los objetos
				 */
				public void addEspectaculos(ArrayList<Espectaculo> objetos) throws Exception {
					DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
					try 
					{
						//////Transacción - ACID Example
						this.conn = darConexion();
						conn.setAutoCommit(false);
						dao.setConn(conn);
						for(Espectaculo objeto : objetos)
							dao.addEspectaculo(objeto);
						conn.commit();
					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						conn.rollback();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						conn.rollback();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}
				
				/**
				 * Método que modela la transacción que actualiza el objeto que entra como parámetro a la base de datos.
				 * <b> post: </b> se ha actualizado el objeto que entra como parámetro
				 * @param objeto -  a actualizar. objeto != null
				 * @throws Exception - cualquier error que se genera actualizando los objetos
				 */
				public void updateEspectaculo(Espectaculo objeto) throws Exception {
					DAOTablaEspectaculo dao = new DAOTablaEspectaculo();							;
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						dao.updateEspectaculo(objeto);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
				}

				public Espectaculo buscarEspectaculoPorId(int id) throws SQLException {
					
		    	 Espectaculo es = null;
		    	
					DAOTablaEspectaculo dao = new DAOTablaEspectaculo();
					try 
					{
						//////Transacción
						this.conn = darConexion();
						dao.setConn(conn);
						es = dao.buscarEspectaculoPorId(id);

					} catch (SQLException e) {
						System.err.println("SQLException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} catch (Exception e) {
						System.err.println("GeneralException:" + e.getMessage());
						e.printStackTrace();
						throw e;
					} finally {
						try {
							dao.cerrarRecursos();
							if(this.conn!=null)
								this.conn.close();
						} catch (SQLException exception) {
							System.err.println("SQLException closing resources:" + exception.getMessage());
							exception.printStackTrace();
							throw exception;
						}
					}
					return es;
				}
			
			
			public Espectador buscarEspectadorPorId(int id) throws Exception {
				Espectador buscado=null;
				DAOTablaEspectadores dao = new DAOTablaEspectadores();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					buscado = dao.buscarEspectadorPorId(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return buscado;
			}
				
		     
		     
			public void addAbono(int silla) throws Exception {
				DAOTablaReservas dao = new DAOTablaReservas();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					//dao.addAbono(silla);
					conn.commit();

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
			}
			
			
			public void deleteAbono(int silla) throws Exception {
				DAOTablaReservas dao = new DAOTablaReservas();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					//dao.deleteAbono(silla);
					conn.commit();

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
			}

			public int darAsistenciaTotalPorCompania(int id) throws SQLException {
				int ans;
				DAOTablaCompanias dao = new DAOTablaCompanias();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					ans = dao.darAsistenciaTotalPorCompania(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return ans;
				
			}
			
			public int darAsistenciaRegistradosPorCompania(int id) throws SQLException {
				int ans;
				DAOTablaCompanias dao = new DAOTablaCompanias();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					ans = dao.darAsistenciaRegistradosPorCompania(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return ans;
				
			}

			public double darGananciaPorCompania(int id) throws SQLException {
				double ans;
				DAOTablaCompanias dao = new DAOTablaCompanias();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					ans = dao.darGananciaPorCompania(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return ans;
			}

			public ArrayList<Sitio> darSitiosPorCompania(int id) throws SQLException {
				ArrayList<Sitio> lista;
				DAOTablaSitios dao = new DAOTablaSitios();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darSitiosPorCompania(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}

			public ArrayList<Funcion> darFuncionesRealizadasEspectador(int id) throws SQLException {
				ArrayList<Funcion> lista;
				DAOTablaFunciones dao = new DAOTablaFunciones();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darFuncionesRealizadasEspectador(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}

			public ArrayList<Funcion> darFuncionesEnCursoEspectador(int id) throws SQLException {
				ArrayList<Funcion> lista;
				DAOTablaFunciones dao = new DAOTablaFunciones();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darFuncionesEnCursoEspectador(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}
			
			public ArrayList<Funcion> darFuncionesPrevistasEspectador(int id) throws SQLException {
				ArrayList<Funcion> lista;
				DAOTablaFunciones dao = new DAOTablaFunciones();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darFuncionesPrevistasEspectador(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}

			public ArrayList<Silla> darSillasCanceladas(int idEspectador, int idFuncion) throws SQLException {
				ArrayList<Silla> lista;
				DAOTablaSillas dao = new DAOTablaSillas();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darSillasCanceladas(idEspectador, idFuncion);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}

			public ArrayList<Funcion> darFuncionesCanceladas(int id) throws SQLException {
				ArrayList<Funcion> lista;
				DAOTablaFunciones dao = new DAOTablaFunciones();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darFuncionesCanceladas(id);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}

			public ArrayList<Espectador> darEspectadoresDeFuncionYDevolverCosto(int idFuncion) throws SQLException {
				ArrayList<Espectador> lista;
				DAOTablaEspectadores dao = new DAOTablaEspectadores();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darEspectadoresDeFuncionYDevolverCosto(idFuncion);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}

			public ArrayList<Reserva> darReservasFuncionYCancelarlas(int idFuncion) throws SQLException {
				ArrayList<Reserva> lista;
				DAOTablaReservas dao = new DAOTablaReservas();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.darReservasFuncionYCancelarlas(idFuncion);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}

			public Reserva buscarReservasPorId(int idReserva) throws SQLException {
				Reserva reserva;
				DAOTablaReservas dao = new DAOTablaReservas();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					reserva = dao.darReservasPorId(idReserva);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return reserva;
			}
			public Silla cancelarReserva(int idReserva) throws SQLException,Exception{
				DAOTablaSillas dao = new DAOTablaSillas();
				Silla silla;
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					Reserva res= buscarReservasPorId(idReserva);
					res.setEstado(Reserva.ESTADO_CANCELADA);
					updateReserva(res);
					silla = dao.cancelarFuncionSillaPorReserva(idReserva); 
					if(silla!=null){
						updateSilla(silla);
					}

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return silla;
			}

			public Espectador devolverAbono(int id, int idReserva) throws SQLException, Exception {

				Espectador espectador;
				DAOTablaReservas dao = new DAOTablaReservas();
				DAOTablaEspectadores daoEsp = new DAOTablaEspectadores();
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					Date fechaReserva = dao.darFechaReserva(idReserva);
					daoEsp.setConn(conn);
					long DAY_IN_MS = 1000 * 60 * 60 * 24;
					Date thatDay = new Date(System.currentTimeMillis() + (7 * 3 * DAY_IN_MS));
					
					if(fechaReserva.after(thatDay)){
						
						Silla silla =cancelarReserva(idReserva);
						espectador = buscarEspectadorPorId(id);
						espectador.setSaldo(0.0);
						updateEspectador(espectador);
						
					}else throw new Exception("El plazo para devolver el abono de la boleta es únicamente hasta 3 semanas antes del evento.");

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						daoEsp.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return espectador;
			}

			public NotaDebito devolverBoleta(int idReservaBoleta, int id) throws SQLException,Exception {
				DAOTablaReservas dao = new DAOTablaReservas();
				NotaDebito note;
				Espectador espectador;
				Silla silla;
				try 
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);
					Date fechaReserva = dao.darFechaReserva(idReservaBoleta);
					long DAY_IN_MS = 1000 * 60 * 60 * 24;
					Date thatDay = new Date(System.currentTimeMillis() + (5 * DAY_IN_MS));
					
					if(fechaReserva.after(thatDay)){
						
						silla =cancelarReserva(idReservaBoleta);
						espectador = buscarEspectadorPorId(id);
						espectador.setSaldo(silla.getCosto());
						updateEspectador(espectador);
						note=new NotaDebito(DAY_IN_MS, espectador.getNombre(), new Date(System.currentTimeMillis()),
								"Motivos personales por los cuales el espectador con ID:"+id+" decide devolver la boleta y se cancela la reserva"+
								"cuyo valor era de "+silla.getCosto(), silla.getCosto(), "13%", silla.getCosto()*(100-13)/100);
					}else throw new Exception("El plazo para devolver la boleta es 5 días antes del evento.");


				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return note;
			}		

			public ArrayList<Silla> buscarSillasPorSitioYLocalidad(int id_sitio, String localidad, boolean conectado) throws Exception {
				ArrayList<Silla> lista;
				DAOTablaSillas dao = new DAOTablaSillas();
				try 
				{
					//////Transacción
					if(!conectado) this.conn = darConexion();
					dao.setConn(conn);
					lista = dao.buscarSillasPorSitioYLocalidad(id_sitio, localidad);

				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(!conectado){
						if(this.conn!=null)
							this.conn.close();
					 }
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
				return lista;
			}		
			

			public void registrarComprasBoletas(Reserva objeto, String localidad, int cantSillas) throws Exception {
				System.out.println("---------------Entro al metodo del master-----------");
				DAOTablaReservas dao = new DAOTablaReservas();
				System.out.println("----Creo el dao en el master---------");
				try
				{
					//////Transacción
					this.conn = darConexion();
					dao.setConn(conn);	
					System.out.println("------Conecto dentro del try del master-------");
											
					Funcion funcion = buscarFuncionPorId(objeto.getIdFuncion(),true);
					
					System.out.println("-----Encontro la funcion----");
					ArrayList<Silla> s = buscarSillasPorSitioYLocalidad(funcion.getId_sitio(), localidad, true);
					if(funcion.isDisponibilidad())
						{	
						System.out.println("-------Entro dentro del primer if master ----------");
						   	int sillasReservadas = 0;
						   	int ultimaPosicion = 0;
						   	System.out.println("----Antes del primer for----");
						   	System.out.println("----Y el size a recorrer es " + s.size());
							for(int i=0; i<s.size() && sillasReservadas!=cantSillas; i++){
					System.out.println("----Entro en el forrr------");			
							Silla s1 = s.get(i);
							ultimaPosicion = i;
							
							if(!s1.isEstaReservada()) sillasReservadas++;				
							else sillasReservadas=0;							
						}
							System.out.println("---Salio del for----");
							
							if(sillasReservadas==0) 
							{
								System.out.println("----No hay sillas suficientes----");
								conn.rollback();
								throw new Exception("No hay suficientes sillas contiguas libres en esta localidad");
							}
							else
							{
								System.out.println("----Hubo sillas suficientes----");		
						    dao.addReserva(objeto);
						    System.out.println("-----Agrego la reserva------");
						    int posicionSilla = ultimaPosicion;
						    for(int i = 1; i<cantSillas; i++)
						      {
						    	System.out.println("-----Entro al for para reservar las sillas----");
						    	 Silla s1 = s.get(posicionSilla);		
						    	 s1.setId_reserva(objeto.getId());
						    	 s1.setEstaReservada(true);
						    	 posicionSilla--;
						      }
						    System.out.println("-----Salio del for-----");
							  }
						}						
						else
						{
							conn.rollback();
							throw new Exception("No hay disponibilidad en esa funcion");								
						}											
					conn.commit();
				} catch (SQLException e) {
					System.err.println("SQLException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} catch (Exception e) {
					System.err.println("GeneralException:" + e.getMessage());
					e.printStackTrace();
					throw e;
				} finally {
					try {
						dao.cerrarRecursos();
						if(this.conn!=null)
							this.conn.close();
					} catch (SQLException exception) {
						System.err.println("SQLException closing resources:" + exception.getMessage());
						exception.printStackTrace();
						throw exception;
					}
				}
			}			
			
}
