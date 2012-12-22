package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.PlayerLastingFaultAlertDao;
import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.PlayerLastingFaultAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.dao.PlayerDao;
import com.zzvc.mmps.model.Player;

public class ScheduleServicePlayerLastingFaultAlertImpl extends ScheduleServiceMultiItemsAlertSupport {
	
	@Resource
	private PlayerDao playerDao;
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private PlayerLastingFaultAlertDao playerLastingFaultAlertDao;
	
	private int minutesBeforePlayerLastingFault;
	
	@Override
	public void init() {
		try {
			minutesBeforePlayerLastingFault = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_MINUTES_BEFORE_PLAYER_LASTING_FAULT));
		} catch (Exception e) {
			minutesBeforePlayerLastingFault = AlertConstants.DEFAULT_MINUTES_BEFORE_PLAYER_LASTING_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_PLAYER_LASTING_FAULT", minutesBeforePlayerLastingFault);
	}

	@Override
	protected List getEfectiveSavedAlerts() {
		return playerLastingFaultAlertDao.findActiveAlert();
	}

	@Override
	protected String getAlertKeyString(AlertBaseEntity alert) {
		return ((PlayerLastingFaultAlert) alert).getAddress();
	}

	@Override
	protected Comparable getAlertComparableProperty(AlertBaseEntity alert) {
		return ((PlayerLastingFaultAlert) alert).getLastHeartbeat();
	}

	@Override
	protected List getEfectiveLiveFaultEntities() {
		return playerDao.findByHeartbeatBefore(new Date(System.currentTimeMillis() - minutesBeforePlayerLastingFault * MILLISECONDS_OF_MINUTE));
	}

	@Override
	protected String getFaultEntityKeyString(Object faultEntity) {
		return ((Player) faultEntity).getAddress();
	}

	@Override
	protected AlertBaseEntity convertFaultEntityToAlert(Object faultEntity) {
		PlayerLastingFaultAlert alert = new PlayerLastingFaultAlert();
		Player player = (Player) faultEntity;
		alert.setAddress(player.getAddress());
		alert.setLastHeartbeat(player.getLastHeartbeat());
		return alert;
	}

	@Override
	protected String getAlertAriseMessage(Object faultEntity) {
		Player player = (Player) faultEntity;
		return findTextFilterNullArgs("alert.arising.player.lasting.fault", player.getName(), player.getAddress(), minutesBeforePlayerLastingFault / 60, player.getLastHeartbeat());
	}

	@Override
	protected String getAlertRecoverMessage(Object recoveredEntity, Date alertAriseTime) {
		Player player = (Player) recoveredEntity;
		return findTextFilterNullArgs("alert.recover.player.lasting.fault", player.getName(), player.getAddress(), alertAriseTime);
	}

	@Override
	protected Object getEntityFromAlert(AlertBaseEntity alert) {
		return playerDao.findByAddress(((PlayerLastingFaultAlert) alert).getAddress());
	}

}
