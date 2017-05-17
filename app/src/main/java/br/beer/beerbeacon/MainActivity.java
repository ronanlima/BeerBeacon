package br.beer.beerbeacon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estimote.sdk.SystemRequirementsChecker;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.leo.simplearcloader.SimpleArcLoader;

import java.text.DecimalFormat;
import java.util.List;

import br.beer.beerbeacon.bean.Tonel;
import br.beer.beerbeacon.firebase.FirebaseUtil;
import br.beer.beerbeacon.firebase.ListenersFB;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Example of using Folding Cell with ListView and ListAdapter
 */
public class MainActivity extends AppCompatActivity implements FoldingCellListAdapter.InflateMessage, ListenersFB, FoldingCellListAdapter.ListenerQrCode {

    private CardView cardView;
    private Snackbar snackbar;
    private SimpleArcDialog simpleArcDialog;
    private ArcConfiguration arcConfiguration;
    private RecyclerView recyclerView;
    private boolean isListenerAdd = false;
    private static final int COD_QR_CODE_SUCCESS = 100;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.mainListView);
        cardView = (CardView) findViewById(R.id.card_total);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SystemRequirementsChecker.checkWithDefaultDialogs(this);
        FirebaseUtil.readInfoInitial(this, this);
    }

    @Override
    public void showSnackbar(String msg) {
        snackbar = Snackbar.make(this.getCurrentFocus(), msg, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(getResources().getColor(R.color.btnRequest));
        textView.setBackgroundColor(getResources().getColor(R.color.black_overlay));
        snackbar.show();
    }

    @Override
    public void showLoading(String msg) {
        if (getArcConfiguration() == null) {
            setupDialog(msg);
        } else {
            configDefaultDialog(msg, SimpleArcLoader.STYLE.COMPLETE_ARC);
            getArcConfiguration().setText(msg);
            getSimpleArcDialog().setConfiguration(getArcConfiguration());
        }
        getSimpleArcDialog().show();
    }

    @Override
    public void hideLoading() {
        getSimpleArcDialog().dismiss();
    }

    @Override
    public void updateLoading(String msg) {
        configDefaultDialog(msg, SimpleArcLoader.STYLE.COMPLETE_ARC);
        getSimpleArcDialog().dismiss();
        setSimpleArcDialog(null);
        setSimpleArcDialog(new SimpleArcDialog(this));
        getSimpleArcDialog().setConfiguration(getArcConfiguration());
        getSimpleArcDialog().show();
        cardView.setVisibility(View.VISIBLE);

        if (!isListenerAdd) {
            enbleCardviewTotal();
            FirebaseUtil.listenPedido(this, this);
        }
    }

    @Override
    public void listenPedidos(Double valor) {
        LinearLayout ll = (LinearLayout) cardView.getChildAt(0);
        TextView tvValor = (TextView) ll.getChildAt(1);
        tvValor.setText(this.getBaseContext().getResources().getString(R.string.coin_locale) + new DecimalFormat("#,##0.00").format(valor));
    }

    @Override
    public void callStartActivityForResult(Context context) {
        startActivityForResult(new Intent(context, QrCodeActivity.class), 100);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        switch (requestCode) {
            case COD_QR_CODE_SUCCESS:
                // FIXME tratar retorno
                break;
            case 0:
                break;
            default:
                break;
        }
    }

    @Override
    public void readTonelsAvailables(List<Tonel> tonel) {
        if (tonel != null && tonel.size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new FoldingCellListAdapter(this, tonel, getSupportFragmentManager()));
        }
    }

    /**
     * Adiciona listener para o scroll do recyclerView, após o usuário ter feito algum pedido, para
     * que sempre que ele role a tela, o total da consumação seja exibido.
     */
    private void enbleCardviewTotal() {
        isListenerAdd = true;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    TranslateAnimation animation = new TranslateAnimation(0, 0, -cardView.getHeight() / 4, cardView.getHeight() * 2);
                    animation.setDuration(500);
                    animation.setFillAfter(true);
                    cardView.startAnimation(animation);
                    cardView.setVisibility(View.GONE);
                } else if (dy < 0) {
                    TranslateAnimation animation = new TranslateAnimation(0, 0, cardView.getHeight() * 2, -cardView.getHeight() / 4);
                    animation.setDuration(500);
                    animation.setFillAfter(true);
                    cardView.startAnimation(animation);
                    cardView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setupDialog(String text) {
        setArcConfiguration(new ArcConfiguration(this));
        configDefaultDialog(text, SimpleArcLoader.STYLE.SIMPLE_ARC);
        setSimpleArcDialog(new SimpleArcDialog(this));
        getSimpleArcDialog().setConfiguration(getArcConfiguration());
    }

    private void configDefaultDialog(String text, SimpleArcLoader.STYLE style) {
        int[] colors = {Color.parseColor("#594691"), Color.parseColor("#ffbf12")};
        getArcConfiguration().setLoaderStyle(style);
        getArcConfiguration().setColors(colors);
        getArcConfiguration().setText(text);
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
