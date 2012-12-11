package reuze.aifiles;

import reuze.aifiles.dg_Message.DataMessage;

public class dg_ChangeStateCallback extends dg_Callback{

	//---------------------------------------------------------
	private void function(Object parent, RefObject<dg_Message> msg)
	{
		int newState = ((DataMessage<Integer>)msg.argvalue).m_dataStorage;
		((dg_MessMachine)parent).SetGoalID(newState);
	}
}