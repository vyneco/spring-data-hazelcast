package test.utils.repository.mapstore;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class Book implements Serializable {

    @Id
    private String id;
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
