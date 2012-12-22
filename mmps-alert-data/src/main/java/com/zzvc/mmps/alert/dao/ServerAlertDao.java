package com.zzvc.mmps.alert.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.zzvc.mmps.alert.model.ServerAlert;

public interface ServerAlertDao extends GenericDao<ServerAlert, Long> {
	List<ServerAlert> findActiveAlert();
}
