package com.helio.boomer.rap.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.helio.boomer.rap.service.CallMailService;

/*
 @Date   : 17-Aug-2012
 @Author : RSystems International Ltd
 @purpose: Creating a new Handler class for Request Support items,which is added to Help menu in application
 @Task   : RMAP-87
 */

public class RequestSupportBoomerHandler extends AbstractHandler {

	public static String ID = "com.helio.boomer.rap.command.requestsupportBoomer";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		CallMailService callMailService = new CallMailService();

		callMailService.callMail();

		return null;
	}

}
