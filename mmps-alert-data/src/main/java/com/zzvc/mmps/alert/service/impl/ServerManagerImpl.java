package com.zzvc.mmps.alert.service.impl;

import java.util.Date;
import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzvc.mmps.alert.service.ServerManager;
import com.zzvc.mmps.dao.ServerDao;
import com.zzvc.mmps.model.Server;

@Service("serverManager")
public class ServerManagerImpl extends GenericManagerImpl<Server, Long> implements ServerManager {
	private static int MILLISECONDS_OF_MINUTE = 60 * 1000;

	private ServerDao serverDao;
	
	@Autowired
	public ServerManagerImpl(ServerDao playerDao) {
		super(playerDao);
		this.serverDao = playerDao;
	}

	@Override
	public List<Server> findByCode(String code) {
		return serverDao.findByCode(code);
	}

	@Override
	public List<Server> findByFaultLasting(int minutes) {
		return serverDao.findByHeartbeatBefore(new Date(System.currentTimeMillis() - minutes * MILLISECONDS_OF_MINUTE));
	}
}
