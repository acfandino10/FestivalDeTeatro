package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Creacion {
	/**
	 * Id del representante
	 */
	@JsonProperty(value="id_representante")
	private int id_representante;

	/**
	 * Id del Funcion
	 */
	@JsonProperty(value="id_evento")
	private int id_evento;



	/**
	 * Metodo constructor de la relacion EventoCategoria
	 * <b>post: </b> Crea la relacion con los valores que entran como parametro
	 * @param id - Id del evento.
	 * @param id - Id del representante
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public Creacion( @JsonProperty(value="id_representante")int id_representante, @JsonProperty(value="id_evento")int id_evento) {
		super();
		this.id_representante = id_representante;
		this.id_evento = id_evento;
	}

	//Getters and Setters


	public int getId_representante() {
		return id_representante;
	}



	public void setId_representante(int id_representante) {
		this.id_representante = id_representante;
	}



	public int getId_evento() {
		return id_evento;
	}



	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}
	
	
}
