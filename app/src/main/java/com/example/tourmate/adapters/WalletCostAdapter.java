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
import com.example.tourmate.pojos.WalletCost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WalletCostAdapter extends RecyclerView.Adapter<WalletCostAdapter.WalletCostViewHolder> {
    private Context context;
    private List<WalletCost> walletCostList;
    private DatabaseReference rootref;
    private DatabaseReference userref;
    private DatabaseReference useridref;
    private DatabaseReference eventref;
    private FirebaseUser currentUser;

    public WalletCostAdapter(Context context, List<WalletCost> walletCostList) {
        this.context = context;
        this.walletCostList = walletCostList;
        rootref = FirebaseDatabase.getInstance().getReference();
        userref= rootref.child("Users");
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        useridref = userref.child(currentUser.getUid());
        eventref = useridref.child("Events");

    }

    @NonNull
    @Override
    public WalletCostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallet_row,parent,false);
        return new WalletCostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletCostViewHolder holder, int position) {
        final WalletCost walletCost = walletCostList.get(position);
        final String expenseid = walletCost.getExpenseid();
        holder.titleTV.setText(walletCostList.get(position).getCosttitle());
        holder.amountTV.setText(String.valueOf(walletCostList.get(position).getCostamount()));

    }

    @Override
    public int getItemCount() {
        return walletCostList.size();
    }

    public class WalletCostViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV,amountTV;

        public WalletCostViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.row_titleTV);
            amountTV = itemView.findViewById(R.id.row_amountTV);
        }
    }
}
