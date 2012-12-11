package reuze.aifiles;

import reuze.aifiles.dg_MessagePump;
import reuze.aifiles.dg_Messages.MSGState;

public abstract class dg_MessState
{
	//data
	public dg_Control m_parent;
	public int m_type;
	public int type;
	public dg_MessagePump.MessageReciever m_messReceiver;
	
	//constructor/functions
	public dg_MessState(int type)
	{
		this.type = MSGState.MFSM_STATE_NONE.getCode();
		this.m_parent = null;
	}
	
	public dg_MessState(int type, RefObject<dg_Control> parent)
		{
			m_type = type;
			m_parent =parent.argvalue;
		}
	public void Enter()
	{
	}
	public void exit()
	{
	}
	public void Update(float dt)
	{
	}
	public void Init()
	{
	}
	public int GetMessageID()
	{
		return m_messReceiver.m_ID;
	}


}