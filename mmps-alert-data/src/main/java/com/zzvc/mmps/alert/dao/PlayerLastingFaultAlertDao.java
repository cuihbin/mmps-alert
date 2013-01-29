package com.zzvc.mmps.alert.dao;

import java.util.List;

import org.appfuse.dao.GenericDao;

import com.zzvc.mmps.alert.model.PlayerLastingFaultAlert;

public interface PlayerLastingFaultAlertDao extends GenericDao<PlayerLastingFaultAlert, Long> {
	List<PlayerLastingFaultAlert> findActiveAlert();
}
