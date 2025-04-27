package org.screen.form;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ClientDTO {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty status;
    private final SimpleStringProperty ip;
    private final SimpleStringProperty job;
    private final SimpleStringProperty name;
    private final SimpleStringProperty position;

    public ClientDTO(int id, String status, String ip, String job, String name, String position) {
        this.id = new SimpleIntegerProperty(id);
        this.status = new SimpleStringProperty(status);
        this.ip = new SimpleStringProperty(ip);
        this.job = new SimpleStringProperty(job);
        this.name = new SimpleStringProperty(name);
        this.position = new SimpleStringProperty(position);
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getIP() {
        return ip.get();
    }

    public void setIP(String ip) {
        this.ip.set(ip);
    }

    public String getJob() {
        return this.job.get();
    }

    public void setJob(String job) {
        this.job.set(job);
    }

    public String getName() {
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getPosition() {
        return this.position.get();
    }

    public void setPosition(String position) {
        this.position.set(position);
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public SimpleStringProperty ipProperty() {
        return ip;
    }

    public SimpleStringProperty jobProperty() {
        return job;
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public SimpleStringProperty positionProperty() {
        return position;
    }
}
