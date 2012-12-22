package reuze.aifiles.messaging;

public class DataMessage<T> extends dg_Message
{
	public DataMessage(int type, reuze.aifiles.messaging.dg_MessState.States mfsmStateEvade)
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