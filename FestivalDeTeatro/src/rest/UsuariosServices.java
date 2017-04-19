package rest;

import java.sql.Date;
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
import vos.Compania;
import vos.Espectaculo;
import vos.Espectador;
import vos.Funcion;
import vos.Preferencia;
import vos.Representante;
import vos.Reserva;
import vos.Silla;
import vos.Sitio;
import vos.Usuario;

@Path("usuarios")
public class UsuariosServices {

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
		 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios
		 * @return Json con todos los objetos de la base de datos O json con 
	     * el error que se produjo
		 */
		@GET
		@Path("espectadores")
		@Produces({ MediaType.APPLICATION_JSON })
		public Response getEspectadores() {
			FestivAndesMaster tm = new FestivAndesMaster(getPath());
			ArrayList<Espectador> lista;
			try {
				lista = tm.darEspectadores();
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(lista).build();
		}
		


	    /**
	     * Método que expone servicio REST usando GET que busca el objeto con el nombre que entra como parámetro
	     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/nombre/"nombre para la busqueda"
	     * @param name - Nombre del objeto a buscar que entra en la URL como parámetro 
	     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
	     * el error que se produjo
	     */
		@GET
		@Path("/nombre/{nombre}")
		@Produces({ MediaType.APPLICATION_JSON })
		public Response getUsuarioName(@javax.ws.rs.PathParam("name") String name) {
			FestivAndesMaster tm = new FestivAndesMaster(getPath());
			ArrayList<Usuario> lista;
			try {
				if (name == null || name.length() == 0)
					throw new Exception("Nombre del video no valido");
				lista = tm.buscarUsuariosPorNombre(name);
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(lista).build();
		}
		
		@PUT
		@Path("/newEspectador")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response addUsuarioEspectador(Espectador objeto) {
			FestivAndesMaster tm = new FestivAndesMaster(getPath());
			try {
				tm.addUsuario((Usuario)objeto);
				tm.addEspectador(objeto);
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(objeto).build();
		}
		
		
		@PUT
		@Path("/newCompania")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response addUsuarioCompania(Representante objeto) {
			FestivAndesMaster tm = new FestivAndesMaster(getPath());
			try {
				tm.addUsuario((Usuario)objeto);
				tm.addRepresentante(objeto);
			} catch (Exception e) {
				return Response.status(500).entity(doErrorMessage(e)).build();
			}
			return Response.status(200).entity(objeto).build();
		}
		
		

		
		/**
	     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
	     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/usuario
	     * @param objeto - a agregar
	     * @return Json con el objeto que agrego o Json con el error que se produjo
		 * @throws Exception 
	     */
		@PUT
		@Path("id/{id}/cliente")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces(MediaType.APPLICATION_JSON)
		public Response registrarEspectador(@javax.ws.rs.PathParam("id") int id, Espectador objeto) throws Exception {
			FestivAndesMaster tm = new FestivAndesMaster(getPath());
			Usuario a = tm.buscarUsuariosPorId(id);
			if(a.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0){
				try {
					Espectador esp = tm.buscarEspectadorPorId(objeto.getId());
					if(esp!=null) {
						esp.setEstaRegistrado(true);
						tm.updateEspectador(esp);
					}
					
					else{
						Espectador toadd = objeto;
						toadd.setEstaRegistrado(true);
						tm.addEspectador(toadd);
					}
				} catch (Exception e) {
					return Response.status(500).entity(doErrorMessage(e)).build();
				}
				return Response.status(200).entity(objeto).build();
			} else return null;
			
		}
				/**
			     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/usuario
			     * @param objeto - a agregar
			     * @return Json con el objeto que agrego o Json con el error que se produjo
				 * @throws Exception 
			     */
				@POST
				@Path("id/{id}/compania")
				@Consumes(MediaType.APPLICATION_JSON)
				@Produces(MediaType.APPLICATION_JSON)
				public Response registrarCompania(@javax.ws.rs.PathParam("id") int id, Compania objeto) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					Usuario a = tm.buscarUsuariosPorId(id);
					if(a.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0){
						
						Representante buscado = tm.buscarRepresentantesPorId(id);
						try {
							tm.addCompania(objeto);
							buscado.setCompania(objeto.getId());
							tm.updateRepresentante(buscado);
						} catch (Exception e) {
							return Response.status(500).entity(doErrorMessage(e)).build();
						}
						return Response.status(200).entity(objeto).build();
					} else return null;
					
				}
		
				
				
				/**
			     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/usuario
			     * @param objeto - a agregar
			     * @return Json con el objeto que agrego o Json con el error que se produjo
				 * @throws Exception 
			     */
				@POST
				@Path("id/{id}/espectaculo")
				@Consumes(MediaType.APPLICATION_JSON)
				@Produces(MediaType.APPLICATION_JSON)
				public Response registrarEspectaculo(@javax.ws.rs.PathParam("id") int id, Espectaculo objeto) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					Usuario a = tm.buscarUsuariosPorId(id);
					if(a.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0){
						
						try {
							objeto.setId_representante(id);
							tm.addEspectaculo(objeto);
						} catch (Exception e) {
							return Response.status(500).entity(doErrorMessage(e)).build();
						}
						return Response.status(200).entity(objeto).build();
					} else return null;
					
				}
				
				/**
			     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/usuario
			     * @param objeto - a agregar
			     * @return Json con el objeto que agrego o Json con el error que se produjo
				 * @throws Exception 
			     */
				@POST
				@Path("id/{id}/sitio")
				@Consumes(MediaType.APPLICATION_JSON)
				@Produces(MediaType.APPLICATION_JSON)
				public Response registrarSitio(@javax.ws.rs.PathParam("id") int id, Sitio objeto) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					Usuario a = tm.buscarUsuariosPorId(id);
					if(a.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0){
						
						try {
							tm.addSitio(objeto);
						} catch (Exception e) {
							return Response.status(500).entity(doErrorMessage(e)).build();
						}
						return Response.status(200).entity(objeto).build();
					} else return null;
					
				}
				
				
				/**
			     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios/usuario
			     * @param objeto - a agregar
			     * @return Json con el objeto que agrego o Json con el error que se produjo
				 * @throws Exception 
			     */
				@POST
				@Path("id/{id}/espectaculo/{idEspectaculo}/sitio/{idSitio}/evento")
				@Consumes(MediaType.APPLICATION_JSON)
				@Produces(MediaType.APPLICATION_JSON)
				public Response programarFuncion(@javax.ws.rs.PathParam("id") int id,@javax.ws.rs.PathParam("idEspectaculo") int idEspectaculo,@javax.ws.rs.PathParam("idSitio") int idSitio, Funcion objeto) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					Usuario a = tm.buscarUsuariosPorId(id);
					if(a.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0){
						
						try {
							objeto.setId_sitio(idSitio);
							objeto.setId_espectaculo(idEspectaculo);
							tm.addEvento(objeto);
						} catch (Exception e) {
							return Response.status(500).entity(doErrorMessage(e)).build();
						}
						return Response.status(200).entity(objeto).build();
					} else return null;
					
				}


			    /**
			     * Método que expone servicio REST usando PUT que agrega el objeto que recibe en Json
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/preferencias/preferencia
			     * @param objeto - a agregar
			     * @return Json con el objeto que agrego o Json con el error que se produjo
			     * @throws Exception 
			     */
				@PUT
				@Path("/id/{id}/preferencia")
				@Consumes(MediaType.APPLICATION_JSON)
				@Produces(MediaType.APPLICATION_JSON)
				public Response addPreferencia(@javax.ws.rs.PathParam("id") int id,Preferencia objeto) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					Usuario a = tm.buscarUsuariosPorId(id);
					if(a.getRol().compareTo(Usuario.ROL_ESPECTADOR)==0){
					try {
						tm.addPreferencia(objeto);
					} catch (Exception e) {
						return Response.status(500).entity(doErrorMessage(e)).build();
					}}
					return Response.status(200).entity(objeto).build();
				}

		
				
				@POST
				@Path("id/{id}/evento/{idFuncion}/realizado")
				@Produces(MediaType.APPLICATION_JSON)
				public Response registrarFuncionRealizada(@javax.ws.rs.PathParam("id") int id,@javax.ws.rs.PathParam("idFuncion") int idFuncion) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					Funcion e=null;
				     Usuario a = tm.buscarUsuariosPorId(id);
						if(a.getRol().compareTo(Usuario.ROL_ADMINISTRADOR)==0){
						try {
							 e = tm.buscarFuncionPorId(idFuncion);
						     e.setDisponibilidad(false);
						     tm.updateEvento(e);
						} catch (Exception o) {
							return Response.status(500).entity(doErrorMessage(o)).build();
						}}
						return Response.status(200).entity(e).build();
					
				}
				
				
				/**
				 * Método que expone servicio REST usando GET que da todos los objetos de la base de datos.
				 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/eventos
				 * @return Json con todos los objetos de la base de datos O json con 
			     * el error que se produjo
				 */
				@GET
				@Path("/id/{id}/funciones")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response getEventos() {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					ArrayList<Funcion> lista;
					try {
						lista = tm.darFunciones();
					} catch (Exception e) {
						return Response.status(500).entity(doErrorMessage(e)).build();
					}
					return Response.status(200).entity(lista).build();
				}
			
				//Por espectaculo
			    /**
			     * Método que expone servicio REST usando GET que busca el objeto con el nombre que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/eventos/espectaculo/"espectaculo para la busqueda"
			     * @param name - Nombre del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/eventos/espectaculo/{id_espectaculo}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response getEventoEspectaculo(@javax.ws.rs.PathParam("espectaculo") int name) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					ArrayList<Funcion> lista;
						lista = tm.buscarEventosPorEspectaculo(name);
					return Response.status(200).entity(lista).build();
				}
				
				//RFC1: Por fecha
			    /**
			     * Método que expone servicio REST usando GET que busca el objeto con el nombre que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/eventos/fecha/"fecha para la busqueda"
			     * @param name - Nombre del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/eventos/fecha/{fecha}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response getEventoFecha(@javax.ws.rs.PathParam("fecha") Date fecha) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					ArrayList<Funcion> lista;
						lista = tm.buscarEventosPorFecha(fecha);
					return Response.status(200).entity(lista).build();
				}
				
				//RFC1: Por compania
			    /**
			     * Método que expone servicio REST usando GET que busca el objeto con el nombre que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/eventos/fecha/"fecha para la busqueda"
			     * @param name - Nombre del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/eventos/compania/{compania}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response getEventoFecha(@javax.ws.rs.PathParam("id_compania") int id_compania) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					ArrayList<Funcion> lista;
						lista = tm.buscarEventosPorCompania(id_compania);
					return Response.status(200).entity(lista).build();
				}

				 /**
			     * Método que expone servicio REST usando GET que busca el reporte que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios/id/"id para la busqueda"
			     * @param name - Ciudad del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/evento/{id}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response reporteFuncionTotal(@javax.ws.rs.PathParam("id_evento") int evento) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
				   double lista;
				   
						lista = tm.reporteFuncionTotal(evento);
					
					return Response.status(200).entity(lista).build();
				}
				
				 /**
			     * Método que expone servicio REST usando GET que busca el reporte que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios/id/"id para la busqueda"
			     * @param name - Ciudad del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/evento/{id}/{localidad}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response reporteFuncionLocalidad(@javax.ws.rs.PathParam("id_evento") int evento,
				@javax.ws.rs.PathParam("localidad") String localidad) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
				   double lista;
				   
						lista = tm.reporteFuncionLocalidad(evento, localidad);
					
					return Response.status(200).entity(lista).build();
				}
				
				
				 /**
			     * Método que expone servicio REST usando GET que busca el reporte que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios/id/"id para la busqueda"
			     * @param name - Ciudad del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/evento/{id}/{id_espectador}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response reporteFuncionEspectador(@javax.ws.rs.PathParam("id_evento") int evento,
				@javax.ws.rs.PathParam("id_espectador") int cliente) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
				   double lista;
				   
						lista = tm.reporteFuncionEspectador(evento, cliente);
					
					return Response.status(200).entity(lista).build();
				}
	
				/**
			     * Método que expone servicio REST usando GET que busca el reporte que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios/id/"id para la busqueda"
			     * @param name - Ciudad del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/espectaculo/{id}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response reporteEspectaculoTotal(@javax.ws.rs.PathParam("id_espectaculo") int espectaculo) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
				   double lista;
				   
						lista = tm.reporteEspectaculoTotal(espectaculo);
					
					return Response.status(200).entity(lista).build();
				}
				
				/**
			     * Método que expone servicio REST usando GET que busca el reporte que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios/id/"id para la busqueda"
			     * @param name - Ciudad del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/espectaculo/{id}/{id_sitio}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response reporteEspectaculoSitio(@javax.ws.rs.PathParam("id_espectaculo") int espectaculo,
				@javax.ws.rs.PathParam("sitio") int sitio) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
				   double lista;
				   
						lista = tm.reporteEspectaculoSitio(espectaculo, sitio);
					
					return Response.status(200).entity(lista).build();
				}
				
				/**
			     * Método que expone servicio REST usando GET que busca el reporte que entra como parámetro
			     * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/sitios/id/"id para la busqueda"
			     * @param name - Ciudad del objeto a buscar que entra en la URL como parámetro 
			     * @return Json con el/los objetos encontrados con el nombre que entra como parámetro o json con 
			     * el error que se produjo
			     * @throws Exception 
			     */
				@GET
				@Path("/id/{id}/espectaculo/{id}/{id_espectador}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response reporteEspectaculoEspectador(@javax.ws.rs.PathParam("id_espectaculo") int espectaculo,
				@javax.ws.rs.PathParam("id_espectador") int espectador) throws Exception {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
				   double lista;
				   
						lista = tm.reporteEspectaculoEspectador(espectaculo, espectador);
					
					return Response.status(200).entity(lista).build();
				}
				
				
				
				
				//Métodos útiles para pruebas
				
				/**
				 * Método que expone servicio REST usando GET que da todos los objetos de la base de datos.
				 * <b>URL: </b> http://"ip o nombre de host":8080/FestivAndes/rest/usuarios
				 * @return Json con todos los objetos de la base de datos O json con 
			     * el error que se produjo
				 */
				@GET
				@Produces({ MediaType.APPLICATION_JSON })
				public Response getUsuarios() {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					ArrayList<Usuario> lista;
					try {
						lista = tm.darUsuarios();
					} catch (Exception e) {
						return Response.status(500).entity(doErrorMessage(e)).build();
					}
					return Response.status(200).entity(lista).build();
				}
				
				

				@GET
				@Path("/id/{id}")
				@Produces({ MediaType.APPLICATION_JSON })
				public Response getUsuarioId(@javax.ws.rs.PathParam("id") int id) {
					FestivAndesMaster tm = new FestivAndesMaster(getPath());
					Usuario usuario;
					try {
						usuario = tm.buscarUsuariosPorId(id);
					} catch (Exception e) {
						return Response.status(500).entity(doErrorMessage(e)).build();
					}
					return Response.status(200).entity(usuario).build();
				}
				
				
							
}

