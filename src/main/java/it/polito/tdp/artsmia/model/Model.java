package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private SimpleWeightedGraph<Artist, DefaultWeightedEdge> grafo;
	private ArtsmiaDAO dao;
	private Map<Integer,Artist> idMap;
	private boolean grafoCreato;
	private List<Adiacenza> adiacenze;
	//private ConnectivityInspector<Vertice, DefaultWeightedEdge> ci; //se chiede roba connessa ad un vertice
	
	private List<Artist> best; 
	
	public Model() {
		this.dao = new ArtsmiaDAO();
		this.idMap= new HashMap<>();
		this.dao.loadIdMap(idMap); 
		this.grafoCreato=false;
		this.adiacenze= new ArrayList<>();
		//this.percorsoBest= new ArrayList<>(); //per ricorsione
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(grafo, dao.getVertici(idMap,ruolo));
		this.adiacenze= this.dao.getAdiacenze(idMap, ruolo);
		for (Adiacenza a : this.adiacenze) {
			if (grafo.vertexSet().contains(a.getA1()) && grafo.vertexSet().contains(a.getA2())) {
				Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), (double)a.getPeso());
			}
		}
		this.grafoCreato=true;
		//this.ci= new ConnectivityInspector<>(grafo);
	}
	
	public List<String> getRuoli(){
		return dao.ruoli();
	}
	
	public boolean isGrafoCreato() {
		return grafoCreato;
	}

	public int getNumVertici() {
		return grafo.vertexSet().size();
	}
	
	public int getNumArchi() {
		return grafo.edgeSet().size();
	}

	public Set<Artist> getVertici(){
		return grafo.vertexSet();
	}
	
	public List<Adiacenza> getCoppieArtisti(){
		List<Adiacenza> result= new ArrayList<>();
		result= this.adiacenze;
		Collections.sort(result);
		return result;
	}
	
	public List<Artist> percorsoMax(int id){ 
		Artist artist= null;
		artist = idMap.get(id);
		if(artist==null)
			return null; //id dell'artista sbagliato
		else {
			this.best=null;
			List<Artist> parziale= new ArrayList<>();
			parziale.add(artist);  
			ricorsione(parziale, -1);
			return this.best;
		}
		
	}
	private void ricorsione(List<Artist> parziale, int peso){
		Artist ultimo = parziale.get(parziale.size()-1);
		List<Artist> adiacenti= Graphs.neighborListOf(grafo, ultimo);
		for (Artist a : adiacenti) {
			if (!parziale.contains(a) && peso==-1) { //primo ciclo
				parziale.add(a);
				ricorsione(parziale, (int)grafo.getEdgeWeight(grafo.getEdge(ultimo, a)) );
				parziale.remove(a);
			}
			else {
				if(!parziale.contains(a) && this.grafo.getEdgeWeight(grafo.getEdge(ultimo, a))==peso ) {
					parziale.add(a);
					ricorsione(parziale, peso );
					parziale.remove(a);
				}
			}
		}
		if(best==null || parziale.size()>best.size()) {
			this.best= new ArrayList<>(parziale);
		}
	}
	
	/*private int pesoParziale(List<Artist> parziale) {
		int peso=0;
		int i=0; //indice che mi serve per prendere il l'artista successivo in parziale
		for (Artist a : parziale) {
			if (i==(parziale.size()-1)) 
				break;
			DefaultWeightedEdge e = grafo.getEdge(a, parziale.get(i+1));
			i++;
			peso += grafo.getEdgeWeight(e);
		}
		return peso;
	}

	public int getTotEsposizioni() {
		return this.max;
	}*/
	
}
