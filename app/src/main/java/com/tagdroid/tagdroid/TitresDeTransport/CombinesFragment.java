package com.tagdroid.tagdroid.TitresDeTransport;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.tagdroid.tagdroid.R;

public class CombinesFragment extends Fragment implements OnItemSelectedListener{
	private Tracker tracker;
	public static Fragment newInstance() {
        return new CombinesFragment();
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
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.combines, container, false);

		String[] items = new String[] {"TAG + Cité Lib", "TAG + TER + TCL", "TAG + Transisère",
				"Métrovélo", "P+R Vallier-Catane"};
		Spinner spinner = (Spinner) view.findViewById(R.id.spinner4);
		spinner.setOnItemSelectedListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	
        return view;
	}   
	 @SuppressLint("NewApi")
	public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
		 TextView combines_description = (TextView) getView().findViewById(R.id.combines_description);
		 ImageView combines_image = (ImageView) getView().findViewById(R.id.combines_image);
		 String description = null;
		 switch(pos){
		 	case 0: 
		 		description = "Si vous êtes abonné annuel TAG, bénéficiez de 40 % de réduction " +
		 				"sur l'abonnement annuel Cité lib et de 15 % de réduction sur " +
		 				"votre Pass' TAG." +
		 				"\n\nSouscription en agences commerciales TAG et à StationMobile - l'Agence.";
		 		combines_image.setBackground(getResources().getDrawable(R.drawable.citelib));
		 	break;
		 	case 1:
		 		description = "Les abonnements mensuels disponibles sur carte OÙRA! vous " +
		 				"permettent de circuler librement sur le réseau TAG et sur un " +
		 				"trajet TER dont la gare d'origine ou de destination se situe " +
		 				"dans l'agglomération grenobloise. Les abonnements mensuels " +
		 				"TER + TAG + TCL offrent la libre circulation sur les réseaux " +
		 				"urbains de Grenoble et Lyon." +
		 				"\n\nSouscription en gare SNCF et à StationMobile - l'Agence." +
		 				"\n\n* TCL : réseau des Transports en Commun Lyonnais.";
		 		combines_image.setBackground(getResources().getDrawable(R.drawable.ter));
	 		break;
		 	case 2:
		 		description = "3 formules donnent accès aux lignes départementales " +
		 				"iséroises et à la libre circulation sur le réseau TAG : " +
		 				"Pass 1 jour, Pass mensuel et Pass annuel incluant la zone A." +
		 				"\n\nSouscription en agence Transisére et à StationMobile - l'Agence.";
		 		combines_image.setBackground(getResources().getDrawable(R.drawable.transisere));
	 		break;
		 	case 3:
		 		description = "Abonnés annuels TAG (+ de 16 ans), bénéficiez d'un " +
		 				"abonnement annuel offert (200 h/an) au service MétrovéloBox " +
		 				"ainsi que de tarifs réduits sur les services Métrovélo." +
		 				"\n\nSouscription en agences Métrovélo et à StationMobile - l'Agence.";
		 		combines_image.setBackground(getResources().getDrawable(R.drawable.metrovelo));
	 		break;
		 	case 4:
		 		description = "Garez votre voiture en P+R et rejoignez le centre-ville en bus, " +
		 				"tram ou vélo pour 2,60 € ou 3,60 €. De 1 à 5 personnes, c'est le méme prix. " +
		 				"\nAbonné TAG, bénéficiez du P+R gratuit, de la mise à" +
		 				"disposition gratuite d'un Métrovélo à la journée, de l'abonnement" +
		 				"24h/24 au Parking Relais Vallier Catane pour 11€/mois"+
		 				"\n(Pass' café, vanille, menthe et Soleil 12 uniquement)"+
		 				"\n\nSouscription auprès de l'agent d'accueil du parking.";
		 		combines_image.setBackground(getResources().getDrawable(R.drawable.pr));
	 		break;
		 	
		 }	 
		 combines_description.setText(description);		 
	    }

	    public void onNothingSelected(AdapterView<?> parent) {    	
	    }
}