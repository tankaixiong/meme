package tank.meme.core.net.socket.mina;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author tank
 * @version 1.0
 * @date Nov 5, 2012 5:14:06 PM
 */
@Component
public class JsonCodecFactory implements ProtocolCodecFactory {
    @Resource(name = "jsonEncoder")
    private ProtocolEncoder encoder;

    @Resource(name = "jsonTextDecoder")
    private ProtocolDecoder decoder;

    @Override
    public ProtocolDecoder getDecoder(IoSession session) throws Exception {
        return decoder;
    }

    @Override
    public ProtocolEncoder getEncoder(IoSession session) throws Exception {
        return encoder;
    }
}
