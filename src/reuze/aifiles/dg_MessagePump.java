package reuze.aifiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import reuze.aifiles.dg_Message;
import reuze.aifiles.dg_Message.DataMessage;
import reuze.aifiles.dg_Messages;
import reuze.aifiles.dg_MessageType;
import reuze.aifiles.dg_Messages.AnonymousEnum;
import reuze.aifiles.dg_Messages.Types;

public class dg_MessagePump {
	//public static Queue messageQueue = new LinkedBlockingQueue<>();
	//public static List messageList = new ArrayList<dg_Message>();
	//public static Map messageTypeMap = new HashMap<Integer,MessageType>();
	
	public static HashMap<Integer,dg_MessageType> m_messageTypes;
	public static List<dg_Message> m_messageQueue = new ArrayList<dg_Message>();
	public static List<dg_Message> m_messageIncomingQueue = new ArrayList<dg_Message>();
	
	
	public class MessageReciever {
		public int m_ID;
		public MessageReciever() {
			this.m_ID = dg_MessagePump.GetUniqueMessageID();
		}		
	}
	
	public static int m_uniqueID = 0;
	public static int GetUniqueMessageID()
	{
		return m_uniqueID++;
	}	
	//---------------------------------------------------------
	//functor for removing delivered messages
	public boolean RemoveIfDelivered(dg_Message msg)
	{
	    if(msg.isM_delivered())
	    {
	        return true;
	    }
	    return false;
	}
	
	public static void Update(float dt)
	{
		//move over new incoming messages
		//m_messageIncomingQueue.sort();
		m_messageQueue.addAll(m_messageIncomingQueue);
		m_messageIncomingQueue.clear();
	
		if(m_messageQueue.size() == 0)
			return;
	
		//process messages
		Iterator<dg_Message> msgItr = m_messageQueue.iterator();
		for(msg =m_messageQueue.begin();msg!=m_messageQueue.end();++msg)
		{
			dg_Message pMsg = msgItr.next();
			if(pMsg.m_timer >= 0)
			{
				//delayed message, decrement timer
				pMsg.m_timer -= dt;
			}
			else
			{
				//check for registrations
				//Iterator //mtypes
				//std.map.iterator geType*>iterator mType;
				//mType = m_messageTypes.find(pMsg.m_typeID);
				dg_MessageType mType = m_messageTypes.get(pMsg.m_typeID);
				if(mType == null)
					continue;
				dg_MessageType pmType = mType.second;
	
				std.list.iterator g*>iterator msgReg;
				std.list<MessageReg*> pMessReg = mType.second.m_messageRegistrations;
				for(msgReg =mType.second.m_messageRegistrations.begin();msgReg!=mType.second.m_messageRegistrations.end();++msgReg)
				{
					MessageReg pmReg = (*msgReg);
					//deliver message by launching callback
					if(pmReg.m_callBack)
						pmReg.m_callBack.function(pmReg.m_parent,pMsg);
				}
				pMsg.m_delivered = true;
			}
		}
	
		//remove all delivered messages from queue
		for(dg_Message msg : m_messageQueue) {
			if(msg.isM_delivered()) {
				m_messageQueue.remove(msg);
			}
		}
	}

	//---------------------------------------------------------
	public static void AddMessageToSystem(int type)
	{
		//ensure that this type isn't already in the system

		dg_MessageType mType = m_messageTypes.get(type);
	
		if(mType == null)
		{
			dg_MessageType newType = new dg_MessageType();
			newType.m_typeID = type;
			m_messageTypes.put(type, newType);
			newType.m_name = new String();
			newType.m_name = GetMessageName(type);
		}
	}

