package br.beer.beerbeacon.firebase;

import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.beer.beerbeacon.BeerApplication;
import br.beer.beerbeacon.R;
import br.beer.beerbeacon.bean.Consumacao;
import br.beer.beerbeacon.bean.Pedido;

/**
 * Created by Ronan.lima on 06/03/17.
 */

public class FirebaseUtil {
    public static final String TAG = FirebaseUtil.class.getCanonicalName().toUpperCase();

    public static void gravaPedido(Context context, Consumacao consumacao) {
        DatabaseReference ref = getDBReference(context.getResources().getString(R.string.fb_consumacao));
        String id = ((BeerApplication) context.getApplicationContext()).getIdConsumoFBase();
        for (Pedido p : consumacao.getPedidos()) {
            if (id == null || id.equals("")) {
                id = ref.push().getKey();
                ((BeerApplication) context.getApplicationContext()).setIdConsumoFBase(id);
            }
            ref.child(id).push().setValue(p);
        }
    }

    public static void listenPedido(final Context context, final ListenersFB listener) {
        DatabaseReference ref = getDBReference(context.getResources().getString(R.string.fb_consumacao));
        final String idConsumoFBase = ((BeerApplication) context.getApplicationContext()).getIdConsumoFBase();
        ref.child(idConsumoFBase).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Double valor = Double.valueOf(0);
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    valor += Integer.valueOf(String.valueOf(snap.child(context.getResources().getString(R.string.pedido_qtd)).getValue()))
                            * Double.valueOf(String.valueOf(snap.child(context.getResources().getString(R.string.pedido_preco)).getValue()));
                }
                listener.listenPedidos(valor);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro ao obter total da consumação para o id = " + idConsumoFBase + ". Detalhes abaixo.");
                Log.e(TAG, databaseError.getMessage());
                listener.listenPedidos(0d);
            }
        });
    }

    private static DatabaseReference getDBReference(String no) {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        return fb.getReference(no);
    }

}
