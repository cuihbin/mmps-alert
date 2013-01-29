package com.zzvc.mmps.alert.dao;

import org.appfuse.dao.GenericDao;

import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;

public interface PlayerMassiveFaultAlertDao extends GenericDao<PlayerMassiveFaultAlert, Long> {
	PlayerMassiveFaultAlert findActiveAlert();
}
