package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessagePump;

public abstract class dg_MessState
{
	//data
	dg_Control   m_parent;
	States        m_type;
	
	public static enum States
	{
		MFSM_STATE_NONE,
		MFSM_STATE_APPROACH,
		MFSM_STATE_ATTACK,
		MFSM_STATE_EVADE,
		MFSM_STATE_GETPOWERUP,
		MFSM_STATE_IDLE,
	    MFSM_STATE_COUNT,
		MFSM_MACH_NONE,
		MFSM_MACH_MAINSHIP,
	    MFSM_MACH_COUNT;
	}
	public static enum Types
	{
		MFSM_MACH_NONE,
		MFSM_MACH_MAINSHIP,
	    MFSM_MACH_COUNT
	};
	
	public static enum REGState
	{
		REGISTER_ERROR_ALREADY_REGISTERED,
		REGISTER_ERROR_MESSAGE_NOT_IN_SYSTEM,
		REGISTER_MESSAGE_OK;
	}
	
	public static enum MSGStates 
	{
		MESSAGE_DEFAULT,
		MESSAGE_WILL_COLLIDE,
		MESSAGE_WONT_COLLIDE,
		MESSAGE_NO_ASTEROIDS,
		MESSAGE_NO_POWERUPS,
		MESSAGE_ASTEROID_FAR,
		MESSAGE_ASTEROID_NEAR,
		MESSAGE_POWERUP_NEAR,
		MESSAGE_POWERUP_FAR,
		MESSAGE_CHANGE_STATE,
		MESSAGE_SHIP_TOTAL_STOP,
		MESSAGE_TOKEN_PSCAN,
		MESSAGE_TOKEN_MAXSPEED,
		MESSAGE_TOKEN_APDIST,
		MESSAGE_TOKEN_ATDIST,
		MESSAGE_TOKEN_SAFERAD,
		MESSAGE_TOKEN_POWSEEK,
		MESSAGE_COUNT
	};
	
	public int type;
	public dg_MessagePump.MessageReciever m_messReceiver;
	
	//constructor/functions
	public dg_MessState()
	{
		this(States.MFSM_STATE_NONE, null);
	}
	
	public dg_MessState(States type, dg_Control parent)
		{
		m_type = type;m_parent = parent;
		}
	abstract void Enter();
	abstract void Exit();	
	abstract void Update(float dt);
	abstract void Init();
	States CheckTransitions() {return States.MFSM_STATE_NONE;}
	
	public int GetMessageID()
	{
		return m_messReceiver.m_ID;
	}


}