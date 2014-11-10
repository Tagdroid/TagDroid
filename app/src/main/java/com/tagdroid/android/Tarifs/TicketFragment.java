package com.tagdroid.android.Tarifs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.tagdroid.android.R;

public class TicketFragment extends Fragment implements OnItemSelectedListener {

	
	public static Fragment newInstance() {
        return new TicketFragment();
    }
	
	@Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
    }

	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ticket, container, false);
		
		String[] items = new String[] {"1 Voyage", "10 Voyages", "10 Voyages (Tarif réduit)", 
				"10 Voyages (Tarif groupe)", "30 Voyages", "VISITAG 1 Jour", 
				"VISITAG 3 Jours", "FAMILI TAG", "Ticket P+R"};
		Spinner spinner = (Spinner) view.findViewById(R.id.spinner1);
		spinner.setOnItemSelectedListener(this);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
        return view;
	}    
	
	 public void onItemSelected(AdapterView<?> parent, View view,int pos, long id) {
		 TextView ticket_description = (TextView) getView().findViewById(R.id.ticket_description);
		 TextView ticket_prix = (TextView) getView().findViewById(R.id.ticket_prix);	 
		 String description = null, prix = null;
		 switch(pos){
		 	case 0: 
		 		description = "Ticket valable 1h, correspondances et retour autoriss";
		 		prix = "1.60 ";
		 	break;
		 	case 1:
		 		description = "Ticket valable 1h, correspondes et retour autorisés." +
		 						"\nPossibilité de voyager à plusieurs avec le même ticket qui doit " +
		 						"être validé autant de fois que de personnes";
		 		prix = "13.20 ";
	 		break;
		 	case 2:
		 		description = "Ticket valable 1h, correspondances et retour autorisés, pour les " +
		 				"détenteurs de la carte \"famille nombreuse\" bleue, les personnes " +
		 				"de plus de 65 ans ou de 60 ans et plus en cas d'inaptitude définitive " +
		 				"au travail selon la législation de la Sécurité Sociale, les anciens " +
		 				"déportés et internés de guerre bénéficiant d'une retraite anticipée " +
		 				"et les personnes invalides détenteurs d'une carte d'invalidit " +
		 				"d'au moins 80 %.\n\nVotre justificatif doit être présenté en cas de contrôle.";
		 		prix = "9.00 ";
	 		break;
		 	case 3:
		 		description = "Ce ticket est réservé aux groupes de 10 à 30 personnes, munis " +
		 				"d'une autorisation de déplacement demandée 48 h avant à Allô" +
		 				"TAG, à présenter en cas de contrôle." +
		 				"\nTicket valable pendant 1h sur l'ensemble du réseau de 9 h à 16 h 30 " +
		 				"les lundis, mardis, jeudis et vendredis, à partir de 9 h les mercredis " +
		 				"et les samedis, sans limitation horaire les dimanches, jours " +
		 				"fériés et pendant les vacances scolaires." +
		 				"\nLes correspondances et retour sont autorisés. Ce ticket doit être " +
		 				"validé autant de fois que de personnes." +
		 				"\n\nVotre justificatif doit être présenté en cas de contrôle.";
		 		prix = "8.70 ";
	 		break;
		 	case 4:
		 		description = "Ticket valable 1h, correspondances et retour autorisés." +
		 				"\nPossibilité de voyager à plusieurs avec le même ticket " +
		 				"qui doit être validé autant de fois que de personnes.";
		 		prix = "36 ";
	 		break;
		 	case 5:
		 		description = "Ticket valable 1 jour calendaire à partir de sa validation pour " +
		 				"un nombre de voyages illimité.";
		 		prix = "4.50 ";
	 		break;
		 	case 6:
		 		description = "Ticket valable 3 jours calendairesà partir de sa validation pour " +
		 				"un nombre de voyages illimité.";
		 		prix = "10.90 ";
	 		break;
		 	case 7:
		 		description = "Ticket valable pendant 1 jour calendaire, sur l'ensemble du réseau " +
		 				"pour une famille de 2 à 5 personnes, circulant obligatoirement " +
		 				"ensemble.\n\nUne famille correspond à 1 adulte accompagn de 1 à 4 enfants " +
		 				"ou 2 adultes accompagnés de 1 à 3 enfants.\nLes enfants doivent être âgés de moins de 18 ans.";
		 		prix = "4.80 ";
	 		break;
		 	case 8:
		 		description = "Gratuit pour les abonnés annuels ou mensuels TAG." +
		 				"\n\nPour 2,30  ou 3,30  par véhicule, l'agent d'accueil remet " +
		 				"un titre TAG aller et retour à chaque passager (valable dans " +
		 				"la journée dans la limite de 5 personnes par véhicule). " +
		 				"\nVous pouvez également bénéficier d'un vélo Métrovélo " +
		 				"à la journée (dans la limite d'un vélo par véhicule). " +
		 				"Plus d'infos dans le dépliant Parkings Relais." +
		 				"\n\n* Grand Sablon,Seyssinet-Pariset Hôtel de ville, Esplanade et Vallier-Catane";
		 		prix = "2.60  ou 3.60 *";
	 		break;
		 }
		 
		 ticket_description.setText(description);
		 ticket_prix.setText(prix);
		 
	    }

	    public void onNothingSelected(AdapterView<?> parent) {
	    	
	    }
}