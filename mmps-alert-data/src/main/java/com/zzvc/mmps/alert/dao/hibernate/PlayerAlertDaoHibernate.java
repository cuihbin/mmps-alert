package com.zzvc.mmps.alert.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zzvc.mmps.alert.dao.PlayerAlertDao;
import com.zzvc.mmps.alert.model.PlayerAlert;

@Repository("playerAlertDao")
public class PlayerAlertDaoHibernate extends GenericDaoHibernate<PlayerAlert, Long> implements PlayerAlertDao {
	
	public PlayerAlertDaoHibernate() {
		super(PlayerAlert.class);
	}

	@Override
	public List<PlayerAlert> findActiveAlert() {
		return getSession().createCriteria(PlayerAlert.class).add(Restrictions.eq("active", true)).addOrder(Order.asc("lastHeartbeat")).list();
	}

}
