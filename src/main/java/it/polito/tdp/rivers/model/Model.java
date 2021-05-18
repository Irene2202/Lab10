package it.polito.tdp.rivers.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.rivers.db.RiversDAO;
import it.polito.tdp.rivers.model.Event.EventType;

public class Model {
	
	private RiversDAO dao;
	
	//coda eventi
	private PriorityQueue<Event> queue;
	
	//modello mondo
	private double C; //H2O nel bacino
	private double f_in; //flusso in ingresso
	private double f_out; //flusso in uscita
	
	//parametri input
	private double Q; //Capienza tot bacino
	private double f_out_min;
	private River river;
	
	//parametri output
	private double Cmed; //C media
	private int numGiorni;
	
	
	public Model() {
		dao=new RiversDAO();
	}
	
	public List<River> getAllRivers(){
		return dao.getAllRivers();
	}
	
	public void selezionaFiume(River r) {
		dao.getFlowsForRiver(r);;
		dao.getFlowAvForRiver(r);
	}
	
	//Inizializzo parametri simulazione
	public void init(float k, River r) {
		this.queue=new PriorityQueue<>();
		this.river=r;
		
		this.Q=k*30*r.getFlowAvg();
		this.f_out_min=0.8*r.getFlowAvg();
		
		this.C=Q/2; //imposto liv iniziale di H2O
		
		Cmed=0.0;
		numGiorni=0;
		
		//Creo eventi iniziali (aumento/diminuzione di C
		for(Flow f: river.getFlows()) {
			f_in=f.getFlow();
			
			//Evento irrigazione
			double irr=Math.random();
			if(irr<0.05) {
				this.queue.add(new Event(EventType.IRRIGAZIONE_CAMPI, f.getDay(), f_in, f_out_min));
			} 
			else {
					this.f_out=Math.random()*100;
					if(f_out<f_out_min)
						f_out=f_out_min;
					else if(f_out>river.getMaxFlow())
						f_out=river.getMaxFlow();
					
					if(f_in>f_out) {
						this.queue.add(new Event(EventType.AUMENTO_LIV, f.getDay(), f_in, f_out));
					}
					else if(f_in<f_out) {
						this.queue.add(new Event(EventType.DIMINUZIONE_LIV, f.getDay(), f_in, f_out));
					}
			}
			
		}// fine ciclo for
	}
	
	//Lancio simulazione
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e=this.queue.poll();
			System.out.println(e);
			processEvent(e);
		}
		
	}
	
	private void processEvent(Event e) {
		switch(e.getType()) {
		case AUMENTO_LIV:
			C+=e.getF_in()-e.getF_out();
			if(C>Q) {
				queue.add(new Event(EventType.TRACIMAZIONE, e.getTime(), e.getF_in(), e.getF_out()));
			}
			else {
				this.Cmed=Cmed+C;
			}
			break;
			
		case DIMINUZIONE_LIV:
			C-=e.getF_out()-e.getF_in();
			if(C<0) {
				if(e.getF_out()>this.f_out_min) {
					//il flusso era > di quello min richiesto, verifico se con quello min
					//ho stesso problema, se sì=> non soddisfo richiesta numGiorni++
					C+=e.getF_out()-e.getF_in(); //riporto a val iniziale di C
					C-=Math.abs(e.getF_in()-this.f_out_min);
					if(C<0) {
						numGiorni++;
						C=0; //ipotizzo che se ciò si verifica svuoto il bacino, ovvero tolgo quanto posso
					}
					
				}
			}
			
			this.Cmed+=C;
			break;
			
		case IRRIGAZIONE_CAMPI:
			C-=10*f_out_min;
			if(C<0) {
				//non posso fare irrigazione: do flusso uscita min richiesto
				C+=10*f_out_min;
				C-=f_out_min;
			}
			this.Cmed+=C;
			break;
			
		case TRACIMAZIONE:
			C=C-e.getF_in();
			this.Cmed+=C;
			break;
		}
	}
	
	public double getCMed() {
		return this.Cmed/river.getFlows().size();
	}
	
	public int getNumGiorni() {
		return numGiorni;
	}

}
