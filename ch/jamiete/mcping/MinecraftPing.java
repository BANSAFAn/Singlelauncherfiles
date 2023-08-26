/*     */ package ch.jamiete.mcping;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import org.xbill.DNS.Lookup;
/*     */ import org.xbill.DNS.Record;
/*     */ import org.xbill.DNS.SRVRecord;
/*     */ import org.xbill.DNS.TextParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MinecraftPing
/*     */ {
/*     */   public MinecraftPingReply getPing(String hostname) throws IOException {
/*  51 */     return getPing((new MinecraftPingOptions()).setHostname(hostname));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MinecraftPingReply getPing(MinecraftPingOptions options) throws IOException {
/*  60 */     MinecraftPingUtil.validate(options.getHostname(), "Hostname cannot be null.");
/*  61 */     MinecraftPingUtil.validate(Integer.valueOf(options.getPort()), "Port cannot be null.");
/*     */     
/*  63 */     try (Socket socket = new Socket()) {
/*  64 */       socket.connect(new InetSocketAddress(options.getHostname(), options.getPort()), options.getTimeout());
/*     */       
/*  66 */       DataInputStream in = new DataInputStream(socket.getInputStream());
/*  67 */       DataOutputStream out = new DataOutputStream(socket.getOutputStream());
/*     */ 
/*     */ 
/*     */       
/*  71 */       ByteArrayOutputStream handshake_bytes = new ByteArrayOutputStream();
/*  72 */       DataOutputStream handshake = new DataOutputStream(handshake_bytes);
/*     */       
/*  74 */       handshake.writeByte(MinecraftPingUtil.PACKET_HANDSHAKE);
/*  75 */       MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.PROTOCOL_VERSION);
/*  76 */       MinecraftPingUtil.writeVarInt(handshake, options.getHostname().length());
/*  77 */       handshake.writeBytes(options.getHostname());
/*  78 */       handshake.writeShort(options.getPort());
/*  79 */       MinecraftPingUtil.writeVarInt(handshake, MinecraftPingUtil.STATUS_HANDSHAKE);
/*     */       
/*  81 */       MinecraftPingUtil.writeVarInt(out, handshake_bytes.size());
/*  82 */       out.write(handshake_bytes.toByteArray());
/*     */ 
/*     */ 
/*     */       
/*  86 */       out.writeByte(1);
/*  87 */       out.writeByte(MinecraftPingUtil.PACKET_STATUSREQUEST);
/*     */ 
/*     */ 
/*     */       
/*  91 */       MinecraftPingUtil.readVarInt(in);
/*  92 */       int id = MinecraftPingUtil.readVarInt(in);
/*     */       
/*  94 */       MinecraftPingUtil.io((id == -1), "Server prematurely ended stream.");
/*  95 */       MinecraftPingUtil.io((id != MinecraftPingUtil.PACKET_STATUSREQUEST), "Server returned invalid packet.");
/*     */       
/*  97 */       int length = MinecraftPingUtil.readVarInt(in);
/*  98 */       MinecraftPingUtil.io((length == -1), "Server prematurely ended stream.");
/*  99 */       MinecraftPingUtil.io((length == 0), "Server returned unexpected value.");
/*     */       
/* 101 */       byte[] data = new byte[length];
/* 102 */       in.readFully(data);
/* 103 */       String json = new String(data, options.getCharset());
/*     */ 
/*     */ 
/*     */       
/* 107 */       out.writeByte(9);
/* 108 */       out.writeByte(MinecraftPingUtil.PACKET_PING);
/* 109 */       out.writeLong(System.currentTimeMillis());
/*     */ 
/*     */ 
/*     */       
/* 113 */       MinecraftPingUtil.readVarInt(in);
/* 114 */       id = MinecraftPingUtil.readVarInt(in);
/* 115 */       MinecraftPingUtil.io((id == -1), "Server prematurely ended stream.");
/* 116 */       MinecraftPingUtil.io((id != MinecraftPingUtil.PACKET_PING), "Server returned invalid packet.");
/*     */ 
/*     */ 
/*     */       
/* 120 */       handshake.close();
/* 121 */       handshake_bytes.close();
/* 122 */       out.close();
/* 123 */       in.close();
/* 124 */       return (MinecraftPingReply)(new Gson()).fromJson(json, MinecraftPingReply.class);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resolveDNS(MinecraftPingOptions options) throws TextParseException {
/* 129 */     String service = "_minecraft._tcp." + (new InetSocketAddress(options.getHostname(), options.getPort())).getHostName();
/* 130 */     Record[] records = (new Lookup(service, 33)).run();
/* 131 */     if (records != null)
/* 132 */       for (Record record : records) {
/* 133 */         SRVRecord srv = (SRVRecord)record;
/* 134 */         String hostname = srv.getTarget().toString();
/* 135 */         int port = srv.getPort();
/* 136 */         options.setHostname(hostname);
/* 137 */         options.setPort(port);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\vovas\Downloads\TLauncher-2.885.jar!\ch\jamiete\mcping\MinecraftPing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */