package rest;


import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import tm.FestivAndesMaster;
import vos.Funcion;
import vos.Usuario;

@Path("eventos")
public class FuncionesServices {
	/**
	 * Atributo que usa la anotación @Context para tener el ServletContext de la conexión actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * Método que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	/**
	 * Método que expone servicio REST usando GET que da todos los objetos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/eventos
	 * @return Json con todos los objetos de la base de datos O json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getEventos() {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Funcion> lista;
		try {
			lista = tm.darEventos();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}

	
	@GET
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response reporteFuncionTotal(@javax.ws.rs.PathParam("id") int evento) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
	   double lista;
	   
			lista = tm.reporteFuncionTotal(evento);
		
		return Response.status(200).entity(lista).build();
	}
	
	//TODO
	/**
	 * RF14
	 * CANCELAR UNA FUNCIÓN
	 * Cancela la realización de una función programada en FestivAndes.
	 * Puede darse, inclusive, si la función ya ha empezado,
	 * pero no si ya terminó.
	 * Cuando se cancela una función,
	 * se debe devolver el dinero de
	 * todos los usuarios registrados que tienen boletas adquiridas.
	 * Esta operación puede ser realizada por un usuario administrador de FestivAndes.
	 * DEBE utilizar como subtransacción la implementación del requerimiento RF12.
	 * @param evento
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response cancelarFuncion(@javax.ws.rs.PathParam("id") int evento) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
	   double lista;
	   
			lista = tm.reporteFuncionTotal(evento);
		
		return Response.status(200).entity(lista).build();
	}
	
	//TODO
	/**
	 * RFC7.
	 * CONSULTAR ASISTENCIA AL FESTIVAL DE UN CLIENTE REGISTRADO
	 * Consulta las funciones a las que asiste un cliente registrado de FestivAndes.
	 * Debe discriminar las funciones ya realizadas o en curso,
	 * las funciones previstas y las boletas devueltas.
	 * Esta operación es realizada por los clientes registrados y por
	 * el usuario administrador de FestivAndes.
	 * Respetando la privacidad de los clientes,
	 * cuando un cliente registrado hace esta consulta obtiene la
	 * información de sus propia actividad, mientras que el administrador
	 * obtiene toda la información de cualquiera de los clientes. Ver RNF1.
	 * @return Asistencia del/los cliente.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAsistenciaClientesRegistrados(@PathParam("id") int id) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Usuario> lista;
		try {
			lista = tm.darUsuarios();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}
	
}
