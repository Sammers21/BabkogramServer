package service.objects;


public class UserInConferenceList {
    private String user_id;

    public UserInConferenceList() {
    }

    public String getUser_id() {

        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UserInConferenceList(String user_id, long join_time) {

        this.user_id = user_id;
        this.join_time = join_time;
    }

    public long getJoin_time() {
        return join_time;
    }

    public void setJoin_time(long join_time) {
        this.join_time = join_time;
    }

    private long join_time;

}
