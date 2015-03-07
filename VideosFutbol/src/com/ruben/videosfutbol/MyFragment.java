package com.ruben.videosfutbol;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MyFragment extends SherlockFragment {
	
	private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;
    
	private static String KEY_CONTENT = "myfragment:content";	
	private String mContent;
	
	private int position;
	private Dia dia;
	
	ListView list;
	ListViewAdapter adapter;
	
	public static MyFragment newInstance(String content, int position){
		
		MyFragment fragment = new MyFragment();
		fragment.mContent = content;
		fragment.position = position;
		
		//De momento esto funciona porque solo hay 3 dias, ayer(-1), hoy(0), y mañana(1)
		int numDia = position - 1;
		
		try {
			fragment.dia = new Dia(numDia);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		
		return fragment;
	}
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_page, container, false);
		
		list = (ListView) rootView.findViewById(R.id.listview);
		
		adapter = new ListViewAdapter(getActivity());
		anyadirDatosAlLisAdapter();
		
		list.setAdapter(adapter);
		
		// Capture clicks on ListView items
		list.setOnItemClickListener(new OnItemClickListener() { 
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int tipo = adapter.getItemViewType(position);
			
				//Si el elemento clicado es un HEADER no haremos nada, y si es un partido abriremos su actividad de detalle.
				switch(tipo){
					case TYPE_ITEM:
						Intent i = new Intent(getActivity(), PartidoActivity.class);
						
						Partido p = null;
						try { //esta excepcion nunca va a saltar, pues ya sabemos que es TYPE_ITEM pero me obliga a ponerla
							p = new Partido((PartidoPreview)adapter.getItem(position));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						i.putExtra("partido", p);
						startActivity(i);
	                    break;
	                case TYPE_HEADER:
	                    break;
				}
			}
 
		});
		
		return rootView;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
	
	
	/**
	 * Vamos a meter en el ArrayList de tipo Object del ListViewAdapter "adapter", los nombres de cada liga
	 * intercalados con sus partidos en el orden correspondiente.
	 */
	private void anyadirDatosAlLisAdapter(){
		String[] ligas = dia.getLigas();
		ArrayList<PartidoPreview>[] partidos = dia.getPartidos();
		
		int numDataTotal = ligas.length + dia.getPartidosTotales();
		
		for (int i=0; i<ligas.length; i++){
			adapter.addHeaderItem(ligas[i]);
			
			for(int j=0; j<partidos[i].size(); j++){
				adapter.addItem(partidos[i].get(j));
			}
        }
	}
}
