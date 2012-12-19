package reuze.aifiles;

import reuze.aifiles.dg_MessState.MSGStates;
import reuze.aifiles.dg_MessState.States;

public class dg_Message {
	
	MSGStates m_typeID;
	public int m_fromID;
	private int m_toID;
	public float m_timer;
	private boolean m_delivered;
	
	
	public dg_Message() {
		this.setM_typeID(null);
	}
	
	public dg_Message(MSGStates messageChangeState) {
		this.setM_typeID(messageChangeState);
		this.setM_delivered(false);
		this.setM_timer(0.0f);
	}

	public class DataMessage<T> extends dg_Message
	{
		public DataMessage(MSGStates messageChangeState, States mfsmStateEvade)
		{
			super(messageChangeState);
			m_dataStorage = mfsmStateEvade;
		}
		public void Dispose()
		{
		}

		//data member
		public States m_dataStorage;
	}
	
	/**
	 * @return the m_typeID
	 */
	public MSGStates getM_typeID() {
		return m_typeID;
	}


	/**
	 * @param messageChangeState the m_typeID to set
	 */
	public void setM_typeID(MSGStates messageChangeState) {
		this.m_typeID = messageChangeState;
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
}

