package com.itba.domain.model;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.common.collect.Lists;
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
	@Column(name = "score")
	private BigDecimal score;
	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL)
	private Set<EvaluatedResourceDetail> details = new HashSet<>();

	EvaluatedResource() {
	}

	public EvaluatedResource(EvaluationSession session, String resource) {
		this.session = session;
		this.resource = resource;
		this.correct = false;
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

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	public Set<EvaluatedResourceDetail> getDetails() {
		return details;
	}

	public boolean hasDetails() {
		return details.size() > 0;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public List<String> getAsCsvLines(Character separator) {
		List<String> lines = Lists.newArrayList();
		StringBuilder prefixBuilder = new StringBuilder();
		prefixBuilder.append(session.getUser().getUsername()).append(separator);
		prefixBuilder.append(session.getUser().getFullName()).append(separator);
		prefixBuilder.append(getFormattedDate()).append(separator);
		prefixBuilder.append(translateCorrect(correct)).append(separator);
		prefixBuilder.append(score).append(separator);
		prefixBuilder.append(doubleQuote(resource)).append(separator);
		String prefix = prefixBuilder.toString();

		if (!this.correct) {
			for (EvaluatedResourceDetail detail : details) {
				StringBuilder builder = new StringBuilder();
				builder.append(prefix);
				builder.append(doubleQuote(detail.getPredicate())).append(separator);
				builder.append(replaceCommas(detail.getObject())).append(separator);
				builder.append(doubleQuote(detail.getError().getName()));
				lines.add(builder.toString());
			}
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append(prefix);
			builder.append(separator);
			builder.append(separator);
			lines.add(builder.toString());
		}
		return lines;
	}

	private String translateCorrect(boolean correct) {
		return correct ? "Si" : "No";
	}
	
	private String doubleQuote(String string) {
		return "\"" + string + "\"";
	}
	
	private String replaceCommas(String string) {
		return string.replaceAll(",", ";");
	}
}
