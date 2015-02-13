package tank.meme.core.net.socket.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import tank.meme.core.net.socket.Protocol;
import tank.meme.utils.EncryptUtils;
import tank.meme.utils.GzipUtils;
import tank.meme.utils.JsonUtils;

/**
 * 数据通信编码
 * 
 * @author tank
 * @version 1.0
 * @date Nov 5, 2012 5:17:46 PM
 */
@Component
public class JsonEncoder implements ProtocolEncoder {
	 
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonEncoder.class);
	
	public JsonEncoder() {}
	
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (!session.isConnected()){
			LOGGER.error("session已经关闭不能再发消息");
			return;
		}
		
		if (message != null) {
			String responseJson = null;
			if (!(message instanceof String)) {
				responseJson = JsonUtils.toJson(message);
			} else {
				responseJson = (String) message;
			}
			
			IoBuffer buffer;
			
			// byte bytes[] = compress(session, responseJson.getBytes("UTF-8"));
			// // 压缩数据
			byte bytes[] = GzipUtils.compress(responseJson.getBytes("UTF-8")); // 压缩数据
			
			int dataLength = bytes.length;
			buffer = IoBuffer.allocate(dataLength + 8, false);// 这个长度是前面8个字节
			// pack+int4
			buffer.put(Protocol.HEADER);// 头部
			buffer.putInt(dataLength);// 长度
			byte[] encryptBt = EncryptUtils.encrypt(bytes);// 加密内容
			 
			buffer.put(encryptBt);
			buffer.flip();// 切换读写状态，改为写
			out.write(buffer);
			buffer.free();
		}
	}

	@Override
	public void dispose(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	 
}
