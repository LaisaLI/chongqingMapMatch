package match;

class QueueNode
{
	public long lineID;//��ǰ·��ID
	public int distant;//���Ѿ���
	public int pastID;//��·����queue�е�λ�ã�����һ����queue��λ��
	public QueueNode(long _lineID, int _distant, int _pastID)
	{
		lineID = _lineID; 
		distant = _distant;
		pastID = _pastID;
	}
}
