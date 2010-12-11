package crescendo.base;

import javax.swing.JOptionPane;

public class ErrorHandler
{
	public static enum Response
	{
		OKAY,YES,NO,RETRY,FAIL
	}
	
	/** This should always return OKAY. **/
	public static Response showNotification(String title,String message)
	{
		JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE);
		return Response.OKAY;
	}
	
	/**
	 * Allows the user to choose between Yes and No.
	 * If the user doesn't choose either (closes the dialog), it will be shown again
	 * until one of the buttons is clicked.
	 */
	public static Response showYesNo(String title,String message)
	{
		int option;
		do
		{
			option=JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
		} while (option==JOptionPane.CLOSED_OPTION);
		if (option==JOptionPane.YES_OPTION)
		{
			return Response.YES;
		}
		else
		{
			return Response.NO;
		}
	}
	
	/**
	 * Allows the user to select whether or not the program should retry some action
	 * or to just close the application.
	 * 
	 * This method does not close the program; this should be done after cleanup is
	 * performed.
	 * 
	 * If the user closes the dialog, RETRY is returned.
	 */
	public static Response showRetryFail(String title,String message)
	{
		Object[] options={"Retry","Fail"};
		int option=JOptionPane.showOptionDialog(null,message,title,JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,options,options[0]);
		if (option==0)
		{
			return Response.RETRY;
		}
		else
		{
			return Response.FAIL;
		}
	}
}
