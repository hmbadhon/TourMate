package com.example.tourmate.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourmate.R;
import com.example.tourmate.pojos.WalletBudget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WalletBudgetAdapter extends RecyclerView.Adapter<WalletBudgetAdapter.WalletBudgetViewHolder> {
    private Context context;
    private List<WalletBudget> walletBudgetList;
    private DatabaseReference rootref;
    private DatabaseReference userref;
    private DatabaseReference useridref;
    private DatabaseReference eventref;
    private FirebaseUser currentUser;

    public WalletBudgetAdapter(Context context, List<WalletBudget> walletBudgets) {
        this.context = context;
        this.walletBudgetList = walletBudgets;
        rootref = FirebaseDatabase.getInstance().getReference();
        userref= rootref.child("Users");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        useridref = userref.child(currentUser.getUid());
        eventref = useridref.child("Events");
    }

    @NonNull
    @Override
    public WalletBudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallet_row,parent,false);
        return new WalletBudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletBudgetViewHolder holder, int position) {
        final WalletBudget walletBudget = walletBudgetList.get(position);
        final String budgetid = walletBudget.getBudgetid();
        holder.titleTV.setText(walletBudgetList.get(position).getBudgettitle());
        holder.amountTV.setText(String.valueOf(walletBudgetList.get(position).getBudgetamount()));

    }

    @Override
    public int getItemCount() {
        return walletBudgetList.size();
    }


    public class WalletBudgetViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV,amountTV;

        public WalletBudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.row_titleTV);
            amountTV = itemView.findViewById(R.id.row_amountTV);
        }
    }
}
