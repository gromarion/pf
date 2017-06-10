package com.itba.domain.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.itba.domain.PersistentEntity;

@Entity
@Table(name = "evaluated_resource")
public class EvaluatedResource extends PersistentEntity {

    @ManyToOne
    private EvaluationSession session;

    @Column(name = "resource")
    private String resource;

    @Column(name = "comments")
    private String comments;
    
    @Column(name = "correct")
    private boolean correct;
    
    @Column(name = "timestamp")
    private long timestamp;

    @OneToMany(mappedBy = "resource")
    private Set<EvaluatedResourceDetail> details;

    EvaluatedResource() {}

    public EvaluatedResource(EvaluationSession session, String resource) {
        this.session = session;
        this.resource = resource;
        this.timestamp = System.currentTimeMillis();
    }

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public EvaluationSession getSession() {
		return session;
	}

	public String getResource() {
		return resource;
	}
	
	public long getTimestamp() {
		return timestamp;
	}
	
	public String getFormattedDate() {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		return format.format(new Date(timestamp));
	}
	
	public boolean isCorrect() {
		return correct;
	}

	public Set<EvaluatedResourceDetail> getDetails() {
		return details;
	}
}
