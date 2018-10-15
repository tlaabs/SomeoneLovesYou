package com.slu.analyzer;

public class AnalyzeResult {
	private double avgValance;
	private double avgArousal;
	private String emotion;
	
	public AnalyzeResult(double avgValance, double avgArousal, String emotion){
		this.avgValance = avgValance;
		this.avgArousal = avgArousal;
		this.emotion = emotion;
	}
	public double getAvgValance() {
		return avgValance;
	}
	public void setAvgValance(double avgValance) {
		this.avgValance = avgValance;
	}
	public double getAvgArousal() {
		return avgArousal;
	}
	public void setAvgArousal(double avgArousal) {
		this.avgArousal = avgArousal;
	}
	public String getEmotion() {
		return emotion;
	}
	public void setEmotion(String emotion) {
		this.emotion = emotion;
	}
	
	

}
