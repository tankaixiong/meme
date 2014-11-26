package tank.meme.core.net.socket.mina;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import javax.annotation.PostConstruct;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tank.meme.utils.EncryptUtils;
import tank.meme.utils.ZipUtils;

/**
 * JSON解码类
 * 
 * @author tank
 * @version :1.0
 * @date:Nov 5, 2012 5:16:52 PM
 */
public class JsonTextDecoder extends CumulativeProtocolDecoder {
	private static final Logger log = LoggerFactory.getLogger(JsonTextDecoder.class);

	private IoBuffer delimBuf;
	private String charset;
	private String delimit;

	private CharsetEncoder coder;

	public JsonTextDecoder() {

	}

	@PostConstruct
	private void init() {
		// Convert delimiter to ByteBuffer if not done yet.
		if (delimBuf == null) {
			IoBuffer tmp = IoBuffer.allocate(4).setAutoExpand(true);
			if (this.charset == null) {
				coder = Charset.forName("UTF-8").newEncoder();
			} else {
				coder = Charset.forName(this.getCharset()).newEncoder();
			}
			if (this.getDelimit() == null) {
				this.setDelimit("pack");
			}
			try {
				tmp.putString(this.getDelimit(), coder);
			} catch (CharacterCodingException e) {
				log.error("{}", e);
			}

			tmp.flip();
			delimBuf = tmp;

		}
	}

	/**
	 * 如果累积buffer中的数据长度大于解析协议需要的长度时，返回true；当该方法返回结果为true时，会再次调用该方法。
	 * 如果累积buffer中的数据不够协议解析需要的长度时，返回false，直到累积buffer中的数据再次可用时。
	 */
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		int matchCount = 0;
		if (in.remaining() > 4) {

			while (in.hasRemaining()) {
				byte b = in.get();

				if (delimBuf.get(matchCount) == b) {
					matchCount++;
					if (matchCount == delimBuf.limit()) {

						if (in.prefixedDataAvailable(4)) {

							int dataLength = in.getInt();

							byte[] data = new byte[dataLength];
							in.get(data);
							// 解密
							byte[] unencrypt = EncryptUtils.encrypt(data);
							// 解压缩
							byte[] uncompass = ZipUtils.unjzlib(unencrypt);

							if (uncompass != null) {
								String jsonStr = new String(uncompass, "UTF-8");
								out.write(jsonStr);
								return true;
							}
						} else {
							in.position(Math.max(0, in.position() - matchCount));
							return false;
						}

					}
				} else {
					matchCount = 0;
					// fix for DIRMINA-506 & DIRMINA-536
					// in.position(Math.max(0, in.position() - matchCount));
					log.warn("数据头匹配不上！当前缓冲数据长度{},{}", in.remaining(), b);

				}
			}
		}
		return false;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getDelimit() {
		return delimit;
	}

	public void setDelimit(String delimit) {
		this.delimit = delimit;
	}

}
