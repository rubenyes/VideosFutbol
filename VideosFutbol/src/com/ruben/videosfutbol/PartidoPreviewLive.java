package com.ruben.videosfutbol;

public class PartidoPreviewLive extends PartidoPreview{
	
	private String minuto;
	private String marcador;
	
	public PartidoPreviewLive(int posDia, String id, String paisYliga, String local, String visit, String minuto, String marcador) {
		super(posDia, id, paisYliga, local, visit);
		
		this.minuto = minuto;
		this.marcador = marcador;
	}

	@Override
	public String getHoraOminuto(){
		return minuto;
	}
	

	@Override
	public String getMarcador(){
		return marcador;
	}
}
