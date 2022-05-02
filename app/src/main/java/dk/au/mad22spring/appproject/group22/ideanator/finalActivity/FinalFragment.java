package dk.au.mad22spring.appproject.group22.ideanator.finalActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import dk.au.mad22spring.appproject.group22.ideanator.R;

public class FinalFragment extends Fragment {
    private String solution ="";



    public void setText(String text){
        if(text!=null) solution=text;
        else solution ="Hmm, tom :(";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_final, container, false);
        TextView textBox = rootView.findViewById(R.id.FinalTxtSolution);
        textBox.setText(solution);

        return rootView;
    }

}