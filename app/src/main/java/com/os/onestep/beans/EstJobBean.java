package com.os.onestep.beans;

public class EstJobBean {

    private String id;
    private String project_id;
    private String status;
    private String job_id;
    private String estimate_id;
    private String Quantity;
    private String Unit;
    private String Coats;
    private String Finish;
    private String Rates;
    private String Gals;
    private String Time;
    private String created_date;
    private String job;

    public EstJobBean(String id, String project_id, String status, String job_id,
                      String estimate_id, String quantity, String unit, String coats,
                      String finish, String rates, String gals, String time, String created_date, String job) {
        this.id = id;
        this.project_id = project_id;
        this.status = status;
        this.job_id = job_id;
        this.estimate_id = estimate_id;
        Quantity = quantity;
        Unit = unit;
        Coats = coats;
        Finish = finish;
        Rates = rates;
        Gals = gals;
        Time = time;
        this.created_date = created_date;
        this.job = job;
    }

    public String getJob() {
        return job;
    }

    public String getId() {
        return id;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getStatus() {
        return status;
    }

    public String getJob_id() {
        return job_id;
    }

    public String getEstimate_id() {
        return estimate_id;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getUnit() {
        return Unit;
    }

    public String getCoats() {
        return Coats;
    }

    public String getFinish() {
        return Finish;
    }

    public String getRates() {
        return Rates;
    }

    public String getGals() {
        return Gals;
    }

    public String getTime() {
        return Time;
    }

    public String getCreated_date() {
        return created_date;
    }
}
