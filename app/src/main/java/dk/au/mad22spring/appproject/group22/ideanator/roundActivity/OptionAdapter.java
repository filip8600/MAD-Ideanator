package dk.au.mad22spring.appproject.group22.ideanator.roundActivity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;

//Entire file Derived from AU MAD lecture 3 "PersonAdaptor"
public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.OptionViewHolder> {
        public interface IOptionItemClickedListener {
            void onOptionClicked(int index);
        }

        private final IOptionItemClickedListener listener;
        private ArrayList<OptionCard> optionList=new ArrayList<>();

        public OptionAdapter(IOptionItemClickedListener listener) {
            this.listener = listener;
            optionList = new ArrayList<OptionCard>();
        }

        public void updateOptionList(ArrayList<OptionCard> list) {
            optionList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public OptionAdapter.OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_round, parent, false);
            return new OptionViewHolder(v, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull OptionViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Log.d("adaptor", "onBindViewHolder: tst");
            //Log.d("adaptor", "onBindViewHolder: "+optionList.size());
            OptionCard optionCard = optionList.get(position);
            holder.optionBtn.setText(optionCard.getOption());
            holder.optionBtn.setOnClickListener(view -> listener.onOptionClicked(position));
        }

        @Override
        public int getItemCount() {
            if( optionList==null) return 0;
            return Math.min(optionList.size(), 5);//Only top 5 options are showed to user

        }

        public class OptionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            Button optionBtn;
            IOptionItemClickedListener listener;

            public OptionViewHolder(@NonNull View itemView, OptionAdapter.IOptionItemClickedListener listener) {
                super(itemView);
                optionBtn = itemView.findViewById(R.id.round_option_item);

                this.listener = listener;

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Log.d("inactivity-log-tag", "option   clicked");

                listener.onOptionClicked(getAdapterPosition());
            }
        }

    }
