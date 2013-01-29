package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.ServerAlert;
import com.zzvc.mmps.alert.service.AlertConfigManager;
import com.zzvc.mmps.alert.service.ServerAlertManager;
import com.zzvc.mmps.alert.service.ServerManager;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.model.Server;

public class ScheduleServiceServerFaultAlertImpl extends ScheduleServiceMultiItemsAlertSupport {
	
	@Resource
	private AlertConfigManager alertConfigManager;
	
	@Resource
	private ServerManager serverDao;
	
	@Resource
	private ServerAlertManager serverAlertManager;
	
	private int minutesBeforeServerFault;
	
	@Override
	public void init() {
		try {
			minutesBeforeServerFault = Integer.parseInt(alertConfigManager.getConfig(AlertConstants.CFG_MINUTES_BEFORE_SERVER_FAULT));
		} catch (Exception e) {
			minutesBeforeServerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_SERVER_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_SERVER_FAULT", minutesBeforeServerFault);
	}
	
	protected List getEfectiveSavedAlerts() {
		return serverAlertManager.findActiveAlert();
	}
	
	protected String getAlertKeyString(AlertBaseEntity alert) {
		return ((ServerAlert) alert).getCode();
	}
	
	protected Comparable getAlertComparableProperty(AlertBaseEntity alert) {
		return ((ServerAlert) alert).getLastHeartbeat();
	}

	protected List getEfectiveLiveFaultEntities() {
		return serverDao.findByFaultLasting(minutesBeforeServerFault);
	}
	
	protected String getFaultEntityKeyString(Object faultEntity) {
		return ((Server) faultEntity).getCode();
	}
	
	protected AlertBaseEntity convertFaultEntityToAlert(Object faultEntity) {
		ServerAlert ariseAlert = new ServerAlert();
		Server server = (Server) faultEntity;
		ariseAlert.setCode(server.getCode());
		ariseAlert.setLastHeartbeat(server.getLastHeartbeat());
		ariseAlert.setActive(true);
		return ariseAlert;
	}
	
	protected String getAlertAriseMessage(Object faultEntity) {
		Server server = (Server) faultEntity;
		return findTextFilterNullArgs("alert.arising.server.fault", server.getName(), server.getLastHeartbeat());
	}
	
	protected String getAlertRecoverMessage(Object recoveredEntity, Date alertAriseTime) {
		Server entity = (Server) recoveredEntity;
		return findTextFilterNullArgs("alert.recover.server.fault", entity.getName(), alertAriseTime);
	}
	
	protected Object getEntityFromAlert(AlertBaseEntity alert) {
		try {
			return serverDao.findByCode(((ServerAlert) alert).getCode()).get(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}
