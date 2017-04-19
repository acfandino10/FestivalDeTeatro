package rest;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.FestivAndesMaster;
import vos.Silla;

@Path("sillas")
public class SillasServices {
	// Servicios REST tipo GET:


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
	@GET
	@Path("sayHello")
	public String sayHello(@QueryParam("name")String name) {
	System.out.println("aasaaa");
		return "asa: "+name;

	}
	

	/**
	 * Método que expone servicio REST usando GET que da todos los objetos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sillas
	 * @return Json con todos los objetos de la base de datos O json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSillas() {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Silla> lista;
		try {
			lista = tm.darSillas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}


    /**
     * Método que expone servicio REST usando GET que busca el objeto con la localidad que entra como parámetro
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sillas/localidad/"localidad para la busqueda"
     * @param name - Nombre del objeto a buscar que entra en la URL como parámetro 
     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
     * el error que se produjo
     */
	@GET
	@Path("/localidad/{localidad}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSillasLocalidad(@javax.ws.rs.PathParam("name") String name) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Silla> lista;
		try {
			if (name == null || name.length() == 0)
				throw new Exception("Nombre del video no valido");
			lista = tm.buscarSillasPorLocalidad(name);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}
	

    /**
     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sillas/silla
     * @param objeto - a agregar
     * @return Json con el objeto que agrego o Json con el error que se produjo
     */
	@PUT
	@Path("/silla")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSilla(Silla objeto) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		try {
			tm.addSilla(objeto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(objeto).build();
	}
	
    /**
     * Método que expone servicio REST usando PUT que agrega los objetos que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sillas/silla
     * @param objetos - a agregar. 
     * @return Json con el objeto que agrego o Json con el error que se produjo
     */
	@PUT
	@Path("/sillas")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addSillas(ArrayList<Silla> lista) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		try {
			tm.addSillas(lista);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}
	
    /**
     * Método que expone servicio REST usando POST que actualiza el objeto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sillas/silla
     * @param objeto - a actualizar. 
     * @return Json con el objeto que actualizo o Json con el error que se produjo
     */
	@POST
	@Path("/silla")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateSilla(Silla objeto) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		try {
			tm.updateSilla(objeto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(objeto).build();
	}
	
    /**
     * Método que expone servicio REST usando DELETE que actualiza el objeto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/categorias/categoria
     * @param objeto - a aliminar. 
     * @return Json con el objeto que elimino o Json con el error que se produjo
     */
	@DELETE
	@Path("/categoria")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteSilla(Silla objeto) {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		try {
			tm.deleteSilla(objeto);
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(objeto).build();
	}
}
