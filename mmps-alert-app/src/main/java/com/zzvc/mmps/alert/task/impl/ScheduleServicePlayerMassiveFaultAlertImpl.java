package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.PlayerMassiveFaultAlertDao;
import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.dao.PlayerDao;
import com.zzvc.mmps.model.Player;

public class ScheduleServicePlayerMassiveFaultAlertImpl extends ScheduleServiceAlertSupport {
	
	@Resource
	private PlayerDao playerDao;
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private PlayerMassiveFaultAlertDao playerMassiveFaultAlertDao;
	
	private int minutesBeforePlayerFault;
	private int playerMassiveFaultAriseThreshold;
	private int playerMassiveFaultRecoverThreshold;
	
	@Override
	public void init() {
		try {
			minutesBeforePlayerFault = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_MINUTES_BEFORE_PLAYER_FAULT));
		} catch (Exception e) {
			minutesBeforePlayerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_PLAYER_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_PLAYER_FAULT", minutesBeforePlayerFault);
		
		try {
			playerMassiveFaultAriseThreshold = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_PLAYER_MASSIVE_FAULT_THRESHOLD));
		} catch (Exception e) {
			playerMassiveFaultAriseThreshold = getDefaultPlayerMassiveFaultThreshold();
		}
		infoMessage("alert.config.parameter", "CFG_PLAYER_MASSIVE_FAULT_THRESHOLD", playerMassiveFaultAriseThreshold);
		
		playerMassiveFaultRecoverThreshold = getPlayerMassiveFaultRecoverThreshold(playerMassiveFaultAriseThreshold);
	}

	@Override
	public void onSchedule() {
		PlayerMassiveFaultAlert playerMassiveFaultAlert = playerMassiveFaultAlertDao.findActiveAlert();
		List<Player> faultPlayers = getFaultPlayerList();
		if (faultPlayers.size() >= playerMassiveFaultAriseThreshold && playerMassiveFaultAlert == null) {
			playerMassiveFaultAlert = new PlayerMassiveFaultAlert();
			playerMassiveFaultAlert.setFaultPlayers(faultPlayers.size());
			activeAlert(playerMassiveFaultAlert);
			playerMassiveFaultAlertDao.save(playerMassiveFaultAlert);
			infoMessage("alert.arising.player.massive.fault", faultPlayers.size());
		} else if (faultPlayers.size() <= playerMassiveFaultRecoverThreshold && playerMassiveFaultAlert != null) {
			recoverAlert(playerMassiveFaultAlert);
			playerMassiveFaultAlertDao.save(playerMassiveFaultAlert);
			info(findTextFilterNullArgs("alert.recover.player.massive.fault", faultPlayers.size(), playerMassiveFaultAlert.getAriseTime()));
		}
	}
	
	private List<Player> getFaultPlayerList() {
		return playerDao.findByHeartbeatBefore(new Date(System.currentTimeMillis() - minutesBeforePlayerFault * MILLISECONDS_OF_MINUTE));
	}
	
	private int getDefaultPlayerMassiveFaultThreshold() {
		int numberOfPlayers = playerDao.findAll().size();
		return (numberOfPlayers - 1) / 2 + 1;
	}
	
	private int getPlayerMassiveFaultRecoverThreshold(int playerMassiveFaultAriseThreshold) {
		return (playerMassiveFaultAriseThreshold - 1) / 2 + 1;
	}

}
