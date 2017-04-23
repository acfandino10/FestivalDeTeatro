package rest;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import tm.FestivAndesMaster;
import vos.Compania;
import vos.Sitio;
import vos.Usuario;

@Path("companias")
public class CompaniasServices
{
	
	/**
	 * Atributo que usa la anotaciÃ³n @Context para tener el ServletContext de la conexiÃ³n actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * MÃ©todo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}
	
	
	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}
	
	/**
	 * 
	 * @return
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getCompanias() {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Compania> lista;
		try {
			lista = tm.darCompanias();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}
	
	//TODO
	/**
	 * RFC8.
	 * CONSULTAR COMPANÌƒIÌ�A
	 * Muestra la informacioÌ�n relevante a una companÌƒiÌ�a de teatro.
	 * Consolida para cada uno de sus espectaÌ�culos
	 * la asistencia total, la asistencia de clientes registrados,
	 * el dinero generado en la taquilla y porcentaje de ocupacioÌ�n
	 * de sus funciones por cada sitio de realizacioÌ�n.
	 * Esta operacioÌ�n es realizada por los usuarios companÌƒiÌ�a de teatro y el administrador de FestivAndes.
	 * 
	 * NOTA: Respetando la privacidad de los clientes,
	 * cuando un una companÌƒiÌ�a hace esta consulta obtiene la informacioÌ�n de
	 * sus propias funciones, mientras que el administrador obtiene toda
	 * la informacioÌ�n. Ver RNF1.
	 * @return JSON
	 * @throws Exception 
	 */
	@GET
	@Path("consultar")
	@Produces({ MediaType.APPLICATION_JSON})
	public Response consultarCompania(@QueryParam("id") int id) throws Exception{
		
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		String rol=tm.buscarUsuariosPorId(id).getRol();
		System.out.println("EL ROL QUE TENEMOS EN ESTE CASO ES: "+rol);
		if(rol.compareTo(Usuario.ROL_ESPECTADOR)==0) throw new Exception("El usuario no es un usuario compaÃ±ia, por lo tanto, no se le permite ver el contenido de esta compaÃ±ia");
		else if(rol.compareTo(Usuario.ROL_COMPANIA)==0){
			System.out.println("ENTRA A ROL COMPANIA");
			return consultarCompaniasPorUnaCompania(id);
		}
		else if(rol.compareTo(Usuario.ROL_ADMINISTRADOR)==0){
			System.out.println("ENTRA A ROL ADMINISTRADOR");
			return consultarCompaniasPorAdmin();
		}
		else return null;
		
	}
	
	public Response consultarCompaniasPorUnaCompania(int id){
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		JsonObjectBuilder object = Json.createObjectBuilder();
		try {
				int asistencia = tm.darAsistenciaTotalPorCompania(id);

				object.add("asistenciaTotal", asistencia);
				object.add("asistenciaRegistrados", tm.darAsistenciaRegistradosPorCompania(id));
				object.add("dineroGeneradoTaquilla", tm.darGananciaPorCompania(id));
				JsonObjectBuilder arr = Json.createObjectBuilder();
				for(Sitio sitio: tm.darSitiosPorCompania(id)){
					arr.add("sitioId", sitio.getId());
					arr.add("sitioCiudad", sitio.getCiudad());
					arr.add("sitioApto", sitio.getApto());
					arr.add("sitioDireccion", sitio.getDireccion());
					arr.add("sitioCapacidad", sitio.getCapacidad());
					arr.add("sitioPorcentajeOcupaciondelaFuncion", (100*asistencia/sitio.getCapacidad())+"%");
				}
				object.add("sitiosDeFunciones", arr.build());
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(object.build()).build();
	}
	
	public Response consultarCompaniasPorAdmin(){
		
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		JsonArrayBuilder lista = Json.createArrayBuilder();
		JsonObjectBuilder object = Json.createObjectBuilder();
		try {
			System.out.println("Comienza a recorrer las companias");
			for(Compania compania: tm.darCompanias()){
				System.out.println("SE supone que tieen que recorrer: "+tm.darCompanias().size());
				System.out.println("Esta Compania");
				int asistencia = tm.darAsistenciaTotalPorCompania(compania.getId());
				object.add("ID CompaÃ±ia", compania.getId());
				object.add("asistenciaTotal", asistencia);
				object.add("asistenciaRegistrados", tm.darAsistenciaRegistradosPorCompania(compania.getId()));
				object.add("dineroGeneradoTaquilla", tm.darGananciaPorCompania(compania.getId()));
				JsonObjectBuilder arr = Json.createObjectBuilder();
				JsonArrayBuilder toarr = Json.createArrayBuilder();
				for(Sitio sitio: tm.darSitiosPorCompania(compania.getId())){
					System.out.println("Entra al sitio");
					arr.add("sitioId", sitio.getId());
					arr.add("sitioCiudad", sitio.getCiudad());
					arr.add("sitioApto", sitio.getApto());
					arr.add("sitioDireccion", sitio.getDireccion());
					arr.add("sitioCapacidad", sitio.getCapacidad());
					arr.add("sitioPorcentajeOcupaciondelaFuncion", (100*asistencia/sitio.getCapacidad())+"%");
				}
				object.add("sitiosDeFunciones", toarr.add(arr.build()).build());
				arr = Json.createObjectBuilder();
				lista.add(object.build());
				object = Json.createObjectBuilder();
				System.out.println("VAMOS POR LA SIGUIENTE");
			}
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista.build()).build();
		
				
	}


}
