/*    */ package ch.jamiete.mcping;
/*    */ 
/*    */ import java.io.DataInputStream;
/*    */ import java.io.DataOutputStream;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MinecraftPingUtil
/*    */ {
/* 37 */   public static byte PACKET_HANDSHAKE = 0, PACKET_STATUSREQUEST = 0;
/* 38 */   public static byte PACKET_PING = 1;
/* 39 */   public static int PROTOCOL_VERSION = 4;
/* 40 */   public static int STATUS_HANDSHAKE = 1;
/*    */   
/*    */   public static void validate(Object o, String m) {
/* 43 */     if (o == null) {
/* 44 */       throw new RuntimeException(m);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void io(boolean b, String m) throws IOException {
/* 49 */     if (b) {
/* 50 */       throw new IOException(m);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int readVarInt(DataInputStream in) throws IOException {
/* 59 */     int k, i = 0;
/* 60 */     int j = 0;
/*    */     do {
/* 62 */       k = in.readByte();
/*    */       
/* 64 */       i |= (k & 0x7F) << j++ * 7;
/*    */       
/* 66 */       if (j > 5) {
/* 67 */         throw new RuntimeException("VarInt too big");
/*    */       }
/* 69 */     } while ((k & 0x80) == 128);
/*    */ 
/*    */ 
/*    */     
/* 73 */     return i;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
/*    */     while (true) {
/* 83 */       if ((paramInt & 0xFFFFFF80) == 0) {
/* 84 */         out.writeByte(paramInt);
/*    */         
/*    */         return;
/*    */       } 
/* 88 */       out.writeByte(paramInt & 0x7F | 0x80);
/* 89 */       paramInt >>>= 7;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\jamiete\mcping\MinecraftPingUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */