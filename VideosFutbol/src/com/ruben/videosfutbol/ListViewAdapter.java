package com.ruben.videosfutbol;

import java.util.ArrayList;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class ListViewAdapter extends BaseAdapter {

	private static final int TYPE_ITEM = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_MAX_COUNT = TYPE_HEADER + 1; //en vez de poner 2, ponemos el tipo maximo+1

    private ArrayList mData = new ArrayList();
    private LayoutInflater mInflater;

    private TreeSet mHeadersSet = new TreeSet();

    public ListViewAdapter(Context c) {
    	mInflater = LayoutInflater.from(c);
    }

    public void addItem(final PartidoPreview item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addHeaderItem(final String item) {
        mData.add(item);
        // save separator position
        mHeadersSet.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mHeadersSet.contains(position) ? TYPE_HEADER : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
                
        switch (type) {
            case TYPE_ITEM:            	
            	ViewHolderItem holderItem = null;
            	
            	//Comprobamos si ya se habia creado el view del item/header
                if (convertView == null) { 
                	holderItem = new ViewHolderItem();                	
	                convertView = mInflater.inflate(R.layout.listview_item, null);	                
	                holderItem.textHoraOminuto = (TextView)convertView.findViewById(R.id.textHoraOminuto);
	                holderItem.textLocal = (TextView)convertView.findViewById(R.id.textLocal);
	                holderItem.textMarcador = (TextView)convertView.findViewById(R.id.textMarcador);
	                holderItem.textVisit = (TextView)convertView.findViewById(R.id.textVisit);	                
	                convertView.setTag(holderItem);
                }
                else { 
                	holderItem = (ViewHolderItem)convertView.getTag(); 
                }
                
                holderItem.textHoraOminuto.setText(((PartidoPreview)mData.get(position)).getHoraOminuto());
                holderItem.textLocal.setText(((PartidoPreview)mData.get(position)).getLocal());
                holderItem.textMarcador.setText(((PartidoPreview)mData.get(position)).getMarcador());
                holderItem.textVisit.setText(((PartidoPreview)mData.get(position)).getVisit());
                
                return convertView;
                
            case TYPE_HEADER:
            	ViewHolderHeader holderHeader = null;
        	
            	//Comprobamos si ya se habia creado el view del item/header
            	if (convertView == null) { 
	            	holderHeader = new ViewHolderHeader();
	                convertView = mInflater.inflate(R.layout.listview_header, null);	                
	                holderHeader.textHeader = (TextView)convertView.findViewById(R.id.textHeader);	                
	                convertView.setTag(holderHeader);
            	}
            	else { 
            		holderHeader = (ViewHolderHeader)convertView.getTag(); 
                }
                
            	holderHeader.textHeader.setText((CharSequence)mData.get(position));
            	
                return convertView;
        }//fin del switch
        
        return null;
    }

	public static class ViewHolderItem{
	    public TextView textHoraOminuto;
	    public TextView textLocal;
	    public TextView textMarcador;
	    public TextView textVisit;
	}
	
	public static class ViewHolderHeader{
	    public TextView textHeader;
	}
}