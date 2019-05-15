package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.generate.GnmRandomBipartiteGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	public Model() {
	}
	
	private Graph<Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	
	public void creaGrafo(int anno) {
		
		//Creo grafo e idMap
		grafo = new SimpleGraph<>(DefaultEdge.class);
		idMap = new HashMap<Integer, Country>();
		
		//Carico vertici grafo
		BordersDAO dao = new BordersDAO();
		List<Country> stati = dao.loadAllCountries(idMap);
		Graphs.addAllVertices(grafo, stati);
		
		//Carico archi grafo
		List<Border> confini = dao.getCountryPairs(anno);
		
		for (Border c : confini) {
			
			Country paese1 = idMap.get(c.getId1());
			Country paese2 = idMap.get(c.getId2());
			
			grafo.addEdge(paese1, paese2);
		}
		
		//Elimino vertici senza confini
		for (Country c : stati) {
			if (grafo.degreeOf(c)==0) grafo.removeVertex(c);
		}
		
		System.out.print("GRAFO CREATO\n#VERTICI: "+grafo.vertexSet().size()+"\n#ARCHI: "+grafo.edgeSet().size());
	}

	//GETTERS AND SETTERS
	public Graph<Country, DefaultEdge> getGrafo() {
		return grafo;
	}

	public void setGrafo(Graph<Country, DefaultEdge> grafo) {
		this.grafo = grafo;
	}

	public Map<Integer, Country> getIdMap() {
		return idMap;
	}

	public void setIdMap(Map<Integer, Country> idMap) {
		this.idMap = idMap;
	}
	
	

}
