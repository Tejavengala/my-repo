package com.ResourceListHaqwi;

import java.io.*;

public class GetNodes {
	
	public static void main(String[] args) throws Exception {

		String commandValue = "mqsilist Broker1";
		System.out.println(executeCommandTwo(commandValue));
	}
	
	public static String executeCommandTwo(String commandValue) {
		String outputCommand ="";
		try {

            Process process = Runtime.getRuntime().exec(commandValue);
  
            StringBuilder output = new StringBuilder();
  
            BufferedReader reader
                = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
  
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
  
            int exitVal = process.waitFor();
            if (exitVal == 0) {

            	outputCommand= output.toString();

            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
		return outputCommand;
    }
		
	
    public static String getNodeList() {
        StringBuilder jsonOutput = new StringBuilder();
        jsonOutput.append("[\n");

        String nodesList = executeCommandone("mqsilist");
        boolean firstNode = true;

        for (String nodeLine : nodesList.split("\n")) {
            if (nodeLine.contains("Integration node") || nodeLine.contains("IntegrationServer")) {
                if (!firstNode) jsonOutput.append(",\n");
                firstNode = false;

                String nodeName = extractNodeName(nodeLine);
                String nodeStatus = extractNodeStatus(nodeLine);

                jsonOutput.append("  {\n    \"nodeName\": \"").append(nodeName)
                          .append("\",\n    \"status\": \"").append(nodeStatus)
                          .append("\"\n  }");
            }
        }

        jsonOutput.append("\n]}");
        return jsonOutput.toString();
    }

    private static String executeCommandone(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return output.toString().trim();
    }

    private static String extractNodeName(String line) {
        String[] parts = line.split("'");
        return (parts.length > 1) ? parts[1].trim() : "Unknown";
    }

    private static String extractNodeStatus(String line) {
        return line.contains("running") ? "running" : "stopped";
    }

}







