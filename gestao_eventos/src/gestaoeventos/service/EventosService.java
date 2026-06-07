package gestaoeventos.service;

import gestaoeventos.dao.CertificadoDAO;
import gestaoeventos.dao.EventoDAO;
import gestaoeventos.dao.InscricaoDAO;
import gestaoeventos.model.Certificado;
import gestaoeventos.model.Evento;
import gestaoeventos.model.Inscricao;
import java.util.UUID;

public class EventosService {
    private final EventoDAO eventoDAO = new EventoDAO();
    private final InscricaoDAO inscricaoDAO = new InscricaoDAO();
    private final CertificadoDAO certificadoDAO = new CertificadoDAO();


    public void registrarInscricao(Inscricao inscricao) throws Exception {

        Inscricao existente = inscricaoDAO.findByParticipanteAndEvento(
                inscricao.getParticipanteId(), inscricao.getEventoId());
        if (existente != null) {
            throw new IllegalArgumentException("Este participante já está inscrito neste evento!");
        }


        if ("Confirmada".equals(inscricao.getStatus())) {
            validarCapacidadeEvento(inscricao.getEventoId());
        }

        inscricaoDAO.create(inscricao);
    }


    public void atualizarStatusInscricao(int inscricaoId, String novoStatus) throws Exception {
        Inscricao insc = inscricaoDAO.findById(inscricaoId);
        if (insc == null) {
            throw new IllegalArgumentException("Inscrição não encontrada!");
        }

        if (insc.getStatus().equals(novoStatus)) {
            return; 
        }


        if ("Confirmada".equals(novoStatus)) {
            validarCapacidadeEvento(insc.getEventoId());
        }




        inscricaoDAO.updateStatus(inscricaoId, novoStatus);
    }


    public Certificado emitirCertificado(int inscricaoId) throws Exception {
        Inscricao insc = inscricaoDAO.findById(inscricaoId);
        if (insc == null) {
            throw new IllegalArgumentException("Inscrição não encontrada!");
        }


        if (!"Confirmada".equals(insc.getStatus())) {
            throw new IllegalStateException("Não é possível emitir certificado para inscrições com status '" + insc.getStatus() + "'. A inscrição deve estar 'Confirmada'.");
        }


        Certificado existente = certificadoDAO.findByInscricaoId(inscricaoId);
        if (existente != null) {
            throw new IllegalArgumentException("Um certificado já foi emitido para esta inscrição! Código: " + existente.getCodigoAutenticacao());
        }


        String codigo = "CERT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Certificado cert = new Certificado();
        cert.setInscricaoId(inscricaoId);
        cert.setCodigoAutenticacao(codigo);

        certificadoDAO.create(cert);


        return certificadoDAO.findById(cert.getId());
    }


    private void validarCapacidadeEvento(int eventoId) throws Exception {
        Evento evento = eventoDAO.findById(eventoId);
        if (evento == null) {
            throw new IllegalArgumentException("Evento não encontrado!");
        }

        int inscritos = eventoDAO.getConfirmedInscriptionsCount(eventoId);
        if (inscritos >= evento.getCapacidadeMaxima()) {
            throw new IllegalStateException("Capacidade máxima do evento '" + evento.getTitulo() + "' atingida (" + evento.getCapacidadeMaxima() + " vagas)!");
        }
    }
}
