package com.example.tourmate;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tourmate.pojos.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripDetailsFragment extends Fragment {
    private Context context;
    private DatabaseReference rootref;
    private DatabaseReference userref;
    private DatabaseReference useridref;
    private DatabaseReference eventref;
    private FirebaseUser currentUser;
    private TextView tripname,tripdes,tripstart,tripend,walletTV,cameraTV;
    private FloatingActionButton editbtn;
    private String eventID = null;
    private OneditListener editListener;
    private OnCameraListener cameraListener;
    private OnWalletListener walletListener;


    public TripDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        editListener = (OneditListener) context;
        cameraListener = (OnCameraListener) context;
        walletListener = (OnWalletListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootref = FirebaseDatabase.getInstance().getReference();
        userref= rootref.child("Users");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        useridref = userref.child(currentUser.getUid());
        eventref = useridref.child("Events");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trip_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tripname = view.findViewById(R.id.tripnameTV);
        tripdes = view.findViewById(R.id.tripdesTV);
        tripstart = view.findViewById(R.id.tripstartTV);
        tripend = view.findViewById(R.id.tripendTV);
        editbtn = view.findViewById(R.id.editbtn);
        walletTV = view.findViewById(R.id.walletTV);
        cameraTV = view.findViewById(R.id.cameraTV);
        Bundle bundle = getArguments();
        if (bundle != null){
            eventID = bundle.getString("id");
        }
        eventref.child(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                if (event != null){
                    tripname.setText(event.getTripName());
                    tripdes.setText(event.getTripDescription());
                    tripstart.setText(event.getTripStartDate());
                    tripend.setText(event.getTripEndDate());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        walletTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletListener.walletlistener(eventID);
            }
        });

        cameraTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraListener.cameralistener(eventID);
            }
        });


        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editListener.editlistener(eventID);
            }
        });

    }

    public interface OneditListener{
        void editlistener(String eventID);
    }

    public interface OnCameraListener{
        void cameralistener(String eventID);
    }

    public interface OnWalletListener{
        void walletlistener(String eventID);
    }

}
