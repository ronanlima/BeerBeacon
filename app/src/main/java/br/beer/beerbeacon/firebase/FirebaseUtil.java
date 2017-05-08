package br.beer.beerbeacon.firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.beer.beerbeacon.BeerApplication;
import br.beer.beerbeacon.R;
import br.beer.beerbeacon.bean.Consumacao;
import br.beer.beerbeacon.bean.Medida;
import br.beer.beerbeacon.bean.Pedido;
import br.beer.beerbeacon.bean.Tonel;

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

    /**
     * Busca todas as torneiras disponíveis assim que o usuário abre o aplicativo e notifica a MainActivity.
     *
     * @param context
     * @param listener
     */
    public static void readInfoInitial(final Context context, final ListenersFB listener) {
        DatabaseReference ref = getDBReference(context.getResources().getString(R.string.fb_taps));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChildren()) {
                    List<Tonel> list = new ArrayList<>();
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        list.add(buildTonel(data, context));
                    }
                    listener.readTonelsAvailables(list);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    @NonNull
    private static Tonel buildTonel(DataSnapshot data, Context context) {
        Tonel t = new Tonel();
        t.setAbv(data.child(context.getResources().getString(R.string.taps_abv)).getValue().toString());
        t.setIbu(data.child(context.getResources().getString(R.string.taps_ibu)).getValue().toString());
        t.setCerveja(data.child(context.getResources().getString(R.string.taps_cerveja)).getValue().toString());
        t.setCervejaria(data.child(context.getResources().getString(R.string.taps_cervejaria)).getValue().toString());
        t.setEstilo(data.child(context.getResources().getString(R.string.taps_estilo)).getValue().toString());
        t.setNota(data.child(context.getResources().getString(R.string.taps_nota)).getValue().toString());
        t.setDataEntrada(Long.parseLong(data.child(context.getResources().getString(R.string.taps_data_entrada)).getValue().toString()));
        for (DataSnapshot dataM : data.child(context.getResources().getString(R.string.fb_taps_medidas)).getChildren()) {
            t.getMedidas().add(buildMedida(context, dataM));
        }
        return t;
    }

    @NonNull
    private static Medida buildMedida(Context context, DataSnapshot dataM) {
        Medida m = new Medida();
        m.getQuantidade().add(dataM.child(context.getResources().getString(R.string.taps_medida_quantidade)).getValue().toString());
        m.getPreco().add(dataM.child(context.getResources().getString(R.string.taps_medida_preco)).getValue().toString());
        return m;
    }

    private static DatabaseReference getDBReference(String no) {
        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        return fb.getReference(no);
    }

}
