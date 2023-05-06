public class Process implements Comparable<Process> {

	private int PID;//프로세스의 PID
	private int bursttime;//프로세스의 실행시간
	private int waitingtime;//프로세스의 대기시간
	private int turnaroundtime;//프로세스의 반환시간
	private int responsetime;//프로세스의 응답시간
	private int priority;//프로세스의 우선순위
	private int arrivetime;//프로세스의 도착시간
	public int[] execution;//프로세스의 실행여부를 저장하는 배열

	public int[] getExecution() {
		return execution;
	}

	public void setExecution(int[] execution, int time) {//현재 time에 실행중이면 1로 설정
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

	
	public int findwaitingtime(int[] execution) {// execution배열에서 도착시간과 반환시간 사이의 0인 인덱스의 개수가 대기시간
		int k = 0;
		for (int i = this.arrivetime; i < this.turnaroundtime + this.arrivetime; i++) {
			if (execution[i] == 0)
				k++;
		}
		return k;
	}

	public int findresponsetime(int[] execution) {// execution배열에서 처음으로 1을 가지는 인덱스의 값과 도착시간을 뺀 것이 응답시간
		int k = 0;
		for (int i = 0; i < execution.length; i++) {

			if (execution[i] == 1) {
				k = i;
				break;
			}
		}
		return k - this.arrivetime;
	}

	public int findturnaroundtime(int[] execution) {// execution배열에서 마지막으로 1을 가지는 인덱스의 값과 도착시간을 뺸 것이 반환시간
		int k = 0;
		for (int i = 0; i < execution.length; i++) {

			if (execution[i] == 1) {
				k = i;
			}
		}
		return k + 1 - this.arrivetime;
	}
}
