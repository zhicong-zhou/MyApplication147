package com.jzkl.Bean;

/**
 * 描述：推广标题</br>
 */
public class IncomeName {
	
	String incomeName = "";
	String incomeRenz = "";

	public String getIncomeName() {
		return incomeName;
	}

	public void setIncomeName(String incomeName) {
		this.incomeName = incomeName;
	}

	public String getIncomeRenz() {
		return incomeRenz;
	}

	public void setIncomeRenz(String incomeRenz) {
		this.incomeRenz = incomeRenz;
	}

	@Override
	public String toString() {
		return "IncomeName{" +
				"incomeName='" + incomeName + '\'' +
				", incomeRenz='" + incomeRenz + '\'' +
				'}';
	}
}
