package com.winnovature.txn.api;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.winnovature.utils.DatabaseManager;

public class ChargebackScheduler implements Runnable {
	static Logger log = Logger.getLogger(ChargebackScheduler.class.getName());

	public void run() {
		String current_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		log.info("ChargebackScheduler starts on :: " + current_date);
		log.info("<<<<<ChargebackScheduler Schedular>>>>>");
		// chargeBackChecker();
		chargeBackCheckerNew();
	}

	private static void chargeBackCheckerNew() {
		Connection conn = null;
		CallableStatement cs = null;
		log.info("EXECUTING pr_auto_raise_dispute()");
		try {
			conn = DatabaseManager.getConnection();
			conn.setAutoCommit(false);
			cs = conn.prepareCall("{call pr_auto_raise_dispute(?)}");
			cs.execute();
			cs.registerOutParameter(1, Types.VARCHAR);
			conn.commit();
			if (cs.getString(1).equals("1")) {
				log.info("AUTO DSIPUTE(CHARGEBACK) RAISED SUCCESSFULLY.");
			}
			else {
				log.info("CHARGEBACK REOCORDS NOT FOUND.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DatabaseManager.closeCallableStatement(cs);
			DatabaseManager.closeConnection(conn);

		}
	}

}
