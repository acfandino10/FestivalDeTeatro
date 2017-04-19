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
     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/usuario
     * @param objeto - a agregar
     * @return Json con el objeto que agrego o Json con el error que se produjo
	 * @throws Exception 
     */
	@PUT
	@Path("usuario/{idUsuario}/evento/{idEvento}/comprarSilla/{numeroSilla}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarCompraBoleta(@javax.ws.rs.PathParam("idUsuario") int idUsuario, @javax.ws.rs.PathParam("idFuncion") int idFuncion,@javax.ws.rs.PathParam("estado") String estado) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
//			Reserva reserva = new Reserva(idUsuario, idFuncion, estado);
			Funcion funcion = tm.buscarFuncionPorId(idFuncion);
			try {
//				if(tm..isEstaReservada() == false)
//				{
//					tm.addReserva(reserva);
//				    Silla s = tm.buscarSillasPorNumero(numeroSilla);
//				    s.setEstaReservada(true);
//				    funcion.setGanancias(funcion.getGanancias()+s.getCosto());
//				    
//				    
//				    Espectaculo es = tm.buscarEspectaculoPorId(funcion.getId_espectaculo());
//				    es.setRentabilidad(es.getRentabilidad() + s.getCosto());
//				}
//				else
//				{
//					throw new Exception("La silla ya esta reservada");
//				}

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(null).build();
		} else return null;
		
	}

	//TODO RF10
	/**
	 * RF10
	 * REGISTRAR COMPRA MÚLTIPLE DE BOLETAS
	 * Registra la compra de grupos de boletas para una función, de acuerdo con el enunciado.
	 * Esta operación puede ser realizada por un cliente registrado de FestivAndes.
	 * DEBE utilizar como subtransacción la implementación del
	 * requerimiento RF8 de la iteración anterior.
	 * @param idUsuario
	 * @param idEvento
	 * @param numeroSilla
	 * @param cantidadSillas
	 * @return
	 * @throws Exception
	 */
	@PUT
	@Path("usuario/{idUsuario}/evento/{idEvento}/comprarSillas/{numeroSilla}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarCompraBoletas(@javax.ws.rs.PathParam("idUsuario") int idUsuario, 
			@javax.ws.rs.PathParam("idEvento") int idEvento,
			@javax.ws.rs.PathParam("numeroSilla") int numeroSilla,
			@javax.ws.rs.PathParam("cantidadSillas") int cantidadSillas) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
//			Reserva reserva = new Reserva(idUsuario, idEvento, numeroSilla);
			try {
				
//				registrarCompraBoleta(idUsuario, idEvento, numeroSilla);
//				
//				for(int i=1; i<cantidadSillas; i++){
//					registrarCompraBoleta(cantidadSillas, idEvento, numeroSilla + i);
//				}
				

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
	 * Esta operación puede ser realizada por un cliente registrado de FestivAndes.
	 * DEBE utilizar como subtransacción la implementación del
	 * requerimiento RF8 de la iteración anterior.
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
	 * Registra la devolución de una boleta, de acuerdo con el enunciado.
	 * Esta operación puede ser realizada por un cliente registrado de FestivAndes.
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
	 * Registra la devolución de un abonamiento, de acuerdo con el enunciado.
	 * Esta operación puede ser realizada por un cliente registrado de FestivAndes.
	 * DEBE utilizar como subtransacción la implementación de los requerimientos RF12.
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