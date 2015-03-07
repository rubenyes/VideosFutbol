package com.ruben.videosfutbol;

import java.io.IOException;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class MyFragment extends SherlockFragment {
	
	private static String KEY_CONTENT = "myfragment:content";	
	
	public static MyFragment newInstance(String content, int position){
		
		MyFragment fragment = new MyFragment();
		fragment.mContent = content;
		fragment.position = position;
		
		//De momento esto funciona porque solo hay 3 dias, ayer(-1), hoy(0), y mañana(1)
		int numDia = position - 1;
		
		try {
			fragment.dia = new Dia(numDia);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		return fragment;
	}
	
	private String mContent;
	private int position;
	
	private Dia dia;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View myView = inflater.inflate(R.layout.fragment_page, null);
		
		TextView tv = (TextView) myView.findViewById(R.id.text1);
		tv.setText(mContent);
		
		return myView;
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CONTENT, mContent);
    }
}
