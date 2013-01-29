package com.zzvc.mmps.alert.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.zzvc.mmps.alert.model.PlayerAlert;

public interface PlayerAlertDao extends GenericDao<PlayerAlert, Long> {
	List<PlayerAlert> findActiveAlert();
}
