package br.beer.beerbeacon;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.util.ArrayList;

import br.beer.beerbeacon.bean.Tonel;

/**
 * Example of using Folding Cell with ListView and ListAdapter
 */
public class MainActivity extends AppCompatActivity implements FoldingCellListAdapter.InflateMessage {

    private Snackbar snackbar;
    private SimpleArcDialog simpleArcDialog;
    private ArcConfiguration arcConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView theListView = (RecyclerView) findViewById(R.id.mainListView);

        final ArrayList<Tonel> items = Tonel.getTonelList();

        // add custom btn handler to first list item
        items.get(0).setRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "CUSTOM HANDLER FOR FIRST BUTTON", Toast.LENGTH_SHORT).show();
            }
        });

        // create custom adapter that holds elements and their state (we need hold a id's of unfolded elements for reusable elements)
        final FoldingCellListAdapter adapter = new FoldingCellListAdapter(this, items);

        // add default btn handler for each request btn on each item if custom handler not found
        adapter.setDefaultRequestBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "DEFAULT HANDLER FOR ALL BUTTONS", Toast.LENGTH_SHORT).show();
            }
        });

        // set elements to adapter
        theListView.setLayoutManager(new LinearLayoutManager(this));
        theListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
    }

    @Override
    public void showSnackbar(String msg) {
        snackbar = Snackbar.make(this.getCurrentFocus(), msg, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.black_overlay));
        textView.setBackgroundColor(getResources().getColor(R.color.btnRequest));
        snackbar.show();
    }

    @Override
    public void showLoading(String msg) {
        setupDialog(msg);
        getSimpleArcDialog().show();
    }

    @Override
    public void hideLoading() {
        getSimpleArcDialog().dismiss();
    }

    @Override
    public void updateLoading(String msg) {
        getArcConfiguration().setLoaderStyle(SimpleArcLoader.STYLE.COMPLETE_ARC);
        getArcConfiguration().setText(msg);
        getSimpleArcDialog().setConfiguration(getArcConfiguration());
    }

    public void setupDialog(String text){
        int[] colors = {Color.parseColor("#594691"), Color.parseColor("#ffbf12")};

        setArcConfiguration(new ArcConfiguration(this));
        getArcConfiguration().setLoaderStyle(SimpleArcLoader.STYLE.SIMPLE_ARC);
        getArcConfiguration().setColors(colors);
        getArcConfiguration().setText(text);
        setSimpleArcDialog(new SimpleArcDialog(this));
        getSimpleArcDialog().setConfiguration(getArcConfiguration());
    }

    public SimpleArcDialog getSimpleArcDialog() {
        return simpleArcDialog;
    }

    public void setSimpleArcDialog(SimpleArcDialog simpleArcDialog) {
        this.simpleArcDialog = simpleArcDialog;
    }

    public ArcConfiguration getArcConfiguration() {
        return arcConfiguration;
    }

    public void setArcConfiguration(ArcConfiguration arcConfiguration) {
        this.arcConfiguration = arcConfiguration;
    }
}
