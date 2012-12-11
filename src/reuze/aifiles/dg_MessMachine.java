package reuze.aifiles;

import java.util.ArrayList;

import reuze.aifiles.dg_MessagePump;
import reuze.aifiles.dg_Messages.MSGState;
import reuze.aifiles.dg_Messages.Types;

public class dg_MessMachine extends dg_MessState
{
	//data
	public int m_type;
	public int type;
	protected ArrayList<dg_MessState> m_states;
	protected dg_MessState m_currentState;
	protected dg_MessState m_defaultState;
	protected dg_MessState m_goalState;
	protected int m_goalID;
	protected dg_ChangeStateCallback m_changeStateCallback = new dg_ChangeStateCallback();
	//constructor/functions

	//---------------------------------------------------------
/**
	public dg_MessMachine()
	{
		this.type = MSGState.MFSM_STATE_NONE.getCode();
		this.m_parent = null;
	}
	public dg_MessMachine(int type)
	{
		
		this.m_type = type;
		this.m_parent = null;
	}
**/
	public dg_MessMachine(int type, RefObject<dg_Control> parent)
	{
		super(type,parent);
		m_currentState = null;
		m_defaultState = null;
		m_goalState = null;
	
		dg_MessagePump.RegisterForMessage(Types.MESSAGE_CHANGE_STATE.getCode(),this,GetMessageID(),m_changeStateCallback);
	}

	//---------------------------------------------------------
	public void UpdateMachine(float dt)
	{
		//don't do anything if you have no states
		if(m_states.size() == 0)
			return;
	
		//don't do anything if there's no current 
		//state, and no default state
		if (m_currentState == null)
			m_currentState = m_defaultState;
		if (m_currentState == null)
			return;
	
		//check for transitions, and then update
		int oldStateID = m_currentState.m_type;
	
		//switch if there was a transition
		if(m_goalID != oldStateID)
		{
			if(TransitionState(m_goalID))
			{
				m_currentState.exit();
				m_currentState = m_goalState;
				m_currentState.Enter();
			}
		}
		m_currentState.Update(dt);
	
	}

	//---------------------------------------------------------
	public void AddState(RefObject<dg_MessState> state)
	{
		m_states.add(state.argvalue);
	}
	public void SetDefaultState(RefObject<dg_MessState> state)
	{
		m_defaultState = state.argvalue;
	}
	public void SetGoalID(int goal)
	{
		m_goalID = goal;
	}

	//---------------------------------------------------------
	public boolean TransitionState(int goal)
	{
		//don't do anything if you have no states
		if(m_states.size() == 0)
			return false;
	
		//determine if we have state of type 'goal'
		//in the list, and switch to it, otherwise, quit out
		for(int i =0;i<m_states.size();i++)
		{
			if(m_states.get(i).m_type == goal)
			{
				m_goalState = m_states.get(i);
				return true;
			}
		}
		return false;
	}

	//---------------------------------------------------------
	public void Reset()
	{
		exit();
		if (m_currentState != null)
			m_currentState.exit();
		m_currentState = m_defaultState;
	
		//init all the states
		for(int i =0;i<m_states.size();i++)
			m_states.get(i).Init();
	
		//and now enter the m_defaultState, if any
		if (m_currentState != null)
			m_currentState.Enter();
	}


}
//----------------------------------------------------------------------------------------
//	Copyright © 2006 - 2008 Tangible Software Solutions Inc.
//
//	This class is used to simulate the ability to pass arguments by reference in Java.
//----------------------------------------------------------------------------------------
final class RefObject<T>
{
	T argvalue;
	RefObject(T refarg)
	{
		argvalue = refarg;
	}
}