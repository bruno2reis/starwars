package br.com.codecs;

import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;

import br.com.starwars.models.PlanetaVO;

public class PlanetaCodec implements CollectibleCodec<PlanetaVO> {

	private Codec<Document> codec;
	
	public PlanetaCodec(Codec<Document> codec) {
		this.codec = codec;
	}	
	public void encode(BsonWriter writer, PlanetaVO planeta, EncoderContext encoder) {

		ObjectId id = planeta.getId();
		String nome = planeta.getNome();
		String clima = planeta.getClima();
		String terreno = planeta.getTerreno();
		
		Document document = new Document();
		document.put("_id", id);
		document.put("nome", nome);
		document.put("clima", clima);
		document.put("terreno", terreno);
		
		codec.encode(writer, document, encoder);
	}

	public Class<PlanetaVO> getEncoderClass() {
		return PlanetaVO.class;
	}

	public PlanetaVO decode(BsonReader reader, DecoderContext decoder) {
		
		Document document = codec.decode(reader, decoder);
		PlanetaVO planeta = new PlanetaVO();
		planeta.setId(document.getObjectId("_id"));
		planeta.setNome(document.getString("nome"));
		planeta.setClima(document.getString("clima"));
		planeta.setTerreno(document.getString("terreno"));
		
		return planeta;
	}

	public boolean documentHasId(PlanetaVO planeta) {
		return planeta.getId() == null;
	}

	public PlanetaVO generateIdIfAbsentFromDocument(PlanetaVO planeta) {
		return documentHasId(planeta) ? planeta.criarId() : planeta;
	}

	public BsonValue getDocumentId(PlanetaVO planeta) {
		if(!documentHasId(planeta)){
			throw new IllegalStateException("Esse Documento não tem id");
		}
		return new BsonString(planeta.getId().toHexString());
	}

}
