package crescendo.base;

import org.junit.Assert;

import org.junit.Test;

public class ErrorHandlerTest
{
	@Test
	public void showNotication()
	{
		Assert.assertEquals(ErrorHandler.showNotification("I'm a thing","It really doesn't matter what you do here"),ErrorHandler.OKAY);
	}
	
	@Test
	public void yes()
	{
		Assert.assertEquals(ErrorHandler.showYesNo("Please","Click yes"),ErrorHandler.YES);
	}
	
	@Test
	public void no()
	{
		Assert.assertEquals(ErrorHandler.showYesNo("Please","Click no"),ErrorHandler.NO);
	}
	
	@Test
	public void retryClick()
	{
		Assert.assertEquals(ErrorHandler.showRetryFail("Please","Click retry"),ErrorHandler.RETRY);
	}
	
	@Test
	public void close()
	{
		Assert.assertEquals(ErrorHandler.showRetryFail("Please","Click close"),ErrorHandler.FAIL);
	}
	
	@Test
	public void retryClose()
	{
		Assert.assertEquals(ErrorHandler.showRetryFail("Please","Close the dialog"),ErrorHandler.RETRY);
	}
}
