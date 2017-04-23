package rest;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import tm.FestivAndesMaster;
import vos.Usuario;
import vos.Espectaculo;
import vos.Espectador;
import vos.Funcion;
import vos.NotaDebito;
import vos.Reserva;
import vos.Silla;

@Path("reservas")
public class ReservaServices {
	// Servicios REST tipo GET:


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
	
	//Para hacer las pruebas
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getReservas() {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Reserva> lista;
		try {
			lista = tm.darReservas();
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(lista).build();
	}
		
	/**
     * MÃ©todo que expone servicio REST usando PUT que agrega el objeto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/usuario
     * @param objeto - a agregar
     * @return Json con el objeto que agrego o Json con el error que se produjo
	 * @throws Exception 
     */
	@GET
	@Path("comprar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarCompraBoleta(@QueryParam("ID_ESPECTADOR") int idUsuario, 
			@QueryParam("ID_FUNCION") int idFuncion,
			@QueryParam("ID") int id,
			@QueryParam("numeroSilla") int numeroSilla) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
			Reserva reserva = new Reserva(idUsuario, idFuncion, "ESTADO_ACTIVA",id);
			try {
				tm.registrarCompraBoleta(reserva, numeroSilla);

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(null).build();
		} else return null;
		
	}

	//TODO RF10
	/**
	 * RF10
	 * REGISTRAR COMPRA MUÌ�LTIPLE DE BOLETAS
	 * Registra la compra de grupos de boletas para una funcioÌ�n, de acuerdo con el enunciado.
	 * Esta operacioÌ�n puede ser realizada por un cliente registrado de FestivAndes.
	 * DEBE utilizar como subtransaccioÌ�n la implementacioÌ�n del
	 * requerimiento RF8 de la iteracioÌ�n anterior.
	 * @param idUsuario
	 * @param idEvento
	 * @param numeroSilla
	 * @param cantidadSillas
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("compras")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarCompraBoletas(@QueryParam("idUsuario") int idUsuario, 
			@QueryParam("idFuncion") int idFuncion,
			@QueryParam("id") int id,
			@QueryParam("localidad") String localidad,
			@QueryParam("cantidadSillas") int cantidadSillas) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
			Reserva reserva = new Reserva(idUsuario, idFuncion, "ESTADO_ACTIVA",id);
			try {
				
				tm.registrarComprasBoletas(reserva, localidad, cantidadSillas);				
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(null).build();
		} else return null;
		
	}
	
//TODO RF11
	/**
	 * RF11
	 * REGISTRAR COMPRA DE UN ABONAMIENTO
	 * Registra la compra de un abonamiento, de acuerdo con el enunciado.
	 * Esta operacioÌ�n puede ser realizada por un cliente registrado de FestivAndes.
	 * DEBE utilizar como subtransaccioÌ�n la implementacioÌ�n del
	 * requerimiento RF8 de la iteracioÌ�n anterior.
	 * @param idUsuario
	 * @param idEvento
	 * @param numeroSilla
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("comprarAbonamiento")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarAbonoBoleta(@QueryParam("id") int id, @QueryParam("localidad") String localidad) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(id);
		Espectador esp = tm.buscarEspectadorPorId(id);
		NotaDebito note;
		if((a.getRol().compareTo(Usuario.ROL_ESPECTADOR)==0)&&(esp.isEstaRegistrado())){

			try {
				
				//note=tm.devolverBoleta(idBoleta, id);

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		} else return Response.status(500).entity(doErrorMessage(new Exception("El usuario tiene que ser rol espectador y estar registrado"))).build();

		return Response.status(200).entity(null).build();
	}


	
	//TODO RF12
	/**
	 * RF12
	 * DEVOLVER BOLETA
	 * Registra la devolucioÌ�n de una boleta, de acuerdo con el enunciado.
	 * Esta operacioÌ�n puede ser realizada por un cliente registrado de FestivAndes.
	 * @param idUsuario
	 * @param idEvento
	 * @param numeroSilla
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("devolverBoleta")
	@Produces(MediaType.APPLICATION_JSON)
	public Response devolverBoleta(@QueryParam("id") int id, @QueryParam("idBoleta") int idBoleta) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(id);
		Espectador esp = tm.buscarEspectadorPorId(id);
		NotaDebito note;
		if((a.getRol().compareTo(Usuario.ROL_ESPECTADOR)==0)&&(esp.isEstaRegistrado())){

			try {
				
				note=tm.devolverBoleta(idBoleta, id);

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		} else return Response.status(500).entity(doErrorMessage(new Exception("El usuario tiene que ser rol espectador y estar registrado"))).build();

		return Response.status(200).entity(note).build();
	}
	

	
	//TODO
	/**
	 * RF13
	 * DEVOLVER ABONAMIENTO
	 * Registra la devolucioÌ�n de un abonamiento, de acuerdo con el enunciado.
	 * Esta operacioÌ�n puede ser realizada por un cliente registrado de FestivAndes.
	 * DEBE utilizar como subtransaccioÌ�n la implementacioÌ�n de los requerimientos RF12.
	 * @param idUsuario
	 * @param idEvento
	 * @param numeroSilla
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("devolverAbono")
	@Produces(MediaType.APPLICATION_JSON)
	public Response devolverAbonoBoleta(@QueryParam("id") int id, @QueryParam("idReserva") int idReserva) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(id);
		Espectador esp = tm.buscarEspectadorPorId(id);
		if((a.getRol().compareTo(Usuario.ROL_ESPECTADOR)==0)&&(esp.isEstaRegistrado())){

			try {
				esp=tm.devolverAbono(id,idReserva);

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
		} else return Response.status(500).entity(doErrorMessage(new Exception("El usuario tiene que ser rol espectador."))).build();

		return Response.status(200).entity(esp).build();
	}
	
	
}