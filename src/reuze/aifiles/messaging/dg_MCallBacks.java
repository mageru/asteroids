package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.MSGStates;
import reuze.aifiles.messaging.dg_MessState.States;
import reuze.aifiles.messaging.DataMessage;
import reuze.aifiles.messaging.dg_MessState;

public class dg_MCallBacks {
	public class EvadeCallback extends dg_Callback
	{
		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			reuze.aifiles.messaging.DataMessage<reuze.aifiles.messaging.dg_MessState> newMsg = new DataMessage<reuze.aifiles.messaging.dg_MessState>(MSGStates.MESSAGE_CHANGE_STATE,States.MFSM_STATE_EVADE);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			dg_MessagePump.SendMessage(newMsg);
		}
	}
	public class ApproachCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			reuze.aifiles.messaging.DataMessage<reuze.aifiles.messaging.dg_MessState> newMsg = new DataMessage<reuze.aifiles.messaging.dg_MessState>(MSGStates.MESSAGE_CHANGE_STATE,States.MFSM_STATE_EVADE);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			
			dg_MessagePump.SendMessage(newMsg);
		}
	}
	public class AttackCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			reuze.aifiles.messaging.DataMessage<reuze.aifiles.messaging.dg_MessState> newMsg = new DataMessage<reuze.aifiles.messaging.dg_MessState>(MSGStates.MESSAGE_CHANGE_STATE,States.MFSM_STATE_ATTACK);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			;
			dg_MessagePump.SendMessage(newMsg);
		}
	}
	public class GetPowerupCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			reuze.aifiles.messaging.DataMessage<reuze.aifiles.messaging.dg_MessState> newMsg = new DataMessage<reuze.aifiles.messaging.dg_MessState>(MSGStates.MESSAGE_CHANGE_STATE,States.MFSM_STATE_GETPOWERUP);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			;
			dg_MessagePump.SendMessage(newMsg);
		}
	}
	public class IdleCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			reuze.aifiles.messaging.DataMessage<reuze.aifiles.messaging.dg_MessState> newMsg = new DataMessage<reuze.aifiles.messaging.dg_MessState>(MSGStates.MESSAGE_CHANGE_STATE,States.MFSM_STATE_IDLE);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			;
			dg_MessagePump.SendMessage(newMsg);
		}
	}

	//#endif

	//----------------------------------------------------------------------------------------
//		Copyright © 2006 - 2008 Tangible Software Solutions Inc.
	//
//		This class is used to simulate the ability to pass arguments by reference in Java.
	//----------------------------------------------------------------------------------------
	final class RefObject<T>
	{
		T argvalue;
		RefObject(T refarg)
		{
			argvalue = refarg;
		}
	}
}
