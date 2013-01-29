package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.PlayerAlert;
import com.zzvc.mmps.alert.service.AlertConfigManager;
import com.zzvc.mmps.alert.service.PlayerAlertManager;
import com.zzvc.mmps.alert.service.PlayerManager;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.model.Player;

public class ScheduleServicePlayerFaultAlertImpl extends ScheduleServiceMultiItemsAlertSupport {
	
	@Resource
	private AlertConfigManager alertConfigManager;
	
	@Resource
	private PlayerManager playerManager;
	
	@Resource
	private PlayerAlertManager playerAlertManager;
	
	private int minutesBeforePlayerFault;
	
	@Override
	public void init() {
		try {
			minutesBeforePlayerFault = Integer.parseInt(alertConfigManager.getConfig(AlertConstants.CFG_MINUTES_BEFORE_PLAYER_FAULT));
		} catch (Exception e) {
			minutesBeforePlayerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_SERVER_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_PLAYER_FAULT", minutesBeforePlayerFault);
	}
	
	@Override
	protected List getEfectiveSavedAlerts() {
		return playerAlertManager.findActiveAlert();
	}
	
	@Override
	protected String getAlertKeyString(AlertBaseEntity alert) {
		return ((PlayerAlert) alert).getAddress();
	}
	
	@Override
	protected Comparable getAlertComparableProperty(AlertBaseEntity alert) {
		return ((PlayerAlert) alert).getLastHeartbeat();
	}

	@Override
	protected List getEfectiveLiveFaultEntities() {
		return playerManager.findByFaultLasting(minutesBeforePlayerFault);
	}
	
	@Override
	protected String getFaultEntityKeyString(Object faultEntity) {
		return ((Player) faultEntity).getAddress();
	}
	
	@Override
	protected AlertBaseEntity convertFaultEntityToAlert(Object faultEntity) {
		PlayerAlert ariseAlert = new PlayerAlert();
		Player server = (Player) faultEntity;
		ariseAlert.setAddress(server.getAddress());
		ariseAlert.setLastHeartbeat(server.getLastHeartbeat());
		return ariseAlert;
	}
	
	@Override
	protected String getAlertAriseMessage(Object faultEntity) {
		return null;
	}
	
	@Override
	protected String getAlertRecoverMessage(Object recoveredEntity, Date alertAriseTime) {
		return null;
	}
	
	@Override
	protected Object getEntityFromAlert(AlertBaseEntity alert) {
		return playerManager.findByAddress(((PlayerAlert) alert).getAddress());
	}
}
