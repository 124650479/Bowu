package fu.prm391.sampl.project.model.address.get_ward;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ward {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    public Ward() {
    }

    public Ward(int id, String name, String prefix) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
