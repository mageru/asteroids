package reuze.aifiles.messaging;

import java.util.List;
import reuze.aifiles.messaging.dg_Callback;
import reuze.aifiles.messaging.dg_MessState.MSGStates;

public abstract class dg_Messages {
	public class MessageReg
	{
		public MessageReg()
		{
			m_objectID = -1;
			m_parent = null;
			m_callBack = null;
		}
		public int m_objectID;
		public Object m_parent;
		public dg_Callback m_callBack;
	}
	public class MessageType
	{
		public MSGStates m_typeID;
		public List<MessageReg> m_messageRegistrations;

		//debug data
		public String m_name;
	}
}
