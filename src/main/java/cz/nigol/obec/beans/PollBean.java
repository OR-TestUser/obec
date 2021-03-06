package cz.nigol.obec.beans;

import java.io.Serializable;
import java.util.*;
import java.util.stream.*;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import cz.nigol.obec.entities.*;
import cz.nigol.obec.services.PollService;
import cz.nigol.obec.qualifiers.CurrentSettings;

@Named
@ViewScoped
public class PollBean implements Serializable {
    private static final long serialVersionUID = 1L;
    @Inject
    private PollService pollService;
    @Inject
    private FacesContext facesContext;
    @Inject
    private SessionBean sessionBean;
    @Inject
    @CurrentSettings
    private Settings settings;
    private Poll poll;
    private long id;
    private String fingerprint;

    public void onLoad() {
        poll = pollService.findById(id);
    }

    public void vote(PollQuestion question) {
        PollAnswer answer = new PollAnswer();
        answer.setCreatedAt(new Date());
        answer.setPollQuestion(question);
        answer.setFingerPrint(fingerprint);
        if (isAnswerUnique(fingerprint)) {
            pollService.savePollAnswer(answer);    
        }
        sessionBean.setPollPerformed(true);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "",
                    "Děkujeme za Váš hlas pro volbu '" + question.getQuestion() + "'."));
    }

    private boolean isAnswerUnique(String fingerprint) {
        int size = poll.getPollQuestions().stream()
                        .flatMap(pq -> pq.getPollAnswers().stream())
                        .map(PollAnswer::getFingerPrint)
                        .filter(fp -> fingerprint.equals(fp))
                        .collect(Collectors.toList()).size();
        return size == 0;
    }

    public String getUrl() {
        return settings.getBaseUrl() + "/anketa.jsf?id=" + poll.getId();
    }


    /**
     * @return the poll
     */
    public Poll getPoll() {
        return poll;
    }

    /**
     * @param poll the poll to set
     */
    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
}
