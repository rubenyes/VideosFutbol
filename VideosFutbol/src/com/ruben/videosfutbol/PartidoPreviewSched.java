package com.ruben.videosfutbol;

public class PartidoPreviewSched extends PartidoPreview{

	private String hora;
	private String marcadorVacio; //pese a ser siempre igual, lo pongo como variable para seguir el mismo modelo en todos los PartidoPreview
	
	public PartidoPreviewSched(int posDia, String id, String paisYliga,	String local, String visit, String hora, String marcadorVacio) {
		super(posDia, id, paisYliga, local, visit);
		this.hora = hora;
		this.marcadorVacio = marcadorVacio;
	}
	
	@Override
	public String getHoraOminuto(){
		return hora;
	}
	
	@Override
	public String getMarcador(){
		return marcadorVacio;
	}
}
