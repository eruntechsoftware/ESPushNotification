package com.eruntech.espushnotification.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/***
 * 用户数据存储
 *
 * @author 杜明悦
 *
 */
public class UserData
{
	private static SharedPreferences sharedPreferences = null;

	public UserData ( Context context )
	{
		if(sharedPreferences == null)
		{
			sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
		}
	}

	/***
	 * 写入共享数据
	 *
	 * @param name 数据名称
	 * @param value 值
	 * @throws Exception
	 */
	public void put(String name, String value)
	{
		try
		{
			if(sharedPreferences != null)
			{
				Editor edit = sharedPreferences.edit();
				edit.putString(name, value);
				edit.commit();
			}
		}
		catch(Exception ex)
		{

		}
	}

	/***
	 * 写入共享数据
	 *
	 * @param name 数据名称
	 * @param value 值
	 * @throws Exception
	 */
	public void put(String name, Set<String> value)
	{
		try
		{
			if(sharedPreferences != null)
			{
				Editor edit = sharedPreferences.edit();
				edit.putStringSet(name, value);
				edit.commit();
			}
		}
		catch(Exception ex)
		{
		}
	}

	/***
	 * 写入共享数据
	 *
	 * @param name 数据名称
	 * @param value 值
	 * @throws Exception
	 */
	public void put(String name, Boolean value)
	{
		try
		{
			if(sharedPreferences != null)
			{
				Editor edit = sharedPreferences.edit();
				edit.putBoolean(name, value);
				edit.commit();
			}
		}
		catch(Exception ex)
		{

		}
	}

	/***
	 * 写入共享数据
	 *
	 * @param name 数据名称
	 * @param value 值
	 * @throws Exception
	 */
	public void put(String name, int value)
	{
		try
		{
			if(sharedPreferences != null)
			{
				Editor edit = sharedPreferences.edit();
				edit.putInt(name, value);
				edit.commit();
			}
		}
		catch(Exception ex)
		{
		}
	}


	/***
	 * 写入共享数据
	 *
	 * @param name 数据名称
	 * @param value 值
	 * @throws Exception
	 */
	public void put(String name, long value)
	{
		try
		{
			if(sharedPreferences != null)
			{
				Editor edit = sharedPreferences.edit();
				edit.putLong(name, value);
				edit.commit();
			}
		}
		catch(Exception ex)
		{

		}
	}



	/***
	 * 获取共享数据
	 *
	 * @param name 数据名称
	 * @return 共享数据
	 * @throws Exception
	 */
	public String getString(String name)
	{
		try
		{
			if(sharedPreferences != null) { return sharedPreferences.getString(name, ""); }
		}
		catch(Exception ex)
		{

		}
		return null;
	}

	/***
	 * 获取共享数据
	 *
	 * @param name 数据名称
	 * @return 共享数据
	 * @throws Exception
	 */
	public Set<String> getStringSet(String name)
	{
		try
		{
			if(sharedPreferences != null) { return sharedPreferences.getStringSet(name,null); }
		}
		catch(Exception ex)
		{

		}
		return null;
	}

	/***
	 * 获取共享数据
	 *
	 * @param name 数据名称
	 * @return 共享数据
	 * @throws Exception
	 */
	public Boolean getBoolean(String name)
	{
		try
		{
			if(sharedPreferences != null) { return sharedPreferences.getBoolean(name, false); }
		}
		catch(Exception ex)
		{
		}
		return false;
	}

	/***
	 * 获取共享数据
	 *
	 * @param name 数据名称
	 * @return 共享数据
	 * @throws Exception
	 */
	public int getInt(String name)
	{
		try
		{
			if(sharedPreferences != null) { return sharedPreferences.getInt(name, -1); }
		}
		catch(Exception ex)
		{

		}
		return -1;
	}

	/***
	 * 获取共享数据
	 *
	 * @param name 数据名称
	 * @return 共享数据
	 * @throws Exception
	 */
	public long getLong(String name)
	{
		try
		{
			if(sharedPreferences != null) { return sharedPreferences.getLong(name, -1); }
		}
		catch(Exception ex)
		{

		}
		return 0;
	}
}
