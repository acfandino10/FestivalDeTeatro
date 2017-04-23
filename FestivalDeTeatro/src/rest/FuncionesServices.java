package rest;


import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import tm.FestivAndesMaster;
import vos.Espectador;
import vos.Funcion;
import vos.Reserva;
import vos.Silla;
import vos.Usuario;

@Path("funciones")
public class FuncionesServices {
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
	 * MÃ©todo que expone servicio REST usando GET que da todos los objetos de la base de datos.
	 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/eventos
	 * @return Json con todos los objetos de la base de datos O json con 
     * el error que se produjo
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getFunciones() {
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		ArrayList<Funcion> lista;
		try {
			lista = tm.darFunciones();
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
	 * CANCELAR UNA FUNCIOÌ�N
	 * Cancela la realizacioÌ�n de una funcioÌ�n programada en FestivAndes.
	 * Puede darse, inclusive, si la funcioÌ�n ya ha empezado,
	 * pero no si ya terminoÌ�.
	 * Cuando se cancela una funcioÌ�n,
	 * se debe devolver el dinero de
	 * todos los usuarios registrados que tienen boletas adquiridas.
	 * Esta operacioÌ�n puede ser realizada por un usuario administrador de FestivAndes.
	 * DEBE utilizar como subtransaccioÌ�n la implementacioÌ�n del requerimiento RF12.
	 * @param evento
	 * @return
	 * @throws Exception
	 */
	@GET
	@Path("cancelar")
	@Produces({ MediaType.APPLICATION_JSON})
	public Response cancelarFuncion(@QueryParam("id") int id, @QueryParam("idFuncion") int idFuncion) throws Exception{
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		JsonArrayBuilder arr = Json.createArrayBuilder();
		try {
			Usuario us = tm.buscarUsuariosPorId(id);
			if(us.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0){
				Funcion fun = tm.buscarFuncionPorId(idFuncion,false);
				if(fun.getEstado().compareTo(Funcion.ESTADO_REALIZADA)==0) throw new Exception("La funcion no puede ser cancelada puesto que ya fue realizada");
				else{
					JsonObjectBuilder obj = Json.createObjectBuilder();
					for(Espectador esp: tm.darEspectadoresDeFuncionYDevolverCosto(idFuncion)){
						tm.updateEspectador(esp);
						obj.add("IDEspectadorActualizado", esp.getId());
						obj.add("EspectadorActualizado", esp.getNombre());
						obj.add("MoneyEspectadorActualizado", esp.getSaldo());
						arr.add(obj.build());
					}
					for(Reserva res: tm.darReservasFuncionYCancelarlas(idFuncion)){
						tm.updateReserva(res);
					}
				}tm.deleteFuncion(idFuncion);
			}else throw new Exception("Ãšnicamente el administrador puede realizar dicha operacion");
			
		}  catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		return Response.status(200).entity(arr.build()).build();
	}
	
	//TODO
	/**
	 * RFC7.
	 * CONSULTAR ASISTENCIA AL FESTIVAL DE UN CLIENTE REGISTRADO
	 * Consulta las funciones a las que asiste un cliente registrado de FestivAndes.
	 * Debe discriminar las funciones ya realizadas o en curso,
	 * las funciones previstas y las boletas devueltas.
	 * Esta operacioÌ�n es realizada por los clientes registrados y por
	 * el usuario administrador de FestivAndes.
	 * Respetando la privacidad de los clientes,
	 * cuando un cliente registrado hace esta consulta obtiene la
	 * informacioÌ�n de sus propia actividad, mientras que el administrador
	 * obtiene toda la informacioÌ�n de cualquiera de los clientes. Ver RNF1.
	 * @return Asistencia del/los cliente.
	 * @throws Exception 
	 */
	@GET
	@Path("asistencia")
	@Produces({ MediaType.APPLICATION_JSON})
	public Response getAsistenciaClientesRegistrados(@QueryParam("id") int id) throws Exception{
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		Usuario usuario = tm.buscarUsuariosPorId(id);
		if(usuario.getRol().compareTo(Usuario.ROL_COMPANIA)==0) throw new Exception("Lamentamos informarle que esta operaciÃ³n no puede ser realizada por una compaÃ±ia ya que esta no tiene asistencia a ninguna funciÃ³n u espectÃ¡culo");
		else if(usuario.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0) return getAsistenciaTodosUsuariosRegistrados();
		else if(usuario.getRol().compareTo(Usuario.ROL_ESPECTADOR)==0){
			Espectador esp = tm.buscarEspectadorPorId(usuario.getId());
			if(esp.isEstaRegistrado()){
				return getAsistenciaUsuarioRegistrado(id);
			}else throw new Exception("Lamentamos informarle que esta operaciÃ³n es Ãºnicamente para usuarios registrados");
		}
		return null;
	}
	
	public Response getAsistenciaTodosUsuariosRegistrados(){
		
		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		JsonArrayBuilder bigarray = Json.createArrayBuilder();
		try {
			for(Espectador esp: tm.darEspectadores()){
				JsonObjectBuilder obj = Json.createObjectBuilder();
				obj.add("ID_Cliente", esp.getId());
				
				JsonArrayBuilder arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesRealizadasEspectador(esp.getId())){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioID", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					arr.add(job.build());
				}
				obj.add("FuncionesRealizadas", arr.build());
				arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesEnCursoEspectador(esp.getId())){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioID", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					arr.add(job.build());
				}
				obj.add("FuncionesEnCurso", arr.build());
				arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesPrevistasEspectador(esp.getId())){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioID", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					arr.add(job.build());
				}
				obj.add("FuncionesPrevistas", arr.build());
				arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesCanceladas(esp.getId())){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioDeFuncion", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					ArrayList<Silla> sillas = tm.darSillasCanceladas(esp.getId(),fun.getId());
					for(Silla silla: sillas){
						job.add("NumeroSilla", silla.getNumero());
						job.add("DineroDevuelto", silla.getCosto());
					}
					
					arr.add(job.build());
				}
				obj.add("BoletasDevueltas", arr.build());
				bigarray.add(obj.build());
			}
			
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		return Response.status(200).entity(bigarray.build()).build();
	}
	
	public Response getAsistenciaUsuarioRegistrado(int id){

		FestivAndesMaster tm = new FestivAndesMaster(getPath());
		JsonObjectBuilder obj = Json.createObjectBuilder();
		try {
				obj.add("ID_Cliente", id);
				
				JsonArrayBuilder arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesRealizadasEspectador(id)){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioID", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					arr.add(job.build());
				}
				obj.add("FuncionesRealizadas", arr.build());
				arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesEnCursoEspectador(id)){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioID", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					arr.add(job.build());
				}
				obj.add("FuncionesEnCurso", arr.build());
				arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesPrevistasEspectador(id)){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioID", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					arr.add(job.build());
				}
				obj.add("FuncionesPrevistas", arr.build());
				arr = Json.createArrayBuilder();
				for(Funcion fun: tm.darFuncionesCanceladas(id)){
					JsonObjectBuilder job = Json.createObjectBuilder();
					job.add("ID_Funcion", fun.getId());
					job.add("SitioDeFuncion", fun.getId_sitio());
					job.add("Fecha", fun.getFecha().toString());
					job.add("Hora", fun.getHora().toString().substring(10));
					ArrayList<Silla> sillas = tm.darSillasCanceladas(id,fun.getId());
					for(Silla silla: sillas){
						job.add("NumeroSilla", silla.getNumero());
						job.add("DineroDevuelto", silla.getCosto());
					}
					
					arr.add(job.build());
				}
				obj.add("BoletasDevueltas", arr.build());
			
		} catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
		
		return Response.status(200).entity(obj.build()).build();


		
	}
	
}
