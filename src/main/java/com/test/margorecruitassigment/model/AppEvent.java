package com.test.margorecruitassigment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AppEvent {
    @Id
    private String eventId;
    @Transient
    private Date startTime;
    @Transient
    private Date endTime;
    private Long eventDuration;
    private String type;
    private String host;
    private Boolean alert;
}
