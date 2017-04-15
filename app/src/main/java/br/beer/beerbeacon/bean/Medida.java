package br.beer.beerbeacon.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronanlima on 14/04/17.
 */

public class Medida {
    private List<String> preco;
    private List<String> quantidade;

    public Medida() {
        setPreco(new ArrayList<String>());
        setQuantidade(new ArrayList<String>());
    }

    public List<String> getPreco() {
        return preco;
    }

    public void setPreco(List<String> preco) {
        this.preco = preco;
    }

    public List<String> getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(List<String> quantidade) {
        this.quantidade = quantidade;
    }
}
