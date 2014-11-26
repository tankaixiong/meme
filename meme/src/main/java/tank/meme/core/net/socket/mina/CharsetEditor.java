package tank.meme.core.net.socket.mina;

import java.beans.PropertyEditorSupport;
import java.nio.charset.Charset;

/**
 * @author tank
 * @version :
 * @date:Apr 15, 2013 3:23:50 PM
 * @description:Charset属性编辑器.默认为utf-8
 */
public class CharsetEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && text != "") {
            this.setValue(Charset.forName(text));
        } else {
            this.setValue(Charset.forName("utf-8"));
        }
    }
}
