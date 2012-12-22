package reuze.aifiles.messaging;

public class DataMessage<T> extends dg_Message
{
	public DataMessage(int type, T data)
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