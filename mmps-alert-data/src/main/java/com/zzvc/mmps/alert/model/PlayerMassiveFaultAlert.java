package com.zzvc.mmps.alert.model;

// Generated 2011-7-11 11:44:27 by Hibernate Tools 3.2.4.GA

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * PlayerMassiveFaultAlert generated by hbm2java
 */
@Entity
@Table(name = "player_massive_fault_alert")
public class PlayerMassiveFaultAlert extends AlertBaseEntity implements java.io.Serializable {
	private int faultPlayers;

	public PlayerMassiveFaultAlert() {
	}

	public PlayerMassiveFaultAlert(long id) {
		this.id = id;
	}

	public PlayerMassiveFaultAlert(long id, int faultPlayers, Date ariseTime, Boolean active,
			Date recoverTime, Boolean mailed, Boolean smsSend) {
		this.id = id;
		this.faultPlayers = faultPlayers;
		this.ariseTime = ariseTime;
		this.active = active;
		this.recoverTime = recoverTime;
		this.mailSend = mailed;
		this.smsSend = smsSend;
	}

	@Column(name = "fault_players")
	public int getFaultPlayers() {
		return faultPlayers;
	}

	public void setFaultPlayers(int faultPlayers) {
		this.faultPlayers = faultPlayers;
	}

}
