//package in.westerncoal.biometric.config;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig1 implements WebSocketMessageBrokerConfigurer {
//
//	@Autowired
//	HandshakeInterceptor handShakeInterceptor;
//	
//	@Override
//	public void configureMessageBroker(MessageBrokerRegistry config) {
//		config.enableSimpleBroker("/");
//		config.setApplicationDestinationPrefixes("");
//	}
//
//	@Override
//	public void registerStompEndpoints(StompEndpointRegistry registry) {
//		registry.addEndpoint("/").setAllowedOriginPatterns("*").addInterceptors(handShakeInterceptor);
//		//.withSockJS();
//		
//		//	registry.addEndpoint("").setAllowedOriginPatterns("*").withSockJS();
//	}
//}
