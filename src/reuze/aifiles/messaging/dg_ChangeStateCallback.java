package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.States;
import reuze.aifiles.messaging.DataMessage;

public class dg_ChangeStateCallback extends dg_Callback
{
	public void function(Object parent, dg_Message msg)
	{
		int newState = ((DataMessage<Integer>)msg).m_dataStorage;
		((dg_MessMachine)parent).SetGoalID(newState);
	}
}