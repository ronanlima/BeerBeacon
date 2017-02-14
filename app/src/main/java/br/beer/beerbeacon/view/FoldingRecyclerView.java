package br.beer.beerbeacon.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.beer.beerbeacon.R;
import br.beer.beerbeacon.bean.Tonel;

/**
 * Created by Ronan.lima on 14/02/17.
 */
public class FoldingRecyclerView extends RecyclerView.Adapter<FoldingHolder> {
    private List<Tonel> items;

    @Override
    public FoldingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FoldingHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public List<Tonel> getItems() {
        return items;
    }

    public void setItems(List<Tonel> items) {
        this.items = items;
    }
}

class FoldingHolder extends RecyclerView.ViewHolder {
    private TextView valueIbu, valueAlcool, valueCor, preco, nomeCerveja;
    private ImageView imgIconCerveja;
//    private List<CheckBox> checkBoxes;
    private LinearLayout llCheckBoxes;
    private Button btnSolicitar;

    public FoldingHolder(View itemView) {
        super(itemView);
        setValueIbu((TextView) itemView.findViewById(R.id.valor_ibu));
        setValueAlcool((TextView) itemView.findViewById(R.id.valor_alcool));
        setValueCor((TextView) itemView.findViewById(R.id.valor_cor));
        setPreco((TextView) itemView.findViewById(R.id.preco_chopp));
        setNomeCerveja((TextView) itemView.findViewById(R.id.name_chopp));
        setImgIconCerveja((ImageView) itemView.findViewById(R.id.img_icon_beer));
        setLlCheckBoxes((LinearLayout) itemView.findViewById(R.id.content_cell));
        setBtnSolicitar((Button) itemView.findViewById(R.id.btn_solicitar));
    }

    private void onClickListenerSolicitarChopp(final Context context) {
        getBtnSolicitar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Pedido feito", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public TextView getValueIbu() {
        return valueIbu;
    }

    public void setValueIbu(TextView valueIbu) {
        this.valueIbu = valueIbu;
    }

    public TextView getValueAlcool() {
        return valueAlcool;
    }

    public void setValueAlcool(TextView valueAlcool) {
        this.valueAlcool = valueAlcool;
    }

    public TextView getValueCor() {
        return valueCor;
    }

    public void setValueCor(TextView valueCor) {
        this.valueCor = valueCor;
    }

    public TextView getPreco() {
        return preco;
    }

    public void setPreco(TextView preco) {
        this.preco = preco;
    }

    public TextView getNomeCerveja() {
        return nomeCerveja;
    }

    public void setNomeCerveja(TextView nomeCerveja) {
        this.nomeCerveja = nomeCerveja;
    }

    public ImageView getImgIconCerveja() {
        return imgIconCerveja;
    }

    public void setImgIconCerveja(ImageView imgIconCerveja) {
        this.imgIconCerveja = imgIconCerveja;
    }

    public LinearLayout getLlCheckBoxes() {
        return llCheckBoxes;
    }

    public void setLlCheckBoxes(LinearLayout llCheckBoxes) {
        this.llCheckBoxes = llCheckBoxes;
    }

    public Button getBtnSolicitar() {
        return btnSolicitar;
    }

    public void setBtnSolicitar(Button btnSolicitar) {
        this.btnSolicitar = btnSolicitar;
    }
}
