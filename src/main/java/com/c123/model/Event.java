package com.c123.model;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

public class Event implements Serializable {
    private int reporterId;
    private Date timestamp;
    private int metricId;
    private int metricValue;
    private String message;

    public Event() {
        this.reporterId = 0;
        this.timestamp = new Date();
        this.metricId = 0;
        this.metricValue = 0;
        this.message = "null";
    }

    public Event(int reporterId, Date timestamp, int metricId, int metricValue, String message) {
        this.reporterId = reporterId;
        this.timestamp = timestamp;
        this.metricId = metricId;
        this.metricValue = metricValue;
        this.message = message;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getMetricId() {
        return metricId;
    }

    public void setMetricId(int metricId) {
        this.metricId = metricId;
    }

    public int getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(int metricValue) {
        this.metricValue = metricValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Event.class.getSimpleName() + "[", "]")
                .add("reporterId=" + reporterId)
                .add("timestamp=" + timestamp.toString())
                .add("metricId=" + metricId)
                .add("metricValue=" + metricValue)
                .add("message='" + message + "'")
                .toString();
    }
}