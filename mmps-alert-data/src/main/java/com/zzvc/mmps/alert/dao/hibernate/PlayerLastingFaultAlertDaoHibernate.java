package com.zzvc.mmps.alert.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zzvc.mmps.alert.dao.PlayerLastingFaultAlertDao;
import com.zzvc.mmps.alert.model.PlayerLastingFaultAlert;

@Repository("playerLastingFaultAlertDao")
public class PlayerLastingFaultAlertDaoHibernate extends GenericDaoHibernate<PlayerLastingFaultAlert, Long> implements PlayerLastingFaultAlertDao {
	
	public PlayerLastingFaultAlertDaoHibernate() {
		super(PlayerLastingFaultAlert.class);
	}
	
	public List<PlayerLastingFaultAlert> findActiveAlert() {
		return getSession().createCriteria(PlayerLastingFaultAlert.class).add(Restrictions.eq("active", true)).addOrder(Order.asc("lastHeartbeat")).list();
	}

}
