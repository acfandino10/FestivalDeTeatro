package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Preferencia {

	/**
	 * Id del espectador
	 */
	@JsonProperty(value="id_espectador")
	private int id_espectador;

	/**
	 * Nombre de la preferencia
	 */
	@JsonProperty(value="preferencia")
	private String preferencia;



	/**
	 * Metodo constructor de la relacion EventoCategoria
	 * <b>post: </b> Crea la relacion con los valores que entran como parametro
	 * @param id - Id del evento.
	 * @param id - Id del representante
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public Preferencia( @JsonProperty(value="id_espectador")int id_espectador, @JsonProperty(value="preferencia")String preferencia) {
		super();
		this.id_espectador = id_espectador;
		this.preferencia = preferencia;
	}



	public int getId_espectador() {
		return id_espectador;
	}



	public void setId_espectador(int id_espectador) {
		this.id_espectador = id_espectador;
	}



	public String getPreferencia() {
		return preferencia;
	}



	public void setPreferencia(String preferencia) {
		this.preferencia = preferencia;
	}

}
