package gestaoeventos.model;

import java.sql.Timestamp;

public class Atividade {
    private Integer id;
    private Integer eventoId;
    private Integer palestranteId;
    private String titulo;
    private Timestamp dataHora;
    private Integer duracaoMinutos;


    private String eventoTitulo;
    private String palestranteNome;

    public Atividade() {}

    public Atividade(Integer id, Integer eventoId, Integer palestranteId, String titulo, Timestamp dataHora, Integer duracaoMinutos) {
        this.id = id;
        this.eventoId = eventoId;
        this.palestranteId = palestranteId;
        this.titulo = titulo;
        this.dataHora = dataHora;
        this.duracaoMinutos = duracaoMinutos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }

    public Integer getPalestranteId() {
        return palestranteId;
    }

    public void setPalestranteId(Integer palestranteId) {
        this.palestranteId = palestranteId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(Integer duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public void setEventoTitulo(String eventoTitulo) {
        this.eventoTitulo = eventoTitulo;
    }

    public String getPalestranteNome() {
        return palestranteNome;
    }

    public void setPalestranteNome(String palestranteNome) {
        this.palestranteNome = palestranteNome;
    }

    @Override
    public String toString() {
        return titulo + " (" + duracaoMinutos + " min)";
    }
}
