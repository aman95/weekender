package codemsit.weekender.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import codemsit.weekender.App;
import codemsit.weekender.R;
import codemsit.weekender.adapters.POIAdapter;
import codemsit.weekender.network.VolleySingleton;

/**
 * Created by aman on 02/04/16.
 */
public class POIFragment extends Fragment {

    RecyclerView recyclerView;
    View rootView;
    RequestQueue requestQueue;

    public POIFragment() {
        //
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_poi, container, false);
        requestQueue = VolleySingleton.getmInstance().getRequestQueue();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_poi_list);
        initRV();
        return rootView;
    }

    public void initRV() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://api.foursquare.com/v2/venues/explore?ll=28.6,77.2&oauth_token=4UHYSN1BOWQUHQLMEPBE4XD0N0BZRIM4B4FGRVKQDHSGUU4E&v=20160402", (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONObject("response").getJSONArray("groups").getJSONObject(0).getJSONArray("items");
                    POIAdapter adapter = new POIAdapter(jsonArray);
                    RecyclerView.LayoutManager llm = new LinearLayoutManager(App.getAppContext());
                    recyclerView.setLayoutManager(llm);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*3,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue.add(jsonObjectRequest);
    }

}
