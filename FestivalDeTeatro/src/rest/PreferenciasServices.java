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
import oracle.jdbc.proxy.annotation.Pre;
import tm.FestivAndesMaster;
import vos.Preferencia;

@Path("preferencias")
public class PreferenciasServices {


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
				 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/preferencias
				 * @return Json con todos los objetos de la base de datos O json con 
			     * el error que se produjo
				 */
				@GET
				@Produces({ MediaType.APPLICATION_JSON })
				public Response getPreferencia() {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					ArrayList<Preferencia> lista;
					try {
						lista = tm.darPreferencias();
					} catch (Exception e) {
						return Response.status(500).entity(doErrorMessage(e)).build();
					}
					return Response.status(200).entity(lista).build();
				}


}
