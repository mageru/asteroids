package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.States;
import reuze.aifiles.messaging.DataMessage;

public class dg_ChangeStateCallback extends dg_Callback{

	//---------------------------------------------------------
	private void function(Object parent, dg_Message msg)
	{
		reuze.aifiles.messaging.dg_MessState.States newState = ((DataMessage<States>)msg).m_dataStorage;
		((dg_MessMachine)parent).SetGoalID(newState);
	}
}