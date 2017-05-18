package br.beer.beerbeacon;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ramotion.foldingcell.FoldingCell;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

import br.beer.beerbeacon.adapter.SimpleAdapter;
import br.beer.beerbeacon.bean.Consumacao;
import br.beer.beerbeacon.bean.Pedido;
import br.beer.beerbeacon.bean.Tonel;
import br.beer.beerbeacon.firebase.FirebaseUtil;
import br.beer.beerbeacon.view.QuantityBeersDialogFragment;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends RecyclerView.Adapter<TorneiraViewHolder> implements View.OnClickListener {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private Context mContext;
    private List<Tonel> items;
    private InflateMessage iMessage;
    private ListenerQrCode listenerQrCode;
    private FragmentManager fm;

    public FoldingCellListAdapter(MainActivity context, List<Tonel> objects, FragmentManager supportFragmentManager) {
        this.mContext = context;
        setItems(objects);
        this.iMessage = context;
        this.listenerQrCode = context;
        this.fm = supportFragmentManager;
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
        // TODO mapear unfold para esconder cardview que informa a consumação.
//        TranslateAnimation animation = new TranslateAnimation(0, 0, -cardView.getHeight() / 4, cardView.getHeight() * 2);
//        animation.setDuration(500);
//        animation.setFillAfter(true);
//        cardView.startAnimation(animation);
//        cardView.setVisibility(View.GONE);
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
        Tonel tonel = getItems().get(position);
        String numTorneira = String.format("%02d", position+1);
        /** Criar Textviews/checkboxes de acordo com a quantidade de preços (isso indica que há + de
         * 1 copo de chopp. Para isso, usar a classe LinearLayout.Params para copiar o textview/checkbox
         * do primeiro elemento e replicar quantas vezes for necessário. **/
        holder.getPreco().setText(numTorneira);
        holder.getNomeChopp().setText(tonel.getCerveja());
        /** **/

        holder.getMarcaChopp().setText(tonel.getCervejaria());
        holder.getIbu().setText(tonel.getIbu());
        holder.getAbv().setText(tonel.getAbv());
        holder.getEstilo().setText(tonel.getEstilo());
        holder.getHeaderNameChopp().setText(tonel.getCervejaria());
        holder.getViewPai().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FoldingCell) view).toggle(false);
                registerToggle(position);
            }
        });

        String preco = tonel.getMedidas().get(0).getPreco().get(0);
        holder.getHeaderNameChopp().setText(tonel.getCervejaria());
        holder.getHeadIbu().setText(holder.getIbu().getText());
        holder.getHeadAbv().setText(holder.getAbv().getText());
        holder.getHeadEstilo().setText(holder.getEstilo().getText());
        holder.getNomeCompostoMarcaCerveja().setText(holder.getMarcaChopp().getText() + " - " + holder.getNomeChopp().getText());
        holder.getTextVolume1().setText(tonel.getMedidas().get(0).getQuantidade().get(0));
        holder.getTextPrecoVol1().setText(preco);
        holder.setPreco1(preco);
        ((View) holder.getTextVolume2().getParent().getParent()).setVisibility(View.GONE);
        if (tonel.getMedidas().size() > 1) {
            holder.getTextVolume2().setText(tonel.getMedidas().get(1).getQuantidade().get(0));
            preco = tonel.getMedidas().get(1).getPreco().get(0);
            holder.getTextPrecoVol2().setText(preco);
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
                if (BeerApplication.getInstance().getTableQrCode() == null) {
                    listenerQrCode.callStartActivityForResult(mContext);
                } else {
                    iMessage.showLoading("Efetuando seu pedido");
                    FirebaseUtil.gravaPedido(mContext, createConsumacao(holder));
                    clearScreen(view, holder);
                    iMessage.updateLoading("Pedido enviado!");
                }
            }
        });

        CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    QuantityBeersDialogFragment fragment = new QuantityBeersDialogFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString("idCheckbox", String.valueOf(buttonView.getId()));
                    arguments.putSerializable("listener", holder);
                    fragment.setArguments(arguments);
                    fragment.show(fm, "");
                }
            }
        };

        holder.getCheckBox1().setOnCheckedChangeListener(checkedListener);
        holder.getCheckBox2().setOnCheckedChangeListener(checkedListener);

        Glide.with(mContext)
                .load(tonel.getUrlImageCerveja())
                .into(holder.getImgCervejaCellTitle());

        Glide.with(mContext)
                .load(tonel.getUrlImageCerveja())
                .into(holder.getImgCerveja());

        Glide.with(mContext)
                .load(tonel.getUrlImgCervejaria())
                .centerCrop()
                .crossFade()
                .into(holder.getImgCervejaria());
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
                    , new Integer(2), Calendar.getInstance().getTimeInMillis(), holder.getPreco1());
            consumacao.getPedidos().add(pedido);
        }
        if (holder.getCheckBox2().isChecked()) {
            pedido = new Pedido(titulo + " - " + holder.getTextVolume2().getText().toString()
                    , new Integer(1), Calendar.getInstance().getTimeInMillis(), holder.getPreco2());
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

    interface ListenerQrCode extends Serializable {
        void callStartActivityForResult(Context context);
    }

    interface InflateMessage extends Serializable {
        void showSnackbar(String msg);

        void showLoading(String msg);

        void hideLoading();

        void updateLoading(String msg);
    }

}

