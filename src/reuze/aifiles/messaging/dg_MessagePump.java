package reuze.aifiles.messaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

import reuze.aifiles.messaging.dg_MessState.MSGStates;
import reuze.aifiles.messaging.dg_MessState.REGState;
import reuze.aifiles.messaging.dg_Message;
import reuze.aifiles.messaging.DataMessage;
import reuze.aifiles.messaging.dg_Messages;
import reuze.aifiles.messaging.dg_MessageType;

public class dg_MessagePump {
	//public static Queue messageQueue = new LinkedBlockingQueue<>();
	//public static List messageList = new ArrayList<dg_Message>();
	//public static Map messageTypeMap = new HashMap<Integer,MessageType>();
	private static dg_MessagePump inst;
	public static dg_MessagePump Instance()
	{
		return inst;
	}
	
	public static void setInstance(dg_MessagePump pump)
	{
		inst = new dg_MessagePump();		
	}
	
	public static HashMap<Integer,dg_MessageType> m_messageTypes = new HashMap<Integer, dg_MessageType>();
	public static List<dg_Message> m_messageQueue = new ArrayList<dg_Message>();
	public static List<dg_Message> m_messageIncomingQueue = new ArrayList<dg_Message>();
	
	
	public static class MessageReciever {
		public static int m_ID;
		public MessageReciever() {
			m_ID = dg_MessagePump.GetUniqueMessageID();
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
		//Iterator<dg_Message> msgItr = m_messageQueue.iterator();
		for(dg_Message pMsg : m_messageQueue)
		{			
			//System.out.println(pMsg);
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
				//dg_MessageType pmType = mType.getNext();
				//dg_MessageType mType = m_messageTypes.get(pMsg.m_typeID);
				if(mType == null)
					continue;
				//dg_MessageType pmType = ;
				
				//Iterator<MessageReg> msgReg = mType.m_messageRegistrations.iterator();
				if(mType.m_messageRegistrations != null) {
					for(dg_MessageReg msg : mType.m_messageRegistrations)
					{
						dg_MessageReg pmReg = msg;
						//deliver message by launching callback
						if(msg.m_callBack != null)
							msg.m_callBack.function(pmReg.m_parent,pMsg);
					}
					pMsg.m_delivered = true;
				}
			}
		}
	
		//remove all delivered messages from queue
		/**
		for(dg_Message msg : m_messageQueue) {
			if(msg.isM_delivered()) {
				synchronized (m_messageQueue) {
					m_messageQueue.remove(msg);
				}
			}
		}
		**/
		for(int i=0;i<m_messageQueue.size();i++) {
			if(m_messageQueue.get(i).m_delivered) {
				m_messageQueue.remove(i);
			}
		}
	}

	//---------------------------------------------------------
	public static void AddMessageToSystem(int type)
	{
		//ensure that this type isn't already in the system
		Set keyS = m_messageTypes.keySet();
		
		int mType;
		for (Iterator<Integer> it = keyS.iterator(); it.hasNext(); ) {
			mType = it.next();
			
		    System.out.println(mType);
		}
		

		//try {
		//mType = m_messageTypes.get(type);
		//}
		//catch(NullPointerException ex) {
			dg_MessageType newType = new dg_MessageType();
			newType.m_typeID = type;
			newType.m_name = GetMessageName(type);
			m_messageTypes.put(type, newType);
			System.out.println("Putting Message... m_typeID: "+newType.m_typeID+" m_name: "+newType.m_name);
			//continue;
		//}
		/**
		if(mType == null)
		{
			dg_MessageType newType = new dg_MessageType();
			newType.m_typeID = messageWontCollide;
			m_messageTypes.put(messageWontCollide, newType);
			newType.m_name = new String();
			//newType.m_name = GetMessageName(messageWontCollide);
		}
		**/
	}

	//---------------------------------------------------------
	public static int RegisterForMessage(int i, Object parent, int objectID, dg_Callback cBack)
	{
		//only register once
		//dg_MessageType mType = m_messageTypes.get(type);
		//dg_MessageType pmtype = mType.second;
		dg_MessageType mType = m_messageTypes.get(i);

		if(mType == null)
			return REGState.REGISTER_ERROR_MESSAGE_NOT_IN_SYSTEM.ordinal();
	
		
		dg_MessageReg msgReg;
		if(mType.m_messageRegistrations != null) {
			Iterator<dg_MessageReg> it = mType.m_messageRegistrations.iterator();
			while(it.hasNext()) {
					msgReg = it.next();			
					if(msgReg.m_objectID == objectID)
						return REGState.REGISTER_ERROR_ALREADY_REGISTERED.ordinal();
			}
		}
		else {
			mType.m_messageRegistrations = new ArrayList<dg_MessageReg>();
		}
		
		/**
		for(dg_MessageReg msgReg : mType.m_messageRegistrations)
		{
			System.out.print("Registering: "+mType.m_name);
			if(msgReg.m_objectID == objectID)
				return REGState.REGISTER_ERROR_ALREADY_REGISTERED.ordinal();
		}
		**/
		//add new registration
		dg_MessageReg newRegistration = new dg_MessageReg();
		newRegistration.m_parent = parent;
		newRegistration.m_callBack = cBack;
		newRegistration.m_objectID = objectID;
	
		mType.m_messageRegistrations.add(newRegistration);
		return REGState.REGISTER_MESSAGE_OK.ordinal();
	
	
	}

	//---------------------------------------------------------
	public void UnRegisterForMessage(int i, int objectID)
	{
		//find entry
		dg_MessageType mType = m_messageTypes.get(i);
	
		if(mType == null)
			return;
	
		for(dg_MessageReg msgReg : mType.m_messageRegistrations)
		{
			if(msgReg.m_objectID == objectID)
			{
				mType.m_messageRegistrations.remove(msgReg);
				//you can exit out here, there is only one registration 
				//allowed per message type
				return;
			}
			else {
				continue;
			}
		}
	}

	//---------------------------------------------------------
	public static void UnRegisterAll(int objectID)
	{
		for(dg_MessageType mType : m_messageTypes.values())
		{
			for(dg_MessageReg msgReg : mType.m_messageRegistrations)
			{
				if(msgReg.m_objectID == objectID)
				{
					mType.m_messageRegistrations.remove(msgReg);
					//you can exit out here, there is only one registration 
					//allowed per message type
					return;
				}
				else
					continue;
			}
		}
	}
	//---------------------------------------------------------
	public void SendMessage(dg_Message msg)
	{
		m_messageIncomingQueue.add(msg);
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
