package testing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
/*
 * File:	FCFS.java 
 * Course: 	Operating Systems
 * Code: 	1DV512
 * Author: 	Suejb Memeti
 * Date: 	November, 2016
 */
public class FCFS extends Thread {

    // The list of processes to be scheduled
    public static ArrayList<Process> processes;
    public static Process amr;
    public static int numberOfProcess;
    // Class constructor

    public FCFS(ArrayList<Process> processes) {
        this.processes = processes;
        numberOfProcess = processes.size();
       for(int i = 0 ; i < numberOfProcess ; i++)
       {
           processes.get(i).setCompletedTime(0);
           processes.get(i).setTurnaroundTime(0);
            processes.get(i).setWaitingTime(0);
        }
    }

    public void run() {
        // TODO Implement the FCFS algorithm here
        //numberOfProcess = processes.size();
        //Sort Process
        int firstCome, tempArrivalTime, tempBurst;
        for (int i = 0; i < (numberOfProcess - 1); i++) {
            for (int j = (i + 1); j < numberOfProcess; j++) {
                if (processes.get(j).getArrivalTime() < processes.get(i).getArrivalTime()) {

                    firstCome = processes.get(j).getProcessId();
                    processes.get(j).processId = processes.get(i).getProcessId();
                    processes.get(i).processId = firstCome;
                    tempArrivalTime = processes.get(j).getArrivalTime();
                    processes.get(j).arrivalTime = processes.get(i).getArrivalTime();
                    processes.get(i).arrivalTime = tempArrivalTime;
                    tempBurst = processes.get(j).getBurstTime();
                    processes.get(j).burstTime = processes.get(i).getBurstTime();
                    processes.get(i).burstTime = tempBurst;
                }

            }

        }
        // calculating completed time 
        for (int i = 0; i < numberOfProcess; i++) {
        if(i == 0)
        {
        processes.get(i).completedTime += processes.get(i).getBurstTime();
        	            	}
        else if(processes.get(i).arrivalTime>processes.get(i-1).completedTime){
         processes.get(i).completedTime=processes.get(i).getBurstTime()+processes.get(i).getArrivalTime();
        }
        else{
        processes.get(i).completedTime = processes.get(i-1).completedTime + processes.get(i).getBurstTime();
        }
       }
       
        
        
        //Calculate CompletedTime
     //   for (int i = 0; i < numberOfProcess; i++) {
   //         if(i > 0)
    //        {
 //   processes.get(i).completedTime = processes.get(i-1).completedTime + processes.get(i).getBurstTime();}
            
    //        else{processes.get(i).completedTime += processes.get(i).getBurstTime();}
     //   }
        //Calculate TurnarroundTime
        for (int i = 0; i < numberOfProcess; i++) {
            processes.get(i).turnaroundTime = processes.get(i).getCompletedTime() - processes.get(i).getArrivalTime();
        }
        //Calculate WaitingTime
        for (int i = 0; i < numberOfProcess; i++) {
            processes.get(i).waitingTime = processes.get(i).getTurnaroundTime() - processes.get(i).getBurstTime();
        }
        printTable();
        printGanttChart();
    }

    public void printTable() {
        // TODO Print the list of processes in form of a table here
        System.out.print("\n===============Scheduling a List of Processes=================\n\n");
        System.out.print("Process Id | Arrival Time | Burst Time | Completed Time | Turnaround Time | Waiting Value\n");

        for (int i = 0; i < numberOfProcess; i++) {
            System.out.print(processes.get(i).getProcessId() + "    |    " + processes.get(i).getArrivalTime() + "    |    " + processes.get(i).getBurstTime() + "    |    "
                    + processes.get(i).getCompletedTime() + "    |    " + processes.get(i).getTurnaroundTime() + "    |    " + processes.get(i).getWaitingTime() + "\n");
        }
        System.out.print("\n============================ End =============================\n\n");
    }

    public void printGanttChart() {
        // TODO Print the demonstration of the scheduling algorithm using Gantt Chart
        System.out.print("\n======================== Gantt Chart =========================\n\n");
        System.out.print("_______________________________________________________________\n");
        String s = "|0  ";
        for (int i = 0; i < numberOfProcess; i++) {
            s += "process" + processes.get(i).getProcessId() + "  |" + processes.get(i).getCompletedTime() + "   ";
        }
        System.out.print(s + "\n");
        System.out.print("________________________________________________________________\n\n");
        System.out.print("\n============================= End ============================\n\n");
    }

    public static void main(String[] args) {
        
        processes = new ArrayList<Process>();
        System.out.println("===================== First Come First Serve (FCFS) =======================\n ");
        Scanner scan = new Scanner(System.in);
        try {
            System.out.print("Please enter the number of Process --> ");
            numberOfProcess = scan.nextInt();
            for (int i = 0; i < numberOfProcess; i++) {
                Process amr = new Process();
                amr.processId = i + 1;
                System.out.print("Enter Arrival time of Process " + (i + 1) + "  --> ");
                amr.arrivalTime = scan.nextInt();
                System.out.print("Enter Burst time of Process " + (i + 1) + "  --> ");
                amr.burstTime = scan.nextInt();
                processes.add(amr);
              
            }
        } catch (Exception e) {
            System.out.println("Wrong Input Format");
        }
        
        FCFS myFcfs = new FCFS(processes);
        myFcfs.start();
        scan.close();
    }
}