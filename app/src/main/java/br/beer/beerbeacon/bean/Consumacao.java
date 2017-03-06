package br.beer.beerbeacon.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ronan.lima on 06/03/17.
 */

public class Consumacao {
    private String id;
    private List<Pedido> pedidos;

    public Consumacao(String id) {
        this.id = id;
        setPedidos(new ArrayList<Pedido>());
    }

    public String getId() {
        return id;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }
}
