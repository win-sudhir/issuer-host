package com.winnovature.dto;

import java.util.List;

public class DashboardDTO {

	public List<DayWise> dayWise;
	public List<MonthWise> monthWise;

	public List<DayWise> getDayWise() {
		return dayWise;
	}

	public void setDayWise(List<DayWise> dayWise) {
		this.dayWise = dayWise;
	}

	public List<MonthWise> getMonthWise() {
		return monthWise;
	}

	public void setMonthWise(List<MonthWise> monthWise) {
		this.monthWise = monthWise;
	}

	public static class DayWise {
		public String date;
		public String txnCount;
		public String txnAmount;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getTxnCount() {
			return txnCount;
		}

		public void setTxnCount(String txnCount) {
			this.txnCount = txnCount;
		}

		public String getTxnAmount() {
			return txnAmount;
		}

		public void setTxnAmount(String txnAmount) {
			this.txnAmount = txnAmount;
		}

		@Override
		public String toString() {
			return "DayWise [date=" + date + ", txnCount=" + txnCount + ", txnAmount=" + txnAmount + "]";
		}

	}

	public static class MonthWise {
		public String month;
		public String txnCount;
		public String txnAmount;

		public String getMonth() {
			return month;
		}

		public void setMonth(String month) {
			this.month = month;
		}

		public String getTxnCount() {
			return txnCount;
		}

		public void setTxnCount(String txnCount) {
			this.txnCount = txnCount;
		}

		public String getTxnAmount() {
			return txnAmount;
		}

		public void setTxnAmount(String txnAmount) {
			this.txnAmount = txnAmount;
		}

		@Override
		public String toString() {
			return "MonthWise [month=" + month + ", txnCount=" + txnCount + ", txnAmount=" + txnAmount + "]";
		}

	}

	@Override
	public String toString() {
		return "DashboardDTO [dayWise=" + dayWise + ", monthWise=" + monthWise + "]";
	}

}
