package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.generate.GnmRandomBipartiteGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	
	private Graph<Country, DefaultEdge> grafo;
	private Map<Integer, Country> idMap;

	public Model() {
		idMap = new HashMap<Integer, Country>();
	}
	
	public void creaGrafo(int anno) {
		
		//Creo grafo e idMap
		grafo = new SimpleGraph<>(DefaultEdge.class);
		
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
		
		System.out.print("GRAFO CREATO\n#VERTICI: "+grafo.vertexSet().size()+"\n#ARCHI: "+grafo.edgeSet().size()+"\n\n");
	}
	
	/**
	 * Metodo per ottenere il numero di componenti connesse (sottografi connessi) del grafo
	 * @return #componentiConnesse
	 */
	public int getNumComponentiConnesse() {
		ConnectivityInspector<Country, DefaultEdge> inspector = new ConnectivityInspector<>(grafo);
		return inspector.connectedSets().size();
	}
	
	/**
	 * Metodo per restiutuire la lista degli stati vicini dato uno stato
	 * (DephFirstIterator)
	 */
	public List<Country> trovaVicini1(Country partenza) {
		
		List<Country> raggiungibili = new ArrayList<Country>();
		
		//CREO ITERATORE E LO ASSOCIO AL GRAFO      
		//GraphIterator<Fermata, DefaultEdge> it = new BreadthFirstIterator<>(this.grafo,source); //in ampiezza
		GraphIterator<Country, DefaultEdge> it = new DepthFirstIterator<>(this.grafo,partenza); //in profondita'
		
		while(it.hasNext()) {
			raggiungibili.add(it.next());
		}
		
		//Pulisco il primo valore della lista che è lo stato stesso
		return raggiungibili.subList(1, raggiungibili.size());
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
