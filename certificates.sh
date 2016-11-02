rm private.key
rm certificate.cer
rm keystore.jks

# Generate key & certificate
openssl req \
-x509 \
-sha256 \
-newkey rsa:2048 \
-keyout private.key \
-out certificate.cer \
-subj "/C=NL/O=Luminis/OU=Crypto/CN=every.dev" \
-nodes

# Put key & certificate into a PKCS12
# Convert the client key to p12 for curl
openssl pkcs12 \
-export \
-nodes \
-out keystore.p12 \
-inkey private.key \
-in certificate.cer \
-passin pass:123456 \
-passout pass:123456

javac src/crypto6/ssl1/SSL.java

java \
-Djavax.net.ssl.keyStore=keystore.p12 \
-Djavax.net.ssl.keyStorePassword=123456 \
-cp src \
crypto6.ssl1.SSL
