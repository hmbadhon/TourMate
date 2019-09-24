package com.example.tourmate.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.pojos.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> eventList;
    private DatabaseReference rootref;
    private DatabaseReference userref;
    private DatabaseReference useridref;
    private DatabaseReference eventref;
    private FirebaseUser currentUser;
    private OnDetailsTripListener detailsTripListener;

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        rootref = FirebaseDatabase.getInstance().getReference();
        userref= rootref.child("Users");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        useridref = userref.child(currentUser.getUid());
        eventref = useridref.child("Events");
        detailsTripListener = (OnDetailsTripListener) context;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trips_row,parent,false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        final Event event = eventList.get(position);
        final String eventid = event.getEventID();
        holder.titleTV.setText(eventList.get(position).getTripName());
        holder.desTV.setText(eventList.get(position).getTripDescription());
        /*holder.memoriesbtn.setBottom(position);*/
        holder.detailsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailsTripListener.onDetailslistener(eventid);

            }
        });

        holder.deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Delete Event!");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        eventref.child(eventid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Deleted!", Toast.LENGTH_SHORT).show();
                                eventList.remove(event);
                                notifyDataSetChanged();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV,desTV;
        Button detailsbtn,memoriesbtn,deletebtn;
        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV= itemView.findViewById(R.id.tourtitleTV);
            desTV= itemView.findViewById(R.id.tourdesTV);
            detailsbtn= itemView.findViewById(R.id.detailsbtn);
            memoriesbtn= itemView.findViewById(R.id.memoriesbtn);
            deletebtn= itemView.findViewById(R.id.deletebtn);


        }
    }

    public interface OnDetailsTripListener{
        void onDetailslistener(String eventid);
    }
}
