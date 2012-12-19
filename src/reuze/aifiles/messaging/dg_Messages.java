package reuze.aifiles.messaging;

import java.util.List;
import reuze.aifiles.messaging.dg_Callback;
import reuze.aifiles.messaging.dg_MessState.MSGStates;

public abstract class dg_Messages {
	public class MessageType
	{
		public MSGStates m_typeID;
		public List<dg_MessageReg> m_messageRegistrations;

		//debug data
		public String m_name;
	}
}
