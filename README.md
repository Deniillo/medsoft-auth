
Ключ генерировал вот так
```bash
keytool -genkeypair -alias medsoft -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore.p12 -validity 3650 -storepass changeit -keypass changeit -dname "CN=localhost, OU=Dev, O=Medsoft, L=City, S=State, C=RU"
```

Создавал сертификат вот так

```bash
keytool -exportcert -alias medsoft -keystore certs/keystore.p12 -storepass changeit -file certs/medsoft.crt
```