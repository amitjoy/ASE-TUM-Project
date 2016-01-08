package de.tum.score.transport4you.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMReader;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEntry;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.BlobEnvelope;
import de.tum.score.transport4you.shared.mobilebusweb.data.impl.ETicket;

@Entity
public class User {
	public static String createTicket(final String id, final String address, final Double balance,
			final ArrayList<ETicket> list) throws IOException {
		// Should be stored seperately
		final String pem = "-----BEGIN RSA PRIVATE KEY-----\n"
				+ "MIIEpQIBAAKCAQEAwXUffSp3HjYVGw4d+Ggba82nh2VBHT5wlRmwbQKXzvDCrEaL\n"
				+ "5cJtcdELlhnTUQbqlQF+eIQl3os6RGHHI75SMHz+QBjxMxOs2C2EcbycqXaqRrD5\n"
				+ "h//7MpzbgWV7hCap7kph++LBxNyhNsm2ZMKjpY71stkRNHrZzsywRgFuwQFdEqiv\n"
				+ "ZiWSvu4OIAhIQP8VhOq0wC5BckS0q421WHcRXIbBACH0VNcNP3b2Qs6urfMIpqjg\n"
				+ "UYjT1kxXwXc36G6JRKCOXaQIblEc7EKCHVE+/2DaAKGylY8swqeIVXm+1x4hWnGU\n"
				+ "71NFfAFicRjFCgJNnLgdgtMEdmckZeSzN4DVFwIDAQABAoIBAQC0qsDhj4r29+L2\n"
				+ "BVUP64nQP5s44uLQgMN7OyZ8Z3OGm2nyoV99DvpO1L9RSoUCosboqSCHREJpdvTP\n"
				+ "b9EeHFt9VP3Mtn5rCPDeYoPSBCb6TAvxQM2IqRLazYphaXjUjZcdJvIi3j2/r5wP\n"
				+ "Ionnx640qzHh+L4MmL5YVug4OJZKP2ySWZlyUHW7NPlKOHkRwostj35NL8KzseJg\n"
				+ "Xlock0khKhcfnfhC28ANpYF5wa8bgi+twjMU/meu6Kn7prGrS6Sj6/LjKnvp6Bx/\n"
				+ "KYHqCgkaxhukxL+7/Eyhq/zcFyHpTu0s0pmORhfHx5FHCy5R0ZyyYxXnhtwJDFdT\n"
				+ "6n4xf/BBAoGBAP1ta1hR5uUzozEflKvWWEWIu9bmJhmryEtSC2Le7dsvmiFXRIqD\n"
				+ "aZjvMNjKmWdtahEDYoxLFKLDSVunIx+CIfs74zLPYaNEyej2lG7hwUWyXg2Lkdeo\n"
				+ "6vSGRLbxYq2BOobB83KlTm3tK4SGHXg511Avfg69kE4XRfLhEOGtVKNvAoGBAMNr\n"
				+ "3DOCbUGyqm2k4DbmlEmRftdOmrgD1frzFEoHtaiYsQbh4RyEiDp4CWtBN5EdOjOV\n"
				+ "v46ahDNLQnVM5z0D6mlUCwtZ751GsKb0VUeRpKxvfyZKVFl5XX7VCVSPqFE6pcoY\n"
				+ "iRUvZsa8S8x9dYOJLcpMBEzMRIVq0rC5Gp1wjHTZAoGBAJU9lJOEV9buG9JX6LNx\n"
				+ "HLaGGSgqjJFdiixg+neVFLmZRMkRnTl8vfjkEv34AXLZCjdOqQA5TsOzAUZKHPL3\n"
				+ "LY/H6roHSlZdshHQ9ASASdMDgUO0x4Qa45JwZ5Lcf+HxUkf9e6IuGwu9OX1nhX9B\n"
				+ "gLyl2zRPCeYS6oxnYgukiU1dAoGACbJsdtHeAgiPlGk+BvtiGFRz6tMnskHeeFlf\n"
				+ "hFzlkrwg7KqAtR2OdPhH316ZF0ZQAQdJPhZEwRbW8WMjhk+PbjKRabrIvREo6t/s\n"
				+ "62Q6u6O8t3Wwwc/X59dCY0PNolo6p9CX3MlBXFMzn64KCsDf2M302Kq6K7SlR8en\n"
				+ "nnBbR+ECgYEA71rF0yKxDLi5sElROAJsepqrG1tGRLfWWTncac4XJKoMgnLQDkjF\n"
				+ "Ehc9+pPi5r3j/xVCQHRbid+wgwTidPTLklP/wdKV4/SUvCUuvPUMVE7+kus7EXDv\n"
				+ "r5Rmu651XXnwwzHdS3coeknhbmzfZbhC8AnUBSphRrA/GMYgWLvTbCM=\n" + "-----END RSA PRIVATE KEY-----\n";
		Security.addProvider(new BouncyCastleProvider());
		PEMReader pemReader = null;
		pemReader = new PEMReader(new StringReader(pem));
		final PrivateKey blobEncryptionPrivateKey = ((KeyPair) pemReader.readObject()).getPrivate();
		final BlobEntry blobEntry = new BlobEntry();
		blobEntry.setAccountType("prePayAccount");
		blobEntry.setUserId(id);
		blobEntry.setUserAddress(address);
		blobEntry.setAccountBalance(new Double(balance));
		blobEntry.seteTicketList(list);
		blobEntry.setModificationDate(new Date());
		BlobEnvelope blobEnvelope = null;
		try {
			blobEnvelope = new BlobEnvelope(blobEntry, blobEncryptionPrivateKey);
		} catch (final Exception e) {
			System.err.println("error creating blobEnvelope");
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(blobEnvelope);
		oos.close();
		return DatatypeConverter.printBase64Binary(baos.toByteArray());

	}

	public static ArrayList<ETicket> dummy_list() {
		final ArrayList<ETicket> list = new ArrayList<ETicket>();
		final ETicket bla = new ETicket();
		bla.setId(4);
		bla.setInvalidated(false);
		final Date d = new Date(2016, 4, 3);
		bla.setValidTime(d.getTime());
		bla.setValidUntil(d);
		bla.setCustomerId("foo");
		list.add(bla);
		return list;
	}

	public static String hash(final String password) {
		byte[] digest = null;
		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			digest = md.digest();
		} catch (final Exception e) {
			System.err.println("Error when trying to get an instance of MD5");
		}
		final StringBuffer sb = new StringBuffer();
		for (final byte b : digest) {
			sb.append(String.format("%02x", b & 0xff));
		}
		return sb.toString();

	}

	@Index
	public Date date;
	@Index
	public String email;
	@Id
	public Long id;
	public String name;

	public String password;

	public String ticket;

	@Parent
	Key<Group> userGroup;

	public User() {
		this.date = new Date();
	}

	public User(final String group, final String user_name, final String user_email, final String user_ticket,
			final String user_password) {
		this();

		if (group != null) {
			this.userGroup = Key.create(Group.class, group);
		} else {
			this.userGroup = Key.create(Group.class, "default");
		}
		this.name = user_name;
		this.email = user_email;
		this.ticket = user_ticket;
		this.password = user_password;
		System.err.println("User.java: name: '" + this.name + "'");
	}

}
