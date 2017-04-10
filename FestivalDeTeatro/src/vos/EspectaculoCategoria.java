package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class EspectaculoCategoria {
	/**
	 * Id del Funcion
	 */
	@JsonProperty(value="idEspectaculo")
	private int idEspectaculo;

	/**
	 * Nombre de la Categoria
	 */
	@JsonProperty(value="nombreCategoria")
	private String nombreCategoria;



	/**
	 * Metodo constructor de la relacion EventoCategoria
	 * <b>post: </b> Crea la relacion con los valores que entran como parametro
	 * @param id - Id del evento.
	 * @param nombre - Nombre de la categoria
	 * @param dirPrincipal - Direccion de la oficina principal.
	 */
	public EspectaculoCategoria(@JsonProperty(value="idEspectaculo")int idEspectaculo, @JsonProperty(value="nombreCategoria")String nombreCategoria) {
		super();
		this.idEspectaculo = idEspectaculo;
		this.nombreCategoria = nombreCategoria;
	}
	
	//Getters and Setters


	public int getIdEspectaculo() {
		return idEspectaculo;
	}


	public void setIdEspectaculo(int idEspectaculo) {
		this.idEspectaculo = idEspectaculo;
	}


	public String getNombreCategoria() {
		return nombreCategoria;
	}


	public void setNombre_categoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}
	
}
