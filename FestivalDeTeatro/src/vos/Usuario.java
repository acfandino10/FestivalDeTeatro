package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Usuario {

	////Atributos

	/**
	 * Id del usuario
	 */
	@JsonProperty(value="id")
	private int id;

	/**
	 * Nombre del usuario
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Correo del usuario
	 */
	@JsonProperty(value="correo")
	private String correo;
	
	/**
	 * Password del usuario
	 * puede ser nulo si es usuario no esta registrado
	 */
	@JsonProperty(value="password")
	private String password;
		
	
	public static final String ROL_ESPECTADOR = "ROL_ESPECTADOR";
	
	public static final String ROL_ADMINISTRADOR ="ROL_ADMINISTRADOR";
	
	public static final String ROL_COMPANIA ="ROL_COMPANIA";
	
	@JsonProperty(value="rol")
	private String rol;

	/**
	 * Metodo constructor de la clase usuario
	 */
	public Usuario(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="correo") String correo, @JsonProperty(value="password") String password, 
			@JsonProperty(value="rol")String rol) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this.rol = rol;
	}
	
	//Getters and Setters



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getNombre() {
		return nombre;
	}



	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getCorreo() {
		return correo;
	}



	public void setCorreo(String correo) {
		this.correo = correo;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
