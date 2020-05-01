package com.kronostools.timehammer.model;

import com.kronostools.timehammer.enums.TrashMessageStatus;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "TrashMessage")
@Table(name = "trash_message")
public class TrashMessage {
    @EmbeddedId
    private TrashMessageId id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrashMessageStatus status;

    @Column(nullable = false, updatable = false)
    private String text;

    public TrashMessageId getId() {
        return id;
    }

    public void setId(TrashMessageId id) {
        this.id = id;
    }

    public TrashMessageStatus getStatus() {
        return status;
    }

    public void setStatus(TrashMessageStatus status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrashMessage that = (TrashMessage) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}