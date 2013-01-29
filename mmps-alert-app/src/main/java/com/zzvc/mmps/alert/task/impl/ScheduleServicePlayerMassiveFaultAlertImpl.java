package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;
import com.zzvc.mmps.alert.service.AlertConfigManager;
import com.zzvc.mmps.alert.service.PlayerManager;
import com.zzvc.mmps.alert.service.PlayerMassiveFaultAlertManager;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.model.Player;

public class ScheduleServicePlayerMassiveFaultAlertImpl extends ScheduleServiceAlertSupport {
	
	@Resource
	private PlayerManager playerManager;
	
	@Resource
	private AlertConfigManager alertConfigManager;
	
	@Resource
	private PlayerMassiveFaultAlertManager playerMassiveFaultAlertManager;
	
	private int minutesBeforePlayerFault;
	private int playerMassiveFaultAriseThreshold;
	private int playerMassiveFaultRecoverThreshold;
	
	@Override
	public void init() {
		try {
			minutesBeforePlayerFault = Integer.parseInt(alertConfigManager.getConfig(AlertConstants.CFG_MINUTES_BEFORE_PLAYER_FAULT));
		} catch (Exception e) {
			minutesBeforePlayerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_PLAYER_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_PLAYER_FAULT", minutesBeforePlayerFault);
		
		try {
			playerMassiveFaultAriseThreshold = Integer.parseInt(alertConfigManager.getConfig(AlertConstants.CFG_PLAYER_MASSIVE_FAULT_THRESHOLD));
		} catch (Exception e) {
			playerMassiveFaultAriseThreshold = getDefaultPlayerMassiveFaultThreshold();
		}
		infoMessage("alert.config.parameter", "CFG_PLAYER_MASSIVE_FAULT_THRESHOLD", playerMassiveFaultAriseThreshold);
		
		playerMassiveFaultRecoverThreshold = getPlayerMassiveFaultRecoverThreshold(playerMassiveFaultAriseThreshold);
	}

	@Override
	public void onSchedule() {
		PlayerMassiveFaultAlert playerMassiveFaultAlert = playerMassiveFaultAlertManager.findActiveAlert();
		List<Player> faultPlayers = getFaultPlayerList();
		if (faultPlayers.size() >= playerMassiveFaultAriseThreshold && playerMassiveFaultAlert == null) {
			playerMassiveFaultAlert = new PlayerMassiveFaultAlert();
			playerMassiveFaultAlert.setFaultPlayers(faultPlayers.size());
			activeAlert(playerMassiveFaultAlert);
			playerMassiveFaultAlertManager.save(playerMassiveFaultAlert);
			infoMessage("alert.arising.player.massive.fault", faultPlayers.size());
		} else if (faultPlayers.size() <= playerMassiveFaultRecoverThreshold && playerMassiveFaultAlert != null) {
			recoverAlert(playerMassiveFaultAlert);
			playerMassiveFaultAlertManager.save(playerMassiveFaultAlert);
			info(findTextFilterNullArgs("alert.recover.player.massive.fault", faultPlayers.size(), playerMassiveFaultAlert.getAriseTime()));
		}
	}
	
	private List<Player> getFaultPlayerList() {
		return playerManager.findByFaultLasting(minutesBeforePlayerFault);
	}
	
	private int getDefaultPlayerMassiveFaultThreshold() {
		int numberOfPlayers = playerManager.getAll().size();
		return (numberOfPlayers - 1) / 2 + 1;
	}
	
	private int getPlayerMassiveFaultRecoverThreshold(int playerMassiveFaultAriseThreshold) {
		return (playerMassiveFaultAriseThreshold - 1) / 2 + 1;
	}

}
