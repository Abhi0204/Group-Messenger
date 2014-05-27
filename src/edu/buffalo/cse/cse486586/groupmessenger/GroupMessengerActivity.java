package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

/**
 * GroupMessengerActivity is the main Activity for the assignment.
 * 
 * @author stevko
 *
 */
public class GroupMessengerActivity extends Activity {

	static final String TAG = GroupMessengerActivity.class.getSimpleName();
    private Uri mri;
    private ContentResolver ContentResolver;
	String myPort;


	public static TextView tv;
	static Integer counter=-1;
	//private String ip_addr = "10.0.2.2";
	static final int SERVER_PORT = 10000;
	
	
	private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }
	protected void onCreate(Bundle savedInstanceState) {

        mri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger.provider");
       ContentResolver =getContentResolver();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_messenger);

		    myPort=getPortNo(); 
		/*
		 * TODO: Use the TextView to display your messages. Though there is no grading component
		 * on how you display the messages, if you implement it, it'll make your debugging easier.
		 */
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setMovementMethod(new ScrollingMovementMethod());

		try {

			ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
			new Server().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
		} catch (IOException e) {

			Log.e(TAG, "Can't create a ServerSocket");
			return;
		}

		final EditText editText = (EditText)findViewById(R.id.editText1);

		/*
		 * Registers OnPTestClickListener for "button1" in the layout, which is the "PTest" button.
		 * OnPTestClickListener demonstrates how to access a ContentProvider.
		 */
		findViewById(R.id.button1).setOnClickListener(
				new OnPTestClickListener(tv, getContentResolver()));

		findViewById(R.id.button4).setOnClickListener(
				new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						String msg = editText.getText().toString() + "\n";

						editText.setText("");

						new Client().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR,msg,myPort);
						//System.out.println("back to MAIN for next input after OnClick ");
					}

				});


		/*
		 * TODO: You need to register and implement an OnClickListener for the "Send" button.
		 * In your implementation you need to get the message from the input box (EditText)
		 * and send it to other AVDs in a total-causal order.
		 */


		/*
		 * Log is a good way to debug your code. LogCat prints out all the messages that
		 * Log class writes.
		 * 
		 * Please read http://developer.android.com/tools/debugging/debugging-projects.html
		 * and http://developer.android.com/tools/debugging/debugging-log.html
		 * for more information on debugging.
		 */
		return;
	}

public String getPortNo()
{
	TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
	String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
     myPort = String.valueOf((Integer.parseInt(portStr) * 2));   
     return myPort;
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
		return true;
	}

	

	public class Server extends AsyncTask<ServerSocket,String,Void>
	{

		@Override
		protected Void doInBackground(ServerSocket... params) {
			try {

				ServerSocket serverSocket = params[0];
				ArrayList<String> arr=new ArrayList<String>();
				//arr.add("11108");
				arr.add("11112");

				arr.add("11116");
				arr.add("11120");
				arr.add("11124");


				while(true) {
					Socket socket= serverSocket.accept();
					ObjectInputStream oistream =new ObjectInputStream(socket.getInputStream());
					message obj=(message)oistream.readObject();
//					oistream.reset();
					if(obj.getKey()==null )//&& myPort=="11108")
					{

						counter++;
						obj.setKey(counter.toString());
						@SuppressWarnings("rawtypes")
						Iterator iterator=arr.iterator();
						while(iterator.hasNext())
						{
							message tmsg = obj;
							Socket msocket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),Integer.parseInt(iterator.next().toString()));
							ObjectOutputStream objectStremOutput =new ObjectOutputStream(msocket.getOutputStream());
							objectStremOutput.writeObject(tmsg);
//							objectStremOutput.reset();
  				        	msocket.close();
						}

					}
					Log.e(obj.getKey(),obj.getValue() + " " + myPort);
			        ContentValues cv = new ContentValues();
			        cv.put( "key",obj.getKey());
		            cv.put("value",obj.getValue());
	                ContentResolver.insert(mri, cv);

	                String key=obj.getKey()+" "+obj.getValue();
                    publishProgress(key);           
				}


			}
			catch (ClassNotFoundException e) {
				Log.e(TAG, e.getMessage());
			} 
			catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 



			return null;
			// TODO Auto-generated method stub


		}

		protected void onProgressUpdate(String...strings) {
			/*
			 * The following code displays what is received in doInBackground().
			 */
			String strReceived = strings[0].trim();
			TextView remoteTextView = (TextView) findViewById(R.id.textView1);
			remoteTextView.append(strReceived + "\t\n");

			/*
			 * The following code creates a file in the AVD's internal storage and stores a file.
			 * 
			 * For more information on file I/O on Android, please take a look at
			 * http://developer.android.com/training/basics/data-storage/files.html
			 */
			return;
		}
	}



}
