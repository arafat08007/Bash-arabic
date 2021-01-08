package app.bash.model;

public class HomeFirstModel {
    String id,price;
    String image;
    String no_of_days_to_complete;

    public String getNo_of_days_to_complete() {
        return no_of_days_to_complete;
    }

    public void setNo_of_days_to_complete(String no_of_days_to_complete) {
        this.no_of_days_to_complete = no_of_days_to_complete;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    String name,updated_at;
    boolean status;
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
