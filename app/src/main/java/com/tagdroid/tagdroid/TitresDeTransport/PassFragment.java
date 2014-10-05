package com.tagdroid.tagdroid.TitresDeTransport;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.tagdroid.tagdroid.R;

public class PassFragment extends Fragment implements OnItemSelectedListener {
	private Tracker tracker;
	private int x = 0, y = 0;
	
	public static Fragment newInstance() {
        return new PassFragment();
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) return null;
		View view = inflater.inflate(R.layout.pass, container, false);
				
		String[] items = new String[] {"Tout public", "Jeune (4 à 18 ans)",
									"Etudiant (19 à 25 ans)", "Plus de 65 ans",
									"Plus de 75 ans", "Invalide (à plus de 80%)","PDE"};
		
		String[] items2 = new String[] {"Aucun", "QF 395 € ou moins",
									"QF entre 396 et 480 €", "QF entre 481 et 570 €",
									"QF entre 571 et 640 €"};
		
		Spinner spinner = (Spinner) view.findViewById(R.id.spinner2);
		spinner.setOnItemSelectedListener(this);
		Spinner spinner2 = (Spinner) view.findViewById(R.id.spinner3);
		spinner2.setOnItemSelectedListener(this);
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items2);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter2);
		

        return view;
	}  
	
	@Override
    public void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.tracker = EasyTracker.getInstance(this.getActivity());
    }

	
	 @Override
	    public void onResume() {

	        super.onResume();

	        this.tracker.set(Fields.SCREEN_NAME, ((Object) this).getClass().getSimpleName());
	        this.tracker.send( MapBuilder.createAppView().build() );
	    }
	
	 @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
     public void onItemSelected(AdapterView<?> parent, View view2,int pos, long id) {
		 LinearLayout fond_pass = (LinearLayout) getView().findViewById(R.id.fond_pass);
		 LinearLayout fond_pass2 = (LinearLayout) getView().findViewById(R.id.fond_pass2);
		 LinearLayout fond_pastel = (LinearLayout) getView().findViewById(R.id.fond_pastel);
		 TextView passmensuel_prix = (TextView) getView().findViewById(R.id.passmensuel_prix);
		 TextView passannuel_prix = (TextView) getView().findViewById(R.id.passannuel_prix);
		 TextView passannuel_bonus = (TextView) getView().findViewById(R.id.passannuel_bonus);
		 TextView pass_name = (TextView) getView().findViewById(R.id.pass_name);
		 TextView pass_pastel = (TextView) getView().findViewById(R.id.pass_pastel);
		 
		 LinearLayout selector1 = (LinearLayout) getView().findViewById(R.id.selector1);
		 LinearLayout selector2 = (LinearLayout) getView().findViewById(R.id.selector2);
		 LinearLayout selector3 = (LinearLayout) getView().findViewById(R.id.selector3);
		 LinearLayout selector4 = (LinearLayout) getView().findViewById(R.id.selector4);
		 LinearLayout selector5 = (LinearLayout) getView().findViewById(R.id.selector5);
		 
		 
		 String mensuel_prix = null, annuel_prix = null, annuel_bonus = null;
		  
		 if(parent.getId() == R.id.spinner2) x=pos;
		 if(parent.getId() == R.id.spinner3) y=pos;
				 
		 switch(y){
		 	case 0:  	fond_pass.setAlpha(1);
 						fond_pass2.setAlpha(1);
 						pass_pastel.setText("");
 						selector1.setVisibility(View.VISIBLE);
 						selector2.setVisibility(View.INVISIBLE);
 						selector3.setVisibility(View.INVISIBLE);
 						selector4.setVisibility(View.INVISIBLE);
 						selector5.setVisibility(View.INVISIBLE);
 						break;
		 	case 1:		fond_pass.setAlpha(.4f);
						fond_pass2.setAlpha(.4f);
						pass_pastel.setText("Pastel 4");
						pass_pastel.setAlpha(.4f);
						selector1.setVisibility(View.INVISIBLE);
						selector2.setVisibility(View.INVISIBLE);
						selector3.setVisibility(View.INVISIBLE);
						selector4.setVisibility(View.INVISIBLE);
						selector5.setVisibility(View.VISIBLE);
						break;
		 	case 2:		fond_pass.setAlpha(.55f);
		 				fond_pass2.setAlpha(.55f);
		 				pass_pastel.setText("Pastel 3");
		 				pass_pastel.setAlpha(.55f);
		 				selector1.setVisibility(View.INVISIBLE);
		 				selector2.setVisibility(View.INVISIBLE);
		 				selector3.setVisibility(View.INVISIBLE);
		 				selector4.setVisibility(View.VISIBLE);
		 				selector5.setVisibility(View.INVISIBLE);
		 				break;
		 	case 3:		fond_pass.setAlpha(.75f);
						fond_pass2.setAlpha(.75f);
						pass_pastel.setText("Pastel 2");
						pass_pastel.setAlpha(.75f);
						selector1.setVisibility(View.INVISIBLE);
						selector2.setVisibility(View.INVISIBLE);
						selector3.setVisibility(View.VISIBLE);
						selector4.setVisibility(View.INVISIBLE);
						selector5.setVisibility(View.INVISIBLE);
						break;
			case 4:		fond_pass.setAlpha(.9f);
						fond_pass2.setAlpha(.9f);
						pass_pastel.setText("Pastel 1");
						pass_pastel.setAlpha(.9f);
						selector1.setVisibility(View.INVISIBLE);
						selector2.setVisibility(View.VISIBLE);
						selector3.setVisibility(View.INVISIBLE);
						selector4.setVisibility(View.INVISIBLE);
						selector5.setVisibility(View.INVISIBLE);
						break;

		 }

		 if(x==0){		 
			 pass_name.setText("PASS VANILLE");
			 fond_pass.setVisibility(View.VISIBLE);
			 fond_pastel.setVisibility(View.VISIBLE);
			 fond_pastel.setBackgroundColor(getResources().getColor(R.color.vanille));
			 pass_name.setTextColor(getResources().getColor(R.color.vanille));
			 pass_pastel.setTextColor(getResources().getColor(R.color.vanille));
			 fond_pass.setBackground(getResources().getDrawable(R.drawable.fond_pass_vanille));			 
			 fond_pass2.setBackground(getResources().getDrawable(R.drawable.fond_pass_vanille));
			 annuel_bonus = getResources().getString(R.string.pass_2mois); 
			 if(y==0){ 	mensuel_prix = "49.20 �";annuel_prix ="492.00 �"; }	 			
			 if(y==1){ mensuel_prix = "2.50 �"; annuel_prix ="492.00 �"; }
			 if(y==2){ mensuel_prix = "9.80 �"; annuel_prix ="492.00 �"; }
			 if(y==3){ mensuel_prix = "14.80 �"; annuel_prix ="492.00 �"; }
			 if(y==4){ mensuel_prix = "19.70 �"; annuel_prix ="492.00 �"; }
		 }
		 if(x==1){
			 pass_name.setText("PASS' GRENADINE");
			 fond_pass.setVisibility(View.VISIBLE);
			 fond_pastel.setVisibility(View.VISIBLE);
			 fond_pastel.setBackgroundColor(getResources().getColor(R.color.grenadine));
			 pass_name.setTextColor(getResources().getColor(R.color.grenadine));
			 pass_pastel.setTextColor(getResources().getColor(R.color.grenadine));
			 fond_pass.setBackground(getResources().getDrawable(R.drawable.fond_pass_grenadine));
			 fond_pass2.setBackground(getResources().getDrawable(R.drawable.fond_pass_grenadine));
			 annuel_bonus = getResources().getString(R.string.pass_2mois); 
			 if(y==0){ mensuel_prix = "15.80 �"; annuel_prix ="158.00 �"; }
			 if(y==1){ mensuel_prix = "2.20 �"; annuel_prix ="158.00 �"; }
			 if(y==2){ mensuel_prix = "9.80 �"; annuel_prix ="158.00 �"; }
			 if(y==3){ mensuel_prix = "14.80 �"; annuel_prix ="158.00 �"; }
			 if(y==4){ mensuel_prix = "15.80 �"; annuel_prix ="158.00 �"; }
		 }
		 if(x==2){
			 pass_name.setText("PASS' CAFE");
			 fond_pass.setVisibility(View.VISIBLE);
			 fond_pastel.setVisibility(View.VISIBLE);
			 fond_pastel.setBackgroundColor(getResources().getColor(R.color.cafe));
			 pass_name.setTextColor(getResources().getColor(R.color.cafe));
			 pass_pastel.setTextColor(getResources().getColor(R.color.cafe));
			 fond_pass.setBackground(getResources().getDrawable(R.drawable.fond_pass_cafe));
			 fond_pass2.setBackground(getResources().getDrawable(R.drawable.fond_pass_cafe));
			 annuel_bonus = getResources().getString(R.string.pass_4mois);
			 if(y==0){ mensuel_prix = "27.10 �"; annuel_prix ="216.80 �"; }
			 if(y==1){ mensuel_prix = "2.50 �"; annuel_prix ="216.80 �"; }
			 if(y==2){ mensuel_prix = "9.80 �"; annuel_prix ="216.80 �"; }
			 if(y==3){ mensuel_prix = "14.80 �"; annuel_prix ="216.80 �"; }
			 if(y==4){ mensuel_prix = "19.70 �"; annuel_prix ="216.80 �"; }
		 }
		 if(x==3){
			 pass_name.setText("PASS' MENTHE");
			 fond_pastel.setVisibility(View.VISIBLE);
			 fond_pass.setVisibility(View.VISIBLE);
			 fond_pastel.setBackgroundColor(getResources().getColor(R.color.menthe));
			 pass_name.setTextColor(getResources().getColor(R.color.menthe));
			 pass_pastel.setTextColor(getResources().getColor(R.color.menthe));
			 fond_pass.setBackground(getResources().getDrawable(R.drawable.fond_pass_menthe));
			 fond_pass2.setBackground(getResources().getDrawable(R.drawable.fond_pass_menthe));
			 annuel_bonus = getResources().getString(R.string.pass_4mois);
			 if(y==0){ mensuel_prix = "26.50 �"; annuel_prix ="216.80 �";}
			 if(y==1){ mensuel_prix = "2.50 �"; annuel_prix ="30.00 �"; }
			 if(y==2){ mensuel_prix = "9.80 �"; annuel_prix ="117.60 �"; }
			 if(y==3){ mensuel_prix = "14.80 �"; annuel_prix ="177.60 �"; }
			 if(y==4){ mensuel_prix = "19.70 �"; annuel_prix ="216.80 �"; }
		 }
		 if(x==4){
			 pass_name.setText("PASS' CANNELLE");	 
			 selector1.setVisibility(View.INVISIBLE);
			 selector2.setVisibility(View.INVISIBLE);
			 selector3.setVisibility(View.INVISIBLE);
			 selector4.setVisibility(View.INVISIBLE);
			 selector5.setVisibility(View.INVISIBLE);
			 pass_name.setTextColor(getResources().getColor(R.color.gris_normal));
			 fond_pastel.setVisibility(View.INVISIBLE);
			 pass_pastel.setTextColor(getResources().getColor(R.color.gris_normal));
			 fond_pass.setBackground(getResources().getDrawable(R.drawable.fond_pass_cannelle));
			 fond_pass2.setBackground(getResources().getDrawable(R.drawable.fond_pass_cannelle));
			 annuel_bonus = getResources().getString(R.string.cannelle);
			 fond_pass.setVisibility(View.GONE);
			 annuel_prix ="GRATUIT";
		 }
		 if(x==5){
			 pass_name.setText("PASS' MENTHE");
			 fond_pass.setVisibility(View.VISIBLE);
			 fond_pastel.setVisibility(View.VISIBLE);
			 fond_pastel.setBackgroundColor(getResources().getColor(R.color.menthe));
			 pass_name.setTextColor(getResources().getColor(R.color.menthe));
			 pass_pastel.setTextColor(getResources().getColor(R.color.menthe));
			 fond_pass.setBackground(getResources().getDrawable(R.drawable.fond_pass_menthe));
			 fond_pass2.setBackground(getResources().getDrawable(R.drawable.fond_pass_menthe));
			 annuel_bonus = getResources().getString(R.string.pass_4mois);
			 if(y==0){ mensuel_prix = "26.50 �"; annuel_prix ="216.80 �";}
			 if(y==1){ mensuel_prix = "2.50 �"; annuel_prix ="30.00 �"; }
			 if(y==2){ mensuel_prix = "9.80 �"; annuel_prix ="117.60 �"; }
			 if(y==3){ mensuel_prix = "14.80 �"; annuel_prix ="177.60 �"; }
			 if(y==4){ mensuel_prix = "19.70 �"; annuel_prix ="216.80 �"; }
		 }
		 if(x==6){
			 pass_name.setText("PASS' SOLEIL 12");
			 selector1.setVisibility(View.INVISIBLE);
			 selector2.setVisibility(View.INVISIBLE);
			 selector3.setVisibility(View.INVISIBLE);
			 selector4.setVisibility(View.INVISIBLE);
			 selector5.setVisibility(View.INVISIBLE);
			 fond_pastel.setVisibility(View.INVISIBLE);
			 pass_pastel.setTextColor(getResources().getColor(R.color.gris_normal));
			 pass_name.setTextColor(getResources().getColor(R.color.gris_normal));
			 fond_pass.setVisibility(View.GONE);
			 fond_pass2.setBackground(getResources().getDrawable(R.drawable.fond_pass_cannelle));
			 annuel_bonus = getResources().getString(R.string.soleil);
			 annuel_prix ="430.70 �"; 
			 
			 pass_pastel.setText("");
		 }
		 
		 
		 passmensuel_prix.setText(mensuel_prix);
		 passannuel_prix.setText(annuel_prix);
		 passannuel_bonus.setText(annuel_bonus);
		 
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    	
	    }
}