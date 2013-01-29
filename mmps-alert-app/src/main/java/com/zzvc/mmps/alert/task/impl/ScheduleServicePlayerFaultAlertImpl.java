package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.PlayerAlertDao;
import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.PlayerAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.dao.PlayerDao;
import com.zzvc.mmps.model.Player;

public class ScheduleServicePlayerFaultAlertImpl extends ScheduleServiceMultiItemsAlertSupport {
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private PlayerDao playerDao;
	
	@Resource
	private PlayerAlertDao playerAlertDao;
	
	private int minutesBeforePlayerFault;
	
	@Override
	public void init() {
		try {
			minutesBeforePlayerFault = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_MINUTES_BEFORE_PLAYER_FAULT));
		} catch (Exception e) {
			minutesBeforePlayerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_SERVER_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_PLAYER_FAULT", minutesBeforePlayerFault);
	}
	
	@Override
	protected List getEfectiveSavedAlerts() {
		return playerAlertDao.findActiveAlert();
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
		return playerDao.findByHeartbeatBefore(new Date(System.currentTimeMillis() - minutesBeforePlayerFault * MILLISECONDS_OF_MINUTE));
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
		return playerDao.findByAddress(((PlayerAlert) alert).getAddress());
	}
}
