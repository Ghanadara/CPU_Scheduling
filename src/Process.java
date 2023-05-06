public class Process implements Comparable<Process> {

	private int PID;//���μ����� PID
	private int bursttime;//���μ����� ����ð�
	private int waitingtime;//���μ����� ���ð�
	private int turnaroundtime;//���μ����� ��ȯ�ð�
	private int responsetime;//���μ����� ����ð�
	private int priority;//���μ����� �켱����
	private int arrivetime;//���μ����� �����ð�
	public int[] execution;//���μ����� ���࿩�θ� �����ϴ� �迭

	public int[] getExecution() {
		return execution;
	}

	public void setExecution(int[] execution, int time) {//���� time�� �������̸� 1�� ����
		this.execution[time] = 1;
	}

	public Process() {
		super();
		// TODO Auto-generated constructor stub
		this.PID = 1;
		this.bursttime = 0;
		this.waitingtime = 0;
		this.turnaroundtime = 0;
		this.responsetime = 0;
		this.priority = 1;
		this.arrivetime = 0;
		this.execution = new int[] {};
	}

	public Process(int maximumtime) {
		super();
		// TODO Auto-generated constructor stub
		this.PID = 1;
		this.bursttime = 0;
		this.waitingtime = 0;
		this.turnaroundtime = 0;
		this.responsetime = 0;
		this.priority = 1;
		this.arrivetime = 0;
		this.execution = new int[maximumtime];
	}

	public Process(int pID, int bursttime, int waitingtime, int turnaroundtime, int responsetime, int priority,
			int arrivetime) {
		super();
		PID = pID;
		this.bursttime = bursttime;
		this.waitingtime = waitingtime;
		this.turnaroundtime = turnaroundtime;
		this.responsetime = responsetime;
		this.priority = priority;
		this.arrivetime = arrivetime;
	}

	public int getArrivetime() {
		return arrivetime;
	}

	public void setArrivetime(int arrivetime) {
		this.arrivetime = arrivetime;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getPID() {
		return PID;
	}

	public void setPID(int pID) {
		PID = pID;
	}

	public int getBursttime() {
		return bursttime;
	}

	public void setBursttime(int bursttime) {
		this.bursttime = bursttime;
	}

	public int getWaitingtime() {
		return waitingtime;
	}

	public void setWaitingtime(int waitingtime) {
		this.waitingtime = waitingtime;
	}

	public int getTurnaroundtime() {
		return turnaroundtime;
	}

	public void setTurnaroundtime(int turnaroundtime) {
		this.turnaroundtime = turnaroundtime;
	}

	public int getResponsetime() {
		return responsetime;
	}

	public void setResponsetime(int responsetime) {
		this.responsetime = responsetime;
	}

	public int compareTo(Process process) {

		if (this.arrivetime < process.arrivetime) {
			return -1;
		} else if (this.arrivetime == process.arrivetime) {
			return 0;
		} else {
			return 1;
		}
	}

	
	public int findwaitingtime(int[] execution) {// execution�迭���� �����ð��� ��ȯ�ð� ������ 0�� �ε����� ������ ���ð�
		int k = 0;
		for (int i = this.arrivetime; i < this.turnaroundtime + this.arrivetime; i++) {
			if (execution[i] == 0)
				k++;
		}
		return k;
	}

	public int findresponsetime(int[] execution) {// execution�迭���� ó������ 1�� ������ �ε����� ���� �����ð��� �� ���� ����ð�
		int k = 0;
		for (int i = 0; i < execution.length; i++) {

			if (execution[i] == 1) {
				k = i;
				break;
			}
		}
		return k - this.arrivetime;
	}

	public int findturnaroundtime(int[] execution) {// execution�迭���� ���������� 1�� ������ �ε����� ���� �����ð��� �A ���� ��ȯ�ð�
		int k = 0;
		for (int i = 0; i < execution.length; i++) {

			if (execution[i] == 1) {
				k = i;
			}
		}
		return k + 1 - this.arrivetime;
	}
}
