package com.winnovature.dto;

public class ReconDates {
	
	private String cycledate;
    private String cycle;

    public ReconDates(String cycledate, String cycle)
    {
        this.cycledate = cycledate;
        this.cycle = cycle;
    }
    
    /*public ReconDates(String reconDate, String reconCycle)
    {
    	Txns txn = new Txns();
        txn.setCycledate(reconDate) = reconDate;
        this.reconCycle = reconCycle;
    }*/
    
    public String getCycle() {
		return cycle;
	}
	public void setCycle(String cycle) {
		this.cycle = cycle;
	}
	public String getCycledate() {
		return cycledate;
	}
	public void setCycledate(String cycledate) {
		this.cycledate = cycledate;
	}

}
