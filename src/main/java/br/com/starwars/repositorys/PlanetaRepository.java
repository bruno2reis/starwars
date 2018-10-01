package br.com.starwars.repositorys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;



import br.com.javax.json.json.JSONObject;
import br.com.javax.json.json.JSONArray;


import br.com.codecs.PlanetaCodec;
import br.com.starwars.models.PlanetaVO;

@Repository
public class PlanetaRepository {
	
	
	private MongoClient cliente;
	private MongoDatabase bancoDeDados;
	
	private static String lerTodos(Reader rd) throws IOException{
		  StringBuilder ObjectLeitura = new StringBuilder();
		    int quantidade;
		    while ((quantidade = rd.read()) != -1) {
		      ObjectLeitura.append((char) quantidade);
		    }
		    return ObjectLeitura.toString();
	}
	private void criarConexao() {
		Codec<Document> codec = MongoClient.getDefaultCodecRegistry().get(Document.class);
		PlanetaCodec planetaCodec = new PlanetaCodec(codec);
		
		
		CodecRegistry registro = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), 
				CodecRegistries.fromCodecs(planetaCodec));
		MongoClientOptions opcoes = MongoClientOptions.builder().codecRegistry(registro).build();
		
		this.cliente = new MongoClient("localhost:27017", opcoes);
		this.bancoDeDados = cliente.getDatabase("test");
		
	}
	
	public void salvar(PlanetaVO planeta) {
		
		criarConexao();
		
	    MongoCollection<PlanetaVO> planetas = this.bancoDeDados.getCollection("planetas", PlanetaVO.class);
	    planetas.insertOne(planeta);
	   cliente.close();
	}	
	public List<PlanetaVO> obterTodosPlanetas(){
		criarConexao();
	    MongoCollection<PlanetaVO>planetas = this.bancoDeDados.getCollection("planetas", PlanetaVO.class);
	    
	    MongoCursor<PlanetaVO> resultado = planetas.find().iterator();
	    
	    List<PlanetaVO> planetaEncontrados = new ArrayList<PlanetaVO>();
	    
	    while(resultado.hasNext()){
	    	PlanetaVO planeta = resultado.next();
	    	
	    	this.atualizaNumeroDeAparicoes(planeta);
	    	
	    	planetaEncontrados.add(planeta);
	    }
	    cliente.close();
		
		return planetaEncontrados;
	}

	public PlanetaVO obterPlanetaPor(String id) {
		criarConexao();
		MongoCollection<PlanetaVO> planetas = this.bancoDeDados.getCollection("planetas", PlanetaVO.class);
		PlanetaVO planeta = planetas.find(Filters.eq("_id", new ObjectId(id))).first();
		return planeta;
	}
	
	public void deletaPor(String id){
		
		criarConexao();
		MongoCollection<PlanetaVO> planetas = this.bancoDeDados.getCollection("planetas", PlanetaVO.class);
		planetas.findOneAndDelete(Filters.eq("_id", new ObjectId(id)));
		cliente.close();
	}

	public List<PlanetaVO> pesquisarPor(String nome) {
		criarConexao();
		MongoCollection<PlanetaVO> planetaColletion = this.bancoDeDados.getCollection("planetas", PlanetaVO.class);
		MongoCursor<PlanetaVO> resultados = planetaColletion.find(Filters.eq("nome", nome), PlanetaVO.class).iterator();
		List<PlanetaVO> planetas = new ArrayList<PlanetaVO>();
		while(resultados.hasNext()){
			planetas.add(resultados.next());
		}
		this.cliente.close();
		
		return planetas;
	}
	
	public List<PlanetaVO> obterListaPlanetas(){
		List<PlanetaVO> listaDePlaneta = new ArrayList<PlanetaVO>();
		try{
			URL url = new URL("https://swapi.co/api/planets/?format=json&q=java&type=post"); 
			 HttpURLConnection httpconneccao = (HttpURLConnection) url.openConnection();
			 
			 InputStream conexaoEntrada = httpconneccao.getInputStream();
			 BufferedReader rd = new BufferedReader(new InputStreamReader(conexaoEntrada, Charset.forName("UTF-8")));
			 String jsonText = lerTodos(rd);
			 JSONObject jsonObject = new JSONObject(jsonText);
			 JSONArray results = (JSONArray)jsonObject.get("results");
			 for (int i = 0; i < results.length(); i++) {
		    	  JSONObject planetaJson = results.getJSONObject(i);
		    	  String nome = (String) planetaJson.get("name");
		    	  JSONArray filmes = (JSONArray) planetaJson.get("films");
		    	  String clima = (String) planetaJson.get("climate");
		    	  String terreno = (String) planetaJson.get("terrain");
		    	  int numeroApari = filmes.length();
		    	  
		    	  
		    	  PlanetaVO planeta = new PlanetaVO();
		    	  planeta.setTerreno(terreno);
		    	  planeta.setClima(clima);
		    	  planeta.setNome(nome);
		    	  planeta.setNumeroDeAparicoes(numeroApari);    	  
		    	  listaDePlaneta.add(planeta);
		      }
			 return listaDePlaneta;
		   
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
				return listaDePlaneta;
		}
	
	public void atualizaNumeroDeAparicoes(PlanetaVO planeta){
		try{
			URL url = new URL("https://swapi.co/api/planets/?format=json&q=java&type=post"); 
			 HttpURLConnection httpconneccao = (HttpURLConnection) url.openConnection();
			    httpconneccao.addRequestProperty("User-Agent", "Mozilla/4.0");
			 
			 InputStream conexaoEntrada = httpconneccao.getInputStream();
			 BufferedReader rd = new BufferedReader(new InputStreamReader(conexaoEntrada, Charset.forName("UTF-8")));
			 String jsonText = lerTodos(rd);
			 JSONObject jsonObject = new JSONObject(jsonText);
			 JSONArray results = (JSONArray)jsonObject.get("results");
			 for (int i = 0; i < results.length(); i++) {
		    	  JSONObject planetaJson = results.getJSONObject(i);
		    	  String nome = (String) planetaJson.get("name");
		    	  if(nome.equals(planeta.getNome())){
		    		  JSONArray filmes = (JSONArray) planetaJson.get("films");
		    		  planeta.setNumeroDeAparicoes(filmes.length()); 
		    	  }  
		      }
			 cliente.close();
		   
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
			
