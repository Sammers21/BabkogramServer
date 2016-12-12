package service.objects;

/**
 * Created by student on 12.12.16.
 */
public class Dialog_IdResponse {

    private String dialog_name;

    public String getDialog_name() {
        return dialog_name;
    }

    public void setDialog_name(String dialog_name) {
        this.dialog_name = dialog_name;
    }

    public Dialog_IdResponse(String dialog_name) {

        this.dialog_name = dialog_name;
    }
}
