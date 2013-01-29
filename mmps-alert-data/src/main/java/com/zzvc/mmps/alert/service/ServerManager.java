package com.zzvc.mmps.alert.service;

import java.util.List;

import org.appfuse.service.GenericManager;

import com.zzvc.mmps.model.Server;

public interface ServerManager extends GenericManager<Server, Long> {
	List<Server> findByCode(String code);
	List<Server> findByFaultLasting(int minutes);
}
