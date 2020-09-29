package de.j4velin.pedometer.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import de.j4velin.pedometer.CustomListAdapter;
import de.j4velin.pedometer.Database1;
import de.j4velin.pedometer.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class DetailActivities extends PreferenceFragment implements Preference.OnPreferenceClickListener {

    private Activity activity;
    private final static Integer[] etat_exo_array = {0, 0, 0, 0, 0};

    public void setEtat_exo_array(int par) {
        etat_exo_array[par] = 1;
    }

    public Integer[] getEtat_exo_array(){

        return etat_exo_array;
    }
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.getItemId() == R.id.action_home || item.getItemId() == R.id.action_about || item.getItemId() == R.id.action_achievements
                    || item.getItemId() == R.id.action_faq || item.getItemId() == R.id.action_settings || item.getItemId() == R.id.action_specific_exercice
                    || item.getItemId() == R.id.action_specific_exercice || item.getItemId() == R.id.action_split_count || item.getItemId() == R.id.action_weekly_exercice)
            {
                SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
                int end = spanString.length();
                spanString.setSpan(new RelativeSizeSpan(1.3f), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                item.setTitle(spanString);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_split_count:

                return true;
            default:
                return ((Activity_Main) getActivity()).optionsItemSelected(item);
        }
    }

    public DetailActivities() {
        // Required empty public constructor
    }
    public void displayMsg(String str){
        Toast.makeText(getActivity(), "Enregistrer : " + str, Toast.LENGTH_SHORT).show();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SpecificExercice()).addToBackStack(null)
                .commit();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Database1 db1 = Database1.getInstance(getActivity());
        db1.truncatetable();
        // Inflate the layout for this fragment
        View rootView1= inflater.inflate(R.layout.fragment_detail_activities,null, false);

        Bundle bundle = getArguments();

        final String message = bundle.getString("message");
        final String message_exo=bundle.getString("message_exo");
        int img = bundle.getInt("img");

        TextView myText = (TextView) rootView1.findViewById(R.id.textView1ID);
        myText.setText(message);

        TextView myText2 = (TextView) rootView1.findViewById(R.id.textView2ID);
        myText2.setText(message_exo);

        ImageView myText1 = (ImageView) rootView1.findViewById(R.id.imageView1exoID);
        myText1.setImageResource(img);



        Button btn1 = (Button) rootView1.findViewById(R.id.button_termine_exoID);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayMsg(message);
                System.out.println(Arrays.toString(etat_exo_array));

            }
        });

        return rootView1 ;
    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }
}

