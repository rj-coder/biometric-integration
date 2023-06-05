//package in.westerncoal.biometric.config;
//import java.util.Map;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import jakarta.servlet.http.HttpSession;
//
//@Configuration
//public class HttpHandshakeInterceptor implements HandshakeInterceptor {
//
//	@Override
//	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//			Map attributes) throws Exception {
//		if (request instanceof ServletServerHttpRequest) {
//			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
//			HttpSession session = servletRequest.getServletRequest().getSession();
//			attributes.put("sessionId", session.getId());
//			System.out.println(attributes);
//			System.out.println("Handshake started");
//		}
//		return true;
//	}
//
//	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//			Exception ex) {
//		System.out.println("Handshake Completed");
//	}
//}