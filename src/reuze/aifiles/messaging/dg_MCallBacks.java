package reuze.aifiles.messaging;

import reuze.aifiles.messaging.dg_MessState.MSGStates;
import reuze.aifiles.messaging.dg_MessState.States;
import reuze.aifiles.messaging.DataMessage;


public class dg_MCallBacks {
	public static class EvadeCallback extends dg_Callback
	{
		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(MSGStates.MESSAGE_CHANGE_STATE.ordinal(),States.MFSM_STATE_EVADE.ordinal());
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			dg_MessagePump.Instance().SendMessage(newMsg);
		}
	}
	public static class ApproachCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(MSGStates.MESSAGE_CHANGE_STATE.ordinal(),States.MFSM_STATE_APPROACH.ordinal());
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			
			dg_MessagePump.Instance().SendMessage(newMsg);
		}
	}
	public static class AttackCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(MSGStates.MESSAGE_CHANGE_STATE.ordinal(),States.MFSM_STATE_ATTACK.ordinal());
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			
			dg_MessagePump.Instance().SendMessage(newMsg);
		}
	}
	public static class GetPowerupCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(MSGStates.MESSAGE_CHANGE_STATE.ordinal(),States.MFSM_STATE_GETPOWERUP.ordinal());
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			
			dg_MessagePump.Instance().SendMessage(newMsg);
		}
	}
	public static class IdleCallback extends dg_Callback
	{

		//---------------------------------------------------------
		void function(Object parent, dg_Message msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(MSGStates.MESSAGE_CHANGE_STATE.ordinal(),States.MFSM_STATE_IDLE.ordinal());
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			
			dg_MessagePump.Instance().SendMessage(newMsg);
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
