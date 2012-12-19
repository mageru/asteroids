package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.States;
import reuze.aifiles.messaging.dg_MessState.MSGStates;

public class DataMessage<T> extends dg_Message
{
	public DataMessage(MSGStates type, reuze.aifiles.messaging.dg_MessState.States mfsmStateEvade)
	{
		super(type);
		m_dataStorage = mfsmStateEvade;
	}
	public void Dispose()
	{
	}

	//data member
	public reuze.aifiles.messaging.dg_MessState.States m_dataStorage;
}