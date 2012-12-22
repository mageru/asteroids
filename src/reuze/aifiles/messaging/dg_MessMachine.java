package reuze.aifiles.messaging;

import java.util.ArrayList;

import reuze.aifiles.messaging.dg_MessagePump;
import reuze.aifiles.messaging.dg_MessState.States;
import reuze.aifiles.messaging.dg_MessState.MSGStates;
import reuze.aifiles.messaging.dg_MessState.Types;

public class dg_MessMachine extends dg_MessState
{
	public dg_MessMachine() {
		this(Types.MFSM_MACH_NONE,null);
	}
	public void SetDefaultState(dg_MessState state) {
		m_defaultState = state;
	}
	public void SetGoalID(States goal) { m_goalID = goal;}
	//data
	public Types m_type;
	protected ArrayList<dg_MessState> m_states;
	protected dg_MessState m_currentState;
	protected dg_MessState m_defaultState;
	protected dg_MessState m_goalState;
	protected States m_goalID;
	protected dg_ChangeStateCallback m_changeStateCallback = new dg_ChangeStateCallback();
	//constructor/functions

	public dg_MessMachine(Types type, dg_Control parent)
	{
		super(States.MFSM_STATE_NONE, parent);
		m_type = type;
		m_currentState = null;
		m_defaultState = null;
		m_goalState = null;
		m_states = new ArrayList<dg_MessState>();
		dg_MessagePump.RegisterForMessage(MSGStates.MESSAGE_CHANGE_STATE.ordinal(),this,GetMessageID(),m_changeStateCallback);
	}

	public void Update(float dt)
	{
		//don't do anything if you have no states
		if(m_states.size() == 0 )
			return;

		//don't do anything if there's no current 
		//state, and no default state
		if(m_currentState==null)
			m_currentState = m_defaultState;
		if(m_currentState==null)
			return;

		//check for transitions, and then update
		States oldStateID = m_currentState.m_type;
		m_goalID = m_currentState.CheckTransitions();
		
		//switch if there was a transition
		if(m_goalID != oldStateID)
		{
			if(TransitionState(m_goalID))
			{
				m_currentState.Exit();
				m_currentState = m_goalState;
				m_currentState.Enter();
			}
		}
		m_currentState.Update(dt);	
		
	}

	//---------------------------------------------------------
	void AddState(dg_MessState state)
	{
		m_states.add(state);
	}

	//---------------------------------------------------------
	boolean TransitionState(States goal)
	{
		//don't do anything if you have no states
		if(m_states.size() == 0 )
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
		Exit();
		if(m_currentState!=null)
			m_currentState.Exit();
		m_currentState = m_defaultState;

		//init all the states
		for(int i =0;i<m_states.size();i++)
			m_states.get(i).Init();

	    //and now enter the m_defaultState, if any
	    if(m_currentState!=null)
	        m_currentState.Enter();
	    
	}
	@Override
	void Enter() {
		// TODO Auto-generated method stub			
	}
	@Override
	void Exit() {
		// TODO Auto-generated method stub			
	}
	@Override
	void Init() {
		// TODO Auto-generated method stub			
	}
	//---------------------------------------------------------
	/**
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

**/
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