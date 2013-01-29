package com.zzvc.mmps.alert.service.impl;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzvc.mmps.alert.dao.PlayerLastingFaultAlertDao;
import com.zzvc.mmps.alert.model.PlayerLastingFaultAlert;
import com.zzvc.mmps.alert.service.PlayerLastingFaultAlertManager;

@Service("PlayerLastingFaultAlertManager")
public class PlayerLastingFaultAlertManagerImpl extends GenericManagerImpl<PlayerLastingFaultAlert, Long> implements PlayerLastingFaultAlertManager {
	
	private PlayerLastingFaultAlertDao playerLastingFaultAlertDao;

	@Autowired
	public PlayerLastingFaultAlertManagerImpl(PlayerLastingFaultAlertDao playerAlertDao) {
		super(playerAlertDao);
		this.playerLastingFaultAlertDao = playerAlertDao;
	}

	@Override
	public List<PlayerLastingFaultAlert> findActiveAlert() {
		return playerLastingFaultAlertDao.findActiveAlert();
	}

}
