package com.xinguang.tubobo.api.rider.dto;

import java.io.Serializable;

public class RiderAuthDTO implements Serializable{

	private boolean rider;
	private boolean depositEnough;
	private String riderId;
	private String riderName;
	private String riderPhone;

	public boolean isRider() {
		return rider;
	}

	public void setRider(boolean rider) {
		this.rider = rider;
	}

	public boolean isDepositEnough() {
		return depositEnough;
	}

	public void setDepositEnough(boolean depositEnough) {
		this.depositEnough = depositEnough;
	}

	public String getRiderId() {
		return riderId;
	}

	public void setRiderId(String riderId) {
		this.riderId = riderId;
	}

	public String getRiderName() {
		return riderName;
	}

	public void setRiderName(String riderName) {
		this.riderName = riderName;
	}

	public String getRiderPhone() {
		return riderPhone;
	}

	public void setRiderPhone(String riderPhone) {
		this.riderPhone = riderPhone;
	}
}