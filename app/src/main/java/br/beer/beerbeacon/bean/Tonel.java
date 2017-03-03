package br.beer.beerbeacon.bean;

import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ronan.lima on 14/02/17.
 */
public class Tonel {
    private String marca, nomeChopp, ibu, abv, estilo;
    private List<Double> volume;
    //FIXME remover esses atributos futuramente
    private String data, hora;
    private List<Double> preco;
    private List<String> descricaoChopp;
    private View.OnClickListener requestBtnClickListener;

    public Tonel(String marca, String nomeChopp, String ibu, String abv, String estilo) {
        setMarca(marca);
        setNomeChopp(nomeChopp);
        setIbu(ibu);
        setAbv(abv);
        setEstilo(estilo);
        setPreco(new ArrayList<Double>());
        setDescricaoChopp(new ArrayList<String>());
        setData("Hoje");
        setHora("12:53");
        setVolume(new ArrayList<Double>());
    }

    public static ArrayList<Tonel> getTonelList() {
        ArrayList<Tonel> lista = new ArrayList<>();
        Tonel t = new Tonel("Dogma", "Hopp Lagger", "10,1", "7,3", "Lagger");
        t.getPreco().add(getNextDouble());
        t.getPreco().add(getNextDouble());
        t.getVolume().add(new Double(200));
        t.getVolume().add(new Double(400));
        lista.add(t);
        t = new Tonel("Colorado", "Colorado SeiLaOQuê", "9,8", "7,3", "ABC");
        t.getPreco().add(getNextDouble());
        t.getVolume().add(new Double(200));
        lista.add(t);
        t = new Tonel("Eisenbhan", "Eisenbhan Weizenbier", "6,2", "4,3", "Weizenbier");
        t.getPreco().add(getNextDouble());
        t.getPreco().add(getNextDouble());
        t.getVolume().add(new Double(200));
        t.getVolume().add(new Double(400));
        lista.add(t);
        t = new Tonel("Brahma", "Extra", "5,2", "7,3", "Lagger");
        t.getPreco().add(getNextDouble());
        t.getVolume().add(new Double(200));
        lista.add(t);
        return lista;
    }

    public static Double getNextDouble() {
        double min = 10.9;
        double max = 134.87;
        Random random = new Random();
        return min + (max - min) * random.nextDouble();
    }

    public String getMarca() { return marca; }

    public void setMarca(String marca) { this.marca = marca; }

    public String getNomeChopp() {
        return nomeChopp;
    }

    public void setNomeChopp(String nomeChopp) {
        this.nomeChopp = nomeChopp;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getAbv() { return abv; }

    public void setAbv(String abv) { this.abv = abv; }

    public String getEstilo() { return estilo; }

    public void setEstilo(String estilo) { this.estilo = estilo; }

    public List<Double> getPreco() {
        return preco;
    }

    public void setPreco(List<Double> preco) {
        this.preco = preco;
    }

    public List<String> getDescricaoChopp() {
        return descricaoChopp;
    }

    public void setDescricaoChopp(List<String> descricaoChopp) { this.descricaoChopp = descricaoChopp; }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public List<Double> getVolume() {
        return volume;
    }

    public void setVolume(List<Double> volume) {
        this.volume = volume;
    }
    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }
}
