package gestaoeventos.model;

import java.sql.Timestamp;

public class Inscricao {
    private Integer id;
    private Integer participanteId;
    private Integer eventoId;
    private Timestamp dataInscricao;
    private String status; 


    private String participanteNome;
    private String participanteEmail;
    private String eventoTitulo;

    public Inscricao() {}

    public Inscricao(Integer id, Integer participanteId, Integer eventoId, Timestamp dataInscricao, String status) {
        this.id = id;
        this.participanteId = participanteId;
        this.eventoId = eventoId;
        this.dataInscricao = dataInscricao;
        this.status = status;
    }

    public Inscricao(Integer participanteId, Integer eventoId, String status) {
        this.participanteId = participanteId;
        this.eventoId = eventoId;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParticipanteId() {
        return participanteId;
    }

    public void setParticipanteId(Integer participanteId) {
        this.participanteId = participanteId;
    }

    public Integer getEventoId() {
        return eventoId;
    }

    public void setEventoId(Integer eventoId) {
        this.eventoId = eventoId;
    }

    public Timestamp getDataInscricao() {
        return dataInscricao;
    }

    public void setDataInscricao(Timestamp dataInscricao) {
        this.dataInscricao = dataInscricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    @Override
    public String toString() {
        return (participanteNome != null ? participanteNome : "ID " + participanteId) + 
               " em " + 
               (eventoTitulo != null ? eventoTitulo : "ID " + eventoId);
    }
}
