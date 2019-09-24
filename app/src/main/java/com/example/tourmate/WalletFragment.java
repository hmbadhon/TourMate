package com.example.tourmate;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourmate.adapters.EventAdapter;
import com.example.tourmate.adapters.WalletBudgetAdapter;
import com.example.tourmate.adapters.WalletCostAdapter;
import com.example.tourmate.pojos.Event;
import com.example.tourmate.pojos.WalletBudget;
import com.example.tourmate.pojos.WalletCost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
public class WalletFragment extends Fragment {
    private static final String TAG = WalletFragment.class.getSimpleName();
    private RecyclerView budgetRV,expenseRV;
    private ImageButton addbtn,minusbtn,walletbtn;
    private Context context;
    private WalletBudgetAdapter budgetAdapter;
    private DatabaseReference rootref;
    private DatabaseReference userref;
    private DatabaseReference useridref;
    private DatabaseReference eventref;
    private DatabaseReference budgetref;
    private DatabaseReference expenseref;
    private FirebaseUser currentUser;
    private String eventID = null;
    private WalletCostAdapter costAdapter;
    private TextView alerttitle;
    private EditText wallettitle,walletamount;
    private Button savebtn,cancelbtn;


    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootref = FirebaseDatabase.getInstance().getReference();
        userref= rootref.child("Users");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        useridref = userref.child(currentUser.getUid());
        eventref = useridref.child("Events");
        budgetref = useridref.child("Budget");
        expenseref = useridref.child("Expense");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wallet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expenseRV = view.findViewById(R.id.expenceRV);
        budgetRV = view.findViewById(R.id.budgetRV);
        walletbtn = view.findViewById(R.id.walletbtn);
        addbtn = view.findViewById(R.id.addbudget);
        minusbtn = view.findViewById(R.id.addcost);
        Bundle bundle = getArguments();
        if (bundle != null){
            eventID = bundle.getString("id");
        }


        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletBudgetAlertDialog("Add Budget");

            }
        });

        minusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                walletCostAlertDialog("Add Expense");
            }
        });

        budgetref.child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<WalletBudget> walletBudgetList = new ArrayList<>();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    WalletBudget walletBudget = d.getValue(WalletBudget.class);
                    walletBudgetList.add(walletBudget);
                }
                budgetAdapter = new WalletBudgetAdapter(context,walletBudgetList);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                budgetRV.setLayoutManager(llm);
                budgetRV.setAdapter(budgetAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        expenseref.child(eventID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<WalletCost> walletCostList = new ArrayList<>();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    WalletCost walletCost = d.getValue(WalletCost.class);
                    walletCostList.add(walletCost);
                }
                costAdapter = new WalletCostAdapter(context,walletCostList);
                LinearLayoutManager llm = new LinearLayoutManager(context);
                expenseRV.setLayoutManager(llm);
                expenseRV.setAdapter(costAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void walletBudgetAlertDialog(String title){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_wallet_alert, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();
        alerttitle = dialog.findViewById(R.id.alertitleTV);
        wallettitle = dialog.findViewById(R.id.wallettitleET);
        walletamount = dialog.findViewById(R.id.walletamountET);
        cancelbtn = dialog.findViewById(R.id.alertCancelbtn);
        savebtn = dialog.findViewById(R.id.alertSavebtn);
        alerttitle.setText(title);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String title = wallettitle.getText().toString();
                int amount = Integer.valueOf(walletamount.getText().toString());
                String eventid = eventID;
                String budgetid = budgetref.push().getKey();
                WalletBudget walletBudget = new WalletBudget(budgetid,title,amount);
                budgetref.child(eventid).child(budgetid).setValue(walletBudget)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Add Successfull", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void walletCostAlertDialog(String title){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_wallet_alert, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        dialog.show();
        alerttitle = dialog.findViewById(R.id.alertitleTV);
        wallettitle = dialog.findViewById(R.id.wallettitleET);
        walletamount = dialog.findViewById(R.id.walletamountET);
        cancelbtn = dialog.findViewById(R.id.alertCancelbtn);
        savebtn = dialog.findViewById(R.id.alertSavebtn);
        alerttitle.setText(title);

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                String title = wallettitle.getText().toString();
                int amount = Integer.valueOf(walletamount.getText().toString());
                String eventid = eventID;
                String expenseid = expenseref.push().getKey();
                WalletCost walletCost = new WalletCost(expenseid,title,amount);
                expenseref.child(eventid).child(expenseid).setValue(walletCost)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(context, "Add Successfull", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }



}
