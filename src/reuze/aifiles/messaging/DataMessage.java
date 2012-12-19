package reuze.aifiles.messaging;

import reuze.aifiles.dg_MessState.States;
import reuze.aifiles.messaging.dg_MessState.MSGStates;

public class DataMessage<T> extends dg_Message
{
	public DataMessage(MSGStates type, T data)
	{
		super(type);
		m_dataStorage = data;
	}
	public void Dispose()
	{
	}

	//data member
	public T m_dataStorage;
}