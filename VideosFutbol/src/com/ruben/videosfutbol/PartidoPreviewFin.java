package com.ruben.videosfutbol;

public class PartidoPreviewFin extends PartidoPreview{

	private String hora;
	private String resultado;
	
	public PartidoPreviewFin(int posDia, String id, String paisYliga, String local, String visit, String hora, String resultado) {
		super(posDia, id, paisYliga, local, visit);
		this.hora = hora;
		this.resultado = resultado;
	}
	
	@Override
	public String getHoraOminuto(){
		return hora;
	}
	
	@Override
	public String getMarcador(){
		return resultado;
	}
}
