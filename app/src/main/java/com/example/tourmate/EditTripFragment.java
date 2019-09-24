package com.example.tourmate;


import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourmate.pojos.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditTripFragment extends Fragment {
    private Context context;
    private EditText tripnameET,tripdesET,tripstartET,tripendET;
    private Button savetripbtn;
    private DatePickerDialog picker;
    private DatabaseReference rootref;
    private DatabaseReference userref;
    private DatabaseReference useridref;
    private DatabaseReference eventref;
    private FirebaseUser currentUser;
    private Event event;
    private String eventID = null;
    private onEditSuccessListener editlistener;


    public EditTripFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        editlistener = (onEditSuccessListener) context;
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
        return inflater.inflate(R.layout.fragment_edit_trip, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tripnameET = view.findViewById(R.id.editevent_tripnameET);
        tripdesET = view.findViewById(R.id.editevent_tripdesET);
        tripstartET = view.findViewById(R.id.editevent_tripstartET);
        tripendET = view.findViewById(R.id.editevent_tripendET);
        savetripbtn = view.findViewById(R.id.editevent_savetripbtn);
        Bundle bundle = getArguments();
        if (bundle != null){
            eventID = bundle.getString("id");
        }
        eventref.child(eventID).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.getValue(Event.class);
                if (event != null){
                    tripnameET.setText(event.getTripName());
                    tripdesET.setText(event.getTripDescription());
                    tripstartET.setText(event.getTripStartDate());
                    tripendET.setText(event.getTripEndDate());
                    tripstartET.setShowSoftInputOnFocus(false);
                    tripstartET.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Calendar cldr = Calendar.getInstance();
                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                            int month = cldr.get(Calendar.MONTH);
                            int year = cldr.get(Calendar.YEAR);
                            // date picker dialog
                            picker = new DatePickerDialog(context,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            tripstartET.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }
                                    }, year, month, day);

                            picker.show();

                        }
                    });
                    tripendET.setShowSoftInputOnFocus(false);
                    tripendET.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Calendar cldr = Calendar.getInstance();
                            int day = cldr.get(Calendar.DAY_OF_MONTH);
                            int month = cldr.get(Calendar.MONTH);
                            int year = cldr.get(Calendar.YEAR);
                            // date picker dialog
                            picker = new DatePickerDialog(context,
                                    new DatePickerDialog.OnDateSetListener() {
                                        @Override
                                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                            tripendET.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                        }
                                    }, year, month, day);

                            picker.show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        savetripbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tripname= tripnameET.getText().toString();
                String tripdes= tripdesET.getText().toString();
                String tripstart= tripstartET.getText().toString();
                String tripend= tripendET.getText().toString();
                String eventid = eventID;
                Event event = new Event(eventid,tripname,tripdes,tripstart,tripend);
                eventref.child(eventid).setValue(event)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                editlistener.onEditSuccess();
                                Toast.makeText(context, "Edit Successfull", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    public interface onEditSuccessListener{
        void onEditSuccess();
    }

}
