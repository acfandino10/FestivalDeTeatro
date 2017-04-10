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
import vos.Sitio;
import vos.Usuario;

@Path("sitios")
public class SitiosServices {
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
	

	/**
	 * Método que expone servicio REST usando GET que da todos los objetos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios
	 * @return Json con todos los objetos de la base de datos O json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getSitios() {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Sitio> lista;
		try {
			lista = tm.darSitios();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}
	
	
	//TODO RFC2.
    /**
     * Método que expone servicio REST usando GET que busca el objeto con la ciudad que entra como parámetro
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios/id/"id para la busqueda"
     * @param name - Ciudad del objeto a buscar que entra en la URL como parámetro 
     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
     * el error que se produjo
     * @throws Exception 
     */
	@GET
	@Path("/id/{id}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response consultarSitioId(@javax.ws.rs.PathParam("id") int id) throws Exception {
		System.out.println("COMIENZA.......................");
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		System.out.println("---------------------------");
		String resultado;
		try {
			System.out.println("MMMMMMMMMMMMMMMMMMMMM");
					resultado = tm.consultarSitios(id);
					System.out.println("NNNNNNNNNNNNNNNNNN");
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(resultado).build();
	}
	


}
