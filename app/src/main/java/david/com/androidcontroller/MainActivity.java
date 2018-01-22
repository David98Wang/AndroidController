package david.com.androidcontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;
import java.util.TimerTask;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity {

	private static final String LOGTAG = "MAINACTIVITY";

	private static final String URL = "ws://192.168.1.105:30303/Controller";

	private WebSocketClient webSocketClient;
	private URI uri;
	boolean isPressed[] = new boolean[4];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		boolean setupSuccess = initializeWebSocket();
		if(!setupSuccess)
			return;
		Button left = (Button)findViewById(R.id.btnLeft);
		Button right = (Button)findViewById(R.id.btnRight);
		Button down =  (Button)findViewById(R.id.btnDown);
		Button up = (Button)findViewById(R.id.btnUp);
		up.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webSocketClient.send("0");
				Log.d(LOGTAG,"Up Pressed");
			}
		});
		down.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webSocketClient.send("1");
				Log.d(LOGTAG,"Down Pressed");
			}
		});
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webSocketClient.send("2");
				Log.d(LOGTAG,"Left Pressed");
			}
		});

		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webSocketClient.send("3");
				Log.d(LOGTAG,"Right Pressed");
			}
		});



		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			@Override
			public void run() {
				int message = 0;
				for(int i=0;i<4;i++)
					if(isPressed[i]){
						message = i;
						isPressed[i] = false;
					}
				webSocketClient.send(String.valueOf(message));
			}
		};
		t.schedule(tt,10);
	}

	private boolean initializeWebSocket(){
		try{
			uri = new URI(URL);
		}catch (URISyntaxException e){
			Log.e(LOGTAG,"URL syntax error: " + e.getMessage());
			return false;
		}
		webSocketClient = new WebSocketClient(uri) {
			@Override
			public void onOpen() {
				System.out.println("onOpen");
				//webSocketClient.send("Hello, World!");
			}

			@Override
			public void onTextReceived(String message) {
				System.out.println("onTextReceived");
			}

			@Override
			public void onBinaryReceived(byte[] data) {
				System.out.println("onBinaryReceived");
			}

			@Override
			public void onPingReceived(byte[] data) {
				System.out.println("onPingReceived");
			}

			@Override
			public void onPongReceived(byte[] data) {
				System.out.println("onPongReceived");
			}

			@Override
			public void onException(Exception e) {
				System.out.println("ASDFASDFASDF"+ e.getMessage());
			}

			@Override
			public void onCloseReceived() {
				System.out.println("onCloseReceived");
			}
		};

		webSocketClient.setConnectTimeout(10000);
		webSocketClient.setReadTimeout(60000);
		webSocketClient.addHeader("Origin", "http://developer.example.com");
		webSocketClient.enableAutomaticReconnection(5000);
		System.out.println("ASDFASDFASDF");
		webSocketClient.connect();


		return true;
	}
}
