package com.github.rest.yaml.test.certificate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;
import java.util.UUID;

import com.github.rest.yaml.test.util.TestException;

public class CertificateLoader {

	private KeyStore trustStore;
	private static CertificateLoader instance;
	final private static String storePassword = "fakepassword";

	private CertificateLoader() {
	}

	public static CertificateLoader instance() {
		CertificateLoader.instance = new CertificateLoader();
		try {
			instance.trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			instance.trustStore.load(null);// Make an empty store
		} catch (Exception e) {
			throw new TestException("Key store get instance failed.", e);
		}
		return instance;
	}

	public void load(String path) {
		if (path == null) {
			return;
		}
		
		try (InputStream fis = CertificateLoader.class.getResourceAsStream("/"+path)) {
			try (BufferedInputStream bis = new BufferedInputStream(fis)) {
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
				while (bis.available() > 0) {
					Certificate cert = cf.generateCertificate(bis);
					trustStore.setCertificateEntry(UUID.randomUUID().toString(), cert);
				}
			}

		} catch (Exception e) {
			throw new TestException("Certificate load failed certificate path=" + path, e);
		}
		
		String trustStoreLocation = saveStore();
		System.setProperty("javax.net.ssl.trustStore", trustStoreLocation);
	}

	public void loadCertificates(List<String> paths) {
		if (paths == null || paths.isEmpty()) {
			return;
		}

		for (String certificate : paths) {
			load(certificate);
		}
	}

	private String saveStore() {
		FileOutputStream fos = null;
		String path;
		String name = "rest-yaml-test";
		String extention = ".truststore";
		try {
			File tempFile = File.createTempFile(name, extention);
			tempFile.deleteOnExit();
			path = tempFile.getAbsolutePath();
			fos = new FileOutputStream(tempFile);
			trustStore.store(fos, storePassword.toCharArray());
		} catch (Exception e) {
			throw new TestException("Key store save failed in the tmp directory with name=" + name + extention, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		
		return path;
	}

	public static void main(String[] args) throws Exception {
		System.out.println("Certificates loading started.");
		CertificateLoader certificateLoader = CertificateLoader.instance();

		certificateLoader.load("certificates/alice.crt");
		System.out.println("Alice certificate loaded.");

		certificateLoader.load("certificates/bob.crt");
		System.out.println("Bob certificate loaded.");

		certificateLoader.load("certificates/carol.crt");
		System.out.println("Carol certificate loaded.");

		System.out.println("Certificates loaded.");
	}
}
