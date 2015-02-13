package tank.meme.utils;

/**
 * @author tank
 * @version :
 * @date:Apr 10, 2013 5:51:29 PM
 * @description:内容加密与解密,倒位，加密<-->解密
 */
public class EncryptUtils {

	public static final String[] KEYS = { "j/tank", "T90A3n,11D/GiveYouSomeColorToSee" };

	public static byte[] encrypt(byte[] _bytes) { //	与或~ 打乱byte
		byte[] _enBytes = new byte[_bytes.length];

		for (int i = 0; i < _bytes.length; i++) {

			for (String _key : KEYS) {
				_enBytes[i] = (byte) (_bytes[i] ^ _key.charAt(i % _key.length()));
			}
		}

		return _enBytes;
	}

	public static int encryptKey(int time, int distance) {
		return (time << distance) | (time >>> -distance);
	}

}