class TorneiraViewHolder extends RecyclerView.ViewHolder implements SimpleAdapter.PedidosListener {
    /**
     * Componentes do layout encolhido
     */
    TextView preco, btnSolicitar, abv, marcaChopp, nomeChopp, ibu, estilo;
    ImageView imgCervejaCellTitle;
    /**
     * Componentes do layout expandido
     */
    TextView headerNameChopp, headIbu, headAbv, headEstilo, nomeCompostoMarcaCerveja, textVolume1, textVolume2, textPrecoVol1, textPrecoVol2;
    CheckBox checkBox1, checkBox2;
    ImageView imgCerveja, imgCervejaria;
    View viewPai;
    private String preco1, preco2;
    private int qtd1, qtd2;

    @Override
    public void listenQuantitySelected(Integer qtd, String idView) {
        if (R.id.check_chopp_1 == Integer.valueOf(idView)) {
            setQtd1(qtd);
        } else {
            setQtd2(qtd);
        }
    }

    public TorneiraViewHolder(View itemView) {
        super(itemView);
        setViewPai(itemView);
        setImgCervejaCellTitle((ImageView) itemView.findViewById(R.id.img_cerveja_cell_title));
        setPreco((TextView) itemView.findViewById(R.id.title_price));
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
        setImgCerveja((ImageView) itemView.findViewById(R.id.img_cerveja));
        setImgCervejaria((ImageView) itemView.findViewById(R.id.img_cervejaria));
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

    public String getPreco1() {
        return preco1;
    }

    public void setPreco1(String preco1) {
        this.preco1 = preco1;
    }

    public String getPreco2() {
        return preco2;
    }

    public void setPreco2(String preco2) {
        this.preco2 = preco2;
    }

    public int getQtd1() {
        return qtd1;
    }

    public void setQtd1(int qtd1) {
        this.qtd1 = qtd1;
    }

    public int getQtd2() {
        return qtd2;
    }

    public void setQtd2(int qtd2) {
        this.qtd2 = qtd2;
    }

    public ImageView getImgCerveja() {
        return imgCerveja;
    }

    public void setImgCerveja(ImageView imgCerveja) {
        this.imgCerveja = imgCerveja;
    }

    public ImageView getImgCervejaria() {
        return imgCervejaria;
    }

    public void setImgCervejaria(ImageView imgCervejaria) {
        this.imgCervejaria = imgCervejaria;
    }

    public ImageView getImgCervejaCellTitle() {
        return imgCervejaCellTitle;
    }

    public void setImgCervejaCellTitle(ImageView imgCervejaCellTitle) {
        this.imgCervejaCellTitle = imgCervejaCellTitle;
    }
}
