package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.MSGStates;

public class dg_Message {
	
	int m_typeID;
	int m_fromID;
	int m_toID;
	float m_timer;
	boolean m_delivered;
	
	
	public dg_Message() {
		this(MSGStates.MESSAGE_DEFAULT.ordinal());
	}
	
	public dg_Message(int i) {
		this.setM_typeID(i);
		this.setM_delivered(false);
		this.setM_timer(0.0f);
	}

	
	
	/**
	 * @return the m_typeID
	 */
	public int getM_typeID() {
		return m_typeID;
	}


	/**
	 * @param i the m_typeID to set
	 */
	public void setM_typeID(int i) {
		this.m_typeID = i;
	}


	/**
	 * @return the m_fromID
	 */
	public int getM_fromID() {
		return m_fromID;
	}


	/**
	 * @param m_fromID the m_fromID to set
	 */
	public void setM_fromID(int m_fromID) {
		this.m_fromID = m_fromID;
	}


	/**
	 * @return the m_toID
	 */
	public int getM_toID() {
		return m_toID;
	}


	/**
	 * @param m_toID the m_toID to set
	 */
	public void setM_toID(int m_toID) {
		this.m_toID = m_toID;
	}


	/**
	 * @return the m_timer
	 */
	public float getM_timer() {
		return m_timer;
	}


	/**
	 * @param m_timer the m_timer to set
	 */
	public void setM_timer(float m_timer) {
		this.m_timer = m_timer;
	}


	/**
	 * @return the m_delivered
	 */
	public boolean isM_delivered() {
		return m_delivered;
	}


	/**
	 * @param m_delivered the m_delivered to set
	 */
	public void setM_delivered(boolean m_delivered) {
		this.m_delivered = m_delivered;
	}
	public static String GetMessageName(int type)
	{
		switch(type)
		{
			case 0:
				return "Will Collide";
			case 1:
				return "Wont Collide";
			case 2:
				return "No Asteroids";
			case 3:
				return "No Powerups";
			case 4:
				return "Asteroid Far";
			case 5:
				return "Asteroid Near";
			case 6:
				return "Powerup Near";
			case 7:
				return "Powerup Far";
			case 8:
				return "Change State";
			case 9:
				return "Ship Total Stop";
			default:
				return "Error Message";
		}
	}


	@Override
	public String toString() {
		return "dg_Message [m_typeID=" + m_typeID + ",m_Name=" + GetMessageName(m_typeID) + ", m_fromID=" + m_fromID
				+ ", m_toID=" + m_toID + ", m_timer=" + m_timer
				+ ", m_delivered=" + m_delivered + "]";
	}
}

