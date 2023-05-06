import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class CpuScheduling {
	public static void main(String[] args) {
		System.out
				.println("1번은 FCFS 2번은 SJF 3번은 SRTF 4번은 비선점 우선순위 5번은 선점형 우선순위 6번은 RoundRobin 7번은 Upgraded RoundRobin");
		Scanner sc = new Scanner(System.in);
		int m = Integer.parseInt(sc.next());

		if (m == 1) {
			FCFS();
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		} else if (m == 2) {
			SJF();
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		} else if (m == 3) {
			SRTF();
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		} else if (m == 4) {
			Priority_nonPreemtive();
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		} else if (m == 5) {
			Priority_Preemtive();
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		} else if (m == 6) {
			Round_Robin();
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		} else if (m == 7) {
			Upgraded_RoundRobin();
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		} else {
			System.out.println("error");
			System.out.println("종료하려면 아무키나 입력하고 엔터를 누르시오");
			String exit = sc.next();
		}
	}

	public static void FCFS() {
		Process[] process = CreateProcess();// 프로세스 생성
		int num = process.length;// 프로세스의 개수 저장
		int i = 0, j = 0;// 반복문에 쓰일 변수

		PrintBefore(process);// 스케쥴링 전 프로세스 상태 표시

		int rt[] = new int[num];// 프로세스 별 남은시간을 저장하는 배열
		for (i = 0; i < num; i++)
			rt[i] = process[i].getBursttime();// 배열rt에 프로세스 별 실행시간을 초기값으로 설정

		int at[] = new int[num];
		for (i = 0; i < num; i++)
			at[i] = process[i].getArrivetime();// 배열rt에 프로세스 별 실행시간을 초기값으로 설정

		int complete = 0;// 실행 종료된 프로세스의 개수
		int t = 0;// 현재 시간

		LinkedList<Process> readyqueue = new LinkedList<Process>();// 레디큐 생성

		while (complete != num) {// 모든 프로세스가 종료될때까지 반복
			for (j = 0; j < num; j++) {
				if ((process[j].getArrivetime() == t) && (rt[j] > 0)) {// 현재시간에 도착한 프로세스를 레디큐에 넣는다.
					if (readyqueue.contains(process[j])) {
						break;
					}
					readyqueue.add(process[j]);
				}
			}
			if (readyqueue.isEmpty()) {
				t++;
				continue;
			}
			Process runningprocess = readyqueue.poll();// 레디큐에 있는 프로세스를 꺼내서 runningprocess에 저장
			for (j = 0; j < process[runningprocess.getPID() - 1].getBursttime(); j++) {// 실행할 프로세스가 있으면 해당 프로세스의 실행시간만큼
																						// 시간 증가
				rt[runningprocess.getPID() - 1]--;// 남은 시간 감소
				process[runningprocess.getPID() - 1].setExecution(process[runningprocess.getPID() - 1].getExecution(),
						t);// 간트차트에 쓰일 정보 저장
				t++;// 시간 증가
				for (i = 0; i < num; i++) {// 실행 중에 들어온 다른 프로세스가 있는 경우 레디큐에 삽입
					if ((process[i].getArrivetime() == t) && (rt[i] > 0)) {
						readyqueue.add(process[i]);
					}
				}
			}
			complete++;// 완료된 프로세스 증가
		}

		for (i = 0; i < num; i++) {// 프로세스 별 응답시간 설정
			process[i].setResponsetime(process[i].findresponsetime(process[i].execution));

		}
		for (i = 0; i < num; i++) {// 프로세스 별 반환시간 설정
			process[i].setTurnaroundtime(process[i].findturnaroundtime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 대기시간 설정
			process[i].setWaitingtime(process[i].findwaitingtime(process[i].execution));
		}
		PrintAfter(process, t);// 스케쥴링 후 시간 출력
		System.out.println("전체 시간 = " + t);// 전체시간 출력
		PrintGanttchart(process, t);// 간트차트 출력
	}

	public static void SJF() {
		Process[] process = CreateProcess();// 프로세스 생성
		int num = process.length;// 프로세스의 개수 저장
		int i = 0, j = 0;// 반복문에 쓰일 변수

		PrintBefore(process);// 스케줄링 전 상태 표시

		int rt[] = new int[num];// 프로세스 별 남은시간을 저장하는 배열

		for (i = 0; i < num; i++)
			rt[i] = process[i].getBursttime();// 배열rt에 프로세스 별 실행시간을 초기값으로 설정

		int complete = 0;// 실행 종료된 프로세스의 개수
		int t = 0;// 현재 시간
		int minm = Integer.MAX_VALUE;// 제일 작은 실행시간을 저장할 변수
		int shortest = 0;// 제일 작은 실행시간을 가진 프로세스의 인덱스를 저장할 변수
		boolean available = false;// 실행가능한 프로세스가 있는지 판단

		while (complete != num) {// 모든 프로세스가 종료될때까지 반복

			for (j = 0; j < num; j++) {
				if ((process[j].getArrivetime() <= t) && (rt[j] < minm) && rt[j] > 0) {// 프로세스의 도착시간이 현재 시간보다 작고 남은시간이
																						// 1이상인 경우
					minm = rt[j];
					shortest = j;
					available = true;// true는 현재 실행할 프로세스가 있다는 것이다.
				}
			}

			if (available == false) {// 실행할 프로세스가 없으면 현재시간을 1 증가시키고 다시 while문 시작
				t++;
				continue;
			}
			for (i = 0; i < process[shortest].getBursttime(); i++) {// 실행할 프로세스가 있으면 프로세스의 실행시간만큼 시간 증가
				process[shortest].setExecution(process[shortest].getExecution(), t);// 간트차트에 쓰일 정보 저장
				rt[shortest]--;// 남은 시간 감소
				t++;// 시간 증가
			}
			complete++;// 실행 종료된 프로세스 하나 증가
			available = false;// 실행할 수 있는 프로세스가 있는지 확인을 위해 false로 변경
			minm = Integer.MAX_VALUE;// 최소값을 int의 최대값으로 초기화. 실행중인 동안 들어온 프로세스 중 minm보다 큰 실행시간이 있으면 실행이 안 되므로 초기화함.
		}
		for (i = 0; i < num; i++) {// 프로세스 별 응답시간 설정
			process[i].setResponsetime(process[i].findresponsetime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 반환시간 설정
			process[i].setTurnaroundtime(process[i].findturnaroundtime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 대기시간 설정
			process[i].setWaitingtime(process[i].findwaitingtime(process[i].execution));
		}
		PrintAfter(process, t);// 스케줄링 후 상태 출력
		System.out.println("전체 시간 = " + t);// 전체시간 출력
		PrintGanttchart(process, t);// 간트차트 출력
	}

	public static void SRTF() {
		Process[] process = CreateProcess();// 프로세스 생성
		int num = process.length;// 프로세스의 개수 저장
		int i = 0, j = 0;// 반복문에 쓰일 변수

		PrintBefore(process);// 스케쥴링 전 프로세스 상태 표시

		int rt[] = new int[num];// 프로세스 별 남은시간을 저장하는 배열

		for (i = 0; i < num; i++)
			rt[i] = process[i].getBursttime();// 프로세스 별 실행시간을 초기값으로 설정

		int complete = 0;// 완료된 프로세스의 개수를 저장하는 변수
		int t = 0;// 현재 시간을 나타내는 변수
		int minm = Integer.MAX_VALUE;// 최소값을 저장하는 변수
		int shortest = 0;// 남은시간이 제일 작은 프로세스의 인덱스를 저장하는 변수
		boolean available = false;// 실행가능여부를 저장하는 변수

		while (complete != num) {// 모든 프로세스가 완료될때까지 반복
			for (j = 0; j < num; j++) {
				if ((process[j].getArrivetime() <= t) && (rt[j] < minm) && rt[j] > 0) {// 프로세스의 도착시간이 현재 시간보다 작고 남은시간이
																						// 1이상인 경우
					minm = rt[j];
					shortest = j;
					available = true;
				}
			}

			if (available == false) {// 실행할 프로세스가 없으면 현재 시간 증가
				t++;
				continue;
			}

			rt[shortest]--;// 남은시간 1 감소
			process[shortest].setExecution(process[shortest].getExecution(), t);// 간트차트에 쓰일 정보 저장

			minm = rt[shortest];// 1 감소하였으므로 남은시간 다시 저장
			if (minm == 0)// 남은시간이 0이면 최소값 초기화
				minm = Integer.MAX_VALUE;

			if (rt[shortest] == 0) {// 남은 시간이 0인 경우
				complete++;// 완료된 프로세스 증가
				available = false;// 실행할 프로세스 없다고 저장

				process[shortest]
						.setWaitingtime(t + 1 - process[shortest].getBursttime() - process[shortest].getArrivetime());// 대기시간
																														// 설정

				if (process[shortest].getWaitingtime() < 0)
					process[shortest].setWaitingtime(0);

			}
			t++;// 시간 증가
		}
		for (i = 0; i < num; i++) {// 프로세스별 응답시간 설정
			process[i].setResponsetime(process[i].findresponsetime(process[i].execution));

		}
		for (i = 0; i < num; i++) {// 프로세스별 반환시간 설정
			process[i].setTurnaroundtime(process[i].findturnaroundtime(process[i].execution));
		}

		PrintAfter(process, t);// 스케쥴링 후 시간 출력
		System.out.println("전체 시간 = " + t);// 전체시간 출력
		PrintGanttchart(process, t);// 간트차트 출력
	}

	public static void Priority_nonPreemtive() {
		Process[] process = CreateProcess();// 프로세스 생성
		int num = process.length;// 프로세스의 개수 저장
		int i = 0, j = 0;// 반복문에 쓰일 변수

		PrintBefore(process);// 스케줄링 전 상태 표시

		int pri[] = new int[num];// 프로세스 별 우선순위를 저장하는 배열

		for (i = 0; i < num; i++)
			pri[i] = process[i].getPriority();// 배열pri에 프로세스 별 우선순위를 초기값으로 설정

		int rt[] = new int[num];// 프로세스 별 남은시간을 저장하는 배열

		for (i = 0; i < num; i++)
			rt[i] = process[i].getBursttime();// 배열rt에 프로세스 별 실행시간을 초기값으로 설정

		int complete = 0;// 실행 종료된 프로세스의 개수
		int t = 0;// 현재 시간
		int highP = Integer.MAX_VALUE;// 제일 높은 우선순위를 저장할 변수
		int highest = 0;// 제일 높은 우선순위를 가진 프로세스의 인덱스를 저장할 변수
		boolean available = false;// 실행가능한 프로세스가 있는지 판단

		while (complete != num) {// 모든 프로세스가 종료될때까지 반복

			for (j = 0; j < num; j++) {
				if ((process[j].getArrivetime() <= t) && (pri[j] < highP) && (rt[j] > 0)) {// 프로세스의 도착시간이 현재 시간보다 작고
																							// 우선순위가 highP보다 작은 경우
					highP = pri[j];
					highest = j;
					available = true;
				}
			}

			if (available == false) {// 실행할 프로세스가 없으면 현재시간을 1 증가시키고 다시 while문 시작
				t++;
				continue;
			}
			for (i = 0; i < process[highest].getBursttime(); i++) {// 실행할 프로세스가 있으면 프로세스의 실행시간만큼 시간 증가
				process[highest].setExecution(process[highest].getExecution(), t);// 간트차트에 쓰일 정보 저장
				rt[highest]--;// 남은 시간 감소
				t++;// 시간 증가
			}
			complete++;// 실행 종료된 프로세스 하나 증가
			available = false;// 실행할 수 있는 프로세스가 있는지 확인을 위해 false로 변경
			highP = Integer.MAX_VALUE;// 우선순위 초기화
		}
		for (i = 0; i < num; i++) {// 프로세스 별 응답시간 설정
			process[i].setResponsetime(process[i].findresponsetime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 반환시간 설정
			process[i].setTurnaroundtime(process[i].findturnaroundtime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 대기시간 설정
			process[i].setWaitingtime(process[i].findwaitingtime(process[i].execution));
		}
		PrintAfter(process, t);// 스케줄링 후 상태 출력
		System.out.println("전체 시간 = " + t);// 전체시간 출력
		PrintGanttchart(process, t);// 간트차트 출력
	}

	public static void Priority_Preemtive() {
		Process[] process = CreateProcess();// 프로세스 생성
		int num = process.length;// 프로세스의 개수 저장
		int i = 0, j = 0;// 반복문에 쓰일 변수

		PrintBefore(process);// 스케줄링 전 상태 표시

		int pri[] = new int[num];// 프로세스 별 우선순위를 저장하는 배열

		for (i = 0; i < num; i++)
			pri[i] = process[i].getPriority();// 배열pri에 프로세스 별 우선순위를 초기값으로 설정

		int rt[] = new int[num];// 프로세스 별 남은시간을 저장하는 배열

		for (i = 0; i < num; i++)
			rt[i] = process[i].getBursttime();// 배열rt에 프로세스 별 실행시간을 초기값으로 설정

		int complete = 0;// 실행 종료된 프로세스의 개수
		int t = 0;// 현재 시간
		int highP = Integer.MAX_VALUE;// 제일 높은 우선순위를 저장할 변수
		int highest = 0;// 제일 높은 우선순위를 가진 프로세스의 인덱스를 저장할 변수
		boolean available = false;// 실행가능한 프로세스가 있는지 판단

		while (complete != num) {// 모든 프로세스가 종료될때까지 반복
			for (j = 0; j < num; j++) {
				if ((process[j].getArrivetime() <= t) && (pri[j] < highP) && (rt[j] > 0)) {// 프로세스의 도착시간이 현재 시간보다 작고
																							// 우선순위가 highP보다 작은 경우
					highP = pri[j];
					highest = j;
					available = true;
				}
			}

			if (available == false) {// 실행할 프로세스가 없으면 현재시간을 1 증가시키고 다시 while문 시작
				t++;
				continue;
			}

			process[highest].setExecution(process[highest].getExecution(), t);// 간트차트에 쓰일 정보 저장
			rt[highest]--;// 남은 시간 감소

			if (rt[highest] == 0) {// 남은시간이 0인 경우
				complete++;
				available = false;
			}
			highP = Integer.MAX_VALUE;// 최소값을 int의 최대값으로 초기화. 실행중인 동안 들어온 프로세스 중 minm보다 큰 실행시간이 있으면 실행이 안 되므로 초기화함.
			t++;// 시간 증가
		}
		for (i = 0; i < num; i++) {// 프로세스 별 응답시간 설정
			process[i].setResponsetime(process[i].findresponsetime(process[i].execution));

		}
		for (i = 0; i < num; i++) {// 프로세스 별 반환시간 설정
			process[i].setTurnaroundtime(process[i].findturnaroundtime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 반환시간 설정
			process[i].setWaitingtime(process[i].findwaitingtime(process[i].execution));
		}
		PrintAfter(process, t);// 스케줄링 후 상태 출력
		System.out.println("전체 시간 = " + t);// 전체시간 출력
		PrintGanttchart(process, t);// 간트차트 출력
	}

	public static void Round_Robin() {
		Process[] process = CreateProcess();// 프로세스 생성
		System.out.println("time slice를 입력하세요.");
		Scanner sc = new Scanner(System.in);
		int time_slice = Integer.parseInt(sc.next());// time slice를 설정
		int num = process.length;// 프로세스의 개수 저장
		int i = 0, j = 0;// 반복문에 쓰일 변수

		PrintBefore(process);// 스케줄링 전 상태 표시

		int rt[] = new int[num];// 프로세스 별 남은시간을 저장하는 배열

		for (i = 0; i < num; i++)
			rt[i] = process[i].getBursttime();// 배열rt에 프로세스 별 실행시간을 초기값으로 설정

		int complete = 0;// 실행 종료된 프로세스의 개수
		int t = 0;// 현재 시간

		LinkedList<Process> readyqueue = new LinkedList<>();// 레디큐 생성

		while (true) {
			for (j = 0; j < num; j++) {
				if ((process[j].getArrivetime() == t) && (rt[j] > 0)) {// 첫번째로 도착한 프로세스를 레디큐에 넣는다.
					readyqueue.add(process[j]);
				}
			}
			if (readyqueue.isEmpty()) {// 해당 시간에 들어온 프로세스가 없는 경우
				t++;// 시간 증가
				continue;
			}
			while (complete != num) {// 모든 프로세스가 종료될때까지 반복
				for (j = 0; j < num; j++) {
					if ((process[j].getArrivetime() == t) && (rt[j] > 0)) {// 현재시간에 도착한 프로세스를 레디큐에 넣는다.
						if (readyqueue.contains(process[j])) {// 레디큐에 있는 경우 넣지 않는다.
							break;
						}
						readyqueue.add(process[j]);
					}
				}
				if (readyqueue.isEmpty()) {// 레디큐에 프로세스가 없으면 현재 시간 증가
					t++;
					continue;
				}
				Process runningprocess = readyqueue.poll();// 레디큐에 있는 프로세스를 꺼내서 runningprocess에 저장
				for (j = 0; j < time_slice; j++) {// 실행할 프로세스가 있으면 time slice만큼 시간 증가
					if (rt[runningprocess.getPID() - 1] == 0)// 할당받은 time slice 시간 안에 종료된 경우
						break;// 반복문 탈출
					rt[runningprocess.getPID() - 1]--;// 남은 시간 감소
					process[runningprocess.getPID() - 1]
							.setExecution(process[runningprocess.getPID() - 1].getExecution(), t);// 간트차트에 쓰일 정보 저장
					t++;// 시간 증가

					for (i = 0; i < num; i++) {// time slice 시간 안에 들어온 다른 프로세스가 있는 경우 레디큐에 삽입
						if ((process[i].getArrivetime() == t) && (rt[i] > 0)) {
							readyqueue.add(process[i]);
						}
					}
				}

				if (rt[runningprocess.getPID() - 1] == 0) {// 남은시간이 0인 경우
					complete++;// 완료된 프로세스 증가
					continue;// 반복문 다시 시작
				}
				readyqueue.add(runningprocess);// 끝나지 않은 경우 레디큐 제일 뒤에 삽입
			}
			break;
		}
		for (i = 0; i < num; i++) {// 프로세스 별 응답시간 설정
			process[i].setResponsetime(process[i].findresponsetime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 반환시간 설정
			process[i].setTurnaroundtime(process[i].findturnaroundtime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 대기시간 설정
			process[i].setWaitingtime(process[i].findwaitingtime(process[i].execution));
		}
		PrintAfter(process, t);// 스케줄링 후 상태 출력
		System.out.println("전체 시간 = " + t);// 전체시간 출력
		PrintGanttchart(process, t);// 간트차트 출력
	}
	//수업 시간에 배우지 않고 새롭게 구현한 정책
	public static void Upgraded_RoundRobin() {// 라운드로빈 기반인데 남은시간이 타임슬라이스보다 작으면 계속 실행한다.
		Process[] process = CreateProcess();// 프로세스 생성
		System.out.println("time slice를 입력하세요.");
		Scanner sc = new Scanner(System.in);
		int time_slice = Integer.parseInt(sc.next());// time slice를 설정
		int num = process.length;// 프로세스의 개수 저장
		int i = 0, j = 0;// 반복문에 쓰일 변수

		PrintBefore(process);// 스케줄링 전 상태 표시

		int rt[] = new int[num];// 프로세스 별 남은시간을 저장하는 배열

		for (i = 0; i < num; i++)
			rt[i] = process[i].getBursttime();// 배열rt에 프로세스 별 실행시간을 초기값으로 설정

		int complete = 0;// 실행 종료된 프로세스의 개수
		int t = 0;// 현재 시간

		LinkedList<Process> readyqueue = new LinkedList<>();// 레디큐 생성

		while (true) {// 실행할 프로세스가 있을때까지 반복

			for (j = 0; j < num; j++) {
				if ((process[j].getArrivetime() == t) && (rt[j] > 0)) {// 프로세스의 도착시간이 현재 시간과 같은 경우
					readyqueue.add(process[j]);
					// check = true;
				}
			}
			if (readyqueue.isEmpty()) {
				t++;
				continue;
			}

			while (complete != num) {// 모든 프로세스가 종료될때까지 반복
				for (j = 0; j < num; j++) {
					if ((process[j].getArrivetime() == t) && (rt[j] > 0)) {// 현재시간에 도착한 프로세스를 레디큐에 넣는다.
						if (readyqueue.contains(process[j])) {// 레디큐에 있는 프로세스는 넣지 않는다.
							break;
						}
						readyqueue.add(process[j]);
					}
				}
				if (readyqueue.isEmpty()) {// 레디큐가 비었으면 현재 시간 증가
					t++;
					continue;
				}
				Process runningprocess = readyqueue.poll();// 레디큐에 있는 프로세스를 꺼내서 runningprocess에 저장
				for (j = 0; j < time_slice; j++) {// 실행할 프로세스가 있으면 time slice만큼 시간 증가
					if (rt[runningprocess.getPID() - 1] == 0)// time slice동안 실행하는 도중 실행이 완료되는 경우
						break;
					rt[runningprocess.getPID() - 1]--;// 남은 시간 감소
					process[runningprocess.getPID() - 1]
							.setExecution(process[runningprocess.getPID() - 1].getExecution(), t);// 간트차트에 쓰일 정보 저장
					t++;// 현재 시간 증가

					for (i = 0; i < num; i++) {// time slice 시간 안에 들어온 다른 프로세스가 있는 경우 레디큐에 삽입
						if ((process[i].getArrivetime() == t) && (rt[i] > 0)) {
							readyqueue.add(process[i]);
						}
					}
				}
				if (rt[runningprocess.getPID() - 1] < time_slice) {// 남은 시간이 time slice보다 작으면 남은 거 다 실행한다.
					for (j = 0; j < time_slice; j++) {
						if (rt[runningprocess.getPID() - 1] == 0)
							break;
						rt[runningprocess.getPID() - 1]--;// 남은 시간 감소
						process[runningprocess.getPID() - 1]
								.setExecution(process[runningprocess.getPID() - 1].getExecution(), t);// 간트차트에 쓰일 정보 저장
						t++;

						for (i = 0; i < num; i++) {// 실행 중에 들어온 다른 프로세스가 있는 경우 레디큐에 삽입
							if ((process[i].getArrivetime() == t) && (rt[i] > 0)) {
								readyqueue.add(process[i]);
							}
						}

					}
				}

				if (rt[runningprocess.getPID() - 1] == 0) {// 남은시간이 0인 경우
					complete++;// 완료된 프로세스 증가
					continue;// 반복문 다시 시작
				}
				readyqueue.add(runningprocess);// 끝나지 않은 경우 레디큐 제일 뒤에 삽입
			}
			break;
		}
		for (i = 0; i < num; i++) {// 프로세스 별 응답시간 설정
			process[i].setResponsetime(process[i].findresponsetime(process[i].execution));

		}
		for (i = 0; i < num; i++) {// 프로세스 별 반환시간 설정
			process[i].setTurnaroundtime(process[i].findturnaroundtime(process[i].execution));
		}
		for (i = 0; i < num; i++) {// 프로세스 별 대기시간 설정
			process[i].setWaitingtime(process[i].findwaitingtime(process[i].execution));
		}
		PrintAfter(process, t);// 스케줄링 후 상태 출력
		System.out.println("전체 시간 = " + t);// 전체시간 출력
		PrintGanttchart(process, t);// 간트차트 출력

	}

	public static Process[] CreateProcess() {
		System.out.println("파일명을 입력하세요");
		Scanner sc = new Scanner(System.in);
		Scanner path = null;
		try {
			path = new Scanner(new File(sc.next()));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			System.out.println("찾을 수 없는 파일입니다.");
			String error = sc.next();
		}
		ArrayList<String> content = new ArrayList<>();

		while (path.hasNext()) {// 파일 내용을 content에 단어 단위로 저장
			content.add(path.next());
		}
		int num = content.size() / 5;// 총 프로세스의 개수
		int i;// 반복문에 쓰이는 변수
		int expecttime = 0;// 예상시간
		int minarrivetime = 0;// 제일 빠른 도착시간
		for (i = 0; i < num; i++) {
			expecttime += Integer.parseInt(content.get(5 * i + 3));
		}
		for (i = 0; i < num; i++) {// 도착시간  저장
				minarrivetime = Integer.parseInt(content.get(5 * i + 2));
		}
		expecttime += minarrivetime ;// 실행시간을 다 더하고 제일 빠른 도착시간을 더함

		Process[] process = new Process[num];// Process객체를 num만큼 생성
		for (i = 0; i < process.length; i++) {// Process객체를 생성
			process[i] = new Process(expecttime);
		}
		for (i = 0; i < num; i++) {
			process[i].setPID(Integer.parseInt(content.get(5 * i + 1)));// 프로세스 이름 설정
			process[i].setArrivetime(Integer.parseInt(content.get(5 * i + 2)));// 도착시간 설정
			process[i].setBursttime(Integer.parseInt(content.get(5 * i + 3)));// 실행시간 설정
			process[i].setPriority(Integer.parseInt(content.get(5 * i + 4)));// 우선순위 설정
		}
		return process;
	}

	public static void PrintBefore(Process[] process) {
		System.out.println("PID\t도착시간\t실행시간\t우선순위");// 프로세스 PID, 도착시간, 실행시간, 우선순위를 출력
		for (int i = 0; i < process.length; i++) {
			System.out.println(process[i].getPID() + "\t" + process[i].getArrivetime() + "\t"
					+ process[i].getBursttime() + "\t" + process[i].getPriority());
		}
	}

	public static void PrintAfter(Process[] process, int time) {
		System.out.println("PID\t대기시간\t응답시간\t반환시간\tCPU utilization ratio");// 프로세스 PID, 대기시간, 응답시간, 반환시간,CPU utilization
																			// ratio을 출력
		for (int i = 0; i < process.length; i++) {
			double CPU_ratio = (double) process[i].getBursttime() / (double) time * 100;//CPU 사용률 double로 변환
			System.out.println(process[i].getPID() + "\t" + process[i].getWaitingtime() + "\t"
					+ process[i].getResponsetime() + "\t" + process[i].getTurnaroundtime() + "\t"
					+ process[i].getBursttime() + "/" + time + " = " + Math.round(CPU_ratio * 1000) / 1000.0 + "%");
		}
		double awt = 0, art = 0, atat = 0;//평균 시간들을 저장할 변수
		for (int i = 0; i < process.length; i++) {//평균 시간들을 구함
			awt += process[i].getWaitingtime();
			art += process[i].getResponsetime();
			atat += process[i].getTurnaroundtime();
		}
		awt /= process.length;
		art /= process.length;
		atat /= process.length;
		System.out.println("평균\t" + Math.round(awt * 1000) / 1000.0 + "\t" + Math.round(art * 1000) / 1000.0 + "\t"
				+ Math.round(atat * 1000) / 1000.0);// 평균 대기시간, 응답시간, 반환시간 출력
	}

	public static void PrintGanttchart(Process[] process, int t) {//간트 차트 출력
		for (int i = 0; i < process.length; i++) {
			System.out.print("p" + process[i].getPID() + "\t");
			for (int j = 0; j < t; j++) {
				if (process[i].execution[j] == 1 && j % 5 == 0) {// 가독성을 위해 5칸 단위로 끊어서 출력
					System.out.print(" ■");// 현재 j값에 1이면 ■ 출력
				} else if (process[i].execution[j] == 1) {
					System.out.print("■");
				} else if (process[i].execution[j] == 0 && j % 5 == 0)// 가독성을 위해 5칸 단위로 끊어서 출력
					System.out.print(" □");// 현재 j값에 0이면 □ 출력
				else
					System.out.print("□");
			}
			System.out.println();
		}
	}
}
