package codemsit.weekender.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import codemsit.weekender.App;
import codemsit.weekender.R;
import codemsit.weekender.adapters.FoodAdapter;
import codemsit.weekender.adapters.POIAdapter;
import codemsit.weekender.listeners.RecyclerItemClickListener;
import codemsit.weekender.network.VolleySingleton;
import codemsit.weekender.utils.ShowMessage;

/**
 * Created by aman on 02/04/16.
 */
public class FoodFragment extends Fragment {

    RecyclerView recyclerView;
    View rootView;
    RequestQueue requestQueue;
    FoodAdapter adapter;

    public FoodFragment() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_food, container, false);
        requestQueue = VolleySingleton.getmInstance().getRequestQueue();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_food_list);
        initRV();
        return rootView;
    }

    public void initRV() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://strawberry-crumble-31686.herokuapp.com/foursquare_food", (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

//                    JSONArray jsonArray = response.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");
                adapter = new FoodAdapter(response);
                RecyclerView.LayoutManager llm = new LinearLayoutManager(App.getAppContext());
                recyclerView.setLayoutManager(llm);
                recyclerView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("FoodFragment", "Error"+error);
            }
        });

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*3,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonArrayRequest);

        /**
         * Adding onClickListner for RecyclerView Elements
         */

//        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(App.getAppContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                ShowMessage.toast("View id: "+view.toString()+" btnID: "+R.id.btn_add+" pos: "+position);
//
//                if(view.getId() == R.id.btn_add) {
//                    TextView poiName = (TextView) view.findViewById(R.id.tv_poi_name);
//                    //                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(poiName.getText().toString()));
//                    //                startActivity(browserIntent);
//                    ShowMessage.snackBar(poiName.getText().toString() + " has been added to your trip", view);
//                    adapter.notifyItemRemoved(position);
//                }
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//
//            }
//        }));
    }
}
