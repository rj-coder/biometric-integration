//package in.westerncoal.biometric.config;
//
//import jakarta.websocket.CloseReason;
//import jakarta.websocket.EndpointConfig;
//import jakarta.websocket.OnClose;
//import jakarta.websocket.OnError;
//import jakarta.websocket.OnMessage;
//import jakarta.websocket.OnOpen;
//import jakarta.websocket.Session;
//import jakarta.websocket.server.ServerEndpoint;
//
//@ServerEndpoint("")
//public class BiometricDeviceWSEndPoint {
//    private Session session;
//
//	@OnOpen
//	public void onOpen(Session session, EndpointConfig ec) {
//			System.out.println("Session Opened: "+session);
//	}
//
//	@OnMessage
//	public void OnMessage(String messageFromClient) {
//	}
//
//	@OnClose
//	public void onClose(Session session, CloseReason cr) {
//	}
//
//	@OnError
//	public void onError(Session session, Throwable t) {
//	}
//
//}
