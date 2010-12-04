package crescendo.base;

import javax.swing.JOptionPane;

public class ErrorHandler
{
	public static final int OKAY=1;
	public static final int YES=1;
	public static final int NO=2;
	public static final int RETRY=3;
	public static final int FAIL=4;
	
	/** This should always return OKAY. **/
	public static int showNotification(String title,String message)
	{
		JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE);
		return OKAY;
	}
	
	/**
	 * Allows the user to choose between Yes and No.
	 * If the user doesn't choose either (closes the dialog), it will be shown again
	 * until one of the buttons is clicked.
	 */
	public static int showYesNo(String title,String message)
	{
		System.out.println(JOptionPane.CLOSED_OPTION);
		System.out.println(JOptionPane.YES_OPTION);
		int option;
		do
		{
			option=JOptionPane.showConfirmDialog(null,message,title,JOptionPane.YES_NO_OPTION);
		} while (option==JOptionPane.CLOSED_OPTION);
		if (option==JOptionPane.YES_OPTION)
		{
			return YES;
		}
		else
		{
			return NO;
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
	public static int showRetryFail(String title,String message)
	{
		Object[] options={"Retry","Exit"};
		int option=JOptionPane.showOptionDialog(null,message,title,JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE,null,options,options[0]);
		if (option!=1)
		{
			return RETRY;
		}
		else
		{
			return FAIL;
		}
	}
}
