package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.dao.ServerAlertDao;
import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.alert.model.ServerAlert;
import com.zzvc.mmps.alert.util.AlertConstants;
import com.zzvc.mmps.dao.ServerDao;
import com.zzvc.mmps.model.Server;

public class ScheduleServiceServerFaultAlertImpl extends ScheduleServiceMultiItemsAlertSupport {
	
	@Resource
	private AlertConfigDao alertConfigDao;
	
	@Resource
	private ServerDao serverDao;
	
	@Resource
	private ServerAlertDao serverAlertDao;
	
	private int minutesBeforeServerFault;
	
	@Override
	public void init() {
		try {
			minutesBeforeServerFault = Integer.parseInt(alertConfigDao.getConfig(AlertConstants.CFG_MINUTES_BEFORE_SERVER_FAULT));
		} catch (Exception e) {
			minutesBeforeServerFault = AlertConstants.DEFAULT_MINUTES_BEFORE_SERVER_FAULT;
		}
		infoMessage("alert.config.parameter", "CFG_MINUTES_BEFORE_SERVER_FAULT", minutesBeforeServerFault);
	}
	
	protected List getEfectiveSavedAlerts() {
		return serverAlertDao.findActiveAlert();
	}
	
	protected String getAlertKeyString(AlertBaseEntity alert) {
		return ((ServerAlert) alert).getCode();
	}
	
	protected Comparable getAlertComparableProperty(AlertBaseEntity alert) {
		return ((ServerAlert) alert).getLastHeartbeat();
	}

	protected List getEfectiveLiveFaultEntities() {
		return serverDao.findByHeartbeatBefore(new Date(System.currentTimeMillis() - minutesBeforeServerFault * MILLISECONDS_OF_MINUTE));
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
