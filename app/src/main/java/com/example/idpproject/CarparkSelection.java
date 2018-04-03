package com.example.idpproject;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CarparkSelection extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.activity_carpark_selection,container,false);
TextView HLL = root.findViewById(R.id.tv_HllCarparkSelection);
HLL.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Fragment fragment = new ParkingSpaceSelection();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.cotent_main,fragment).commit();
    }
});
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Carpark Selection");


    }
}
