/*    */ package ch.qos.logback.core.net.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.ServerSocket;
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class RemoteReceiverServerListener
/*    */   extends ServerSocketListener<RemoteReceiverClient>
/*    */ {
/*    */   public RemoteReceiverServerListener(ServerSocket serverSocket) {
/* 34 */     super(serverSocket);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected RemoteReceiverClient createClient(String id, Socket socket) throws IOException {
/* 42 */     return new RemoteReceiverStreamClient(id, socket);
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\qos\logback\core\net\server\RemoteReceiverServerListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */