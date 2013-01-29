package com.zzvc.mmps.alert.service.impl;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzvc.mmps.alert.dao.PlayerMassiveFaultAlertDao;
import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;
import com.zzvc.mmps.alert.service.PlayerMassiveFaultAlertManager;

@Service("PlayerMassiveFaultAlertManager")
public class PlayerMassiveFaultAlertManagerImpl extends GenericManagerImpl<PlayerMassiveFaultAlert, Long> implements PlayerMassiveFaultAlertManager {
	
	private PlayerMassiveFaultAlertDao playerMassiveFaultAlertDao;

	@Autowired
	public PlayerMassiveFaultAlertManagerImpl(PlayerMassiveFaultAlertDao playerAlertDao) {
		super(playerAlertDao);
		this.playerMassiveFaultAlertDao = playerAlertDao;
	}

	@Override
	public PlayerMassiveFaultAlert findActiveAlert() {
		return playerMassiveFaultAlertDao.findActiveAlert();
	}

}
