package com.zzvc.mmps.alert.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zzvc.mmps.alert.dao.ServerAlertDao;
import com.zzvc.mmps.alert.model.ServerAlert;

@Repository("serverAlertDao")
public class ServerAlertDaoHibernate extends GenericDaoHibernate<ServerAlert, Long> implements ServerAlertDao {

	public ServerAlertDaoHibernate() {
		super(ServerAlert.class);
	}

	@Override
	public List<ServerAlert> findActiveAlert() {
		return getSession().createCriteria(ServerAlert.class).add(Restrictions.eq("active", true)).addOrder(Order.asc("lastHeartbeat")).list();
	}

}
