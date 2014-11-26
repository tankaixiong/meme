package tank.meme.core.net.socket.mina.policy;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineDecoder;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

import java.nio.charset.Charset;

/**
 * @author tank
 * @version :
 * @date:Apr 15, 2013 4:37:30 PM
 * @description:解析flash自动沙箱请求的安全协议的解码工厂类
 */
public class PolicyCoderFactory implements ProtocolCodecFactory {

    private final TextLineEncoder encoder;

    private final TextLineDecoder decoder;

    /**
     * Creates a new instance of TextLineCodecFactory. This constructor provides
     * more flexibility for the developer.
     *
     * @param charset           The charset to use in the encoding and decoding
     * @param encodingDelimiter The line delimeter for the encoder
     * @param decodingDelimiter The line delimeter for the decoder
     */
    public PolicyCoderFactory(Charset charset) {
        encoder = new TextLineEncoder(charset, LineDelimiter.NUL);
        decoder = new TextLineDecoder(charset, LineDelimiter.NUL);
    }

    public ProtocolEncoder getEncoder(IoSession session) {
        return encoder;
    }

    public ProtocolDecoder getDecoder(IoSession session) {
        return decoder;
    }

    /**
     * Returns the allowed maximum size of the encoded line. If the size of the
     * encoded line exceeds this value, the encoder will throw a
     * {@link IllegalArgumentException}. The default value is
     * {@link Integer#MAX_VALUE}.
     * <p/>
     * This method does the same job with
     * {@link org.apache.mina.filter.codec.textline.TextLineEncoder#getMaxLineLength()}.
     */
    public int getEncoderMaxLineLength() {
        return encoder.getMaxLineLength();
    }

    /**
     * Sets the allowed maximum size of the encoded line. If the size of the
     * encoded line exceeds this value, the encoder will throw a
     * {@link IllegalArgumentException}. The default value is
     * {@link Integer#MAX_VALUE}.
     * <p/>
     * This method does the same job with
     * {@link org.apache.mina.filter.codec.textline.TextLineEncoder#setMaxLineLength(int)}.
     */
    public void setEncoderMaxLineLength(int maxLineLength) {
        encoder.setMaxLineLength(maxLineLength);
    }

    /**
     * Returns the allowed maximum size of the line to be decoded. If the size
     * of the line to be decoded exceeds this value, the decoder will throw a
     * {@link org.apache.mina.core.buffer.BufferDataException}. The default value is <tt>1024</tt> (1KB).
     * <p/>
     * This method does the same job with
     * {@link org.apache.mina.filter.codec.textline.TextLineDecoder#getMaxLineLength()}.
     */
    public int getDecoderMaxLineLength() {
        return decoder.getMaxLineLength();
    }

    /**
     * Sets the allowed maximum size of the line to be decoded. If the size of
     * the line to be decoded exceeds this value, the decoder will throw a
     * {@link org.apache.mina.core.buffer.BufferDataException}. The default value is <tt>1024</tt> (1KB).
     * <p/>
     * This method does the same job with
     * {@link org.apache.mina.filter.codec.textline.TextLineDecoder#setMaxLineLength(int)}.
     */
    public void setDecoderMaxLineLength(int maxLineLength) {
        decoder.setMaxLineLength(maxLineLength);
    }
}
