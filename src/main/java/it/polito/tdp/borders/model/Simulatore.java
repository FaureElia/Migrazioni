package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Simulatore {
	
	//Stato del sistema
	private Map<Country, Integer> stanziali;
	
	//Parametri della simulazione
	private int nPersone=1000;
	private Graph <Country, DefaultEdge> graph;
	private Country partenza;
	
	//output
	private int nPassi;
	
	//Coda degli eventi
	private PriorityQueue <Event> queue;
	
	
	public Simulatore (Graph<Country,DefaultEdge> graph,Country partenza) {
		this.graph=graph;
		this.partenza=partenza;
		
		this.stanziali=new HashMap<Country,Integer>(); //country deve avere Hashcode e equals, ma c'è sicuramente perchè è stato inserto nel grafo
		for(Country c: this.graph.vertexSet()) {
			this.stanziali.put(c, 0); //ad ogni paese associo il numero di stanziali presenti!
			
		}
		this.queue=new PriorityQueue<>();
	}
	
	public Map<Country, Integer> simula() {
		this.initialize();
		this.run();
		return this.stanziali;
	}
	
	private void initialize() {
		this.queue.add(new Event(0,this.partenza,this.nPersone));// all'inizio c'è solo un evento!
	}
	
	private void run() {
		while(!this.queue.isEmpty()) {
			Event e=this.queue.poll();
			int time=e.getTime();
			Country destinazione=e.getDestinazione();
			int dimensione=e.getDimensione();
			
			this.nPassi=time;//l'ultimo time che ho visto, grazie alla coda prioritaia, sarà maggiore o uguale 
			
			List<Country> vicini=Graphs.neighborListOf(this.graph, destinazione);
			System.out.println(destinazione+", totale vicini:"+vicini.size()+", totale migranti:"+(dimensione/2)+"="+(dimensione/2/vicini.size()));
			int migranti=dimensione/2/vicini.size(); //quelli che non rimangono lì si dividiono in maniera uguale negli stati confinanti
			//System.out.println(destinazione+" ha "+ vicini.size()+"confinanti");
			//dimensione/2 negli stati adiacenti e generano
			//eventi di ingresso con la quota di persone.
			if(migranti>0) {
				for (Country c: vicini) {
					this.queue.add(new Event(time+1,c,migranti));
				}
			}
			
			//gli altri diventano stanziali nello stato destinazione,
			int nuovi_stanziali=dimensione-migranti*vicini.size();
			this.stanziali.put(destinazione, this.stanziali.get(destinazione)+nuovi_stanziali);
			
			
			
			
			
			
		}
	}
	
	

}
