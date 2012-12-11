package reuze.aifiles;

public class dg_ChangeStateCallback  extends dg_Callback{

	//---------------------------------------------------------
	private void function(Object parent, RefObject<Message> msg)
	{
		int newState = ((DataMessage<Integer>*)msg.argvalue).m_dataStorage;
		((MessMachine)parent).SetGoalID(newState);
	}
}

public class dg_MessMachine extends MessState
{
	//constructor/functions

	//---------------------------------------------------------
	public dg_MessMachine(int type)
	{
		this(type, null);
	}
	public dg_MessMachine()
	{
		this(MFSM_MACH_NONE, null);
	}
//C++ TO JAVA CONVERTER NOTE: Java does not allow default values for parameters. Overloaded methods are inserted above.
//ORIGINAL LINE: MessMachine(int type = MFSM_MACH_NONE, Control* parent = null): MessState(type,parent)
	public dg_MessMachine(int type, RefObject<Control> parent)
	{
		super(type,parent.argvalue);
		m_currentState = null;
		m_defaultState = null;
		m_goalState = null;
	
		MessagePump.Instance().RegisterForMessage(AnonymousEnum.MESSAGE_CHANGE_STATE,this,GetMessageID(),m_changeStateCallback);
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
	public void AddState(RefObject<MessState> state)
	{
		m_states.push_back(state.argvalue);
	}
	public void SetDefaultState(RefObject<MessState> state)
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
			if(m_states[i].m_type == goal)
			{
				m_goalState = m_states[i];
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
			m_states[i].Init();
	
		//and now enter the m_defaultState, if any
		if (m_currentState != null)
			m_currentState.Enter();
	}

	//data
	public int m_type;
	protected std.vector<MessState*> m_states;
	protected MessState m_currentState;
	protected MessState m_defaultState;
	protected MessState m_goalState;
	protected int m_goalID;
	protected ChangeStateCallback m_changeStateCallback = new ChangeStateCallback();
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