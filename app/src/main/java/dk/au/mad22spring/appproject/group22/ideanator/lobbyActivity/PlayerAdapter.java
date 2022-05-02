package dk.au.mad22spring.appproject.group22.ideanator.lobbyActivity;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import dk.au.mad22spring.appproject.group22.ideanator.IdeainatorApplication;
import dk.au.mad22spring.appproject.group22.ideanator.R;
import dk.au.mad22spring.appproject.group22.ideanator.model.OptionCard;
import dk.au.mad22spring.appproject.group22.ideanator.model.Player;

//Entire file Derived from AU MAD lecture 3 "PersonAdaptor"
public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {


        private ArrayList<Player> playerList;

        public PlayerAdapter() {
            playerList = new ArrayList<>();
        }

        public void updatePlayerList(ArrayList<Player> list) {
            playerList = list;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public PlayerAdapter.PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_player, parent, false);
            return new PlayerViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull PlayerViewHolder holder, @SuppressLint("RecyclerView") int position) {
            Log.d("adaptor", "onBindViewHolder: tst");
            //Log.d("adaptor", "onBindViewHolder: "+optionList.size());
            Player player = playerList.get(position);
            holder.textView.setText(player.getName());
            Glide.with(IdeainatorApplication.getAppContext())
                    .load(player.getImgUrl())
                    .into(holder.imageView);        }

        @Override
        public int getItemCount() {
            if( playerList==null) return 0;
            return playerList.size();

        }

        public class PlayerViewHolder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView textView;

            public PlayerViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.lobby_item_imageView);
                textView = itemView.findViewById(R.id.lobby_item_txt_name);

            }


        }

    }
