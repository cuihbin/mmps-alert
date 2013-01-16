package com.zzvc.mmps.alert.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.zzvc.mmps.alert.dao.PlayerMassiveFaultAlertDao;
import com.zzvc.mmps.alert.model.PlayerMassiveFaultAlert;

@Repository("playerMassiveFaultAlertDao")
public class PlayerMassiveFaultAlertDaoHibernate extends GenericDaoHibernate<PlayerMassiveFaultAlert, Long>
		implements PlayerMassiveFaultAlertDao {

	public PlayerMassiveFaultAlertDaoHibernate() {
		super(PlayerMassiveFaultAlert.class);
	}
	
	public PlayerMassiveFaultAlert findActiveAlert() {
		List<PlayerMassiveFaultAlert> activeAlerts = getSession().createCriteria(PlayerMassiveFaultAlert.class).add(Restrictions.eq("active", true)).addOrder(Order.desc("ariseTime")).list();
		if (activeAlerts.size() > 1) {
			for (int i = 1; i < activeAlerts.size(); i++) {
				PlayerMassiveFaultAlert expiredAlert = activeAlerts.get(i);
				expiredAlert.setActive(false);
				save(expiredAlert);
			}
		}
		if (activeAlerts.size() >= 1) {
			return activeAlerts.get(0);
		} else {
			return null;
		}
	}

}
