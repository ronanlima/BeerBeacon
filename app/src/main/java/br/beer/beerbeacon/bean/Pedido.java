package br.beer.beerbeacon.bean;

/**
 * Created by Ronan.lima on 06/03/17.
 */

public class Pedido {
    private String titulo;
    private Integer qtd;
    private Long dateTime;
    private Double preco;

    public Pedido(String titulo, Integer qtd, Long dateTime, Double preco) {
        setTitulo(titulo);
        setQtd(qtd);
        setDateTime(dateTime);
        setPreco(preco);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
