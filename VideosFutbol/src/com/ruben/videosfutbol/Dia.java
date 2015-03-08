package com.ruben.videosfutbol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import android.os.AsyncTask;

public class Dia {
    //Los nombres de las ligas tienen que estar en orden alfabetico, que es el orden en que aparecen en m.mismarcadores.com, si ellos cambiaran el criterio
    //de ordenacion yo tambien tendria que cambiar el orden en el que pongo los strings de las ligas en el siguiente array.
    private String[] ligas = {"ALEMANIA: Bundesliga", 
                              "ESPAÑA: Liga BBVA",
                              "FRANCIA: Ligue 1",
                              "INGLATERRA: Premier League",
                              "ITALIA: Serie A"};
     
     private MyFragment fragment;
     private int numDia;
     
     private ArrayList<PartidoPreview> alemania = new ArrayList<PartidoPreview>();
     private ArrayList<PartidoPreview> espanya = new ArrayList<PartidoPreview>();
     private ArrayList<PartidoPreview> francia = new ArrayList<PartidoPreview>();
     private ArrayList<PartidoPreview> inglaterra = new ArrayList<PartidoPreview>();
     private ArrayList<PartidoPreview> italia = new ArrayList<PartidoPreview>();
     
     //Creamos este array de ArrayList auxiliar para no tener que repetir codigo, y poder acceder a las diferentes listas con un unico for.
     @SuppressWarnings("unchecked")
     private ArrayList<PartidoPreview>[] partidos = new ArrayList[5];
     
     private int partidosTotales;
     private Document doc;
     
	/**
     * De momento esto funciona porque solo hay 3 dias, ayer(-1), hoy(0), y mañana(1)
     * asi que numDia valdra: -1,0,1.
     * @throws IOException 
     */
    public Dia(MyFragment fragment, int numDia){
    	this.fragment = fragment;
    	this.numDia = numDia;
    	this.partidosTotales = 0;
    	
        partidos[0]=alemania;
        partidos[1]=espanya;
        partidos[2]=francia;
        partidos[3]=inglaterra;
        partidos[4]=italia;
        
    	new MyTask().execute(); //Para conectarse a internet hay que utilizar una AsyncTask
    }
    
