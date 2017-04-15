package br.beer.beerbeacon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.Serializable;

import br.beer.beerbeacon.R;
import br.beer.beerbeacon.view.QuantityBeersDialogFragment;

/**
 * Created by ronanlima on 15/04/17.
 */

public class SimpleAdapter extends BaseAdapter {
    private String[] items = {"1 copo", "2 copos", "3 copos", "4 copos", "5 copos", "6 copos", "7 copos"
            , "8 copos", "9 copos", "10 copos"};
    private QuantityBeersDialogFragment bottomSheet;
    private Context mContext;
    private PedidosListener listener;
    private String idView;

    public SimpleAdapter(QuantityBeersDialogFragment quantityBeersDialogFragment, Context context, PedidosListener listener, String idView) {
        super();
        bottomSheet = quantityBeersDialogFragment;
        this.mContext = context;
        this.listener = listener;
        this.idView = idView;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String item = items[position];
        View v = LayoutInflater.from(mContext).inflate(R.layout.simple_adapter_beer, parent, false);
        final TextView text = (TextView) v.findViewById(R.id.qtd_beer);
        text.setText(item);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String texto = text.getText().toString();
                listener.listenQuantitySelected(Integer.valueOf(texto.substring(0, texto.indexOf("c") - 1)), idView);
                bottomSheet.dismiss();
            }
        });
        return v;
    }

    public interface PedidosListener extends Serializable {
        void listenQuantitySelected(Integer qtd, String idView);
    }
}
