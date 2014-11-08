package com.tagdroid.android.Legacy;

import com.tagdroid.android.R;

public class Stations{
		
	protected String ligneX;
	protected int couleur_text;
	protected int couleur_gris = R.color.gris_fonce;
	
	public Stations(String ligneX){
		/*
		if(ligneX.equals("Ligne A")){
	    	    ligneX="Ligne A";
	   			couleur_text=R.color.lignea;
		}else if(ligneX.equals("Ligne B")){
				ligneX="Ligne B";
				couleur_text=R.color.ligneb;
		}else if(ligneX.equals("Ligne C")){
				ligneX="Ligne C";
				couleur_text=R.color.lignec;
		}else if(ligneX.equals("Ligne D")){
				ligneX="Ligne D";
				couleur_text=R.color.ligned;
		}else if(ligneX.equals("Ligne E")){
				ligneX="Ligne E";
				couleur_text=R.color.lignee;
		}else if(ligneX.equals("Ligne CO")){
			ligneX="Ligne CO";
			couleur_text=R.color.ligneco;
		}else if(ligneX.equals("Ligne CEA")){
			ligneX="Ligne CEA";
			couleur_text=R.color.lignecea;
		}else if(ligneX.equals("Ligne 1")){
			ligneX="Ligne 1";
			couleur_text=R.color.ligne1;
		}else if(ligneX.equals("Ligne 11")){
			ligneX="Ligne 11";
			couleur_text=R.color.ligne11;
		}else if(ligneX.equals("Ligne 13")){
			ligneX="Ligne 13";
			couleur_text=R.color.ligne13;
		}else if(ligneX.equals("Ligne 16")){
			ligneX="Ligne 16";
			couleur_text=R.color.ligne16;
		}else if(ligneX.equals("Ligne 17")){
			ligneX="Ligne 17";
			couleur_text=R.color.ligne17;
		}else if(ligneX.equals("Ligne 21")){
			ligneX="Ligne 21";
			couleur_text=R.color.ligne21;
		}else if(ligneX.equals("Ligne 23")){
			ligneX="Ligne 23";
			couleur_text=R.color.ligne23;
		}else if(ligneX.equals("Ligne 26")){
			ligneX="Ligne 26";
			couleur_text=R.color.ligne26;
		}else if(ligneX.equals("Ligne 30")){
			ligneX="Ligne 30";
			couleur_text=R.color.ligne30;
		}else if(ligneX.equals("Ligne 31")){
			ligneX="Ligne 31";
			couleur_text=R.color.ligne31;
		}else if(ligneX.equals("Ligne 32")){
			ligneX="Ligne 32";
			couleur_text=R.color.ligne32;
		}else if(ligneX.equals("Ligne 33")){
			ligneX="Ligne 33";
			couleur_text=R.color.ligne33;
		}else if(ligneX.equals("Ligne 34")){
			ligneX="Ligne 34";
			couleur_text=R.color.ligne34;
		}else if(ligneX.equals("Ligne 41")){
			ligneX="Ligne 41";
			couleur_text=R.color.ligne41;
		}else if(ligneX.equals("Ligne 43")){
			ligneX="Ligne 43";
			couleur_text=R.color.ligne43;
		}else if(ligneX.equals("Ligne 51")){
			ligneX="Ligne 51";
			couleur_text=R.color.ligne51;
		}else if(ligneX.equals("Ligne 54")){
			ligneX="Ligne 54";
			couleur_text=R.color.ligne54;
		}else if(ligneX.equals("Ligne 55")){
			ligneX="Ligne 55";
			couleur_text=R.color.ligne55;
		}else if(ligneX.equals("Ligne 56")){
			ligneX="Ligne 56";
			couleur_text=R.color.ligne56;
		}else if(ligneX.equals("Ligne 58")){
			ligneX="Ligne 58";
			couleur_text=R.color.ligne58;
		}else if(ligneX.equals("Ligne N1")){
			ligneX="Ligne N1";
			couleur_text=R.color.lignen1;
		}else if(ligneX.equals("Ligne N3")){
			ligneX="Ligne N3";
			couleur_text=R.color.lignen3;
		}else if(ligneX.equals("Ligne N4")){
			ligneX="Ligne N4";
			couleur_text=R.color.lignen4;
		}else{
			ligneX=" ";

		}
		    */
	}
	
	public String getLigne(){
		return ligneX;
	}
	
	public int getCouleurText(){
		return couleur_text;
	}
	
	public int getCouleurGris(){
		return couleur_gris;
	}
	
	
}
