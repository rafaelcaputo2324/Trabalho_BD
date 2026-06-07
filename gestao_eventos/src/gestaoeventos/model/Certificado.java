package gestaoeventos.model;

import java.sql.Timestamp;

public class Certificado {
    private Integer id;
    private Integer inscricaoId;
    private String codigoAutenticacao;
    private Timestamp dataEmissao;


    private String participanteNome;
    private String participanteEmail;
    private String eventoTitulo;
    private String eventoData;

    public Certificado() {}

    public Certificado(Integer id, Integer inscricaoId, String codigoAutenticacao, Timestamp dataEmissao) {
        this.id = id;
        this.inscricaoId = inscricaoId;
        this.codigoAutenticacao = codigoAutenticacao;
        this.dataEmissao = dataEmissao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInscricaoId() {
        return inscricaoId;
    }

    public void setInscricaoId(Integer inscricaoId) {
        this.inscricaoId = inscricaoId;
    }

    public String getCodigoAutenticacao() {
        return codigoAutenticacao;
    }

    public void setCodigoAutenticacao(String codigoAutenticacao) {
        this.codigoAutenticacao = codigoAutenticacao;
    }

    public Timestamp getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Timestamp dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getParticipanteNome() {
        return participanteNome;
    }

    public void setParticipanteNome(String participanteNome) {
        this.participanteNome = participanteNome;
    }

    public String getParticipanteEmail() {
        return participanteEmail;
    }

    public void setParticipanteEmail(String participanteEmail) {
        this.participanteEmail = participanteEmail;
    }

    public String getEventoTitulo() {
        return eventoTitulo;
    }

    public void setEventoTitulo(String eventoTitulo) {
        this.eventoTitulo = eventoTitulo;
    }

    public String getEventoData() {
        return eventoData;
    }

    public void setEventoData(String eventoData) {
        this.eventoData = eventoData;
    }

    @Override
    public String toString() {
        return "Certificado " + codigoAutenticacao + " - " + participanteNome;
    }
}
