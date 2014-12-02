package com.tagdroid.android.Pages;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.tagdroid.android.Page;
import com.tagdroid.android.R;


public class TarifsFragment extends Page{

    Spinner spinner2,
            spinner3;
    View card_layout,
            card_layout1,
            card_layout2;

    TextView title_pass,
            pass_mensuel,
            pass_mensuel_prix,
            pass_mensuel_bonus,
            pass_annuel,
            pass_annuel_prix,
            pass_annuel_bonus;


    @Override
    public String getTitle() {
        return getString(R.string.tarif);
    }

    @Override
    public Integer getMenuId() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tarif, container, false);

        /**** SLIDING PAGER ****/

        ViewPager pager = (ViewPager) view.findViewById(R.id.viewpager);

        ViewPagerAdapter adapter = new ViewPagerAdapter();
        pager.setAdapter(adapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setViewPager(pager);


        return view;
    }

    class ViewPagerAdapter extends PagerAdapter {
        private final String[] TITLES = {"Tickets", "Abonnements", "Combinés"};

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        public Object instantiateItem(View collection, int position) {
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = null;
            switch (position) {
                case 0:
                    view = inflater.inflate(R.layout.tarif1, null);
                    Spinner spinner1 = (Spinner) view.findViewById(R.id.spinner1);
                    final TextView ticket_text = (TextView) view.findViewById(R.id.ticket_text);
                    final TextView ticket_prix = (TextView) view.findViewById(R.id.ticket_prix);
                    final TextView ticket_prix_bis = (TextView) view.findViewById(R.id.ticket_prix_bis);

                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            ticket_text.setText(getResources().getStringArray(R.array.ticket_text)[i]);
                            ticket_prix.setText(getResources().getStringArray(R.array.ticket_prix)[i]);
                            ticket_prix_bis.setText(getResources().getStringArray(R.array.ticket_prix_bis)[i]);
                        }
                        public void onNothingSelected(AdapterView<?> adapterView) {return;}
                    });

                    break;
                case 1:
                    view = inflater.inflate(R.layout.tarif2, null);
                    spinner2 = (Spinner) view.findViewById(R.id.spinner2);
                    spinner3 = (Spinner) view.findViewById(R.id.spinner3);
                    card_layout = (View) view.findViewById(R.id.card_layout);
                    card_layout1 = (View) view.findViewById(R.id.card_layout1);
                    card_layout2 = (View) view.findViewById(R.id.card_layout2);

                    title_pass = (TextView) view.findViewById(R.id.title_pass);
                    pass_mensuel = (TextView) view.findViewById(R.id.pass_mensuel);
                    pass_mensuel_prix = (TextView) view.findViewById(R.id.pass_mensuel_prix);
                    pass_mensuel_bonus = (TextView) view.findViewById(R.id.pass_mensuel_bonus);
                    pass_annuel = (TextView) view.findViewById(R.id.pass_annuel);
                    pass_annuel_prix = (TextView) view.findViewById(R.id.pass_annuel_prix);
                    pass_annuel_bonus = (TextView) view.findViewById(R.id.pass_annuel_bonus);

                    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){setValue();}
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            return;
                        }
                    });

                    spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){setValue();}
                        public void onNothingSelected(AdapterView<?> adapterView) {return;}
                    });
                    break;
                case 2:
                    view = inflater.inflate(R.layout.tarif3, null);
                    Spinner spinner4 = (Spinner) view.findViewById(R.id.spinner4);
                    final ImageView image = (ImageView)view.findViewById(R.id.combines_image);
                    final TextView combines_text = (TextView) view.findViewById(R.id.combines_text);
                    final TextView combines_text_gras = (TextView) view.findViewById(R.id.combines_text_gras);
                    final TextView combines_text_plus = (TextView) view.findViewById(R.id.combines_text_plus);

                    spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            TypedArray imgs = getResources().obtainTypedArray(R.array.combines_image);
                            imgs.getResourceId(i, -1);
                            image.setImageResource(imgs.getResourceId(i, -1));

                            combines_text.setText(getResources().getStringArray(R.array.combines_text)[i]);
                            combines_text_gras.setText(getResources().getStringArray(R.array.combines_text_gras)[i]);
                            combines_text_plus.setText(getResources().getStringArray(R.array.combines_text_plus)[i]);
                        }
                        public void onNothingSelected(AdapterView<?> adapterView) {return;}
                    });
                    break;
            }
            ((ViewPager) collection).addView(view, 0);
            return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        private void setValue(){
            int situation =  spinner2.getSelectedItemPosition();
            int qf =  spinner3.getSelectedItemPosition();

            switch (situation){
                case 0 :
                    card_layout.setBackgroundColor(getResources().getColor(R.color.grenadine));
                    card_layout1.setBackgroundColor(getResources().getColor(R.color.grenadine));
                    card_layout2.setBackgroundColor(getResources().getColor(R.color.grenadine));
                    title_pass.setText("PASS' GRENADINE");
                    break;

                case 1 :
                    card_layout.setBackgroundColor(getResources().getColor(R.color.cafe));
                    card_layout1.setBackgroundColor(getResources().getColor(R.color.cafe));
                    card_layout2.setBackgroundColor(getResources().getColor(R.color.cafe));
                    title_pass.setText("PASS' CAFE");
                    break;

                case 2 :
                    card_layout.setBackgroundColor(getResources().getColor(R.color.vanille));
                    card_layout1.setBackgroundColor(getResources().getColor(R.color.vanille));
                    card_layout2.setBackgroundColor(getResources().getColor(R.color.vanille));
                    title_pass.setText("PASS' VANILLE");
                    break;

                case 3 :
                    card_layout.setBackgroundColor(getResources().getColor(R.color.menthe));
                    card_layout1.setBackgroundColor(getResources().getColor(R.color.menthe));
                    card_layout2.setBackgroundColor(getResources().getColor(R.color.menthe));
                    title_pass.setText("PASS' MENTHE");
                    break;

                case 4 :
                    card_layout.setBackgroundColor(getResources().getColor(R.color.canelle));
                    card_layout1.setBackgroundColor(getResources().getColor(R.color.canelle));
                    card_layout2.setBackgroundColor(getResources().getColor(R.color.canelle));
                    title_pass.setText("PASS' CANELLE 12");
                    break;

                case 5 :
                    card_layout.setBackgroundColor(getResources().getColor(R.color.menthe));
                    card_layout1.setBackgroundColor(getResources().getColor(R.color.menthe));
                    card_layout2.setBackgroundColor(getResources().getColor(R.color.menthe));
                    title_pass.setText("PASS' MENTHE");
                    break;
                default:
                    break;
            }

            int ColorText;
            if(situation==0||situation==1||situation==3||situation==5) ColorText = Color.WHITE;
            else ColorText = Color.BLACK;
            title_pass.setTextColor(ColorText);
            pass_mensuel.setTextColor(ColorText);
            pass_mensuel_prix.setTextColor(ColorText);
            pass_annuel.setTextColor(ColorText);
            pass_annuel_prix.setTextColor(ColorText);
            pass_annuel_bonus.setTextColor(ColorText);
            pass_mensuel_bonus.setTextColor(ColorText);


            pass_annuel_prix.setText(getResources().getStringArray(R.array.pass_annuel_prix)[situation]);
            pass_annuel_bonus.setText(getResources().getStringArray(R.array.pass_annuel_bonus)[situation]);
            if(qf==0 || situation==4){
                // SI QF = 0 OU + de 75 ANS
                pass_mensuel_prix.setText(getResources().getStringArray(R.array.pass_mensuel_prix)[situation]);
                pass_mensuel_bonus.setText(getResources().getStringArray(R.array.pass_mensuel_bonus)[0]);
                if(qf==0) card_layout1.getBackground().setAlpha(255);
            }else{
                card_layout1.getBackground().setAlpha(255 - (100/qf));

                if((situation==0 && spinner3.getSelectedItemPosition() ==4)){
                    pass_mensuel_prix.setText(getResources().getStringArray(R.array.pass_mensuel_prix)[situation]);
                    pass_mensuel_bonus.setText("");
                }else{
                    pass_mensuel_prix.setText(getResources().getStringArray(R.array.pass_mensuel_prix_pastel)[qf]);
                    pass_mensuel_bonus.setText(getResources().getStringArray(R.array.pass_mensuel_bonus)[qf]);
                }
            }
        }
    }
 }
