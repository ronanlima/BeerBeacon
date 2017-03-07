package br.beer.beerbeacon;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import br.beer.beerbeacon.bean.Consumacao;
import br.beer.beerbeacon.bean.Pedido;
import br.beer.beerbeacon.bean.Tonel;
import br.beer.beerbeacon.firebase.FirebaseUtil;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends RecyclerView.Adapter<TorneiraViewHolder> implements View.OnClickListener {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private Context mContext;
    private List<Tonel> items;
    private InflateMessage iMessage;

    public FoldingCellListAdapter(MainActivity context, List<Tonel> objects) {
        this.mContext = context;
        setItems(objects);
        this.iMessage = context;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    @Override
    public TorneiraViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cell, parent, false);
        return new TorneiraViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return getItems() != null ? getItems().size() : 0;
    }

    @Override
    public void onBindViewHolder(final TorneiraViewHolder holder, final int position) {
        /** Criar Textviews/checkboxes de acordo com a quantidade de preços (isso indica que há + de
         * 1 copo de chopp. Para isso, usar a classe LinearLayout.Params para copiar o textview/checkbox
         * do primeiro elemento e replicar quantas vezes for necessário. **/
        holder.getPreco().setText(new String("" + (position + 1)));
        holder.getNomeChopp().setText(getItems().get(position).getNomeChopp());
        /** **/
        holder.getTime().setText(getItems().get(position).getHora());
        holder.getDate().setText(getItems().get(position).getData());
        holder.getMarcaChopp().setText(getItems().get(position).getMarca());
        holder.getIbu().setText(getItems().get(position).getIbu());
        holder.getAbv().setText(getItems().get(position).getAbv());
        holder.getEstilo().setText(getItems().get(position).getEstilo());
        holder.getHeaderNameChopp().setText(getItems().get(position).getMarca());
        holder.getViewPai().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FoldingCell) view).toggle(false);
                registerToggle(position);
            }
        });

        BigDecimal preco = getItems().get(position).getPreco().get(0);
        holder.getHeaderNameChopp().setText(getItems().get(position).getMarca());
        holder.getHeadIbu().setText(holder.getIbu().getText());
        holder.getHeadAbv().setText(holder.getAbv().getText());
        holder.getHeadEstilo().setText(holder.getEstilo().getText());
        holder.getNomeCompostoMarcaCerveja().setText(holder.getMarcaChopp().getText() + " - " + holder.getNomeChopp().getText());
        holder.getTextVolume1().setText("Copo " + getItems().get(position).getVolume().get(0).intValue() + " ml");
        holder.getTextPrecoVol1().setText("R$ " + holder.getPrecoFmt(preco.doubleValue()));
        holder.setPreco1(preco);
        ((View) holder.getTextVolume2().getParent().getParent()).setVisibility(View.GONE);
        if (getItems().get(position).getVolume().size() > 1) {
            holder.getTextVolume2().setText("Copo " + getItems().get(position).getVolume().get(1).intValue() + " ml");
            preco = getItems().get(position).getPreco().get(1);
            holder.getTextPrecoVol2().setText("R$ " + holder.getPrecoFmt(preco.doubleValue()));
            holder.setPreco2(preco);
            ((View) holder.getTextVolume2().getParent().getParent()).setVisibility(View.VISIBLE);
        }
        holder.getBtnSolicitar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.getCheckBox1().isChecked() && !holder.getCheckBox2().isChecked()) {
                    iMessage.showSnackbar("Antes de fazer o pedido, informe o volume desejado e sua quantidade!");
                    return;
                }
                iMessage.showLoading("Efetuando seu pedido");// FIXME loading não está aparecendo, arrumar context adequado
                FirebaseUtil.gravaPedido(mContext, createConsumacao(holder));
                clearScreen(view, holder);
                iMessage.updateLoading("Pedido enviado!");
            }
        });
    }

    private void clearScreen(View view, TorneiraViewHolder holder) {
        holder.getCheckBox1().setChecked(false);
        holder.getCheckBox2().setChecked(false);
        ((FoldingCell) view.getParent().getParent().getParent().getParent()).toggle(false);
        registerToggle(holder.getPosition());
    }

    @NonNull
    private Consumacao createConsumacao(TorneiraViewHolder holder) {
        Consumacao consumacao = new Consumacao("");
        Pedido pedido;
        String titulo = holder.getMarcaChopp().getText().toString() + " " + holder.getNomeChopp()
                .getText().toString();
        if (holder.getCheckBox1().isChecked()) {
            pedido = new Pedido(titulo + " - " + holder.getTextVolume1().getText().toString()
                    , new Integer(2), Calendar.getInstance().getTimeInMillis(), holder.getPreco1().doubleValue());
            consumacao.getPedidos().add(pedido);
        }
        if (holder.getCheckBox2().isChecked()) {
            pedido = new Pedido(titulo + " - " + holder.getTextVolume2().getText().toString()
                    , new Integer(1), Calendar.getInstance().getTimeInMillis(), holder.getPreco2().doubleValue());
            consumacao.getPedidos().add(pedido);
        }
        return consumacao;
    }

    public List<Tonel> getItems() {
        return items;
    }

    public void setItems(List<Tonel> items) {
        this.items = items;
    }

    @Override
    public void onClick(View view) {
        registerToggle(0);
    }

    interface InflateMessage extends Serializable {
        void showSnackbar(String msg);

        void showLoading(String msg);

        void hideLoading();

        void updateLoading(String msg);
    }

}

