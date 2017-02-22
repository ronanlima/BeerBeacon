package br.beer.beerbeacon;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;

import java.util.HashSet;
import java.util.List;

import br.beer.beerbeacon.bean.Tonel;

/**
 * Simple example of ListAdapter for using with Folding Cell
 * Adapter holds indexes of unfolded elements for correct work with default reusable views behavior
 */
public class FoldingCellListAdapter extends RecyclerView.Adapter<TorneiraViewHolder> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener defaultRequestBtnClickListener;
    private Context mContext;
    private List<Tonel> items;

    public FoldingCellListAdapter(Context context, List<Tonel> objects) {
        this.mContext = context;
        setItems(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        Item item = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        TorneiraViewHolder torneiraViewHolder;
        if (cell == null) {
            torneiraViewHolder = new TorneiraViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            torneiraViewHolder.preco = (TextView) cell.findViewById(R.id.title_price);
            torneiraViewHolder.time = (TextView) cell.findViewById(R.id.title_time_label);
            torneiraViewHolder.date = (TextView) cell.findViewById(R.id.title_date_label);
            torneiraViewHolder.marcaChopp = (TextView) cell.findViewById(R.id.marca_chopp);
            torneiraViewHolder.nomeChopp = (TextView) cell.findViewById(R.id.nome_chopp);
            torneiraViewHolder.ibu = (TextView) cell.findViewById(R.id.label_ibu);
            torneiraViewHolder.abv = (TextView) cell.findViewById(R.id.label_abv);
            torneiraViewHolder.btnSolicitar = (TextView) cell.findViewById(R.id.btn_solicitar);
            cell.setTag(torneiraViewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            torneiraViewHolder = (TorneiraViewHolder) cell.getTag();
        }

        // bind data from selected element to view through view holder
        torneiraViewHolder.preco.setText(item.getPrice());
        torneiraViewHolder.time.setText(item.getTime());
        torneiraViewHolder.date.setText(item.getDate());
        torneiraViewHolder.marcaChopp.setText(item.getFromAddress());
        torneiraViewHolder.nomeChopp.setText(item.getToAddress());
        torneiraViewHolder.ibu.setText(String.valueOf(item.getRequestsCount()));
        torneiraViewHolder.abv.setText(item.getPledgePrice());

        // set custom btn handler for list item from that item
        if (item.getRequestBtnClickListener() != null) {
            torneiraViewHolder.btnSolicitar.setOnClickListener(item.getRequestBtnClickListener());
        } else {
            // (optionally) add "default" handler if no handler found in item
            torneiraViewHolder.btnSolicitar.setOnClickListener(defaultRequestBtnClickListener);
        }


        return cell;
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

    public View.OnClickListener getDefaultRequestBtnClickListener() {
        return defaultRequestBtnClickListener;
    }

    public void setDefaultRequestBtnClickListener(View.OnClickListener defaultRequestBtnClickListener) {
        this.defaultRequestBtnClickListener = defaultRequestBtnClickListener;
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
    public void onBindViewHolder(TorneiraViewHolder holder, int position) {
        /** Criar Textviews/checkboxes de acordo com a quantidade de preços (isso indica que há + de
         * 1 copo de chopp. Para isso, usar a classe LinearLayout.Params para copiar o textview/checkbox
         * do primeiro elemento e replicar quantas vezes for necessário. **/
        holder.getPreco().setText(getItems().get(position).getPreco().toString());
        holder.getNomeChopp().setText(getItems().get(position).getVolume() + " - " + getItems().get(position).getNomeChopp()
                + "(" + holder.getPreco().getText() + ")");
        /** **/
        holder.getTime().setText(getItems().get(position).getHora());
        holder.getDate().setText(getItems().get(position).getData());
        holder.getMarcaChopp().setText(getItems().get(position).getMarca());
        holder.getIbu().setText(getItems().get(position).getIbu());
        holder.getAbv().setText(getItems().get(position).getAbv());
        holder.getEstilo().setText(getItems().get(position).getEstilo());
    }

    public List<Tonel> getItems() {
        return items;
    }

    public void setItems(List<Tonel> items) {
        this.items = items;
    }
}

class TorneiraViewHolder extends RecyclerView.ViewHolder {
    TextView preco, btnSolicitar, abv, marcaChopp, nomeChopp, ibu, estilo, date, time;

    public TorneiraViewHolder(View itemView) {
        super(itemView);
        setPreco((TextView) itemView.findViewById(R.id.title_price));
        setTime((TextView) itemView.findViewById(R.id.title_time_label));
        setDate((TextView) itemView.findViewById(R.id.title_date_label));
        setMarcaChopp((TextView) itemView.findViewById(R.id.marca_chopp));
        setNomeChopp((TextView) itemView.findViewById(R.id.nome_chopp));
        setIbu((TextView) itemView.findViewById(R.id.value_ibu));
        setAbv((TextView) itemView.findViewById(R.id.value_abv));
        setEstilo((TextView) itemView.findViewById(R.id.value_estilo));
        setBtnSolicitar((TextView) itemView.findViewById(R.id.btn_solicitar));
    }

    public TextView getPreco() {
        return preco;
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
}
