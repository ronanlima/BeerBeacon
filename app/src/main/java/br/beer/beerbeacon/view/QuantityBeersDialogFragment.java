package br.beer.beerbeacon.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import br.beer.beerbeacon.R;
import br.beer.beerbeacon.adapter.SimpleAdapter;

/**
 * Created by ronanlima on 15/04/17.
 */

public class QuantityBeersDialogFragment extends BottomSheetDialogFragment {

    private static final String TAG = QuantityBeersDialogFragment.class.getCanonicalName().toUpperCase();

    private ListView listView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.quantity_beer_bottom_sheet, container, false);
        String idView = getArguments().getString("idCheckbox");
        SimpleAdapter.PedidosListener listener = (SimpleAdapter.PedidosListener) getArguments().getSerializable("listener");

        setListView((ListView) v.findViewById(R.id.list_quantity_beers));
        getListView().setAdapter(new SimpleAdapter(this, v.getContext(), listener, idView));
        return v;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override
    public Object getEnterTransition() {
        return super.getEnterTransition();
        //TODO animar entrada
    }

    @Override
    public Object getExitTransition() {
        return super.getExitTransition();
        //TODO animar saída
    }

    public ListView getListView() {
        return listView;
    }

    public void setListView(ListView listView) {
        this.listView = listView;
    }
}
