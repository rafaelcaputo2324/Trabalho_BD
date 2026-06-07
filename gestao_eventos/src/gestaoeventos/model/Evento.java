package gestaoeventos.model;

import java.sql.Date;

public class Evento {
    private Integer id;
    private String titulo;
    private String descricao;
    private Date dataEvento;
    private String local;
    private Integer capacidadeMaxima;

    public Evento() {}

    public Evento(Integer id, String titulo, String descricao, Date dataEvento, String local, Integer capacidadeMaxima) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataEvento = dataEvento;
        this.local = local;
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Evento(String titulo, String descricao, Date dataEvento, String local, Integer capacidadeMaxima) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataEvento = dataEvento;
        this.local = local;
        this.capacidadeMaxima = capacidadeMaxima;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataEvento() {
        return dataEvento;
    }

    public void setDataEvento(Date dataEvento) {
        this.dataEvento = dataEvento;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Integer getCapacidadeMaxima() {
        return capacidadeMaxima;
    }

    public void setCapacidadeMaxima(Integer capacidadeMaxima) {
        this.capacidadeMaxima = capacidadeMaxima;
    }

    @Override
    public String toString() {
        return titulo + " (" + dataEvento + ")";
    }
}