class TorneiraViewHolder extends RecyclerView.ViewHolder {
    /**
     * Componentes do layout encolhido
     */
    TextView preco, btnSolicitar, abv, marcaChopp, nomeChopp, ibu, estilo, date, time;
    /**
     * Componentes do layout expandido
     */
    TextView headerNameChopp, headIbu, headAbv, headEstilo, nomeCompostoMarcaCerveja, textVolume1, textVolume2, textPrecoVol1, textPrecoVol2;
    CheckBox checkBox1, checkBox2;
    View viewPai;
    private BigDecimal preco1, preco2;

    public TorneiraViewHolder(View itemView) {
        super(itemView);
        setViewPai(itemView);
        setPreco((TextView) itemView.findViewById(R.id.title_price));
        setTime((TextView) itemView.findViewById(R.id.title_time_label));
        setDate((TextView) itemView.findViewById(R.id.title_date_label));
        setMarcaChopp((TextView) itemView.findViewById(R.id.marca_chopp));
        setNomeChopp((TextView) itemView.findViewById(R.id.nome_chopp));
        setIbu((TextView) itemView.findViewById(R.id.value_ibu));
        setAbv((TextView) itemView.findViewById(R.id.value_abv));
        setEstilo((TextView) itemView.findViewById(R.id.value_estilo));
        setBtnSolicitar((TextView) itemView.findViewById(R.id.btn_solicitar));

        setHeaderNameChopp((TextView) itemView.findViewById(R.id.head_text_marca_chopp));
        setHeadIbu((TextView) itemView.findViewById(R.id.head_text_ibu));
        setHeadAbv((TextView) itemView.findViewById(R.id.head_text_abv));
        setHeadEstilo((TextView) itemView.findViewById(R.id.head_text_estilo));
        setNomeCompostoMarcaCerveja((TextView) itemView.findViewById(R.id.nome_composto_marca_cerveja));
        setTextVolume1((TextView) itemView.findViewById(R.id.text_volume_1));
        setTextVolume2((TextView) itemView.findViewById(R.id.text_volume_2));
        setTextPrecoVol1((TextView) itemView.findViewById(R.id.text_preco_volume_1));
        setTextPrecoVol2((TextView) itemView.findViewById(R.id.text_preco_volume_2));
        setCheckBox1((CheckBox) itemView.findViewById(R.id.check_chopp_1));
        setCheckBox2((CheckBox) itemView.findViewById(R.id.check_chopp_2));
    }

    public TextView getPreco() {
        return preco;
    }

