package com.ruben.videosfutbol;

/**
 * Esta clase sera para representar los partidos que se mostrarn en listView en las pestañas de: ayer,hoy,mañana
 * Solo tendra los datos basicos que podeos obtener desde la pagina m.mismarcadores.com
 * Para los datos ya mas detallados del partido tenemos la clase Partido a la que le pasaremos como parametro
 * su PartidoPreview correspondiente.
 */
public abstract class PartidoPreview {

	protected int numDia; //esto sera -1,0,1 dependiendo si el partido es de ayer,hoy,mañana
	protected String id;
	protected String paisYliga;
	protected String local;
	protected String visit;
    
	public PartidoPreview(int posDia, String id, String paisYliga, String local, String visit) {
		this.numDia = posDia;
		this.id = id;
		this.paisYliga = paisYliga;
		this.local = local;
		this.visit = visit;
	}

	public int getPosDia() {
		return numDia;
	}

	public String getId() {
		return id;
	}

	public String getPaisYliga() {
		return paisYliga;
	}

	public String getLocal() {
		return local;
	}

	public String getVisit() {
		return visit;
	}
	
	public abstract String getHoraOminuto();
	public abstract String getMarcador();
}
