package com.zzvc.mmps.alert.dao.hibernate;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zzvc.mmps.alert.dao.AlertConfigDao;
import com.zzvc.mmps.alert.model.AlertConfig;

@Repository("alertConfigDao")
public class AlertConfigDaoHibernate extends GenericDaoHibernate<AlertConfig, Long> implements AlertConfigDao {
	
	public AlertConfigDaoHibernate() {
		super(AlertConfig.class);
	}
	
	public AlertConfig findAlertConfig(String configKey) {
		return (AlertConfig) getSession().createCriteria(AlertConfig.class).add(Restrictions.eq("configKey", configKey)).uniqueResult();
	}
	

}
