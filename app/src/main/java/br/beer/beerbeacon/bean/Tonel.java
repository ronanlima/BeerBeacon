package br.beer.beerbeacon.bean;

import android.view.View;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Ronan.lima on 14/02/17.
 */
public class Tonel {
    private String cervejaria, cerveja, ibu, abv, bid, estilo, nota, urlImageCerveja, urlImgCervejaria;
    private Long dataEntrada;
    private List<Medida> medidas;
    private View.OnClickListener requestBtnClickListener;

    public Tonel() {
        setMedidas(new ArrayList<Medida>());
    }

    public Tonel(String cervejaria, String cerveja, String ibu, String abv, String estilo) {
        setCervejaria(cervejaria);
        setCerveja(cerveja);
        setIbu(ibu);
        setAbv(abv);
        setEstilo(estilo);
    }

    public static ArrayList<Tonel> getTonelList() {
        ArrayList<Tonel> lista = new ArrayList<>();
        Tonel t = new Tonel("Dogma", "Hopp Lagger", "10,1", "7,3", "Lagger");
        lista.add(t);
        t = new Tonel("Colorado", "Colorado SeiLaOQuê", "9,8", "7,3", "ABC");
        lista.add(t);
        t = new Tonel("Eisenbhan", "Eisenbhan Weizenbier", "6,2", "4,3", "Weizenbier");
        lista.add(t);
        t = new Tonel("Brahma", "Extra", "5,2", "7,3", "Lagger");
        lista.add(t);
        return lista;
    }

    public static BigDecimal getNextBigDecimal() {
        BigDecimal min = new BigDecimal(10d);
        BigDecimal max = new BigDecimal(134.87);
        Random random = new Random();
        return new BigDecimal(min.doubleValue() + (max.doubleValue() - min.doubleValue()) * random.nextDouble());
    }

    public String getCervejaria() {
        return cervejaria;
    }

    public void setCervejaria(String cervejaria) {
        this.cervejaria = cervejaria;
    }

    public String getCerveja() {
        return cerveja;
    }

    public void setCerveja(String cerveja) {
        this.cerveja = cerveja;
    }

    public String getIbu() {
        return ibu;
    }

    public void setIbu(String ibu) {
        this.ibu = ibu;
    }

    public String getAbv() {
        return abv;
    }

    public void setAbv(String abv) {
        this.abv = abv;
    }

    public String getEstilo() {
        return estilo;
    }

    public void setEstilo(String estilo) {
        this.estilo = estilo;
    }

    public Long getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Long dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    public List<Medida> getMedidas() {
        return medidas;
    }

    public void setMedidas(List<Medida> medidas) {
        this.medidas = medidas;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public View.OnClickListener getRequestBtnClickListener() {
        return requestBtnClickListener;
    }

    public void setRequestBtnClickListener(View.OnClickListener requestBtnClickListener) {
        this.requestBtnClickListener = requestBtnClickListener;
    }

    public String getUrlImageCerveja() {
        return urlImageCerveja;
    }

    public void setUrlImageCerveja(String urlImageCerveja) {
        this.urlImageCerveja = urlImageCerveja;
    }

    public String getUrlImgCervejaria() {
        return urlImgCervejaria;
    }

    public void setUrlImgCervejaria(String urlImgCervejaria) {
        this.urlImgCervejaria = urlImgCervejaria;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
