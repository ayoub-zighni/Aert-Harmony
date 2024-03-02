package models;

public class Images {

    private int id;
    private String img;

    // Constructors, getters, and setters
    public Images() {
    }

    public Images(String img) {
        this.img = img;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Images(int id, String img) {
        this.id = id;
        this.img = img;
    }

}