	//---------------------------------------------------------
	public static int RegisterForMessage(int type, Object parent, int objectID, RefObject<dg_Callback> cBack)
	{
		//only register once
		dg_MessageType mType = m_messageTypes.get(type);
		dg_MessageType pmtype = mType.second;
	
		if(mType == m_messageTypes.end())
			return AnonymousEnum.REGISTER_ERROR_MESSAGE_NOT_IN_SYSTEM;
	
		std.list.iterator g*>iterator msgReg;
		for(msgReg =mType.second.m_messageRegistrations.begin();msgReg!=mType.second.m_messageRegistrations.end();++msgReg)
		{
			if((*msgReg).m_objectID == objectID)
				return AnonymousEnum.REGISTER_ERROR_ALREADY_REGISTERED;
		}
		//add new registration
		MessageReg newRegistration = new dg_MessageReg();
		newRegistration.m_parent = parent;
		newRegistration.m_callBack = cBack.argvalue;
		newRegistration.m_objectID = objectID;
	
		mType.second.m_messageRegistrations.push_back(newRegistration);
		return AnonymousEnum.REGISTER_MESSAGE_OK;
	
	
	}

	//---------------------------------------------------------
	public static void UnRegisterForMessage(int type, int objectID)
	{
		//find entry
		std.map.iterator geType*>iterator mType;
		mType = m_messageTypes.find(type);
	
		if(mType == m_messageTypes.end())
			return;
	
		std.list.iterator g*>iterator msgReg;
		for(msgReg =mType.second.m_messageRegistrations.begin();msgReg!=mType.second.m_messageRegistrations.end();)
		{
			if((*msgReg).m_objectID == objectID)
			{
				mType.second.m_messageRegistrations.erase(msgReg);
				//you can exit out here, there is only one registration 
				//allowed per message type
				return;
			}
			else
				msgReg++;
		}
	}

	//---------------------------------------------------------
	public static void UnRegisterAll(int objectID)
	{
		std.map.iterator geType*>iterator mType;
		for(mType = m_messageTypes.begin();mType != m_messageTypes.end(); ++mType)
		{
			Iterator msgRegItr = msgReg.iterator();
			for(msgReg =mType.second.m_messageRegistrations.begin();msgReg!=mType.second.m_messageRegistrations.end();)
			{
				if((*msgReg).m_objectID == objectID)
				{
					mType.second.m_messageRegistrations.erase(msgReg);
					//you can exit out here, there is only one registration 
					//allowed per message type
					return;
				}
				else
					msgReg++;
			}
		}
	}
	//---------------------------------------------------------
	public static void SendMessage(DataMessage<Integer> newMsg)
	{
		m_messageIncomingQueue.add(newMsg);
	}

	//---------------------------------------------------------
	public static String GetMessageName(int type)
	{
		switch(type)
		{
			case 0:
				return "Will Collide";
			case 1:
				return "Wont Collide";
			case 2:
				return "No Asteroids";
			case 3:
				return "No Powerups";
			case 4:
				return "Asteroid Far";
			case 5:
				return "Asteroid Near";
			case 6:
				return "Powerup Near";
			case 7:
				return "Powerup Far";
			case 8:
				return "Change State";
			case 9:
				return "Ship Total Stop";
			default:
				return "Error Message";
		}
	}


	
	
	/**
	public static String GetMessageName(int type) {
		switch(type) {
			case MESSAGE_WILL_COLLIDE:
				return "Will collide";
			case MESSAGE_WONT_COLLIDE:
				return "Wont collide";
			case MESSAGE_NO_ASTEROIDS:
	            return "No Asteroids";
	        case MESSAGE_NO_POWERUPS:
	            return "No Powerups";
	        case MESSAGE_ASTEROID_FAR:
	            return "Asteroid Far";
	        case MESSAGE_ASTEROID_NEAR:
	            return "Asteroid Near";
	        case MESSAGE_POWERUP_NEAR:
	            return "Powerup Near";
	        case MESSAGE_POWERUP_FAR:
	            return "Powerup Far";
	        case MESSAGE_CHANGE_STATE:
	            return "Change State";
	        case MESSAGE_SHIP_TOTAL_STOP:
	            return "Ship Total Stop";
	        default:
	            return "Error Message";
	            break;
		}
	}
	**/
	final class RefObject<T>
	{
		T argvalue;
		RefObject(T refarg)
		{
			argvalue = refarg;
		}
	}
	
	public static List<?> flatten(List<?> input) {
	    List<Object> result = new ArrayList<Object>();

	    for (Object o: input) {
	        if (o instanceof List<?>) {
	            result.addAll(flatten((List<?>) o));
	        } else {
	            result.add(o);
	        }
	    }

	    return result;
	}


}
