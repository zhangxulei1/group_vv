import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

public class Tcpservice {
	// ////////////////////////////////////////////////////////////////////////////////////
	// 服务器端口
	private static final int SERVERPORT = 12345;
	// 存储所有客户端Socket连接对象
	private static List<Socket> mClientList = new ArrayList<Socket>();
	// 线程池
	private ExecutorService mExecutorService;
	// ServerSocket对象
	private ServerSocket mServerSocket;

	// 开启服务器
	public static void main(String[] args) {
		new Tcpservice();
	}

	//
	public Tcpservice() {
		try {
			// 设置服务器端口
			mServerSocket = new ServerSocket(12345);
			// 创建一个线程池
			mExecutorService = Executors.newCachedThreadPool();
			System.out.println("start...");
			// 用来临时保存客户端连接的Socket对象
			Socket client = null;
			while (true) {
				// 接收客户连接并添加到list中
				client = mServerSocket.accept();
				System.out.println("ServerListener run while");
				JOptionPane.showMessageDialog(null, "有客户端连接到了本机的12345端口");

				mClientList.add(client);
				//长链接
				client.setKeepAlive(true);
				// 开启一个客户端线程
				mExecutorService.execute(new ThreadServer(client));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 每个客户端单独开启一个线程
	static class ThreadServer implements Runnable {
		private Socket mSocket;
		private BufferedReader mBufferedReader;
		private PrintWriter mPrintWriter;
		private String mStrMSG;
		int s=0;
		public ThreadServer(Socket socket) throws IOException {
			this.mSocket = socket;
			mBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			s=mClientList.size();
			//mStrMSG = "欢迎用户" + s+ " 来到聊天室！"  ;
			sendMessage();
		}

		public void run() {
			try {
				while ((mStrMSG = mBufferedReader.readLine()) != null) {
					if (mStrMSG.trim().equals("exit")) {
						// 当一个客户端退出时
						mClientList.remove(mSocket);
						mBufferedReader.close();
						mPrintWriter.close();
						mStrMSG = this.mSocket.getInetAddress()+"user:" + "\n" + " exit total:" + mClientList.size();
						mSocket.close();
						sendMessage();
						break;
					} else {
						mStrMSG = "用户"+s + ":" +""+ mStrMSG;
						sendMessage();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 发送消息给所有客户端
		private void sendMessage() throws IOException {
			System.out.println(mStrMSG);

			for (Socket client : mClientList) {
				//改动
				if (!mSocket.equals(client)) {

					mPrintWriter = new PrintWriter(client.getOutputStream(), true);
					mPrintWriter.println(mStrMSG);
					
					

				}
			}
		}
	}
}