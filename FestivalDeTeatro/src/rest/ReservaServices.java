package rest;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import tm.FestivAndesMaster;
import vos.Usuario;
import vos.Espectaculo;
import vos.Funcion;
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
	public Response registrarCompraBoleta(@javax.ws.rs.PathParam("idUsuario") int idUsuario, @javax.ws.rs.PathParam("idEvento") int idEvento,@javax.ws.rs.PathParam("numeroSilla") int numeroSilla) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
			Reserva reserva = new Reserva(idUsuario, idEvento, numeroSilla);
			Funcion funcion = tm.buscarEventoPorId(idEvento);
			try {
				if(tm.buscarSillasPorNumero(numeroSilla).isEstaReservada() == false)
				{
					tm.addReserva(reserva);
				    Silla s = tm.buscarSillasPorNumero(numeroSilla);
				    s.setEstaReservada(true);
				    funcion.setGanancias(funcion.getGanancias()+s.getCosto());
				    
				    
				    Espectaculo es = tm.buscarEspectaculoPorId(funcion.getId_espectaculo());
				    es.setRentabilidad(es.getRentabilidad() + s.getCosto());
				}
				else
				{
					throw new Exception("La silla ya esta reservada");
				}

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(reserva).build();
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
			Reserva reserva = new Reserva(idUsuario, idEvento, numeroSilla);
			try {
				
				registrarCompraBoleta(idUsuario, idEvento, numeroSilla);
				
				for(int i=1; i<cantidadSillas; i++){
					registrarCompraBoleta(cantidadSillas, idEvento, numeroSilla + i);
				}
				

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(reserva).build();
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
	@PUT
	@Path("usuario/{idUsuario}/evento/{idEvento}/abonarSilla/{numeroSilla}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarAbonoBoleta(@javax.ws.rs.PathParam("idUsuario") int idUsuario, @javax.ws.rs.PathParam("idEvento") int idEvento,@javax.ws.rs.PathParam("numeroSilla") int numeroSilla) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
			Reserva reserva = new Reserva(idUsuario, idEvento, numeroSilla);
			Funcion funcion = tm.buscarEventoPorId(idEvento);
			
			
			long time = System.currentTimeMillis();
			java.sql.Date today = new java.sql.Date(time);
			//La ultima condicion compara con hoy y cuenta 3 semanas --- (... || funcion.getFecha().getTime() - today.getTime() >= 3 semanas*7 dias a la semana*86,400,000 millisegundos al dia)
			try {
				if(tm.buscarSillasPorNumero(numeroSilla).isEstaReservada() == false && funcion.getFecha().before(today) == true && funcion.getFecha().getTime() - today.getTime() >= (3*7*86400000))
				{
					registrarCompraBoleta(idUsuario, idEvento, numeroSilla);
					tm.addAbono(numeroSilla);
				    Silla s = tm.buscarSillasPorNumero(numeroSilla);
				   
				    //Como registrarCompraBoleta sube las ganancias, aqui se le bajan
				    funcion.setGanancias(funcion.getGanancias()-s.getCosto()+(s.getCosto()*0.2));
				    
				    Espectaculo es = tm.buscarEspectaculoPorId(funcion.getId_espectaculo());
				    es.setRentabilidad(es.getRentabilidad() - s.getCosto()+(s.getCosto()*0.2));
				}
				else
				{
					throw new Exception("La silla ya est� reservada o se acabo el plazo para abonar en esta reserva");
				}

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(reserva).build();
		} else return null;
		
	}

	
	@PUT
	@Path("id/{id}/evento/{idEvento}/abonarSilla/{numeroSilla}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registrarCompraConAbonoBoleta(@javax.ws.rs.PathParam("id") int id, @javax.ws.rs.PathParam("idEvento") int idEvento,@javax.ws.rs.PathParam("numeroSilla") int numeroSilla) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(id);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
			Reserva reserva = new Reserva(id, idEvento, numeroSilla);
			Funcion funcion = tm.buscarEventoPorId(idEvento);
			try {
				if(tm.buscarSillasPorNumero(numeroSilla).isEstaReservada() == false && reserva.getAbono()>0)
				{
				
				    Silla s = tm.buscarSillasPorNumero(numeroSilla);
				    funcion.setGanancias(funcion.getGanancias()+(s.getCosto()-(s.getCosto()*0.4)));
				    
				    Espectaculo es = tm.buscarEspectaculoPorId(funcion.getId_espectaculo());
				    es.setRentabilidad(es.getRentabilidad() + s.getCosto()-(s.getCosto()*0.4));
				}
				else
				{
					throw new Exception("La silla ya est� reservada");
				}

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(reserva).build();
		} else return null;
		
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
	@PUT
	@Path("usuario/{idUsuario}/evento/{idEvento}/devolverBoleta/{numeroSilla}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response devolverBoleta(@javax.ws.rs.PathParam("idUsuario") int idUsuario, @javax.ws.rs.PathParam("idEvento") int idEvento,@javax.ws.rs.PathParam("numeroSilla") int numeroSilla) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
			Reserva reserva = new Reserva(idUsuario, idEvento, numeroSilla);
			Funcion funcion = tm.buscarEventoPorId(idEvento);
			try {
				
				long time = System.currentTimeMillis();
				java.sql.Date today = new java.sql.Date(time);
				
				//la ultima condicion dice que hay 5 dias de diferencia entre hoy y el evento || funcion.getFecha().getTime() - today.getTime() >= 5 dias * 86,400,000 millisegundos al dia)				
				if(tm.buscarSillasPorNumero(numeroSilla).isEstaReservada() == true && funcion.getFecha().before(today) == true && funcion.getFecha().getTime() - today.getTime() >= (5*86400000))
				{
					tm.deleteReserva(reserva);;
				    Silla s = tm.buscarSillasPorNumero(numeroSilla);
				    s.setEstaReservada(false);
				    funcion.setGanancias(funcion.getGanancias()-s.getCosto());
				    
				    
				    Espectaculo es = tm.buscarEspectaculoPorId(funcion.getId_espectaculo());
				    es.setRentabilidad(es.getRentabilidad() - s.getCosto());
				}
				else
				{
					throw new Exception("No existe una reserva para esa silla");
				}

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(reserva).build();
		} else return null;
		
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
	@PUT
	@Path("usuario/{idUsuario}/evento/{idEvento}/devolverAbono/{numeroSilla}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response devolverAbonoBoleta(@javax.ws.rs.PathParam("idUsuario") int idUsuario, @javax.ws.rs.PathParam("idEvento") int idEvento,@javax.ws.rs.PathParam("numeroSilla") int numeroSilla) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario a = tm.buscarUsuariosPorId(idUsuario);
		if(a.getRol()==Usuario.ROL_ESPECTADOR){
			Reserva reserva = new Reserva(idUsuario, idEvento, numeroSilla);
			Funcion funcion = tm.buscarEventoPorId(idEvento);
			
			
			long time = System.currentTimeMillis();
			java.sql.Date today = new java.sql.Date(time);
			try {
				
				//La ultima condicion dice que hay 3 semanas de diferencia entre hoy y el evento || funcion.getFecha().getTime() - today.getTime() >= 3 semanas* 7 dias a la semana * 86,400,000 millisegundos al dia)
				if(tm.buscarSillasPorNumero(numeroSilla).isEstaReservada() == false && funcion.getFecha().before(today) == true && funcion.getFecha().getTime() - today.getTime() >= (3*7*86400000) )
				{
					devolverBoleta(idUsuario, idEvento, numeroSilla);
					tm.deleteAbono(numeroSilla);
				    Silla s = tm.buscarSillasPorNumero(numeroSilla);
				   
				    //Como devolverBoleta disminuye las ganancias pero en abono nunca se sumo eso, ac� se le agrega esa resta
				    funcion.setGanancias(funcion.getGanancias()-(s.getCosto()*0.2));
				    
				    Espectaculo es = tm.buscarEspectaculoPorId(funcion.getId_espectaculo());
				    es.setRentabilidad(es.getRentabilidad() - (s.getCosto()*0.2));
				}
				else
				{
					throw new Exception("La silla ya est� reservada o se acabo el plazo para abonar en esta reserva");
				}

			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(reserva).build();
		} else return null;
		
	}
	
	
}