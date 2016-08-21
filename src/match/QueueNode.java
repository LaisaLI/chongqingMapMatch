package match;

class QueueNode
{
	public long lineID;//当前路链ID
	public int distant;//广搜距离
	public int pastID;//父路链在queue中的位置，即下一跳在queue的位置
	public QueueNode(long _lineID, int _distant, int _pastID)
	{
		lineID = _lineID; 
		distant = _distant;
		pastID = _pastID;
	}
}
