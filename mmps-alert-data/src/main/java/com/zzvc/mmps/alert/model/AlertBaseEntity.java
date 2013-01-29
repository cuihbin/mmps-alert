package com.zzvc.mmps.alert.model;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class AlertBaseEntity implements java.io.Serializable {

	protected long id;
	protected Boolean active;
	protected Date ariseTime;
	protected Date recoverTime;
	protected Boolean mailSend;
	protected Boolean smsSend;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "active")
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "arise_time", length = 23)
	public Date getAriseTime() {
		return ariseTime;
	}

	public void setAriseTime(Date ariseTime) {
		this.ariseTime = ariseTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "recover_time", length = 23)
	public Date getRecoverTime() {
		return this.recoverTime;
	}

	public void setRecoverTime(Date recoverTime) {
		this.recoverTime = recoverTime;
	}

	@Column(name = "mail_send")
	public Boolean getMailSend() {
		return this.mailSend;
	}

	public void setMailSend(Boolean mailed) {
		this.mailSend = mailed;
	}

	@Column(name = "sms_send")
	public Boolean getSmsSend() {
		return this.smsSend;
	}

	public void setSmsSend(Boolean smsSend) {
		this.smsSend = smsSend;
	}

}
