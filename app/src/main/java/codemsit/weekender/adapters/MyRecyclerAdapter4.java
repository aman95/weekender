package codemsit.weekender.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import codemsit.weekender.R;
import codemsit.weekender.Trip;
import codemsit.weekender.models.FeedItem;
import codemsit.weekender.network.Connectiondetector;

public class MyRecyclerAdapter4 extends RecyclerView.Adapter<FeedListRowHolder> {


    private List<FeedItem> feedItemList4;
    Connectiondetector cd;
     Boolean isInternetPresent= false;
    private Context mContext;

    public MyRecyclerAdapter4(Context context, List<FeedItem> feedItemList) {
        this.feedItemList4 = feedItemList;
        this.mContext = context;

    }

    @Override
    public FeedListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        FeedListRowHolder mh = new FeedListRowHolder(v);

        return mh;
    }

    @Override
    public void onBindViewHolder(FeedListRowHolder feedListRowHolder, int i) {
        FeedItem feedItem = feedItemList4.get(i);
        cd = new Connectiondetector(mContext.getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();

        if(!isInternetPresent) {
            feedListRowHolder.directionpointer.setVisibility(View.GONE);
            feedListRowHolder.dist.setVisibility(View.GONE);

        }


        Picasso.with(mContext).load(feedItem.getLogo())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(feedListRowHolder.logo);

        feedListRowHolder.id.setText(Html.fromHtml(feedItem.getId()));
        feedListRowHolder.name.setText(Html.fromHtml(feedItem.getName()));

        feedListRowHolder.description.setText(Html.fromHtml(feedItem.getDescription()));

        feedListRowHolder.dist.setText(Html.fromHtml(feedItem.getDistance()));
        feedListRowHolder.sliderstringholder.setText(Html.fromHtml(feedItem.getSliderstring()));
        String SliderImage=feedItem.getSliderstring();
        Log.d("Slider string",SliderImage);


        String distance=feedItem.getDistance();









        /*String canpay = feedItem.getCheckbox();
        if(canpay.matches("1"))
        {
            String a = "Payments Accepted";
            feedListRowHolder.checkbox.setText(a);
        }
        else
        {

            feedListRowHolder.checkbox.setVisibility(TextView.INVISIBLE);
        }*/





        final String Rid=feedItem.getId();
        final String Rname=feedItem.getName();
        final String Rdescription=feedItem.getDescription();
        final String foodtype=feedItem.getFoodType();
        //final String Rdisc_amt=feedListRowHolder.disc_amt.getText().toString();
        final String Rslider=feedListRowHolder.sliderstringholder.getText().toString();


        feedListRowHolder.setClickListener(new FeedListRowHolder.ClickListener()

        {
            @Override
            public void onClick(View v, int pos, boolean isLongClick) {
                if (isLongClick) {
                    // View v at position pos is long-clicked.
                } else {
                    Intent intent =  new Intent(mContext, Trip.class);
                    intent.putExtra("rid",Rid);
                    intent.putExtra("rname", Rname);

                    intent.putExtra("rdescription",Rdescription);

                    intent.putExtra("rslider", Rslider);
                    intent.putExtra("foodtype", foodtype);

                    Bundle extras = new Bundle();
                    mContext.startActivity(intent);

                    // View v at position pos is clicked.
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != feedItemList4 ? feedItemList4.size() : 0);
    }
}