    public String getPrecoFmt(Double preco) {
        DecimalFormat df = new DecimalFormat("#,##0.00");
        df.setMaximumFractionDigits(2);
        return df.format(preco);
    }

    public void setPreco(TextView preco) {
        this.preco = preco;
    }

    public TextView getBtnSolicitar() {
        return btnSolicitar;
    }

    public void setBtnSolicitar(TextView btnSolicitar) {
        this.btnSolicitar = btnSolicitar;
    }

    public TextView getAbv() {
        return abv;
    }

    public void setAbv(TextView abv) {
        this.abv = abv;
    }

    public TextView getMarcaChopp() {
        return marcaChopp;
    }

    public void setMarcaChopp(TextView marcaChopp) {
        this.marcaChopp = marcaChopp;
    }

    public TextView getNomeChopp() {
        return nomeChopp;
    }

    public void setNomeChopp(TextView nomeChopp) {
        this.nomeChopp = nomeChopp;
    }

    public TextView getIbu() {
        return ibu;
    }

    public void setIbu(TextView ibu) {
        this.ibu = ibu;
    }

    public TextView getEstilo() {
        return estilo;
    }

    public void setEstilo(TextView estilo) {
        this.estilo = estilo;
    }

    public TextView getDate() {
        return date;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public TextView getTime() {
        return time;
    }

    public void setTime(TextView time) {
        this.time = time;
    }

    public TextView getHeaderNameChopp() {
        return headerNameChopp;
    }

    public void setHeaderNameChopp(TextView headerNameChopp) {
        this.headerNameChopp = headerNameChopp;
    }

    public TextView getHeadIbu() {
        return headIbu;
    }

    public void setHeadIbu(TextView headIbu) {
        this.headIbu = headIbu;
    }

    public TextView getHeadAbv() {
        return headAbv;
    }

    public void setHeadAbv(TextView headAbv) {
        this.headAbv = headAbv;
    }

    public TextView getHeadEstilo() {
        return headEstilo;
    }

    public void setHeadEstilo(TextView headEstilo) {
        this.headEstilo = headEstilo;
    }

    public TextView getNomeCompostoMarcaCerveja() {
        return nomeCompostoMarcaCerveja;
    }

    public void setNomeCompostoMarcaCerveja(TextView nomeCompostoMarcaCerveja) {
        this.nomeCompostoMarcaCerveja = nomeCompostoMarcaCerveja;
    }

    public TextView getTextVolume1() {
        return textVolume1;
    }

    public void setTextVolume1(TextView textVolume1) {
        this.textVolume1 = textVolume1;
    }

    public TextView getTextVolume2() {
        return textVolume2;
    }

    public void setTextVolume2(TextView textVolume2) {
        this.textVolume2 = textVolume2;
    }

    public TextView getTextPrecoVol1() {
        return textPrecoVol1;
    }

    public void setTextPrecoVol1(TextView textPrecoVol1) {
        this.textPrecoVol1 = textPrecoVol1;
    }

    public TextView getTextPrecoVol2() {
        return textPrecoVol2;
    }

    public void setTextPrecoVol2(TextView textPrecoVol2) {
        this.textPrecoVol2 = textPrecoVol2;
    }

    public CheckBox getCheckBox1() {
        return checkBox1;
    }

    public void setCheckBox1(CheckBox checkBox1) {
        this.checkBox1 = checkBox1;
    }

    public CheckBox getCheckBox2() {
        return checkBox2;
    }

    public void setCheckBox2(CheckBox checkBox2) {
        this.checkBox2 = checkBox2;
    }

    public View getViewPai() {
        return viewPai;
    }

    public void setViewPai(View viewPai) {
        this.viewPai = viewPai;
    }

    public BigDecimal getPreco1() { return preco1; }

    public void setPreco1(BigDecimal preco1) { this.preco1 = preco1; }

    public BigDecimal getPreco2() { return preco2; }

    public void setPreco2(BigDecimal preco2) { this.preco2 = preco2; }
}
