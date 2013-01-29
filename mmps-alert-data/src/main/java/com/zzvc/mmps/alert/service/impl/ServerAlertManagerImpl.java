package com.zzvc.mmps.alert.service.impl;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzvc.mmps.alert.dao.ServerAlertDao;
import com.zzvc.mmps.alert.model.ServerAlert;
import com.zzvc.mmps.alert.service.ServerAlertManager;

@Service("ServerAlertManager")
public class ServerAlertManagerImpl extends GenericManagerImpl<ServerAlert, Long> implements ServerAlertManager {
	
	private ServerAlertDao serverAlertDao;

	@Autowired
	public ServerAlertManagerImpl(ServerAlertDao playerAlertDao) {
		super(playerAlertDao);
		this.serverAlertDao = playerAlertDao;
	}

	@Override
	public List<ServerAlert> findActiveAlert() {
		return serverAlertDao.findActiveAlert();
	}

}
