package br.beer.beerbeacon.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronan.lima on 14/02/17.
 */
public class Tonel {
    private String nomeChopp, ibu, alcool, cor;
    private List<Double> preco;
    private List<String> descricaoChopp;

    public Tonel(String nomeChopp, String ibu, String alcool, String cor, List<Double> preco, List<String> descricaoChopp) {
        setNomeChopp(nomeChopp);
        setIbu(ibu);
        setAlcool(alcool);
        setCor(cor);
        setPreco(new ArrayList<Double>());
        setDescricaoChopp(new ArrayList<String>());
    }

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

    public String getAlcool() {
        return alcool;
    }

    public void setAlcool(String alcool) {
        this.alcool = alcool;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public List<Double> getPreco() {
        return preco;
    }

    public void setPreco(List<Double> preco) {
        this.preco = preco;
    }

    public List<String> getDescricaoChopp() {
        return descricaoChopp;
    }

    public void setDescricaoChopp(List<String> descricaoChopp) {
        this.descricaoChopp = descricaoChopp;
    }
}
