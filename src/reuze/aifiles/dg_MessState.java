package reuze.aifiles;

//C++ TO JAVA CONVERTER NOTE: Java has no need of forward class declarations:
//class Control;
import reuze.aifiles.dg_MessagePump;

public class dg_MessState
{
	//constructor/functions
	public dg_MessState(int type)
	{
		this(type, null);
	}
	public dg_MessState()
	{
		this(MFSM_STATE_NONE, null);
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: MessState(int type = MFSM_STATE_NONE,Control* parent = null)
	public dg_MessState(int type, RefObject<Control> parent)
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

	//data
	public dg_Control m_parent;
	public int m_type;
	public dg_MessageReceiver m_messReceiver;
}