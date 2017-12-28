package com.github.rest.yaml.test.certificate;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.List;

import com.github.rest.yaml.test.util.TestException;

public class CertificateLoader {

	private KeyStore trustStore;
	private static CertificateLoader instance;
	final private static String userHomeDir = System.getProperty("user.home");
	final private static String trustStoreName = "rest-yaml-test-trust-store";
	final private static String trustStoreLocation = userHomeDir + "/" + trustStoreName;
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
					trustStore.setCertificateEntry("rest-yaml-test-cert-" + bis.available(), cert);
				}
			}

		} catch (Exception e) {
			throw new TestException("Certificate load failed certificate path=" + path, e);
		}
		
		saveStore();
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

	private void saveStore() {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(trustStoreLocation);
			trustStore.store(fos, storePassword.toCharArray());
		} catch (Exception e) {
			throw new TestException("Key store save failed key store path=" + trustStoreLocation, e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
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
