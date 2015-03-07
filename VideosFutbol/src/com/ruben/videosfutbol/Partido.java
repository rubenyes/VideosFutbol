package com.ruben.videosfutbol;

import java.io.Serializable;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;


/**
 * Esta clase contendra los datos detallados de cada partido. Y un atributo con su correspondiente PartidoPreview.
 * 
 * DEBERIA IMPLEMENTENTAR EL Parceable que tarda mucho menos:
 * http://www.developerphil.com/parcelable-vs-serializable/
 * http://elbauldelprogramador.com/adapter-personalizado-en-android/
 * http://stackoverflow.com/questions/2139134/how-to-send-an-object-from-one-android-activity-to-another-using-intents
 * http://www.javacodegeeks.com/2014/01/android-tutorial-two-methods-of-passing-object-by-intent-serializableparcelable.html
 */
public class Partido implements Serializable{
	
	private PartidoPreview ppre;
	
    private String id;
    private String paisYliga;
    private String local;
    private String visit;
    private String marcadoresAll;
    private String estadoOminuto;
    private String fechaYhora;
    private boolean partidoIdaVuelta; //se inicializa sola a false, asi que solo la tocare para ponerla a true si el partido es de Ida/Vuelta.
    private String idaVuelta;
    
    private String marcador, marcador1tiempo, marcador2tiempo;
    
    private ArrayList<String> acciones1tiempo = new ArrayList<String>();
    private ArrayList<String> acciones2tiempo = new ArrayList<String>();
            
    private ArrayList<String> jugTitularesLoc = new ArrayList<String>();
    private ArrayList<String> jugSuplentesLoc = new ArrayList<String>();
    private ArrayList<String> jugTitularesVis = new ArrayList<String>();
    private ArrayList<String> jugSuplentesVis = new ArrayList<String>();
    
    private ArrayList<Video> videos = new ArrayList<Video>();
    
    public Partido(PartidoPreview ppre)throws Exception{
    	this.ppre = ppre;
        this.id = ppre.getId();
        this.paisYliga = ppre.getPaisYliga();
        
        extraerDatosPartido();
        extraerAlineaciones();
        extraerVideos();
    }
        
    public void extraerDatosPartido()throws Exception{
        Document doc = Jsoup.connect("http://m.mismarcadores.com/partido/"+id+"/").get();
        
        Element main = doc.getElementById("main");
        String titulo = main.getElementsByTag("h3").first().text();
        String[] aux = titulo.split(" - ");
        this.local = aux[0];
        this.visit = aux[1];
        
        //Si el partido no ha empezado solo tendra un "detail" con la fechaYhora. O 2 detail si es un partido de eliminatoria ida/veulta:
        //  1. 1治uelta,2治uelta
        //  2. fechaYhora
        //Si el partido ya ha empezado para tendra unas cabeceras de tipo "detail" con:
        //  1. marcdor
        //  2. minuto(tiempo)/finalizado
        //  3. fechaYhora
        //Solo que si es un partido de eliminatoria ida/vuelta, en vez de tener la fechaYhora en 3pos, tendra:
        //  3. 1治uelta,2治uelta  //tambien puede que no tenga esto si es un partido de la fase de grupos
        //  4. fechaYhora
        //Y por ultimo tendra un "detail" mas para las acciones del primer tiempo, y si ha empezado el 2 tiempo, tendra otro "detail" extra.
        
        //Aqui estaran todos los "detail" pero olo utilizare los de la cabecera.
        Elements details = main.getElementsByClass("detail"); 
        
        //Aqui solo estan los "detail" de las acciones de cada tiempo, asi que su longitud sera 0 (no empezado), 1 (1ra parte), 2 (2da parte).
        Elements detailsAcciones = main.getElementById("detail-tab-content").getElementsByClass("detail");
        
        int sizeCabeceras = details.size()-detailsAcciones.size();
        
        if(sizeCabeceras==1) this.fechaYhora = details.get(0).text();
        else if(sizeCabeceras==1){
            this.idaVuelta = details.get(0).text();
            this.fechaYhora = details.get(1).text();
        }
        else {            
            this.marcadoresAll = details.get(0).text();
            this.estadoOminuto = details.get(1).text();
            
            if(sizeCabeceras==3) this.fechaYhora = details.get(2).text();
            else{ //esto sera si sizeCabeceras==4
                this.partidoIdaVuelta = true;
                this.idaVuelta = details.get(2).text();
                this.fechaYhora = details.get(3).text();
            }
                                               
            Elements tiempo1 = detailsAcciones.get(0).children();
            tiempo1.remove(tiempo1.size()-1); //borramos el ultimo elemento ya que este no es una accion.
            for(Element e : tiempo1) acciones1tiempo.add(e.child(1).className()+"\t"+e.text());
            
            if(detailsAcciones.size()>1){ //comprobamos si ya se esta jugando o se ha jugado el segundo tiempo.
                Elements tiempo2 = detailsAcciones.get(1).children();
                tiempo2.remove(tiempo2.size()-1); //borramos el ultimo elemento ya que este no es una accion.
                for(Element e : tiempo2) acciones2tiempo.add(e.child(1).className()+"\t"+e.text());
            }
            
            Elements marcadores = main.getElementsByTag("b");
            this.marcador = marcadores.get(0).text();
            this.marcador1tiempo = marcadores.get(1).text();
            if(marcadores.size()>2) this.marcador2tiempo = marcadores.get(2).text();
        }       
        
    }
    
