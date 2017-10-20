package com.lsg.project;

import java.util.Date;

public class NoteBean {

	private String title;
	private String description;
	private String tags;
	private Date creationDate;
	private Date plannedDate;
	private NoteType type;
	private NoteStatus status;
	private String attachment;
	
	public NoteBean() {
		
	}
	
	public NoteBean(String title, String description, String tags,Date creationDate,NoteType type,String attachment) {
		super();
		this.title = title;
		this.description = description;
		this.tags = tags;
		this.creationDate = creationDate;
		this.type=type;
		this.attachment=attachment;
	}
	
	public NoteBean(String title, String description, String tags,Date creationDate,NoteType type,Date plannedDate,NoteStatus status,String attachment) {
		super();
		this.title = title;
		this.description = description;
		this.tags = tags;
		this.creationDate = creationDate;
		this.type=type;
		this.plannedDate=plannedDate;
		this.status=status;
		this.attachment=attachment;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getPlannedDate() {
		return plannedDate;
	}

	public void setPlannedDate(Date plannedDate) {
		this.plannedDate = plannedDate;
	}

	public NoteType getType() {
		return type;
	}

	public void setType(NoteType type) {
		this.type = type;
	}

	public NoteStatus getStatus() {
		return status;
	}

	public void setStatus(NoteStatus status) {
		this.status = status;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((attachment == null) ? 0 : attachment.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((plannedDate == null) ? 0 : plannedDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tags == null) ? 0 : tags.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NoteBean other = (NoteBean) obj;
		if (attachment == null) {
			if (other.attachment != null)
				return false;
		} else if (!attachment.equals(other.attachment))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (plannedDate == null) {
			if (other.plannedDate != null)
				return false;
		} else if (!plannedDate.equals(other.plannedDate))
			return false;
		if (status != other.status)
			return false;
		if (tags == null) {
			if (other.tags != null)
				return false;
		} else if (!tags.equals(other.tags))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NoteBean [title=" + title + ", description=" + description + ", tags=" + tags + ", creationDate="
				+ creationDate + ", plannedDate=" + plannedDate + ", type=" + type + ", status=" + status
				+ ", attachment=" + attachment + "]";
	}
	
	

}
