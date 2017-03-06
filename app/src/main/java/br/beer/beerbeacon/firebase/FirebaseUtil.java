package br.beer.beerbeacon.firebase;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.beer.beerbeacon.BeerApplication;
import br.beer.beerbeacon.bean.Consumacao;
import br.beer.beerbeacon.bean.Pedido;

/**
 * Created by Ronan.lima on 06/03/17.
 */

public class FirebaseUtil {

    public static void gravaPedido(Context context, Consumacao consumacao) {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        DatabaseReference ref = fb.getReference("consumacao");
        String id = ((BeerApplication) context.getApplicationContext()).getIdConsumoFBase();
        for (Pedido p : consumacao.getPedidos()) {
            if (id == null || id.equals("")) {
                id = ref.push().getKey();
                ((BeerApplication) context.getApplicationContext()).setIdConsumoFBase(id);
            }
            ref.child(id).push().setValue(p);
        }
    }

}
