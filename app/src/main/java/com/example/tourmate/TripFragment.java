package com.example.tourmate;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tourmate.adapters.EventAdapter;
import com.example.tourmate.pojos.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TripFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton addbtn;
    private Context context;
    private Addtriplistener addlistener;
    private EventAdapter adapter;
    private DatabaseReference rootref;
    private DatabaseReference userref;
    private DatabaseReference useridref;
    private DatabaseReference eventref;
    private FirebaseUser currentUser;
    private ProgressBar progressBar;


    public TripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        addlistener = (Addtriplistener) context;
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
        return inflater.inflate(R.layout.fragment_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= view.findViewById(R.id.tripsRV);
        addbtn = view.findViewById(R.id.addbtn);
        progressBar = view.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addlistener.addtrips();
            }
        });

        eventref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);
                List<Event> eventList = new ArrayList<>();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    Event e = d.getValue(Event.class);
                    eventList.add(e);
                }
                adapter = new EventAdapter(context,eventList);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public interface Addtriplistener{
        void addtrips();
    }
}
