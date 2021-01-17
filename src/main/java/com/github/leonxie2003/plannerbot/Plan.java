package com.github.leonxie2003.plannerbot;

import java.util.ArrayList;

public class Plan {
	private ArrayList<Task> tasks;
	
	public Plan() {
		tasks = new ArrayList<Task>();
	}
	
	public Plan(ArrayList<Task> t) {
		tasks = new ArrayList<Task>();
		for(int i = 0; i < t.size(); i++) {
			tasks.add(t.get(i));
		}
	}
	
	public void addTask(String name, double time) 
	{
		tasks.add(new Task(name, time));
	}
	
	public void removeTask(String name) {
		for(int i = tasks.size(); i >= 0; i--) {
			if(tasks.get(i).getName().equals(name)) {
				tasks.remove(i);
				return;
			}
		}
	}
	
	public void addBreak(double time) 
	{
		tasks.add(new Task("BREAK", time));
	}
	
	public Task getTask(int i) {
		return tasks.get(i);
	}
	
	public void reset() {
		tasks = new ArrayList<Task>();
	}
	
	public String toString() {
		if(tasks.size() == 0) {
			return "No plan yet! Add tasks and breaks!";
		}
		
		String toReturn = "";
		for(int i = 0; i < tasks.size(); i++) {
			toReturn += (i+1) + ") " + tasks.get(i).getName() + ", lasting " + tasks.get(i).getTime() + " minutes. \n";
		}
		return toReturn;
	}

	public int taskListSize() {
		return tasks.size();
	}
}
