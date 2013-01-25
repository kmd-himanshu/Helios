package com.helio.app.boomer.common.dal;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ImportHistory {

	private static Log LOG = LogFactory.getLog("ImportHistory");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//String logFileName = "/Users/Shared/dev/hes/csv" + "/" + "ElMonteSubSdata_1142_2010-10-01_2010-10-31 23_59_59.csv";
//		String[] files = dirlist(args[0]);
		String deviceMonitorId = args[1];
		Float pPulseConstant = Float.valueOf(args[2]);
		String[] pFieldNames = { args[3],args[4] };
		LOG.info("*********BEG***********" + args[0] + "***********BEG**********");
		(new ImportHistory()).persistLogs(args[0], deviceMonitorId, pFieldNames, pPulseConstant);
		LOG.info("*********END***********" + args[0] + "***********END**********");
//		for (int i = 0; i < files.length; i++) {
//			LOG.info("*********BEG***********" + files[i] + "***********BEG**********");
//			(new ImportHistory()).persistLogs(args[0]+"/"+files[i], deviceMonitorId, pFieldNames, pPulseConstant);
//			LOG.info("*********END***********" + files[i] + "***********END**********");
//		}
		System.exit(0);

		//;
	}
	
//	private static String[] dirlist(String pDirName)
//	{
//		File dir = new File(pDirName);
//		FilenameFilter filter = new FilenameFilter() {
//		    public boolean accept(File dir, String name) {
//		        return name.endsWith(".csv");
//		    }
//		};
//
//		String[] files = dir.list(filter);
//		return files;
//	}
	private synchronized boolean persistLogs(
			String pLogFileName, 
			String pDeviceMonitorId,
			String[] pFieldNames, 
			Float pPulseConstant) {
		int count = 0, errors = 0;
		try{
			FileInputStream fstream = new FileInputStream(pLogFileName);
			DataInputStream in = new DataInputStream(fstream);
			InputStreamReader ir = new InputStreamReader(in);
			BufferedReader br = new BufferedReader(ir);
			String logEntry; 
			while ((logEntry = br.readLine()) != null)   { // Always put serial number as first token
				count++;
				String wsReturn = DataAcquisitionDAO.getInstance().saveDeckLogEntry(pDeviceMonitorId + "," + logEntry,pFieldNames, pPulseConstant);
				String daoInstance = wsReturn.substring(7);
				if (wsReturn.startsWith("FAILURE")) {
					LOG.error("ERROR: " + pLogFileName + daoInstance  + ": when persisting log entry: " + pDeviceMonitorId + "," + logEntry);
					errors++;
				}
				else {
					LOG.debug("SAVED: " + pLogFileName + daoInstance +": " + pDeviceMonitorId + "," + logEntry);
				}
			}
			br.close();
			ir.close();
			in.close();
			fstream.close();
		}
		catch (Exception e) { //Catch exception if any
			 LOG.error("ERROR: " + pLogFileName + ": persistLogs - message: " + e.getMessage());
		}
		if (errors > 0) {
			LOG.error("WARNING: " + pLogFileName + ": Of the " + count + " log entries " + errors + " had errors.");
		}
		 
		return true;
	}

}
