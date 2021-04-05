package com.winnovature.tag;

import java.security.GeneralSecurityException;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;
import java.security.NoSuchProviderException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class GenerateKeys
{
    private KeyPairGenerator keyGen;
    private KeyPair pair;
    private PrivateKey privateKey;
    private PublicKey publicKey;
    
    public GenerateKeys(final int keylength) throws NoSuchAlgorithmException, NoSuchProviderException {
        (this.keyGen = KeyPairGenerator.getInstance("DSA")).initialize(keylength);
    }
    
    public void createKeys() {
        this.pair = this.keyGen.generateKeyPair();
        this.privateKey = this.pair.getPrivate();
        this.publicKey = this.pair.getPublic();
    }
    
    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }
    
    public PublicKey getPublicKey() {
        return this.publicKey;
    }
    
    public void writeToFile(final String path, final byte[] key) throws IOException {
        final File f = new File(path);
        if (!f.exists()) {
            f.getParentFile().mkdirs();
        }
        final FileOutputStream fos = new FileOutputStream(f);
        fos.write(key);
        fos.flush();
        fos.close();
    }
    
    public static void main(final String[] args) {
        try {
            final GenerateKeys gk = new GenerateKeys(1024);
            gk.createKeys();
            gk.writeToFile("src/publicKey", gk.getPublicKey().getEncoded()); //KeyPair/publicKey
            gk.writeToFile("src/privateKey", gk.getPrivateKey().getEncoded()); //KeyPair/privateKey
            System.out.println("GenerateKeys.java ::: Keys written successfully...");
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException ex2) {
            final GeneralSecurityException ex = null;
            final GeneralSecurityException e = ex;
            System.err.println(e.getMessage());
        }
        catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
    }
}