    public void extraerAlineaciones()throws Exception{
        Document doc = Jsoup.connect("http://m.mismarcadores.com/partido/"+id+"/?t=alineaciones").get();        
        Elements tables = doc.getElementsByTag("table");
        
        //Creamos este array de ArrayList auxiliar para no tener que repetir codigo, y poder acceder a las diferentes listas con un unico for.
        @SuppressWarnings("unchecked")
		ArrayList<String>[] jugadores = new ArrayList[4];
        jugadores[0]=jugTitularesLoc;
        jugadores[1]=jugSuplentesLoc;
        jugadores[2]=jugTitularesVis;
        jugadores[3]=jugSuplentesVis;
        
        if(tables.size()==4){
            for(int i=0; i<4; i++){
                //tenemos que coger los hijos del primer hijo, ya que el Jsoup se inventa un <tbody> que contiene todos los <tr>.
                //Pero dicho <tbody> no se porque no aparece si vemos el codigo fuente en chrome.
                Elements players = tables.get(i).child(0).children(); 
                for(Element e : players) jugadores[i].add(e.text());
            }
        }
    }
    
    public void extraerVideos()throws Exception{
        ArrayList<String> textosVideos = new ArrayList<String>();
        ArrayList<String> linksVideos = new ArrayList<String>();
    
        Document doc = Jsoup.connect("http://d.mismarcadores.com/x/feed/d_hi_"+id+"_es_1").header("X-Fsign","SW9D1eZo").get();
        //System.out.println(doc.outerHtml());
        
        Elements textos = doc.getElementsByTag("th");
        for(Element texto : textos) {
            textosVideos.add(texto.text());
        }
        
        Elements links = doc.select("a[href]");
        for(Element link : links) {
            linksVideos.add(link.attr("abs:href"));
        }
        
        //Se supone que linksVideos.size()==nombresVideos.size().
        for(int i=0; i<linksVideos.size(); i++){
            Video v = new Video(this, linksVideos.get(i), textosVideos.get(i));
            videos.add(v);
        }
        
        //PODRIA SEPARA QUIZAS LOS TEXTOS, Y COMPROBAR LAS ETIQUETAS DE ICONOS, QUE DICEN SI EL PENALTY FUE FALALDO O NO, Y SI LA TAREJTA FUE ROJA Y ESO.        
    }
}
