package br.beer.beerbeacon.firebase;

import java.util.List;

import br.beer.beerbeacon.bean.Tonel;

/**
 * Created by Ronan.lima on 10/03/17.
 */

public interface ListenersFB {
    void listenPedidos(Double valor);
    void readTonelsAvailables(List<Tonel> tonel);
}
