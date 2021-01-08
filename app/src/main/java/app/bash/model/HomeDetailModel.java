package app.bash.model;

public class HomeDetailModel {
    String id,status,title,category_id,governmental_fees,details,logo_file,requirements,price,category_name,updated_at,no_of_days_to_complete;

    public String getNo_of_days_to_complete() {
        return no_of_days_to_complete;
    }

    public void setNo_of_days_to_complete(String no_of_days_to_complete) {
        this.no_of_days_to_complete = no_of_days_to_complete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getGovernmental_fees() {
        return governmental_fees;
    }

    public void setGovernmental_fees(String governmental_fees) {
        this.governmental_fees = governmental_fees;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLogo_file() {
        return logo_file;
    }

    public void setLogo_file(String logo_file) {
        this.logo_file = logo_file;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}