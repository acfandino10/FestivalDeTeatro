package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Reserva {
	/**
	 * Id del espectador
	 */
	@JsonProperty(value="id_espectador")
	private int id_espectador;

	/**
	 * Id del Funcion
	 */
	@JsonProperty(value="id_evento")
	private int id_evento;
	
	/**
	 * 
	 */
	@JsonProperty(value="numero_silla")
	private int numero_silla;


    private double abono;

	/**
	 * Metodo constructor de la relacion EventoCategoria
	 * <b>post: </b> Crea la relacion con los valores que entran como parametro
	 * @param id - Id del evento.
	 * @param id - Id del espectador
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public Reserva( @JsonProperty(value="id_espectador")int id_espectador, @JsonProperty(value="id_evento")int id_evento,
	@JsonProperty(value="numero_silla")	int silla	) {
		super();
		this.id_espectador = id_espectador;
		this.id_evento = id_evento;
		this.numero_silla = silla;
		this.abono = 0.0;
	}

	//Getters and Setters

	public int getId_espectador() {
		return id_espectador;
	}



	public void setId_espectador(int id_espectador) {
		this.id_espectador = id_espectador;
	}



	public int getId_evento() {
		return id_evento;
	}



	public void setId_evento(int id_evento) {
		this.id_evento = id_evento;
	}

	public int getNumero_silla() {
		return numero_silla;
	}

	public void setNumero_silla(int numero_silla) {
		this.numero_silla = numero_silla;
	}
	
	public double getAbono() {
		return abono;
	}
	
	public void setAbono(double abono) {
		this.abono = abono;
	}
	
}
