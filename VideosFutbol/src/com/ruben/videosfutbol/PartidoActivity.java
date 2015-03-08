package com.ruben.videosfutbol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class PartidoActivity extends Activity {
	
	private PartidoPreview ppre;
	private Partido p;
	
	TextView textLocal, textVisit;
	TextView textFechaYhora;
	TextView textMarcador;
	TextView textEstadoMinuto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_partido);
		
		Intent i = getIntent();
		this.ppre = (PartidoPreview) i.getSerializableExtra("partidoPreview");
		this.p = new Partido(this, ppre);
		
		textLocal = (TextView) findViewById(R.id.textLocal);
		textVisit = (TextView) findViewById(R.id.textVisit);
		textFechaYhora = (TextView) findViewById(R.id.textFechaYhora);
		textMarcador = (TextView) findViewById(R.id.textMarcador);
		textEstadoMinuto = (TextView) findViewById(R.id.textEstadoMinuto);
		
		textLocal.setText(p.getLocal());
		textVisit.setText(p.getVisit());
		textFechaYhora.setText(p.getFechaYhora());
		textMarcador.setText(p.getMarcador());
		textEstadoMinuto.setText(p.getEstadoOminuto());
	}
	
	public void updateView(){
		textLocal.setText(p.getLocal());
		textVisit.setText(p.getVisit());
		textFechaYhora.setText(p.getFechaYhora());
		textMarcador.setText(p.getMarcador());
		textEstadoMinuto.setText(p.getEstadoOminuto());		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.partido, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
