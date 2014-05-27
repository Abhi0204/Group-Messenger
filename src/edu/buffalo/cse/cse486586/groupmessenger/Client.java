package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;

public class Client extends AsyncTask<String,Void,Void> {

	public static final int sequencer=11108;
	@Override
	protected Void doInBackground(String... msgs) {
		try {

			/*
			 * TODO: Fill in your client code that sends out a message.
			 */


			Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),sequencer);

			String msgToSend = msgs[0];		

			message msg=new message(null,msgToSend);
			ObjectOutputStream objectStremOutput =new ObjectOutputStream(socket.getOutputStream());
			objectStremOutput.writeObject(msg);
			socket.close();
//			objectStremOutput.reset();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}

