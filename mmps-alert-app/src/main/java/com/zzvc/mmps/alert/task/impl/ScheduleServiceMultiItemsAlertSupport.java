package com.zzvc.mmps.alert.task.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.zzvc.mmps.alert.model.AlertBaseEntity;
import com.zzvc.mmps.dao.UniversalDao;

public abstract class ScheduleServiceMultiItemsAlertSupport extends ScheduleServiceAlertSupport {
	
	@Resource
	protected UniversalDao universalDao;
	
	abstract protected List<AlertBaseEntity> getEfectiveSavedAlerts();
	abstract protected String getAlertKeyString(AlertBaseEntity alert);
	abstract protected Comparable getAlertComparableProperty(AlertBaseEntity alert);
	abstract protected List<Object> getEfectiveLiveFaultEntities();
	abstract protected String getFaultEntityKeyString(Object faultEntity);
	abstract protected AlertBaseEntity convertFaultEntityToAlert(Object faultEntity);
	abstract protected String getAlertAriseMessage(Object faultEntity);
	abstract protected String getAlertRecoverMessage(Object recoveredEntity, Date alertAriseTime);
	abstract protected Object getEntityFromAlert(AlertBaseEntity alert);

	@Override
	public void onSchedule() {
		Map<String, AlertBaseEntity> savedAlerts = getSavedAlerts();
		Map<String, AlertBaseEntity> liveAlerts = getLiveAlerts(savedAlerts);
		
		if (savedAlerts.keySet().equals(liveAlerts.keySet())) {
			return;
		}
		
		saveArisedAlerts(liveAlerts, savedAlerts);
		saveRecoveredAlerts(liveAlerts, savedAlerts);
	}
	
	private Map<String, AlertBaseEntity> getSavedAlerts() {
		Map<String, AlertBaseEntity> savedAlerts = new HashMap<String, AlertBaseEntity>();
		for (AlertBaseEntity alert : getEfectiveSavedAlerts()) {
			String keyString = getAlertKeyString(alert);
			if (savedAlerts.containsKey(keyString)) {
				savedAlerts.put(keyString, handleDuplicatedAlerts(alert, savedAlerts.get(keyString)));
			} else {
				savedAlerts.put(keyString, alert);
			}
		}
		return savedAlerts;
	}
	
	private AlertBaseEntity handleDuplicatedAlerts(AlertBaseEntity oneAlert, AlertBaseEntity anotherAlert) {
		AlertBaseEntity newerAlert = null;
		AlertBaseEntity earlerAlert = null;
		if (compare(getAlertComparableProperty(oneAlert), getAlertComparableProperty(anotherAlert)) > 0) {
			newerAlert = oneAlert;
			earlerAlert = anotherAlert;
		} else {
			newerAlert = anotherAlert;
			earlerAlert = oneAlert;
		}
		recoverAlert(earlerAlert);
		universalDao.save(earlerAlert);
		return newerAlert;
	}
	
	private int compare(Comparable one, Comparable another) {
		if (another == null) {
			return 1;
		}
		return one.compareTo(another);
	}
	
	private Map<String, AlertBaseEntity> getLiveAlerts(Map<String, AlertBaseEntity> savedAlerts) {
		Map<String, AlertBaseEntity> newAlerts = new HashMap<String, AlertBaseEntity>();
		for (Object faultEntity : getEfectiveLiveFaultEntities()) {
			String keyString = getFaultEntityKeyString(faultEntity);
			if (savedAlerts.containsKey(keyString)) {
				newAlerts.put(keyString, savedAlerts.get(keyString));
			} else {
				newAlerts.put(keyString, convertFaultEntityToAlert(faultEntity));
			}
		}
		return newAlerts;
	}
	
	private void saveArisedAlerts(Map<String, AlertBaseEntity> liveAlerts, Map<String, AlertBaseEntity> savedAlerts) {
		Set<String> keyStringsForArisedAlerts = new HashSet<String>(liveAlerts.keySet());
		keyStringsForArisedAlerts.removeAll(savedAlerts.keySet());
		for (String keyString : keyStringsForArisedAlerts) {
			AlertBaseEntity alert = liveAlerts.get(keyString);
			activeAlert(alert);
			universalDao.save(alert);
			
			Object faultEntity = getEntityFromAlert(alert);
			if (faultEntity != null) {
				String alertAriseMessage = getAlertAriseMessage(faultEntity);
				if (alertAriseMessage != null) {
					info(alertAriseMessage);
				}
			}
		}
	}
	
	private void saveRecoveredAlerts(Map<String, AlertBaseEntity> liveAlerts, Map<String, AlertBaseEntity> savedAlerts) {
		Set<String> keyStringsForRecoveredAlerts = new HashSet<String>(savedAlerts.keySet());
		keyStringsForRecoveredAlerts.removeAll(liveAlerts.keySet());
		for (String keyString : keyStringsForRecoveredAlerts) {
			AlertBaseEntity alert = savedAlerts.get(keyString);
			recoverAlert(alert);
			universalDao.save(alert);
			
			Object recoveredEntity = getEntityFromAlert(alert);
			if (recoveredEntity != null) {
				String alertRecoverMessage = getAlertRecoverMessage(recoveredEntity, alert.getAriseTime());
				if (alertRecoverMessage != null) {
					info(alertRecoverMessage);
				}
			}
		}
	}
	
}
