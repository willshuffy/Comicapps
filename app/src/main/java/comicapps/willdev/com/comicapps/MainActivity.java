package comicapps.willdev.com.comicapps;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import comicapps.willdev.com.comicapps.Adapter.MyComicAdapter;
import comicapps.willdev.com.comicapps.Adapter.MySliderAdapter;
import comicapps.willdev.com.comicapps.Common.Common;
import comicapps.willdev.com.comicapps.Interface.IBannerLoadDone;
import comicapps.willdev.com.comicapps.Interface.IComicLoadDone;
import comicapps.willdev.com.comicapps.Model.Comic;
import comicapps.willdev.com.comicapps.Service.PicassoLoadingService;
import dmax.dialog.SpotsDialog;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity implements IBannerLoadDone, IComicLoadDone {

    Slider slider;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recycler_comic;
    TextView txt_comic;

    //Database
    DatabaseReference banners, comics;

    //Listener
    IBannerLoadDone bannerListener;
    IComicLoadDone comicListener;

    android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init Database
        banners = FirebaseDatabase.getInstance().getReference("Banners");
        comics = FirebaseDatabase.getInstance().getReference("Comic");

        //Init Listener
        bannerListener = this;
        comicListener = this;

        slider = (Slider)findViewById(R.id.slider);
        Slider.init(new PicassoLoadingService());

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_to_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadBanner();
                loadComic();
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
                loadComic();

            }
        });

        recycler_comic = (RecyclerView)findViewById(R.id.recyclerview_comic);
        recycler_comic.setHasFixedSize(true);
        recycler_comic.setLayoutManager(new GridLayoutManager(this, 2));

        txt_comic = (TextView)findViewById(R.id.txt_comic);
    }

    private void loadComic() {
        //Show Dialog
        alertDialog = new SpotsDialog.Builder().setContext(this)
                .setCancelable(false)
                .setMessage("Please Wait... ")
                .build();

        if (!swipeRefreshLayout.isRefreshing())
        alertDialog.show();


        comics.addListenerForSingleValueEvent(new ValueEventListener() {
            List<Comic> comicc_load = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot comicSnapShot:dataSnapshot.getChildren())
                {
                    Comic comic = comicSnapShot.getValue(Comic.class);
                    comicc_load.add(comic);
                }

                comicListener.onComicLoadDoneListener(comicc_load);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void loadBanner() {

        banners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String>bannerList= new ArrayList<>();
                for (DataSnapshot bannerSnapshot:dataSnapshot.getChildren())
                {
                    String image = bannerSnapshot.getValue(String.class);
                    bannerList.add(image);
                }

                //Call Listener
                bannerListener.onBannerLoadDoneListener(bannerList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        
    }


    @Override
    public void onBannerLoadDoneListener(List<String> banners) {
        slider.setAdapter(new MySliderAdapter(banners));

    }

    @Override
    public void onComicLoadDoneListener(List<Comic> comicList) {
        Common.comicList = comicList;

        recycler_comic.setAdapter(new MyComicAdapter(getBaseContext(),comicList));

        txt_comic.setText(new StringBuilder("NEW COMIC (")
        .append(comicList.size())
        .append(")"));

        if (!swipeRefreshLayout.isRefreshing())
            alertDialog.dismiss();

    }
}
