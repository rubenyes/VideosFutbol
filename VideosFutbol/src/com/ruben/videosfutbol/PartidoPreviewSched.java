package com.ruben.videosfutbol;

public class PartidoPreviewSched extends PartidoPreview{

	private String hora;
	
	public PartidoPreviewSched(int posDia, String id, String paisYliga,	String local, String visit, String hora) {
		super(posDia, id, paisYliga, local, visit);
		this.hora = hora;
	}

}
