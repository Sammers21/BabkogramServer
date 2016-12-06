package service.objects;


public class ReturnDialogConferenceId {
    private String conference_id;

    public ReturnDialogConferenceId(String conference_id) {
        this.conference_id = conference_id;
    }

    public String getConference_id() {

        return conference_id;
    }

    public void setConference_id(String conference_id) {
        this.conference_id = conference_id;
    }
}
