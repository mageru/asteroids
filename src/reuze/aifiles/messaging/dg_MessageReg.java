package reuze.aifiles.messaging;

public class dg_MessageReg
{
	public dg_MessageReg()
	{
		m_objectID = -1;
		m_parent = null;
		m_callBack = null;
	}
	public int m_objectID;
	public Object m_parent;
	public dg_Callback m_callBack;
}