    private class MyTask extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params){
			try {
				System.out.println("dentro de tarea sin iniciar");
		        doc = Jsoup.connect("http://m.mismarcadores.com/?d="+numDia).get();
				System.out.println("dentro de tarea justo al acabar");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} 
		
		@Override
		protected void onPostExecute(Void param) {
			extraerFixtures();
			
		    fragment.anyadirDatosAlListAdapter();
			System.out.println("data actualizada");
		}
    }
        
    public void extraerFixtures(){
    	//Document doc = Jsoup.connect("http://m.mismarcadores.com/?d="+numDia).get();
        
        Element data = doc.getElementById("score-data");
        List<TextNode> hijosTexto = data.textNodes();
        Elements lineas = data.children();
        Elements h4s = lineas.select("h4");
        
        int j=0; //pongo el contador fuera para que el programa sea mas eficiente y no vuelva a recorrer el array desde la primera pos, sino desde 
                 //la ultima comprobada, para que esto funcione hay que poner las ligas en orden alfabetico en el array "ligas".
                
        for(int i=0; i<ligas.length; i++){
        	//Primero vamos a sacar todos los datos por separado y luego crearemos los PartidoPreview
        	ArrayList<String> nombresEquipos = new ArrayList<String>();
        	ArrayList<String> horasPartidos = new ArrayList<String>();
        	ArrayList<String> idsPartidos = new ArrayList<String>();
        	
        	
            Element liga = h4s.select(":matchesOwn("+ligas[i]+"$)").first();
            int pos = h4s.indexOf(liga);
            
            //Si no encontramos el titulo de la liga, es que este dia no hay partidos de ella, y pasamos a la siguiente liga.
            if(pos==-1) continue;
            
            //No se muy bien porque pero hay que usar el elementSiblingIndex(), en vez del siblingIndex(), ya que dan un numero diferente.
            int indexIni = h4s.get(pos).elementSiblingIndex(); //esto es equivalente a: bundesliga.elementSiblingIndex()
            int indexFin = h4s.get(pos+1).elementSiblingIndex();
            
            //Estos indices se usan para sacar los nombres de los quipos ya que su texto no esta encapsulado dentro de ninguna etiqueta.
            int indexIniTextos = h4s.get(pos).siblingIndex();
            int indexFinTextos = h4s.get(pos+1).siblingIndex();
            
            //TODO ESTO ES PARA SACAR LOS NOMBRES DE LOS EQUIPOS, ES DIFICL PORQUE PUEDEN ESTAR SEPARADOS EN DIFERENTES Element
            for(; j<hijosTexto.size(); j++){
                int index = hijosTexto.get(j).siblingIndex();
                if(index>indexFinTextos) break; //ya hemos recogido todos los nombres de los equipos de los partidos de la liga actual.
                if(index>=indexIniTextos){ //mientras idex<indexIniTextos significa que todavia no hemos llegado a donde estan los textos de la liga actual.
                    String texto = hijosTexto.get(j).text();
                    //El siguiente es el caso perfecto, caso mas comun, en el que el nombre del equipo local y visitante estan seguidos, en el mismo nodo.
                    if(texto.contains(" - ")){ 
                        nombresEquipos.add(texto);
                    }
                    //Luego hay casos en los que el nombre del equipo local y el del visitante estan en nodos separados, debido a que si el equipo local
                    //tiene alguna tarjeta roja, se intercala un elemento <img>, tambien puede ser que texto=" ", si la tarjeta la tiene el visitante.
                    else if(!texto.equals(" ")){
                        String textoSiguiente = hijosTexto.get(j+1).text(); //cogemos el nombre del equipo visitante que estara en el nodo siguiente.
                        nombresEquipos.add(texto+textoSiguiente);
                        j++; //nos saltamos el siguiente nodo pues ya lo hemos tenido en cuenta ahora.
                    }
                }
            }
            //FIN - NOMBRES
            
            Elements lineasLiga = lineas.select(":gt("+indexIni+"):lt("+indexFin+")");
            
            Elements horas = lineasLiga.select("span");            
            for(Element e : horas) {
                if(e.hasClass("live")) horasPartidos.add("VIVO:"+e.text());
                else horasPartidos.add(e.text());
            }
            
            Elements links = lineasLiga.select("a");            
            for(Element e : links) {
                String link = e.attr("href"); //formato: "/partido/Me60K5Dt/?d=1" o "/partido/Me60K5Dt/"
                idsPartidos.add(link.substring(9,17)); 
            }
            
            //AQUI VAMOS A IR CREANDO LOS PartidoPreview, pues vamos a ir viendo si es: Sched, Live, Fin.
            for(int z=0; z<links.size(); z++) {
            	Element e = links.get(z);
            	
            	String[] nombres = nombresEquipos.get(z).split(" - ");
            	
            	PartidoPreview pp = null;
            	
            	if(e.hasClass("sched")) 
            		pp = new PartidoPreviewSched(numDia, idsPartidos.get(z), ligas[i], nombres[0], nombres[1], horasPartidos.get(z), e.text());
            	else if(e.hasClass("live"))
            		pp = new PartidoPreviewLive(numDia, idsPartidos.get(z), ligas[i], nombres[0], nombres[1], horasPartidos.get(z), e.text());
            	else if(e.hasClass("fin"))
            		pp = new PartidoPreviewFin(numDia, idsPartidos.get(z), ligas[i], nombres[0], nombres[1], horasPartidos.get(z), e.text());
            	System.out.println(ligas[i]+" "+pp.getId());
            	partidos[i].add(pp);
            	partidosTotales++;
            }            
        }                
    }
    

    public int getPartidosTotales() {
		return partidosTotales;
	}

	public String[] getLigas() {
		return ligas;
	}

	public ArrayList<PartidoPreview>[] getPartidos() {
		return partidos;
	}
    
    
    
    /**
     * RESUMEN PARTIDO:     http://d.mismarcadores.com/x/feed/d_su_hrXAK3Wc_es_1
     * ULTIMOS COMENTARIOS: http://d.mismarcadores.com/x/feed/d_pv_hrXAK3Wc_es_1
     * TODOS COMENTARIOS:   http://d.mismarcadores.com/x/feed/d_ph_hrXAK3Wc_es_1
     * ESTADISTICAS:        http://d.mismarcadores.com/x/feed/d_st_hrXAK3Wc_es_1
     * ESTADISTICAS INDIV:  http://d.mismarcadores.com/x/feed/d_ps_hrXAK3Wc_es_1
     * ALINEACIONES:        http://d.mismarcadores.com/x/feed/d_li_hrXAK3Wc_es_1
     * 
     * COMP DE CUOTAS:      http://d.mismarcadores.com/x/feed/d_od_hrXAK3Wc_es_1_eu
     * 
     * H2H:                 http://d.mismarcadores.com/x/feed/d_hh_hrXAK3Wc_es_1
     * 
     * VIDEO:               http://d.mismarcadores.com/x/feed/d_hi_vaZMH1GG_es_1 
     * 
     * 
     * LIGA BBVA
     * mas resultados 1:    http://d.mismarcadores.com/x/feed/tr_1_176_320_160_1_1_es_1 si cambio el 160 por 137 me coge de la temporda anterior 13-14
     * mas resultados 2:    http://d.mismarcadores.com/x/feed/tr_1_176_320_160_2_1_es_1 
     * mas partidos 1:      http://d.mismarcadores.com/x/feed/tf_1_176_320_160_1_1_es_1
     * 
     * LIGA ADEANTE:
     * mas resultados 1:    http://d.mismarcadores.com/x/feed/tr_1_176_321_160_1_1_es_1
     * mas partidos 1:      http://d.mismarcadores.com/x/feed/tf_1_176_321_160_1_1_es_1
     * 
     * BUNDESLIGA:
     * mas resultados 1:    http://d.mismarcadores.com/x/feed/tr_1_81_160_160_1_1_es_1
     * 
     * LIGUE 1:
     * mas resultados 1:    http://d.mismarcadores.com/x/feed/tr_1_77_151_160_1_1_es_1 
     * mas partidos 1:      http://d.mismarcadores.com/x/feed/tf_1_77_151_160_1_1_es_1
     * 
     * LIGUE 2:
     * mas resultados 1:    http://d.mismarcadores.com/x/feed/tr_1_77_152_160_1_1_es_1
     * 
     * 
     * MISMARCADORES PAGINA PRINCIPAL, TODOS LOS PARTIDOS:
     * Ayer:    http://d.mismarcadores.com/x/feed/f_1_-1_1_es_1
     * Hoy:     http://d.mismarcadores.com/x/feed/f_1_0_1_es_1
     * Manyana: http://d.mismarcadores.com/x/feed/f_1_1_1_es_1
     * 
     * http://m.mismarcadores.com/?d=-1 o http://m.mismarcadores.com/?d=1
     */
}
