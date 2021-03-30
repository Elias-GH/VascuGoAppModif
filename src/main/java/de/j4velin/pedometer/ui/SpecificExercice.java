package de.j4velin.pedometer.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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

import de.j4velin.pedometer.CustomListAdapter;
import de.j4velin.pedometer.Database1;
import de.j4velin.pedometer.R;
/**
 * A simple {@link Fragment} subclass.
 */
public class SpecificExercice extends PreferenceFragment {


    private android.app.Fragment newFragment;
    private FragmentTransaction transaction;

    String message, message_exo;
    int img;

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_exo() {
        return message_exo;
    }
    public void setMessage_exo(String message_exo) {
        this.message_exo = message_exo;
    }

    String[] nameArray = {"Exercice 1", "Exercice 2", "Exercice 3", "Exercice 4", "Exercice 5"};

    String[] infoArray = {
        "Debout-assis",
        "Pointe des pieds -talon en position assise, jambes tendues",
        "Pointe des pieds- talon en position assise, pieds sur le sol",
        "Talon- pointe en position debout",
        "Fente avant "
    };

    String[] detailArray = {
        "Cet exercice fait travailler les muscles de la cuisse.Asseyez-vous sur une chaise. Maintenant, levez-vous, asseyez -vous, et répétez ceci 20 fois. Faites une pause de quelques minutes puis recommencez pour une deuxième série. Si vous commencez à ressentir une douleur, faites une pause.",
        "Cet exercice fait travailler les muscles du mollet, de la cuisse et les abdominaux. Asseyez-vous sur une chaise. Tendez les jambes en avant. Maintenant, tendez les pointes des pieds ; tenez 10 secondes. Remontez les pointes des pieds vers vous, et tenez 10 secondes.  Répétez 20 fois, puis faites une pause de quelques minutes avant de recommencer pour une deuxième série. Si vous commencez à ressentir une douleur, faites une pause.",
        "Cet exercice fait travailler les muscles du mollet. Asseyez-vous sur une chaise, les deux pieds sur le sol. Soulevez les avant-pieds vers le haut, autant que vous pouvez, le talon reste sur le sol. Tenez 10 secondes. Reposez le pieds puis soulevez le talon, autant que vous pouvez, avant pieds au sol ; tenez 10 secondes. Répétez ceci 20 fois. Faites une pause de quelques minutes avant de recommencer pour une deuxième série. Si vous commencez à ressentir une douleur, faites une pause.",
        "Cet exercice fait travailler les muscles du mollet. Mettez-vous en position debout ; pour l’équilibre, sous pouvez tenir le dos d’une chaise. Levez-vous sur la pointe des pieds, et tenez-vous sur les orteils, puis abaissez les talons. Répétez ce mouvement 20 fois ; faites une pause de quelques minutes avant de recommencer pour une deuxième série. Si vous commencez à ressentir une douleur, faites une pause.",
        "Cet exercice permet d’étirer les muscles du mollet et de la jambe. Mettez-vous en position debout ; pour l’équilibre, vous pouvez tenir le dos d’une chaise. Faites un pas en arrière avec la première jambe, puis fléchissez la jambe qui est en avant. Tenez bien le talon des deux pieds collés par terre ; il faut sentir un étirement sur la face arrière de la jambe qui est vers l’arrière. Tenez 30 secondes, puis faites le même exercice avec la deuxième jambe. Répétez 10 fois."
    };

    Integer[] imageArray = {R.drawable.icons1,
        R.drawable.icons2,
        R.drawable.icons3,
        R.drawable.icons4,
        R.drawable.icons5};

    Integer[] exoArray = {
        R.drawable.exercice1,
        R.drawable.exercice2,
        R.drawable.exercice3,
        R.drawable.exercice4,
        R.drawable.exercice5};
    ListView listView;
    private Activity activity;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public SpecificExercice() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Database1 db1 = Database1.getInstance(getActivity());
        db1.truncatetable();
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_specific_exercice, container, false);

        CustomListAdapter whatever = new CustomListAdapter(getActivity(), nameArray, infoArray, imageArray);
        listView = (ListView) rootView.findViewById(R.id.listviewID);
        listView.setAdapter(whatever);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailActivities detailActivities = new DetailActivities();
                Bundle bundle = new Bundle();

                String message = nameArray[position];
                Integer img = exoArray[position];
                String message_exo = detailArray[position];

                bundle.putString("message", message);
                bundle.putInt("img", img);
                bundle.putString("message_exo", message_exo);

                detailActivities.setArguments(bundle);

                FragmentManager manager = getFragmentManager();
                manager.beginTransaction().replace(android.R.id.content, detailActivities).commit();

            }
        });

        Button button1 = (Button) rootView.findViewById(R.id.bouton1);
        Button button2 = (Button) rootView.findViewById(R.id.bouton2);
        Button button3 = (Button) rootView.findViewById(R.id.bouton3);
        ImageView button4 = (ImageView) rootView.findViewById(R.id.bouton4);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newFragment = new Fragment_Overview();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newFragment = new Fragment_Exercices();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newFragment = new Fragment_Information();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newFragment = new Fragment_Settings();
                transaction = getFragmentManager().beginTransaction();
                transaction.replace(android.R.id.content, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return rootView;
    }

}
