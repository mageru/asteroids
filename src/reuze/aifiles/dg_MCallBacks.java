package reuze.aifiles;

import reuze.aifiles.dg_Message.DataMessage;
import reuze.aifiles.dg_Messages.MSGState;
import reuze.aifiles.dg_Messages.Types;

public class dg_MCallBacks {
	public class EvadeCallback extends dg_Callback
	{

		//---------------------------------------------------------
		private void function(Object parent, RefObject<dg_Message> msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(Types.MESSAGE_CHANGE_STATE.getCode(),MSGState.MFSM_STATE_EVADE.getCode());
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			dg_MessagePump.SendMessage(newMsg);
		}
	}
	public class ApproachCallback extends dg_Callback
	{

		//---------------------------------------------------------
		private void function(Object parent, RefObject<dg_Message> msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(Types.MESSAGE_CHANGE_STATE.getCode(),MSGState.MFSM_STATE_EVADE);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			;
			dg_MessagePump.SendMessage(newMsg);
		}
	}
	public class AttackCallback extends dg_Callback
	{

		//---------------------------------------------------------
		private void function(Object parent, RefObject<dg_Message> msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(AnonymousEnum.MESSAGE_CHANGE_STATE,AnonymousEnum.MFSM_STATE_ATTACK);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			;
			dg_MessagePump.Instance().SendMessage(newMsg);
		}
	}
	public class GetPowerupCallback extends dg_Callback
	{

		//---------------------------------------------------------
		private void function(Object parent, RefObject<dg_Message> msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(AnonymousEnum.MESSAGE_CHANGE_STATE,AnonymousEnum.MFSM_STATE_GETPOWERUP);
			newMsg.m_fromID = ((dg_MessState)parent).GetMessageID();
			;
			dg_MessagePump.Instance().SendMessage(newMsg);
		}
	}
	public class IdleCallback extends dg_Callback
	{

		//---------------------------------------------------------
		private void function(Object parent, RefObject<Message> msg)
		{
			DataMessage<Integer> newMsg = new DataMessage<Integer>(AnonymousEnum.MESSAGE_CHANGE_STATE,AnonymousEnum.MFSM_STATE_IDLE);
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
