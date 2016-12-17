package service.objects;


import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    private List<String> user_ids=new ArrayList<>();

    public SearchResponse(List<String> user_ids) {
        this.user_ids = user_ids;
    }

    public SearchResponse() {

    }

    public List<String> getUser_ids() {

        return user_ids;
    }

    public void setUser_ids(List<String> user_ids) {
        this.user_ids = user_ids;
    }
}
