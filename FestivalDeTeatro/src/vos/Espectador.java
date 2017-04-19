package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Espectador extends Usuario{

	////Atributos

	/**
	 * Id del espectador
	 */
	@JsonProperty(value="id")
	private int id;

	/**
	 * Nombre del espectador
	 */
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * Correo del espectador
	 */
	@JsonProperty(value="correo")
	private String correo;
	
	/**
	 * Password del espectador
	 * puede ser nulo si es usuario no esta registrado
	 */
	@JsonProperty(value="password")
	private String password;
	
	/**
	 * Tarjeta bancaria del espectador
	 */
	@JsonProperty(value="tarjeta")
	private String tarjeta;
	
	/**
	 * Indica si esta registrado el usuario
	 */
	@JsonProperty(value="estaRegistrado")
	private boolean estaRegistrado;
	
	/**
	 * Banco de la tarjeta del usuario
	 */
	@JsonProperty(value="saldo")
	private double saldo;

	

	/**
	 * Metodo constructor de la clase espectador
	 */
	public Espectador(@JsonProperty(value="id")int id, @JsonProperty(value="nombre")String nombre,@JsonProperty(value="correo") String correo, @JsonProperty(value="password") String password, @JsonProperty(value="tarjeta") String tarjeta, @JsonProperty(value="estaRegistrado") boolean estaRegistrado, @JsonProperty(value="saldo") double saldo) {
		super(id, nombre, correo, password, "ROL_ESPECTADOR");
		this.id = id;
		this.nombre = nombre;
		this.correo = correo;
		this.password = password;
		this.tarjeta = tarjeta;
		this.estaRegistrado = estaRegistrado;
		this.saldo = saldo;
	}

   //Getters and Setters
	
	
	public int getId() {
		return id;
	}
	
	public void setSaldo(double saldo){
		this.saldo=saldo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
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

	public String getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(String tarjeta) {
		this.tarjeta = tarjeta;
	}

	public boolean isEstaRegistrado() {
		return estaRegistrado;
	}

	public void setEstaRegistrado(boolean estaRegistrado) {
		this.estaRegistrado = estaRegistrado;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setBancoTarjeta(double saldo) {
		this.saldo = saldo;
	}

}
