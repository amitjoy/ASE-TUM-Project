package de.tum.score.transport4you.shared.mobilebus.data.impl.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

/**
 * In this Message any data may be transfered.
 *
 * @author hoerning
 *
 */
public class DataMessage extends BluetoothData {

	private static final long serialVersionUID = 7047224747928275745L;

	private Object data;

	private boolean encrypted = false;

	public DataMessage(final Object data) {
		this.data = data;
	}

	public void decryptData(final SecretKey key) throws InvalidKeyException {
		if (!this.encrypted) {
			return;
		}

		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES", BouncyCastleProvider.PROVIDER_NAME);
			cipher.init(Cipher.DECRYPT_MODE, key);

			this.data = Base64.decode(cipher.doFinal((byte[]) this.data));

			final ByteArrayInputStream byteInputStream = new ByteArrayInputStream((byte[]) this.data);
			ObjectInput in;

			in = new ObjectInputStream(byteInputStream);
			this.data = in.readObject();

			this.encrypted = false;

		} catch (final Exception e) {
			throw new InvalidKeyException(e.getMessage());
		}

	}

	public void encryptData(final SecretKey key) throws InvalidKeyException {

		Cipher cipher;
		try {
			cipher = Cipher.getInstance("AES", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, key);

			final ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			final ObjectOutput out = new ObjectOutputStream(byteOutputStream);
			out.writeObject(this.data);
			final byte[] dataBytes = byteOutputStream.toByteArray();

			this.data = cipher.doFinal(Base64.encode(dataBytes));

			this.encrypted = true;
		} catch (final NoSuchAlgorithmException e) {
			throw new InvalidKeyException();
		} catch (final NoSuchPaddingException e) {
			throw new InvalidKeyException();
		} catch (final IOException e) {
			throw new InvalidKeyException();
		} catch (final IllegalBlockSizeException e) {
			throw new InvalidKeyException();
		} catch (final BadPaddingException e) {
			throw new InvalidKeyException();
		} catch (final NoSuchProviderException e) {
			throw new InvalidKeyException();
		}

	}

	public Object getData() {
		return this.data;
	}

	public boolean isEncrypted() {
		return this.encrypted;
	}
}
