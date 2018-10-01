package br.com.starwars.models;

import org.bson.types.ObjectId;

public class PlanetaVO {
	
	private ObjectId id;
	private String nome;
	private String clima;
	private String terreno;
	private int numeroDeAparicoes;
	
	/**
	 * @return the numeroDeAparicoes
	 */
	public int getNumeroDeAparicoes() {
		return numeroDeAparicoes;
	}
	/**
	 * @param numeroDeAparicoes the numeroDeAparicoes to set
	 */
	public void setNumeroDeAparicoes(int numeroDeAparicoes) {
		this.numeroDeAparicoes = numeroDeAparicoes;
	}
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getClima() {
		return clima;
	}
	public void setClima(String clima) {
		this.clima = clima;
	}
	public String getTerreno() {
		return terreno;
	}
	public void setTerreno(String terreno) {
		this.terreno = terreno;
	}
	public PlanetaVO criarId() {
		setId(new ObjectId());
		
		return this;
	}

}
