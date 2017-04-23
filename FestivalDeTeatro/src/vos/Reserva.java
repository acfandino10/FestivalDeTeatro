package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Reserva {
	
	@JsonProperty(value="id")
	private int id;
	
	/**
	 * Id del espectador
	 */
	@JsonProperty(value="id_espectador")
	private int id_espectador;

	/**
	 * Id del Funcion
	 */
	@JsonProperty(value="id_funcion")
	private int id_funcion;
	
	
	@JsonProperty(value="estadoactive")
	private String estadoactive;
	
	public static String ESTADO_ACTIVA="ESTADO_ACTIVA";
	public static String ESTADO_CANCELADA="ESTADO_CANCELADA";


	/**
	 * Metodo constructor de la relacion EventoCategoria
	 * <b>post: </b> Crea la relacion con los valores que entran como parametro
	 * @param estado 
	 * @param id - Id del evento.
	 * @param id - Id del espectador
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public Reserva(@JsonProperty(value="id")int id, @JsonProperty(value="id_espectador")int id_espectador, @JsonProperty(value="id_funcion")int id_funcion,
	@JsonProperty(value="estadoactive") String estadoactive	) {
		super();
		this.id=id;
		this.id_espectador = id_espectador;
		this.id_funcion = id_funcion;
		this.estadoactive=estadoactive;
	}

	//Getters and Setters

	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	public String getEstado(){
		return estadoactive;
	}
	
	public void setEstado(String estado){
		this.estadoactive=estado;
	}
	public int getId_espectador() {
		return id_espectador;
	}



	public void setId_espectador(int id_espectador) {
		this.id_espectador = id_espectador;
	}



	public int getIdFuncion() {
		return id_funcion;
	}



	public void setIdFuncion(int idFuncion) {
		this.id_funcion = idFuncion;
	}
	
}
