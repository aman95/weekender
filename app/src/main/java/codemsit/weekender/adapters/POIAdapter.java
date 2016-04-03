package codemsit.weekender.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import codemsit.weekender.App;
import codemsit.weekender.R;
import codemsit.weekender.utils.ShowMessage;

/**
 * Created by aman on 02/04/16.
 */
public class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {

    private JSONArray dataSet;

    public POIAdapter(JSONArray dataSet) {
        this.dataSet = dataSet;
    }

    public void setData(JSONArray dataSet) {
        this.dataSet = dataSet;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_poi_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            JSONObject data = dataSet.getJSONObject(position).getJSONObject("venue");
            JSONObject img = dataSet.getJSONObject(position).getJSONArray("tips").getJSONObject(0);
            holder.name.setText((data.getString("name").trim().equals(""))?"<No Title>":data.getString("name").trim());
            holder.addr.setText(data.getJSONObject("location").getString("address"));
            Picasso.with(App.getAppContext()).load(img.getString("photourl")).into(holder.back);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView name, addr;
        ImageView back;
        Button addToTrip;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_poi_name);
            addr = (TextView) itemView.findViewById(R.id.tv_poi_location);
            back = (ImageView) itemView.findViewById(R.id.iv_poi_background);
            addToTrip = (Button) itemView.findViewById(R.id.btn_add);
            addToTrip.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
//            ShowMessage.toast(v.toString());
            ShowMessage.snackBar(name.getText().toString() + " has been added to your trip", v);
//            dataSet.get
            dataSet.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }
}
