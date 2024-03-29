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
	 * Atributo que usa la anotaciÃƒÂ³n @Context para tener el ServletContext de la conexiÃƒÂ³n actual.
	 */
	@Context
	private ServletContext context;

	/**
	 * MÃƒÂ©todo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
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
			System.out.println("-----------Entra al metodo por rest---------");
			FestivAndesMaster tm = new FestivAndesMaster(getPath());
			System.out.println("------Encuentra el master-------");
			Espectador a = tm.buscarEspectadorPorId(idUsuario);
			System.out.println("-----------Encontro el usuario-----------");
			
				System.out.println("--------Entro al if----------");
				Reserva reserva = new Reserva(id,idUsuario, idFuncion, "ESTADO_ACTIVA");
				System.out.println("--------------Creo la reserva------");
				try {
					System.out.println("----entro al trydel rest-----");
					tm.registrarComprasBoletas(reserva, localidad, cantidadSillas);	
					System.out.println("--------Registro las boletas");
				} catch (Exception e) {
					System.out.println("------Entro al catch----------");
					return Response.status(500).entity(doErrorMessage(e)).build();
				}
				return Response.status(200).entity(reserva).build();		
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
		public Response registrarAbonoBoleta(@QueryParam("ID_USUARIO") int id, @QueryParam("ID") int idBoleta,
				@QueryParam("ID_FUNCION") int idFuncion,
				@QueryParam("cantSillas") int cantSillas,
				@QueryParam("localidad") String localidad) throws Exception {
			FestivAndesMaster tm = new FestivAndesMaster(getPath());
			Usuario a = tm.buscarUsuariosPorId(id);
			Espectador esp = tm.buscarEspectadorPorId(id);
			Reserva res = null; 
			if((a.getRol().compareTo(Usuario.ROL_ESPECTADOR)==0)&&(esp.isEstaRegistrado())){

				try {
					res = new Reserva(id,idBoleta, idFuncion, "ESTADO_ACTIVA");
					tm.registrarAbonoBoletas(res,localidad,cantSillas);
					

				} catch (Exception e) {
					return Response.status(500).entity(doErrorMessage(e)).build();
				}
			} else return Response.status(500).entity(doErrorMessage(new Exception("El usuario tiene que ser rol espectador y estar registrado"))).build();

			return Response.status(200).entity(res).build();
		}


	
	//TODO RF12
	/**
	 * RF12
	 * DEVOLVER BOLETA
	 * Registra la devolucioÃŒï¿½n de una boleta, de acuerdo con el enunciado.
	 * Esta operacioÃŒï¿½n puede ser realizada por un cliente registrado de FestivAndes.
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
	 * Registra la devolucioÃŒï¿½n de un abonamiento, de acuerdo con el enunciado.
	 * Esta operacioÃŒï¿½n puede ser realizada por un cliente registrado de FestivAndes.
	 * DEBE utilizar como subtransaccioÃŒï¿½n la implementacioÃŒï¿½n de los requerimientos RF12.
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
	
	
	//Probando...
	@PUT
	@Path("agregar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addReserva(Reserva objeto) throws Exception {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Reserva reserva;
		try {
			System.out.println("----comienza add REserva---");
			reserva=tm.addReserva(objeto);
			System.out.println("----termina add Reserva--");
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		return Response.status(200).entity(reserva).build();
	}
	
	
}