package android.app;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class StatusBarNotifyList implements Parcelable
{
   private List<StatusBarNotificationEntity> list = new ArrayList<StatusBarNotificationEntity>(); 
  
   
	public List<StatusBarNotificationEntity> getList() 
	{
		return list;
	}
	
	public void setList(List<StatusBarNotificationEntity> list) 
	{
		this.list = list;
	}
	public  StatusBarNotifyList() 
	    { 
		   list = new ArrayList<StatusBarNotificationEntity>(); 
	    } 

		public StatusBarNotifyList(Parcel in)
		{ 
			in.readTypedList(list, StatusBarNotificationEntity.CREATOR); 

		} 
		
	@Override
	public int describeContents() 
	{
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int arg1)
	{
		dest.writeTypedList(list);
	}
	
	
	public static final Parcelable.Creator<StatusBarNotifyList> CREATOR = new Parcelable.Creator<StatusBarNotifyList>() 
	{ 
		@Override 
		public StatusBarNotifyList createFromParcel(Parcel in) 
		{ 
			return new StatusBarNotifyList(in); 
		} 

		@Override 
		public StatusBarNotifyList[] newArray(int size)
		{ 
			return new StatusBarNotifyList[size]; 
		} 
	}; 


}
